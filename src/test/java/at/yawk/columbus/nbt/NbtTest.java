package at.yawk.columbus.nbt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import at.yawk.columbus.nbt.NBT;
import at.yawk.columbus.nbt.NamedTag;
import at.yawk.columbus.nbt.TagArrayByte;
import at.yawk.columbus.nbt.TagByte;
import at.yawk.columbus.nbt.TagCompound;
import at.yawk.columbus.nbt.TagDouble;
import at.yawk.columbus.nbt.TagFloat;
import at.yawk.columbus.nbt.TagInt;
import at.yawk.columbus.nbt.TagList;
import at.yawk.columbus.nbt.TagLong;
import at.yawk.columbus.nbt.TagShort;
import at.yawk.columbus.nbt.TagString;

public class NbtTest {
    private InputStream getResource(String name) {
        InputStream stream = NbtTest.class.getResourceAsStream(name);
        assertNotNull(stream);
        return stream;
    }
    
    private NamedTag small() {
        return new NamedTag("hello world", new TagCompound(new NamedTag("name", new TagString("Bananrama"))));
    }
    
    private NamedTag big() {
        TagCompound c1 = new TagCompound();
        {
            TagCompound c2 = new TagCompound();
            c1.addTag("nested compound test", c2);
            {
                TagCompound c3 = new TagCompound();
                c2.addTag("egg", c3);
                c3.addTag("name", new TagString("Eggbert"));
                c3.addTag("value", new TagFloat(0.5F));
            }
            {
                TagCompound c3 = new TagCompound();
                c2.addTag("ham", c3);
                c3.addTag("name", new TagString("Hampus"));
                c3.addTag("value", new TagFloat(0.75F));
            }
        }
        c1.addTag("intTest", new TagInt(2147483647));
        c1.addTag("longTest", new TagLong(9223372036854775807L));
        c1.addTag("byteTest", new TagByte((byte) 127));
        c1.addTag("stringTest", new TagString("HELLO WORLD THIS IS A TEST STRING ÅÄÖ!"));
        c1.addTag("listTest (long)", new TagList(new TagLong(11), new TagLong(12), new TagLong(13), new TagLong(14), new TagLong(15)));
        c1.addTag("doubleTest", new TagDouble(0.49312871321823148));
        c1.addTag("floatTest", new TagFloat(0.49823147058486938F));
        {
            TagList c2 = new TagList();
            c1.addTag("listTest (compound)", c2);
            c2.addTag(new TagCompound(new NamedTag("created-on", new TagLong(1264099775885L)), new NamedTag("name", new TagString("Compound tag #0"))));
            c2.addTag(new TagCompound(new NamedTag("created-on", new TagLong(1264099775885L)), new NamedTag("name", new TagString("Compound tag #1"))));
        }
        byte[] b1 = new byte[1000];
        for (int i = 0; i < b1.length; i++) {
            b1[i] = (byte) ((i * i * 255 + i * 7) % 100);
        }
        c1.addTag("byteArrayTest (the first 1000 values of (n*n*255+n*7)%100, starting with n=0 (0, 62, 34, 16, 8, ...))", new TagArrayByte(b1));
        c1.addTag("shortTest", new TagShort((short) 32767));
        
        return new NamedTag("Level", c1);
    }
    
    @Test
    public void testReadSmall() throws IOException {
        NamedTag read = NBT.deserializeStream(this.getResource("small.nbt"));
        assertEquals(this.small(), read);
    }
    
    @Test
    public void testReadBig() throws IOException {
        NamedTag read = NBT.deserializeStreamZipped(this.getResource("big.nbt"));
        assertEquals(this.big(), read);
    }
    
    @Test
    public void testWriteSmall() throws IOException {
        ByteArrayOutputStream sm = new ByteArrayOutputStream();
        NBT.serializeStream(sm, this.small());
        byte[] d = sm.toByteArray();
        InputStream expected = this.getResource("small.nbt");
        for (byte element : d) {
            assertEquals(expected.read(), element);
        }
    }
    
    /*
    @Test
    public void testWriteBig() throws IOException {
        final ByteArrayOutputStream sm = new ByteArrayOutputStream();
        NBT.serializeStreamZipped(sm, this.big());
        final byte[] d = sm.toByteArray();
        final InputStream expected = this.getResource("big.nbt");
        for (final byte element : d) {
            assertEquals(expected.read(), element);
        }
    }
    */
}
