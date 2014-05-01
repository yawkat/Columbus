package at.yawk.columbus.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * long value.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public final class TagLong extends TagValue {
    private long value;
    
    @Override
    void serialize(DataOutput output) throws IOException {
        output.writeLong(this.getValue());
    }
    
    @Override
    void deserialize(DataInput input) throws IOException {
        this.setValue(input.readLong());
    }
    
    @Override
    public TagLong clone() {
        return new TagLong(this.getValue());
    }
}
