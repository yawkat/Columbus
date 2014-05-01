package at.yawk.columbus.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * List tag.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public final class TagList extends TagStructure {
    private List<Tag> tags;
    
    public TagList() {
        this.tags = Lists.newArrayList();
    }
    
    public TagList(Iterable<? extends Tag> initialValue) {
        this.tags = Lists.newArrayList(initialValue);
    }
    
    public TagList(Tag... initialValue) {
        this.tags = Lists.newArrayList(initialValue);
    }
    
    static boolean checkListIntegrity(List<? extends Tag> tags) {
        TagType type = null;
        for (Tag tag : tags) {
            if (tag == null) {
                return false;
            }
            if (type == null) {
                type = tag.getType();
            } else if (type != tag.getType()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Replace contents with the given tags.
     */
    public synchronized void setTags(List<Tag> tags) {
        assert checkListIntegrity(tags);
        this.tags = Lists.newArrayList(tags);
    }

    /**
     * Returns an immutable list of all tags.
     */
    public List<Tag> getTags() {
        return Collections.unmodifiableList(this.tags);
    }

    /**
     * Appends a tag.
     */
    public synchronized void addTag(Tag tag) {
        this.tags.add(tag);
    }
    
    @Override
    synchronized void serialize(DataOutput output) throws IOException {
        List<Tag> currentTags = this.getTags();
        
        assert TagList.checkListIntegrity(currentTags);
        
        TagType type = currentTags.isEmpty() ? TagType.BYTE : currentTags.get(0).getType();
        output.write(type.getId());
        output.writeInt(currentTags.size());
        for (Tag tag : currentTags) {
            tag.serialize(output);
        }
    }
    
    @Override
    void deserialize(DataInput input) throws IOException {
        TagType type = TagType.forId(input.readByte());
        int length = input.readInt();
        List<Tag> newTags = Lists.newArrayListWithCapacity(length);
        for (int i = 0; i < length; i++) {
            Tag tag = type.newInstance();
            tag.deserialize(input);
            newTags.add(tag);
        }
        this.tags = newTags;
    }

    /**
     * Clone this TagList.
     */
    @Override
    public TagList clone() {
        // copying is done by the constructor
        return new TagList(Lists.transform(this.getTags(), Tag::clone));
    }
}
