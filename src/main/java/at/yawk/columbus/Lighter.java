package at.yawk.columbus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import org.apache.mahout.math.map.AbstractIntObjectMap;
import org.apache.mahout.math.map.OpenIntObjectHashMap;

/**
 * Object that stores data about different block IDs and their properties, particularly light values.
 */
@Getter(AccessLevel.PRIVATE)
public class Lighter {
    /**
     * Known block data.
     */
    private final AbstractIntObjectMap<BlockLightData> blocks = new OpenIntObjectHashMap<>();
    private final int[] lightUpdateBlockList = new int[32768];

    /**
     * Return either the light data for the given ID or the default light data if it is unknown.
     */
    private BlockLightData lookup(int id) {
        return this.getBlocks().containsKey(id) ? this.getBlocks().get(id) : BlockLightData.DEFAULT;
    }

    /**
     * Set the lighting data for the given block ID.
     */
    public void putBlockLightData(int blockId, int opacity, int brightness) {
        this.getBlocks().put(blockId, new BlockLightData(opacity, brightness));
    }

    /**
     * Returns the opacity of the given block ID.
     */
    public int getOpacity(int blockId) {
        return this.lookup(blockId).getOpacity();
    }

    /**
     * Insert the default lighting values.
     */
    public void putDefaultBlockLightData() {
        Tables.putDefaultLightValues(this);
    }

    @Value
    private static final class BlockLightData {
        private static final BlockLightData DEFAULT = new BlockLightData(0, 0);

        int opacity;
        int brightness;
    }
}
