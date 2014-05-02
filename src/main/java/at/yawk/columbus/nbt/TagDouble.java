package at.yawk.columbus.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * double value.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public final class TagDouble extends TagValue {
    private double value;
    
    @Override
    void serialize(DataOutput output) throws IOException {
        output.writeDouble(this.getValue());
    }
    
    @Override
    void deserialize(DataInput input) throws IOException {
        this.setValue(input.readDouble());
    }

    @Override
    public Number getNumber() {
        return getValue();
    }
    
    @Override
    public TagDouble clone() {
        return new TagDouble(this.getValue());
    }
}
