package at.yawk.columbus;

import at.yawk.columbus.nbt.TagCompound;

/**
 * Helper class used for copying a schematic into a world.
 */
public class SchematicCopier {
    private SchematicCopier() {}

    /**
     * Copy a schematic into the given world so that the (0,0,0) coordinates of the schematic are the
     * (startX,startY,startZ) coordinates of the world. Also copies tile entities but not normal entities.
     */
    public static void copy(Schematic source, World target, int startX, int startY, int startZ) {
        assert source.getHeight() + startY < target.getProperties().getHeight();
        for (int x = 0; x < source.getWidth(); x++) {
            int tx = x + startX;
            for (int y = 0; y < source.getHeight(); y++) {
                int ty = y + startY;
                for (int z = 0; z < source.getLength(); z++) {
                    int tz = z + startZ;
                    short blockId = (short) (source.getBlockId(x, y, z) & 0xFF);
                    byte blockData = source.getBlockData(x, y, z);
                    target.getChunkOrCreate(tx >> 4, tz >> 4).setBlock(tx, ty, tz, blockId, blockData);
                }
            }
        }
        for (TagCompound compound : source.getTileEntities()) {
            TagCompound copy = compound.clone();
            int x = Entities.getTileEntityX(copy) + startX;
            int y = Entities.getTileEntityY(copy) + startY;
            int z = Entities.getTileEntityZ(copy) + startZ;
            Entities.setTileEntityX(copy, x);
            Entities.setTileEntityY(copy, y);
            Entities.setTileEntityZ(copy, z);
            target.getChunkIfExists(x >> 4, z >> 4).getTileEntities().add(copy);
        }
    }
}
