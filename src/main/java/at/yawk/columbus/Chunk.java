package at.yawk.columbus;

import at.yawk.columbus.nbt.*;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * A chunk in a minecraft world.
 */
@Getter
@Setter
public class Chunk {
    /**
     * The world
     */
    private final World world;
    /**
     * X position of this chunk, in 16 blocks.
     */
    private final int chunkX;
    /**
     * Z position of this chunk, in 16 blocks.
     */
    private final int chunkZ;

    @Getter(AccessLevel.PRIVATE) private final ChunkSection[] sections;
    /**
     * Biome IDs for each column in this chunk.
     */
    @Getter(AccessLevel.PRIVATE) private final byte[] biomes = new byte[16 * 16];
    @Getter(AccessLevel.PRIVATE) private final int[] heightMap = new int[16 * 16];

    /**
     * Tick when this chunk was last updated.
     */
    private long lastUpdated = 0;
    /**
     * Whether this chunk was populated.
     */
    private boolean populated = true;
    /**
     * How many ticks players have been here.
     */
    private long inhabitatedTime = 0;

    /**
     * A list of all entities in this chunk.
     */
    private Collection<TagCompound> entities = Lists.newArrayList();
    /**
     * A list of all tile entities (blocks with special metadata such as signs and chests) in this chunk.
     */
    private Collection<TagCompound> tileEntities = Lists.newArrayList();
    /**
     * Active, ticking tiles in this chunk.
     */
    private Collection<TagCompound> tileTicks = Lists.newArrayList();

    public Chunk(World world, int chunkX, int chunkZ) {
        assert world != null;

        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;

        this.sections = new ChunkSection[this.getWorld().getProperties().getHeightInChunkSections()];

        // Generate empty chunk sections for this array
        for (byte i = 0; i < this.getSections().length; i++) {
            this.getSections()[i] = new ChunkSection(this, i);
        }
    }

    /**
     * Gets the biome at the given coordinates
     *
     * @param x The x coordinate of the block
     * @param z The z coordinate of the block
     * @return The biome at this block
     */
    public Biome getBiome(int x, int z) {
        return Biome.forId(this.getBiomes()[(x & 0xf) << 4 | z & 0xf]);
    }

    /**
     * Sets the biome of the given block in this chunk
     *
     * @param x     The x coordinate of the block
     * @param z     The y coordinate of the block
     * @param biome The biome to set
     */
    public void setBiome(int x, int z, Biome biome) {
        this.getBiomes()[getIndex(x, z)] = biome.getId();
    }

    /**
     * Get the height of the given column.
     * <p/>
     * <i>This only queries the height map, which may be incorrect.</i>
     *
     * @param x The x coordinate of the block
     * @param z The y coordinate of the block
     * @return The height at these coordinates
     */
    public int getHeight(int x, int z) {
        return this.getHeightMap()[getIndex(x, z)];
    }

    /**
     * Set the height of the given column.
     * <p/>
     * <i>This only sets the height map and does not edit any blocks.</i>
     *
     * @param x      The x coordinate of the block
     * @param z      The y coordinate of the block
     * @param height The height of the block
     */
    public void setHeight(int x, int z, int height) {
        this.getHeightMap()[getIndex(x, z)] = height;
    }

    /**
     * Get the chunk section by vertical index.
     */
    public ChunkSection getChunkSection(int verticalIndex) {
        assert verticalIndex >= 0;
        assert this.getSections().length > verticalIndex;
        return this.getSections()[verticalIndex];
    }

    /**
     * Recalculates the height map for this chunk based on block data.
     */
    public void calculateHeightMap(Lighter lighter) {
        Arrays.fill(this.getHeightMap(), 0);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int sy = this.getSections().length - 1; sy >= 0; sy--) {
                    ChunkSection sect = this.getSections()[sy];
                    int y = sect.getHighestBlock(x, z, lighter);
                    if (y != -1) {
                        this.getHeightMap()[getIndex(x, z)] = (sy << 4) + y + 1;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Serialize this chunk to NBT.
     */
    public NamedTag serialize() {
        TagCompound root = new TagCompound();
        root.addTag("xPos", new TagInt(this.getChunkX()));
        root.addTag("zPos", new TagInt(this.getChunkZ()));
        root.addTag("LastUpdate", new TagLong(this.getLastUpdated()));
        root.addTag("TerrainPopulated", new TagByte((byte) (this.isPopulated() ? 1 : 0)));
        root.addTag("InhabitedTime", new TagLong(this.getInhabitatedTime()));
        if (!Util.isAll((byte) -1, this.getBiomes())) {
            root.addTag("Biomes", new TagArrayByte(this.getBiomes()));
        }
        root.addTag("HeightMap", new TagArrayInt(this.getHeightMap()));
        {
            Collection<TagCompound> sectionTags = Lists.newArrayListWithCapacity(this.getSections().length);
            for (ChunkSection section : this.getSections()) {
                if (!section.isEmpty()) {
                    sectionTags.add(section.serialize());
                }
            }
            root.addTag("Sections", new TagList(sectionTags));
        }
        root.addTag("Entities", new TagList(this.getEntities()));
        root.addTag("TileEntities", new TagList(this.getTileEntities()));
        root.addTag("TileTicks", new TagList(this.getTileTicks()));
        return new NamedTag("", new TagCompound(new NamedTag("Level", root)));
    }

    public static Chunk deserialize(World world, NamedTag from) {
        TagCompound root = from.getValue().asCompound().getTag("Level").asCompound();
        Chunk chunk = new Chunk(world, root.getInt("xPos"), root.getInt("zPos"));
        root.getOptional("LastUpdate").ifPresent(tag -> chunk.lastUpdated = tag.getLong());
        root.getOptional("TerrainPopulated").ifPresent(tag -> chunk.populated = tag.getByte() != 0);
        root.getOptional("InhabitedTime").ifPresent(tag -> chunk.inhabitatedTime = tag.getLong());
        root.getOptional("Biomes")
            .ifPresent(tag -> System.arraycopy(((TagArrayByte) tag).getValue(),
                                               0,
                                               chunk.biomes,
                                               0,
                                               chunk.biomes.length));
        root.getOptional("Sections").ifPresent(tag -> {
            tag.asList().getTagsChecked(TagCompound.class).forEach(sectionTag -> {
                ChunkSection section = ChunkSection.deserialize(chunk, sectionTag);
                chunk.sections[section.getChunkY()] = section;
            });
        });
        root.getOptional("Entities").ifPresent(tag -> chunk.entities = tag.asList().getTagsChecked(TagCompound.class));
        root.getOptional("TileEntities")
            .ifPresent(tag -> chunk.tileEntities = tag.asList().getTagsChecked(TagCompound.class));
        root.getOptional("TileTicks")
            .ifPresent(tag -> chunk.tileTicks = tag.asList().getTagsChecked(TagCompound.class));
        return chunk;
    }

    /**
     * Set the block ID and data at the given coordinates.
     */
    public void setBlock(int x, int y, int z, short id, byte data) {
        this.getChunkSection(y >> 4).setBlock(x, y, z, id, data);
    }

    /**
     * Fullbright (set brightness to 100%) all blocks in this chunk.
     */
    public void fullbright() {
        for (ChunkSection section : this.getSections()) {
            section.fullbright();
        }
    }

    private static int getIndex(int x, int z) {
        return (z & 0b1111) << 4 | x & 0b1111;
    }
}
