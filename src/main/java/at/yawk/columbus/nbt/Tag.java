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
     * @throws java.lang.ClassCastException if this is not a numeric tag.
     */
    public byte getByte() {
        return getNumber().byteValue();
    }

    /**
     * Return the short value of this tag.
     *
     * @throws java.lang.ClassCastException if this is not a numeric tag.
     */
    public short getShort() {
        return getNumber().shortValue();
    }

    /**
     * Return the int value of this tag.
     *
     * @throws java.lang.ClassCastException if this is not a numeric tag.
     */
    public int getInt() {
        return getNumber().intValue();
    }

    /**
     * Return the long value of this tag.
     *
     * @throws java.lang.ClassCastException if this is not a numeric tag.
     */
    public long getLong() {
        return getNumber().longValue();
    }

    /**
     * Return the float value of this tag.
     *
     * @throws java.lang.ClassCastException if this is not a numeric tag.
     */
    public float getFloat() {
        return getNumber().floatValue();
    }

    /**
     * Return the double value of this tag.
     *
     * @throws java.lang.ClassCastException if this is not a numeric tag.
     */
    public double getDouble() {
        return getNumber().doubleValue();
    }

    /**
     * Return the numeric value of this tag.
     *
     * @throws java.lang.ClassCastException if this is not a numeric tag.
     */
    public Number getNumber() {
        throw new ClassCastException("Cannot cast " + this + " to Number");
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
     * Returns this tag as a compound tag.
     *
     * @throws java.lang.ClassCastException if this is not a TagCompound.
     */
    public TagCompound asCompound() {
        return (TagCompound) this;
    }

    /**
     * Returns this tag as a list tag.
     *
     * @throws java.lang.ClassCastException if this is not a TagList.
     */
    public TagList asList() {
        return (TagList) this;
    }

    /**
     * Clone this tag.
     */
    @Override
    public abstract Tag clone();
}
