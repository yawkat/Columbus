package at.yawk.columbus;

import at.yawk.columbus.nbt.TagArrayByte;
import at.yawk.columbus.nbt.TagByte;
import at.yawk.columbus.nbt.TagCompound;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * One 16x16x16 cube of blocks. Multiple of these stacked make up a chunk.
 */
@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public class ChunkSection {
    /**
     * Amount of blocks in this chunk.
     */
    private static final int LENGTH = 16 * 16 * 16;

    /**
     * The chunk this section is part of.
     */
    @Getter @NonNull private final Chunk chunk;
    /**
     * The y coordinate (in 16 blocks) in the parent chunk.
     */
    @Getter private final byte chunkY;

    /**
     * Block IDs of this chunk.
     */
    private final short[] blockIds = new short[LENGTH];
    /**
     * Block data values in this chunk.
     */
    private final byte[] blockData = new byte[LENGTH];
    /**
     * Brightness values coming from other blocks of the blocks in this chunk.
     */
    private final byte[] lightBlock = new byte[LENGTH];
    /**
     * Brightness values coming from the sky of the blocks in this chunk.
     */
    private final byte[] lightSky = new byte[LENGTH];

    /**
     * Find the highest block y-coordinate in the given column, according to the opacity values supplied by the given
     * Lighter.
     *
     * @return The y-coordinate or -1 if the column is empty (or translucent).
     */
    public int getHighestBlock(int x, int z, Lighter lighter) {
        for (int y = 15; y >= 0; y--) {
            if (lighter.getOpacity(this.blockIds[getIndex(x, y, z)]) != 0) {
                return y;
            }
        }
        return -1;
    }

    /**
     * Returns whether this section is entirely empty (only air).
     */
    public boolean isEmpty() {
        return Util.isAllZero(this.getBlockIds());
    }

    /**
     * Serialize this section.
     */
    public TagCompound serialize() {
        TagCompound res = new TagCompound();
        res.addTag("Y", new TagByte(this.getChunkY()));
        byte[][] split = Util.split(this.getBlockIds());
        res.addTag("Blocks", new TagArrayByte(split[0]));
        if (!Util.isAllZero(split[1])) {
            res.addTag("Add", new TagArrayByte(Util.half(split[1])));
        }
        res.addTag("Data", new TagArrayByte(Util.half(this.getBlockData())));
        res.addTag("BlockLight", new TagArrayByte(Util.half(this.getLightBlock())));
        res.addTag("SkyLight", new TagArrayByte(Util.half(this.getLightSky())));
        return res;
    }

    public static ChunkSection deserialize(Chunk chunk, TagCompound from) {
        byte chunkY = from.getByte("Y");
        ChunkSection section = new ChunkSection(chunk, chunkY);
        byte[] lower = ((TagArrayByte) from.getTag("Blocks")).getValue();
        byte[] upper = from.getOptional("Add")
                           .map(t -> Util.twice(((TagArrayByte) t).getValue()))
                           .orElseGet(() -> new byte[LENGTH]);
        short[] ids = Util.merge(lower, upper);
        System.arraycopy(ids, 0, section.blockIds, 0, LENGTH);

        byte[] data = Util.twice(((TagArrayByte) from.getTag("Data")).getValue());
        System.arraycopy(data, 0, section.blockData, 0, LENGTH);

        byte[] blight = Util.twice(((TagArrayByte) from.getTag("BlockLight")).getValue());
        System.arraycopy(blight, 0, section.lightBlock, 0, LENGTH);

        byte[] slight = Util.twice(((TagArrayByte) from.getTag("SkyLight")).getValue());
        System.arraycopy(slight, 0, section.lightSky, 0, LENGTH);

        return section;
    }

    /**
     * Returns the block ID at the given location.
     * <p/>
     * The coordinates' axis origin may be the 0-corner of this chunk section, the parent chunk, the parent region file
     * or the entire world.
     */
    public short getBlockId(int x, int y, int z) {
        return this.getBlockIds()[getIndex(x, y, z)];
    }

    /**
     * Returns the block data value at the given location.
     * <p/>
     * The coordinates' axis origin may be the 0-corner of this chunk section, the parent chunk, the parent region file
     * or the entire world.
     */
    public byte getBlockData(int x, int y, int z) {
        return this.getBlockData()[getIndex(x, y, z)];
    }

    /**
     * Set block ID and data value at the given location.
     * <p/>
     * The coordinates' axis origin may be the 0-corner of this chunk section, the parent chunk, the parent region file
     * or the entire world.
     */
    public void setBlock(int x, int y, int z, short id, byte data) {
        int i = getIndex(x, y, z);
        this.getBlockIds()[i] = id;
        this.getBlockData()[i] = data;
    }

    /**
     * Returns the sky light value at the given location.
     * <p/>
     * The coordinates' axis origin may be the 0-corner of this chunk section, the parent chunk, the parent region file
     * or the entire world.
     */
    public byte getLightSky(int x, int y, int z) {
        return this.getLightSky()[getIndex(x, y, z)];
    }

    /**
     * Sets the sky light value at the given location.
     * <p/>
     * The coordinates' axis origin may be the 0-corner of this chunk section, the parent chunk, the parent region file
     * or the entire world.
     */
    public void setLightSky(int x, int y, int z, byte light) {
        this.getLightSky()[getIndex(x, y, z)] = light;
    }

    /**
     * Returns the block light value at the given location.
     * <p/>
     * The coordinates' axis origin may be the 0-corner of this chunk section, the parent chunk, the parent region file
     * or the entire world.
     */
    public byte getLightBlock(int x, int y, int z) {
        return this.getLightBlock()[getIndex(x, y, z)];
    }

    /**
     * Sets the block light value at the given location.
     * <p/>
     * The coordinates' axis origin may be the 0-corner of this chunk section, the parent chunk, the parent region file
     * or the entire world.
     */
    public void setLightBlock(int x, int y, int z, byte light) {
        this.getLightBlock()[getIndex(x, y, z)] = light;
    }

    /**
     * Set the sky light value of all blocks in this section to 100%.
     */
    public void fullbright() {
        Arrays.fill(this.getLightSky(), (byte) 15);
    }

    /**
     * Return the index of the block at the given coordinates.
     */
    private static int getIndex(int x, int y, int z) {
        return (y & 0xf) << 8 | (z & 0xf) << 4 | x & 0xf;
    }
}
