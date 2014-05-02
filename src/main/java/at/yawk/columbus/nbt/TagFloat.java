package at.yawk.columbus.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * float value.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public final class TagFloat extends TagValue {
    private float value;
    
    @Override
    void serialize(DataOutput output) throws IOException {
        output.writeFloat(this.getValue());
    }
    
    @Override
    void deserialize(DataInput input) throws IOException {
        this.setValue(input.readFloat());
    }

    @Override
    public Number getNumber() {
        return getValue();
    }
    
    @Override
    public TagFloat clone() {
        return new TagFloat(this.getValue());
    }
}
