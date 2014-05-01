package at.yawk.columbus.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * byte array.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public final class TagArrayByte extends TagValue {
    private byte[] value;
    
    @Override
    void serialize(DataOutput output) throws IOException {
        byte[] currentValue = this.getValue();
        output.writeInt(currentValue.length);
        output.write(currentValue);
    }
    
    @Override
    void deserialize(DataInput input) throws IOException {
        int length = input.readInt();
        byte[] newValue = new byte[length];
        input.readFully(newValue);
        this.setValue(newValue);
    }
    
    @Override
    public TagArrayByte clone() {
        return new TagArrayByte(this.getValue().clone());
    }
}
