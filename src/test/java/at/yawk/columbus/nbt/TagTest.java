package at.yawk.columbus.nbt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TagTest {
    @Test
    public void testString() {
        assertEquals("Test", new TagString("Test").getValue());
    }

    @Test(expected = ClassCastException.class)
    public void testStringFail() {
        new TagString("").getNumber();
    }

    @Test
    public void testByte() {
        assertEquals(1, new TagByte((byte) 1).getByte());
        assertEquals(1, new TagByte((byte) 1).getNumber().intValue());
        assertEquals(1, new TagByte((byte) 1).getDouble(), 0.00001);
    }
}
