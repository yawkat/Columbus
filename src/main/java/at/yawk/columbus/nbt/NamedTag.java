package at.yawk.columbus.nbt;

import lombok.Value;

/**
 * Key-value NBT tag combination.
 */
@Value
public class NamedTag implements Cloneable {
    private final String name;
    private final Tag value;
    
    @Override
    public NamedTag clone() {
        return new NamedTag(this.getName(), this.getValue().clone());
    }
}
