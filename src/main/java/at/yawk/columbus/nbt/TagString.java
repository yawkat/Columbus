package at.yawk.columbus.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public final class TagString extends TagValue {
    private String value;
    
    @Override
    void serialize(DataOutput output) throws IOException {
        output.writeUTF(this.getValue());
    }
    
    @Override
    void deserialize(DataInput input) throws IOException {
        this.setValue(input.readUTF());
    }
    
    @Override
    public TagString clone() {
        return new TagString(this.getValue());
    }
}
