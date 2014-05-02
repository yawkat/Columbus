package at.yawk.columbus;

import at.yawk.columbus.nbt.NBT;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.mahout.math.map.AbstractLongObjectMap;
import org.apache.mahout.math.map.OpenLongObjectHashMap;

/**
 * A world (dimension).
 */
@RequiredArgsConstructor
public final class World {
    /**
     * Properties of this world
     */
    @Getter @NonNull private final WorldProperties properties;
    /**
     * All chunks in this world
     */
    @Getter(AccessLevel.PRIVATE) private final AbstractLongObjectMap<Chunk> chunks = new OpenLongObjectHashMap<>();

    /**
     * Returns a chunk or creates an empty one if necessary.
     * <p/>
     * Coordinates in 16 blocks.
     */
    public synchronized Chunk getChunkOrCreate(int x, int z) {
        long index = getIndex(x, z);
        Chunk result;
        if (this.getChunks().containsKey(index)) {
            result = this.getChunks().get(index);
        } else {
            result = new Chunk(this, x, z);
            this.getChunks().put(index, result);
        }
        return result;
    }

    /**
     * Returns a chunk or null if no chunk exists at the location.
     * <p/>
     * Coordinates in 16 blocks.
     */
    public synchronized Chunk getChunkIfExists(int x, int z) {
        long index = getIndex(x, z);
        Chunk result = null;
        if (this.getChunks().containsKey(index)) {
            result = this.getChunks().get(index);
        }
        return result;
    }

    private static long getIndex(int x, int z) {
        return (z & 0xffffffffL) << 32L | x & 0xffffffffL;
    }

    private static int getRegionIndex(int x, int z) {
        return (z & 31) << 5 | x & 31;
    }

    /**
     * Writes the region file with the given coordinates. Coordinates are in region files = 32 chunks = 512 blocks.
     */
    public void writeRegionFile(int x, int z, DataOutput output) throws IOException {
        // collect chunk data
        ChunkData[] data = new ChunkData[32 * 32];
        int usedSectors = 2;
        for (int coz = 0; coz < 32; coz++) {
            for (int cox = 0; cox < 32; cox++) {
                Chunk chunk = this.getChunkIfExists(x * 32 | cox, z * 32 | coz);
                if (chunk == null) {
                    continue;
                }
                byte[] rdata = NBT.serializeArray(chunk.serialize());
                ChunkData cdata = new ChunkData();
                cdata.chunk = chunk;
                cdata.rawData = rdata;
                cdata.compressedData = cdata.compress();
                cdata.sector = usedSectors;
                cdata.sectorCount = (cdata.compressedData.length + 5 >> 12) + 1;
                usedSectors += cdata.sectorCount;
                data[getRegionIndex(cox, coz)] = cdata;
            }
        }
        for (int i = 0; i < 32 * 32; i++) {
            if (data[i] == null) {
                output.write(new byte[4]);
                continue;
            }
            output.writeInt(data[i].sector << 8 | data[i].sectorCount);
        }
        for (int i = 0; i < 32 * 32; i++) {
            output.writeInt(data[i] == null ? 0 : (int) (data[i].chunk.getLastUpdated() / 1000L));
        }
        for (int i = 0; i < 32 * 32; i++) {
            if (data[i] == null) {
                continue;
            }
            output.writeInt(data[i].compressedData.length + 1);
            output.write(2);
            output.write(data[i].compressedData);
            output.write(new byte[(data[i].sectorCount << 12) - 5 - data[i].compressedData.length]);
        }
    }

    /**
     * Reads the region file with the given coordinates. Coordinates are in region files = 32 chunks = 512 blocks.
     */
    public void readRegionFile(int x, int z, DataInput input) throws IOException {
        ChunkData[] chunkArray = new ChunkData[32 * 32];
        for (int i = 0; i < 32 * 32; i++) {
            int id = input.readInt();
            if (id == 0) { continue; }
            ChunkData cdata = new ChunkData();
            cdata.sector = id >> 8;
            cdata.sectorCount = id & 0xff;
            cdata.x = i & 0x1f;
            cdata.z = i >> 5;
            chunkArray[i] = cdata;
        }
        for (int i = 0; i < 32 * 32; i++) {
            if (chunkArray[i] == null) {
                input.skipBytes(4);
                continue;
            }
            chunkArray[i].lastUpdated = input.readInt() * 1000L;
        }
        List<ChunkData> sorted = Stream.of(chunkArray)
                                       .filter(d -> d != null)
                                       .sorted(Comparator.comparingInt(d -> d.sector))
                                       .collect(Collectors.toList());
        int sector = 2;
        for (ChunkData data : sorted) {
            // skip to sector
            for (; sector < data.sector; sector++) { input.skipBytes(0x1000); }
            data.compressedData = new byte[input.readInt() - 1];
            input.readByte(); // version
            input.readFully(data.compressedData);
            input.skipBytes((data.sectorCount << 12) - 5 - data.compressedData.length);
            data.rawData = data.decompress();
            data.chunk = Chunk.deserialize(this, NBT.deserializeArray(data.rawData));
            chunks.put(getIndex(data.x, data.z), data.chunk);
            sector += data.sectorCount;
        }
    }

    /**
     * Refresh the height map of this world using the given lighter.
     */
    public synchronized void refreshHeightMap(Lighter lighter) {
        for (Chunk chunk : this.getAllChunks()) {
            chunk.calculateHeightMap(lighter);
        }
    }

    /**
     * Fullbright this world (set skylight to 100% on all blocks).
     */
    public synchronized void fullbright() {
        for (Chunk chunk : this.getAllChunks()) {
            chunk.fullbright();
        }
    }

    /**
     * Returns a list of all loaded chunks.
     */
    public List<Chunk> getAllChunks() {
        return this.getChunks().values();
    }

    private static class ChunkData {
        Chunk chunk;
        int sector;
        int sectorCount;
        byte[] rawData;
        byte[] compressedData;
        int x;
        int z;
        long lastUpdated;

        private byte[] compress() {
            ByteArrayOutputStream res = new ByteArrayOutputStream(8096);
            DeflaterOutputStream dos = new DeflaterOutputStream(res);
            try {
                dos.write(this.rawData);
                dos.finish();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return res.toByteArray();
        }

        private byte[] decompress() {
            ByteArrayOutputStream res = new ByteArrayOutputStream(8096);
            InflaterOutputStream ios = new InflaterOutputStream(res);
            try {
                ios.write(this.compressedData);
                ios.finish();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return res.toByteArray();
        }
    }
}
