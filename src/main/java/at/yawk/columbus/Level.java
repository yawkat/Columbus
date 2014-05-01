package at.yawk.columbus;

import at.yawk.columbus.nbt.*;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Data;

/**
 * Wrapper class of the level.dat file, containing various data about the world.
 *
 * @author Yawkat
 */
@Data
public class Level {
    public static final String RULE_COMMAND_BLOCKS_CHAT = "commandBlockOutput";
    public static final String RULE_DAYLIGHT_CYCLE = "doDaylightCycle";
    public static final String RULE_FIRE_TICK = "doFireTick";
    public static final String RULE_MOB_LOOT = "doMobLoot";
    public static final String RULE_MOB_SPAWNING = "doMobSpawning";
    public static final String RULE_TILE_DROPS = "doTileDrops";
    public static final String RULE_KEEP_INVENTORY = "keepInventory";
    public static final String RULE_MOB_GRIEFING = "mobGriefing";
    public static final String RULE_REGENERATION = "naturalRegeneration";

    /**
     * Game rules of this map.
     */
    private final Map<String, String> rules = Maps.newHashMap();
    /**
     * Whether this map has been initialized (usually true).
     */
    private boolean initialized = true;
    /**
     * The name of this map.
     */
    private String name = "Columbus";
    /**
     * The generator name of this map.
     */
    private String generatorName = "flat";
    /**
     * The version number of the generator of this world.
     */
    private int generatorVersion = 0;
    /**
     * The options for the generator of this map, only used by flatland.
     */
    private String generatorOptions = "0";
    /**
     * The level seed of this map.
     */
    private long seed = 0;
    /**
     * Whether this map is generated with man-made structures.
     */
    private boolean mapFeatures = false;
    /**
     * The UNIX time of the last time this map was loaded.
     */
    private long lastPlayed = 0;
    /**
     * The estimated byte size of this map; unused.
     */
    private long size = 0;
    /**
     * Whether cheat commands are enabled.
     */
    private boolean allowCommands = false;
    /**
     * Whether this is a hardcore (iron man) world.
     */
    private boolean hardcore = false;
    /**
     * The default gamemode of this world.
     */
    private int gameType = 1;
    /**
     * The number of ticks that this world has lived.
     */
    private long time = 6000;
    /**
     * The day time of this world.
     */
    private long dayTime = 6000;
    /**
     * The x-coordinate of the spawn.
     */
    private int spawnX = 0;
    /**
     * The y-coordinate of the spawn.
     */
    private int spawnY = 0;
    /**
     * The z-coordinate of the spawn.
     */
    private int spawnZ = 0;
    /**
     * Whether it is currently raining on this map.
     */
    private boolean raining = false;
    /**
     * The amount of ticks until #raining should be toggled again.
     */
    private int rainTime = Integer.MAX_VALUE;
    /**
     * Whether it is thundering on this map.
     */
    private boolean thundering = false;
    /**
     * The amount of ticks until #thundering should be toggled again.
     */
    private int thunderTime = Integer.MAX_VALUE;

    /**
     * Serialize this to a level.dat-compatible NBT tag.
     */
    public NamedTag serialize() {
        TagCompound root = new TagCompound();
        root.addTag("version", new TagInt(19133));
        root.addTag("initialized", new TagByte((byte) (this.isInitialized() ? 1 : 0)));
        root.addTag("LevelName", new TagString(this.getName()));
        root.addTag("generatorName", new TagString(this.getGeneratorName()));
        root.addTag("generatorVersion", new TagInt(this.getGeneratorVersion()));
        root.addTag("generatorOptions", new TagString(this.getGeneratorOptions()));
        root.addTag("RandomSeed", new TagLong(this.getSeed()));
        root.addTag("MapFeatures", new TagByte((byte) (this.isMapFeatures() ? 1 : 0)));
        root.addTag("LastPlayed", new TagLong(this.getLastPlayed()));
        root.addTag("SizeOnDisk", new TagLong(this.getSize()));
        root.addTag("allowCommands", new TagByte((byte) (this.isAllowCommands() ? 1 : 0)));
        root.addTag("hardcore", new TagByte((byte) (this.isHardcore() ? 1 : 0)));
        root.addTag("GameType", new TagInt(this.getGameType()));
        root.addTag("Time", new TagLong(this.getTime()));
        root.addTag("DayTime", new TagLong(this.getDayTime()));
        root.addTag("SpawnX", new TagInt(this.getSpawnX()));
        root.addTag("SpawnY", new TagInt(this.getSpawnY()));
        root.addTag("SpawnZ", new TagInt(this.getSpawnZ()));
        root.addTag("raining", new TagByte((byte) (this.isRaining() ? 1 : 0)));
        root.addTag("rainTime", new TagInt(this.getRainTime()));
        root.addTag("thundering", new TagByte((byte) (this.isThundering() ? 1 : 0)));
        root.addTag("thunderTime", new TagInt(this.getThunderTime()));

        TagCompound rules = new TagCompound();
        root.addTag("GameRules", rules);
        for (Entry<String, String> rule : this.getRules().entrySet()) {
            rules.addTag(rule.getKey(), new TagString(rule.getValue()));
        }

        return new NamedTag("", new TagCompound(new NamedTag("Data", root)));
    }

    /**
     * A map of rules of this level.
     */
    public Map<String, String> getRules() {
        return Collections.unmodifiableMap(this.rules);
    }

    /**
     * Sets a rule in this level.
     */
    public void setRule(String name, String value) {
        this.rules.put(name, value);
    }

    /**
     * Sets a rule in this level to a boolean value.
     */
    public void setRule(String name, boolean value) {
        this.setRule(name, String.valueOf(value));
    }
}
