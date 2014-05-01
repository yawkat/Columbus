package at.yawk.columbus;

import java.util.Arrays;
import lombok.Getter;
import lombok.NonNull;

/**
 * Repesents all Biomes known to Columbus
 */
public enum Biome {
    UNKNOWN(-1),
    UNSET(-1),
    OCEAN(0),
    PLAINS(1),
    DESERT(2),
    EXTREME_HILLS(3),
    FOREST(4),
    TAIGA(5),
    SWAMPLAND(6),
    RIVER(7),
    NETHER(8),
    END(9),
    FROZEN_OCEAN(10),
    FROZEN_RIVER(11),
    ICE_PLAINS(12),
    ICE_MOUNTAINS(13),
    MUSHROOM_ISLAND(14),
    MUSHROOM_ISLAND_SHORE(15),
    BEACH(16),
    DESERT_HILLS(17),
    FOREST_HILLS(18),
    TAIGA_HILLS(19),
    EXTREME_HILLS_EDGE(20),
    JUNGLE(21),
    JUNGLE_HILLS(22),
    JUNGLE_EDGE(23),
    DEEP_OCEAN(24),
    STONE_BEACH(25),
    COLD_BEACH(26),
    BIRCH_FOREST(27),
    BIRCH_FOREST_HILLS(28),
    ROOFED_FOREST(29),
    COLD_TAIGA(30),
    COLD_TAIGA_HILLS(31),
    MEGA_TAIGA(32),
    MEGA_TAIGA_HILLS(33),
    EXTREME_HILLS_PLUS(34),
    SAVANNA(35),
    SAVANNA_PLATEAU(36),
    MESA(37),
    MESA_PLATEAU_F(38),
    MESA_PLATEAU(39),
    SUNFLOWER_PLAINS(129),
    DESERT_M(130),
    EXTREME_HILLS_M(131),
    FLOWER_FOREST(132),
    TAIGA_M(133),
    SWAMPLAND_M(134),
    ICE_PLAINS_SPIKES(140),
    ICE_MOUNTAINS_SPIKES(141),
    JUNGLE_M(149),
    JUNGLEEDGE_M(151),
    BIRCH_FOREST_M(155),
    BIRCH_FOREST_HILLS_M(156),
    ROOFED_FOREST_M(157),
    COLD_TAIGA_M(158),
    MEGA_SPRUCE_TAIGA(160),
    MEGA_SPRUCE_TAIGA_2(161),
    EXTREME_HILLS_PLUS_M(162),
    SAVANNA_M(163),
    SAVANNA_PLATEAU_M(164),
    MESA_BRYCE(165),
    MESA_PLATEAU_F_M(166),
    MESA_PLATEAU_M(167);

    /**
     * Array of biomes by ID.
     */
    private static final Biome[] biomes;

    static {
        biomes = new Biome[256];
        // default to UNKNOWN
        Arrays.fill(biomes, UNKNOWN);
        // map biomes by ID
        for (Biome biome : values()) {
            // ignore UNSET & UNKNOWN
            if (biome.getId() >= 0) {
                biomes[biome.getId() & 0xFF] = biome;
            }
        }
    }

    /**
     * ID of the biome
     */
    @Getter private final byte id;

    Biome(int id) {
        this.id = (byte) id;
    }

    /**
     * Find a biome by ID.
     *
     * @return the biome or UNKNOWN if no biome with that ID was found.
     */
    @NonNull
    public static Biome forId(byte id) {
        if (id < -1 || id >= biomes.length - 1) {
            return UNKNOWN;
        }
        return biomes[id + 1];
    }
}
