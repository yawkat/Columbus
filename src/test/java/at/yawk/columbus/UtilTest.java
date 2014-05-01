package at.yawk.columbus;

import org.junit.Assert;
import org.junit.Test;

public class UtilTest {
    @Test
    public void testIsAllZero() {
        Assert.assertTrue(Util.isAllZero((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0));
        Assert.assertFalse(Util.isAllZero((short) 1, (short) 1, (short) 1, (short) 1, (short) 1, (short) 1, (short) 1, (short) 1, (short) 1));
        Assert.assertFalse(Util.isAllZero((short) 0, (short) 0, (short) 34, (short) 0, (short) 2, (short) 0, (short) 1, (short) 0, (short) 0, (short) 0));
        Assert.assertTrue(Util.isAllZero((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0));
        Assert.assertFalse(Util.isAllZero((byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1));
        Assert.assertFalse(Util.isAllZero((byte) 0, (byte) 0, (byte) 34, (byte) 0, (byte) 2, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0));
    }
    
    @Test
    public void testIsAll() {
        Assert.assertTrue(Util.isAll((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0));
        Assert.assertFalse(Util.isAll((short) 0, (short) 1, (short) 1, (short) 1, (short) 1, (short) 1, (short) 1, (short) 1, (short) 1, (short) 1));
        Assert.assertFalse(Util.isAll((short) 0, (short) 0, (short) 0, (short) 34, (short) 0, (short) 2, (short) 0, (short) 1, (short) 0, (short) 0, (short) 0));
        Assert.assertTrue(Util.isAll((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0));
        Assert.assertFalse(Util.isAll((byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1));
        Assert.assertFalse(Util.isAll((byte) 0, (byte) 0, (byte) 0, (byte) 34, (byte) 0, (byte) 2, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0));
    }
    
    @Test
    public void testHalf() {
        byte[] sample = {
                (byte) 0xE,
                (byte) 0xF,
                (byte) 0x6,
                (byte) 0xC,
                (byte) 0x8,
                (byte) 0x3, };
        byte[] half = {
                (byte) 0xFE,
                (byte) 0xC6,
                (byte) 0x38, };
        Assert.assertArrayEquals(half, Util.half(sample));
    }
    
    @Test
    public void testTwice() {
        byte[] sample = {
                (byte) 0xFE,
                (byte) 0xC6,
                (byte) 0x38, };
        byte[] twice = {
                (byte) 0xE,
                (byte) 0xF,
                (byte) 0x6,
                (byte) 0xC,
                (byte) 0x8,
                (byte) 0x3, };
        Assert.assertArrayEquals(twice, Util.twice(sample));
    }
    
    @Test
    public void testSplit() {
        short[] sample = {
                (short) 0xABCD,
                (short) 0x5732,
                (short) 0x6F4A,
                (short) 0xE45A, };
        byte[][] split = {
                {
                        (byte) 0xCD,
                        (byte) 0x32,
                        (byte) 0x4A,
                        (byte) 0x5A, },
                {
                        (byte) 0xAB,
                        (byte) 0x57,
                        (byte) 0x6F,
                        (byte) 0xE4, } };
        Assert.assertArrayEquals(split, Util.split(sample));
    }
}
