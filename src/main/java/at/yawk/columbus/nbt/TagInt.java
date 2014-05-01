package at.yawk.columbus.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * int value.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public final class TagInt extends TagValue {
    private int value;
    
    @Override
    void serialize(DataOutput output) throws IOException {
        output.writeInt(this.getValue());
    }
    
    @Override
    void deserialize(DataInput input) throws IOException {
        this.setValue(input.readInt());
    }
    
    @Override
    public TagInt clone() {
        return new TagInt(this.getValue());
    }
}
