package at.yawk.columbus.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Abstract NBT tag class.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class Tag implements Cloneable {
    @Getter(lazy = true) private final TagType type = this.lookupType();
    
    private final TagType lookupType() {
        return TagType.forClass(this.getClass());
    }
    
    abstract void serialize(DataOutput output) throws IOException;
    
    abstract void deserialize(DataInput input) throws IOException;

    /**
     * Return the byte value of this tag.
     *
     * @throws java.lang.ClassCastException if this is not a TagByte.
     */
    public byte getByte() {
        return ((TagByte) this).getValue();
    }

    /**
     * Return the short value of this tag.
     *
     * @throws java.lang.ClassCastException if this is not a TagShort.
     */
    public short getShort() {
        return ((TagShort) this).getValue();
    }

    /**
     * Return the int value of this tag.
     *
     * @throws java.lang.ClassCastException if this is not a TagInt.
     */
    public int getInt() {
        return ((TagInt) this).getValue();
    }

    /**
     * Return the float value of this tag.
     *
     * @throws java.lang.ClassCastException if this is not a TagFloat.
     */
    public float getFloat() {
        return ((TagFloat) this).getValue();
    }

    /**
     * Return the double value of this tag.
     *
     * @throws java.lang.ClassCastException if this is not a TagDouble.
     */
    public double getDouble() {
        return ((TagDouble) this).getValue();
    }

    /**
     * Return the String value of this tag.
     *
     * @throws java.lang.ClassCastException if this is not a TagString.
     */
    public String getString() {
        return ((TagString) this).getValue();
    }

    /**
     * Clone this tag.
     */
    @Override
    public abstract Tag clone();
}
