package at.yawk.columbus;

import at.yawk.columbus.nbt.NBT;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.Setter;
import org.apache.mahout.math.list.IntArrayList;
import org.apache.mahout.math.map.AbstractIntObjectMap;
import org.apache.mahout.math.map.OpenIntObjectHashMap;
import org.apache.mahout.math.set.AbstractLongSet;
import org.apache.mahout.math.set.OpenLongHashSet;

/**
 * A level/save folder.
 */
public class LevelFolder {
    public static final int WORLD_REGULAR = 0;
    public static final int WORLD_NETHER = -1;
    public static final int WORLD_END = 1;

    private final AbstractIntObjectMap<World> worlds = new OpenIntObjectHashMap<>(3);
    /**
     * The level.dat data.
     */
    @Getter @Setter private Level level;

    public LevelFolder() {}

    public LevelFolder(Level level) {
        this();
        this.setLevel(level);
    }

    public LevelFolder(Level level, World overworld) {
        this(level);
        this.setWorld(WORLD_REGULAR, overworld);
    }

    /**
     * Assign a world object to the given type.
     */
    public void setWorld(int type, World world) {
        this.worlds.put(type, world);
    }

    /**
     * Save this level to the given directory, deleting old files.
     */
    public void print(Path directory) throws IOException {
        this.print(directory, true);
    }

    /**
     * Save this level to the given directory.
     *
     * @param deleteOld whether existing files should be purged.
     * @throws java.io.IOException if at least one operation failed. Note that this method will execute multi-threaded
     *                             in a ForkJoinPool and thus multiple exceptions might happen.
     */
    public void print(Path directory, boolean deleteOld) throws IOException {
        assert directory != null;
        assert !Files.exists(directory) || Files.isDirectory(directory) : directory;
        if (deleteOld && Files.exists(directory)) {
            Util.removeRecursive(directory);
        }
        Files.createDirectories(directory);

        try (OutputStream levelFile = Files.newOutputStream(directory.resolve("level.dat"))) {
            NBT.serializeStreamZipped(levelFile, this.getLevel().serialize());
        }

        IntArrayList indicies = this.worlds.keys();

        AtomicReference<IOException> ref = new AtomicReference<>();

        indicies.toList().parallelStream().forEach(i -> {
            if (ref.get() != null) { return; }

            Path worldDir = directory.resolve(i == 0 ? "region" : "DIM" + i);
            assert !Files.exists(worldDir) || Files.isDirectory(worldDir) : worldDir;
            try {
                Files.createDirectories(worldDir);
            } catch (IOException e) {
                ref.compareAndSet(null, e);
                return;
            }
            AbstractLongSet requiredRegionFiles = new OpenLongHashSet();
            World world = this.worlds.get(i);
            for (Chunk chunk : world.getAllChunks()) {
                requiredRegionFiles.add((chunk.getChunkX() >> 5) | (((long) chunk.getChunkZ() >> 5) << 32L));
            }
            requiredRegionFiles.keys().toList().parallelStream().forEach(ri -> {
                if (ref.get() != null) { return; }

                int regionX = (int) (long) ri;
                int regionZ = (int) (ri >> 32);

                Path regionFile = worldDir.resolve("r." + regionX + "." + regionZ + ".mca");
                assert !Files.exists(regionFile) || Files.isRegularFile(regionFile) : regionFile;
                try (final OutputStream o = Files.newOutputStream(regionFile)) {
                    world.writeRegionFile(regionX, regionZ, new DataOutputStream(o));
                } catch (IOException e) {
                    ref.compareAndSet(null, e);
                }
            });
        });

        IOException exc = ref.get();
        if (exc != null) { throw exc; }
    }
}
