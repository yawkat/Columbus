package at.yawk.columbus.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * int array.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public final class TagArrayInt extends TagValue {
    private int[] value;
    
    @Override
    void serialize(DataOutput output) throws IOException {
        int[] currentValue = this.getValue();
        output.writeInt(currentValue.length);
        byte[] buf = new byte[currentValue.length << 2];
        ByteBuffer.wrap(buf).asIntBuffer().put(currentValue);
        output.write(buf);
    }
    
    @Override
    void deserialize(DataInput input) throws IOException {
        int length = input.readInt();
        byte[] buf = new byte[length << 2];
        input.readFully(buf);
        int[] newValue = new int[length];
        ByteBuffer.wrap(buf).asIntBuffer().get(newValue);
        this.setValue(newValue);
    }
    
    @Override
    public TagArrayInt clone() {
        return new TagArrayInt(this.getValue().clone());
    }
}
