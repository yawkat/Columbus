package at.yawk.columbus.nbt;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum of all tag types and their IDs.
 */
@RequiredArgsConstructor
@Getter(AccessLevel.PACKAGE)
public enum TagType {
    /**
     * @see at.yawk.columbus.nbt.TagByte
     */
    BYTE(1, TagByte.class),
    /**
     * @see at.yawk.columbus.nbt.TagShort
     */
    SHORT(2, TagShort.class),
    /**
     * @see at.yawk.columbus.nbt.TagInt
     */
    INT(3, TagInt.class),
    /**
     * @see at.yawk.columbus.nbt.TagLong
     */
    LONG(4, TagLong.class),
    /**
     * @see at.yawk.columbus.nbt.TagFloat
     */
    FLOAT(5, TagFloat.class),
    /**
     * @see at.yawk.columbus.nbt.TagDouble
     */
    DOUBLE(6, TagDouble.class),
    /**
     * @see at.yawk.columbus.nbt.TagArrayByte
     */
    ARRAY_BYTE(7, TagArrayByte.class),
    /**
     * @see at.yawk.columbus.nbt.TagString
     */
    STRING(8, TagString.class),
    /**
     * @see at.yawk.columbus.nbt.TagList
     */
    LIST(9, TagList.class),
    /**
     * @see at.yawk.columbus.nbt.TagCompound
     */
    COMPOUND(10, TagCompound.class),
    /**
     * @see at.yawk.columbus.nbt.TagArrayInt
     */
    ARRAY_INT(11, TagArrayInt.class);

    private static final TagType[] tagsById = new TagType[12];
    private static final Map<Class<? extends Tag>, TagType> tagsByClass = Maps.newHashMap();

    static {
        for (TagType tagType : values()) {
            tagsById[tagType.getId()] = tagType;
            tagsByClass.put(tagType.getTagClass(), tagType);
        }
    }

    private final byte id;
    private final Class<? extends Tag> tagClass;

    TagType(int id, Class<? extends Tag> tagClass) {
        assert tagClass != null;

        this.id = (byte) id;
        this.tagClass = tagClass;
    }

    Tag newInstance() {
        try {
            return this.getTagClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static TagType forId(byte id) {
        assert id > 0 : id;
        assert id < 12 : id;
        return tagsById[id];
    }

    static TagType forClass(Class<? extends Tag> type) {
        return tagsByClass.get(type);
    }
}
