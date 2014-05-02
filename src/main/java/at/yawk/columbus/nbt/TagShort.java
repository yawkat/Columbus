package at.yawk.columbus.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * short value.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public final class TagShort extends TagValue {
    private short value;
    
    @Override
    void serialize(DataOutput output) throws IOException {
        output.writeShort(this.getValue());
    }
    
    @Override
    void deserialize(DataInput input) throws IOException {
        this.setValue(input.readShort());
    }

    @Override
    public Number getNumber() {
        return getValue();
    }
    
    @Override
    public TagShort clone() {
        return new TagShort(this.getValue());
    }
}
