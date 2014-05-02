package at.yawk.columbus;

import at.yawk.columbus.nbt.TagArrayByte;
import at.yawk.columbus.nbt.TagCompound;
import at.yawk.columbus.nbt.TagList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Describes the .schematic format, a format for storing a cuboid of blocks and entities for easy transfer across worlds
 * (for example WorldEdit).
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public final class Schematic {
    /**
     * Width (X) of the schematic in blocks.
     */
    private short width;
    /**
     * Height (Y) of the schematic in blocks.
     */
    private short height;
    /**
     * Length (Z) of the schematic in blocks.
     */
    private short length;

    /**
     * The block IDs in this schematic.
     */
    private byte[] blockIds;
    /**
     * The block data in this schematic.
     */
    private byte[] blockData;
    /**
     * A list of entities in this schematic.
     */
    private Collection<TagCompound> entities;
    /**
     * A list of tile entities in this schematic.
     */
    private Collection<TagCompound> tileEntities;

    /**
     * Read a schematic from an NBT tag.
     */
    @SuppressWarnings("unchecked")
    public void load(TagCompound source) {
        this.setWidth(source.getShort("Width"));
        this.setHeight(source.getShort("Height"));
        this.setLength(source.getShort("Length"));

        this.setBlockIds(((TagArrayByte) source.getTag("Blocks")).getValue());
        this.setBlockData(((TagArrayByte) source.getTag("Data")).getValue());

        this.setEntities((List) source.getTag("Entities").asList().getTags());
        this.setTileEntities((List) source.getTag("TileEntities").asList().getTags());
    }

    /**
     * The entities in this schematic.
     */
    public Collection<TagCompound> getEntities() {
        return Collections.unmodifiableCollection(this.entities);
    }

    /**
     * The tile entities in this schematic.
     */
    public Collection<TagCompound> getTileEntities() {
        return Collections.unmodifiableCollection(this.tileEntities);
    }

    /**
     * Get the block ID at the given coordinate in this schematic.
     */
    public byte getBlockId(int x, int y, int z) {
        return this.getBlockIds()[this.getIndex(x, y, z)];
    }

    /**
     * Get the block data at the given coordinate in this schematic.
     */
    public byte getBlockData(int x, int y, int z) {
        return this.getBlockData()[this.getIndex(x, y, z)];
    }

    /**
     * Set the block ID at the given coordinate in this schematic.
     */
    public void setBlockId(int x, int y, int z, byte id) {
        this.getBlockIds()[this.getIndex(x, y, z)] = id;
    }

    /**
     * Set the block data at the given coordinate in this schematic.
     */
    public void setBlockData(int x, int y, int z, byte data) {
        this.getBlockData()[this.getIndex(x, y, z)] = data;
    }

    private int getIndex(int x, int y, int z) {
        return x + (z + y * this.getLength()) * this.getWidth();
    }
}
