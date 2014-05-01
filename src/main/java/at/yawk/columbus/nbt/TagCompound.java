package at.yawk.columbus.nbt;

import com.google.common.collect.Maps;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Compound (Map) tag.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public final class TagCompound extends TagStructure {
    private Map<String, NamedTag> tags = Maps.newHashMap();

    public TagCompound(NamedTag... initialTags) {
        this();
        for (NamedTag initial : initialTags) {
            this.addTag(initial);
        }
    }

    private TagCompound(Map<String, NamedTag> initialTags) {
        this();
        this.setTags(initialTags);
    }

    /**
     * Replace the tags with the given tags.
     */
    public synchronized void setTags(Map<String, NamedTag> tags) {
        this.tags = Maps.newHashMap(tags);
    }

    /**
     * Returns an immutable map of all tags in this compound.
     */
    public Map<String, NamedTag> getTags() {
        return Collections.unmodifiableMap(this.tags);
    }

    /**
     * Add a tag with a name.
     */
    public synchronized void addTag(String name, Tag tag) {
        this.tags.put(name, new NamedTag(name, tag));
    }

    /**
     * Add a NamedTag directly.
     */
    public synchronized void addTag(NamedTag named) {
        this.tags.put(named.getName(), named);
    }

    /**
     * Returns a tag by name.
     *
     * @throws java.util.NoSuchElementException if no tag with the name was found.
     */
    public Tag getTag(String name) {
        NamedTag named = this.getTags().get(name);
        if (named == null) { throw new NoSuchElementException(); }
        return named.getValue();
    }

    /**
     * Removes a tag by name.
     */
    public synchronized void removeTag(String name) {
        this.tags.remove(name);
    }

    @Override
    synchronized void serialize(DataOutput output) throws IOException {
        Map<String, NamedTag> currentTags = this.getTags();
        for (Entry<String, NamedTag> entry : currentTags.entrySet()) {
            assert entry.getKey().equals(entry.getValue().getName());
            output.write(entry.getValue().getValue().getType().getId());
            output.writeUTF(entry.getKey());
            entry.getValue().getValue().serialize(output);
        }
        output.write((byte) 0);
    }

    @Override
    void deserialize(DataInput input) throws IOException {
        Map<String, NamedTag> newTags = Maps.newHashMap();
        while (true) {
            byte typeByte = input.readByte();
            if (typeByte == 0) {
                break;
            }
            TagType type = TagType.forId(typeByte);
            String name = input.readUTF();
            Tag tag = type.newInstance();
            tag.deserialize(input);
            newTags.put(name, new NamedTag(name, tag));
        }
        this.tags = newTags;
    }

    /**
     * Return a byte by name.
     *
     * @throws java.util.NoSuchElementException if no tag with the name was found.
     * @throws java.lang.ClassCastException     if the tag is of a different type.
     */
    public byte getByte(String name) {
        return this.getTag(name).getByte();
    }

    /**
     * Return a short by name.
     *
     * @throws java.util.NoSuchElementException if no tag with the name was found.
     * @throws java.lang.ClassCastException     if the tag is of a different type.
     */
    public short getShort(String name) {
        return this.getTag(name).getShort();
    }

    /**
     * Return an int by name.
     *
     * @throws java.util.NoSuchElementException if no tag with the name was found.
     * @throws java.lang.ClassCastException     if the tag is of a different type.
     */
    public int getInt(String name) {
        return this.getTag(name).getInt();
    }

    /**
     * Return a float by name.
     *
     * @throws java.util.NoSuchElementException if no tag with the name was found.
     * @throws java.lang.ClassCastException     if the tag is of a different type.
     */
    public float getFloat(String name) {
        return this.getTag(name).getFloat();
    }

    /**
     * Return a double by name.
     *
     * @throws java.util.NoSuchElementException if no tag with the name was found.
     * @throws java.lang.ClassCastException     if the tag is of a different type.
     */
    public double getDouble(String name) {
        return this.getTag(name).getDouble();
    }

    /**
     * Return a String by name.
     *
     * @throws java.util.NoSuchElementException if no tag with the name was found.
     * @throws java.lang.ClassCastException     if the tag is of a different type.
     */
    public String getString(String name) {
        return this.getTag(name).getString();
    }

    /**
     * Clone this tag.
     */
    @Override
    public TagCompound clone() {
        return new TagCompound(Maps.transformValues(this.getTags(), NamedTag::clone));
    }
}
