package at.yawk.columbus;

import at.yawk.columbus.nbt.NBT;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
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
    private static final Pattern REGION_FILE_NAME = Pattern.compile("r\\.([0-9]+)\\.([0-9]+)\\.mca");

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
     * Returns a world by type or null if no such world exists.
     */
    public World getWorldIfExists(int type) {
        return this.worlds.get(type);
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
                try (OutputStream o = Files.newOutputStream(regionFile)) {
                    world.writeRegionFile(regionX, regionZ, new DataOutputStream(o));
                } catch (IOException e) {
                    ref.compareAndSet(null, e);
                }
            });
        });

        IOException exc = ref.get();
        if (exc != null) { throw exc; }
    }

    public void read(Path directory) throws IOException {
        assert directory != null;
        assert Files.isDirectory(directory) : directory;

        try (InputStream levelFile = Files.newInputStream(directory.resolve("level.dat"))) {
            if (getLevel() == null) { setLevel(new Level()); }
            getLevel().deserialize(NBT.deserializeStreamZipped(levelFile));
        }

        AtomicReference<IOException> ref = new AtomicReference<>();

        IntStream.of(WORLD_REGULAR, WORLD_NETHER, WORLD_END).parallel().forEach(i -> {
            if (ref.get() != null) { return; }

            Path worldDir = directory.resolve(i == 0 ? "region" : "DIM" + i);

            if (!Files.isDirectory(worldDir)) { return; }

            World world = new World(new WorldProperties(128));

            try {
                Files.list(worldDir).parallel().forEach(entry -> {
                    if (ref.get() != null) { return; }

                    if (!Files.isRegularFile(entry)) { return; }
                    Matcher matcher = REGION_FILE_NAME.matcher(entry.getFileName().toString());
                    if (!matcher.find()) { return; }
                    int regionX = Integer.parseInt(matcher.group(1));
                    int regionZ = Integer.parseInt(matcher.group(2));

                    try (InputStream is = Files.newInputStream(entry)) {
                        world.readRegionFile(regionX, regionZ, new DataInputStream(is));
                    } catch (IOException e) {
                        ref.compareAndSet(null, e);
                        return;
                    }
                });
            } catch (IOException e) {
                ref.compareAndSet(null, e);
                return;
            }

            setWorld(i, world);
        });

        IOException exc = ref.get();
        if (exc != null) { throw exc; }
    }
}
