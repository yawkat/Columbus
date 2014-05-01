package at.yawk.columbus;

import at.yawk.columbus.nbt.TagCompound;
import at.yawk.columbus.nbt.TagDouble;
import at.yawk.columbus.nbt.TagInt;
import at.yawk.columbus.nbt.TagList;

/**
 * Helper methods for entity NBT data.
 */
public class Entities {
    private Entities() {}

    /**
     * Get the X position of an entity from its Tag.
     */
    public static double getEntityX(TagCompound compound) {
        return ((TagList) compound.getTag("Pos")).getTags().get(0).getDouble();
    }

    /**
     * Get the Y position of an entity from its Tag.
     */
    public static double getEntityY(TagCompound compound) {
        return ((TagList) compound.getTag("Pos")).getTags().get(1).getDouble();
    }

    /**
     * Get the Z position of an entity from its Tag.
     */
    public static double getEntityZ(TagCompound compound) {
        return ((TagList) compound.getTag("Pos")).getTags().get(2).getDouble();
    }

    /**
     * Set the X position of an entity in its Tag.
     */
    public static void setEntityX(TagCompound compound, double x) {
        ((TagDouble) ((TagList) compound.getTag("Pos")).getTags().get(0)).setValue(x);
    }

    /**
     * Set the Y position of an entity in its Tag.
     */
    public static void setEntityY(TagCompound compound, double y) {
        ((TagDouble) ((TagList) compound.getTag("Pos")).getTags().get(0)).setValue(y);
    }

    /**
     * Set the Z position of an entity in its Tag.
     */
    public static void setEntityZ(TagCompound compound, double z) {
        ((TagDouble) ((TagList) compound.getTag("Pos")).getTags().get(0)).setValue(z);
    }

    /**
     * Get the X position of a tile entity from its Tag.
     */
    public static int getTileEntityX(TagCompound compound) {
        return compound.getInt("x");
    }

    /**
     * Get the Y position of a tile entity from its Tag.
     */
    public static int getTileEntityY(TagCompound compound) {
        return compound.getInt("y");
    }

    /**
     * Get the Z position of a tile entity from its Tag.
     */
    public static int getTileEntityZ(TagCompound compound) {
        return compound.getInt("z");
    }

    /**
     * Set the X position of a tile entity in its Tag.
     */
    public static void setTileEntityX(TagCompound compound, int x) {
        ((TagInt) compound.getTag("x")).setValue(x);
    }

    /**
     * Set the Y position of a tile entity in its Tag.
     */
    public static void setTileEntityY(TagCompound compound, int y) {
        ((TagInt) compound.getTag("y")).setValue(y);
    }

    /**
     * Set the Z position of a tile entity in its Tag.
     */
    public static void setTileEntityZ(TagCompound compound, int z) {
        ((TagInt) compound.getTag("z")).setValue(z);
    }
}
