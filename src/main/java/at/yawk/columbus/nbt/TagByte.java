package at.yawk.columbus.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * byte value.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public final class TagByte extends TagValue {
    private byte value;
    
    @Override
    void serialize(DataOutput output) throws IOException {
        output.write(this.getValue());
    }
    
    @Override
    void deserialize(DataInput input) throws IOException {
        this.setValue(input.readByte());
    }
    
    @Override
    public TagByte clone() {
        return new TagByte(this.getValue());
    }
}
