package at.yawk.columbus;

import lombok.Value;

/**
 * World-specific properties.
 */
@Value
public class WorldProperties {
    /**
     * Height of the world in blocks
     */
    int height;

    /**
     * @param height Height, must be a multiple of 16
     */
    public WorldProperties(int height) {
        assert (height & 0xF) == 0;
        this.height = height;
    }

    /**
     * Height of the world in ChunkSections
     */
    public int getHeightInChunkSections() {
        return this.getHeight() >> 4;
    }
}
