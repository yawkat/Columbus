package at.yawk.columbus.nbt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Serialization and Deserialization of NBT data.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NBT {
    /**
     * Read a NamedTag from the given DataInput.
     */
    public static NamedTag deserialize(DataInput input) throws IOException {
        byte typeId = input.readByte();
        TagType type = TagType.forId(typeId);
        String name = input.readUTF();
        Tag tag = type.newInstance();
        tag.deserialize(input);
        return new NamedTag(name, tag);
    }

    /**
     * Write a NamedTag to the given DataOutput.
     */
    public static void serialize(DataOutput output, NamedTag tag) throws IOException {
        output.write(tag.getValue().getType().getId());
        output.writeUTF(tag.getName());
        tag.getValue().serialize(output);
    }

    /**
     * Read a NamedTag from the given InputStream.
     */
    public static NamedTag deserializeStream(InputStream stream) throws IOException {
        return deserialize(new DataInputStream(stream));
    }

    /**
     * Write a NamedTag to the given OutputStream.
     */
    public static void serializeStream(OutputStream stream, NamedTag tag) throws IOException {
        serialize(new DataOutputStream(stream), tag);
    }

    /**
     * Read a NamedTag from the given compressed stream.
     */
    public static NamedTag deserializeStreamZipped(InputStream stream) throws IOException {
        return deserializeStream(new GZIPInputStream(stream));
    }

    /**
     * Write a compressed NamedTag to the given stream.
     */
    public static void serializeStreamZipped(OutputStream stream, NamedTag tag) throws IOException {
        GZIPOutputStream gzip = new GZIPOutputStream(stream);
        serializeStream(gzip, tag);
        gzip.finish();
    }

    /**
     * Read a NamedTag from the given array.
     */
    public static NamedTag deserializeArray(byte[] array) throws IOException {
        return deserializeStream(new ByteArrayInputStream(array));
    }

    /**
     * Write a NamedTag to an array.
     */
    public static byte[] serializeArray(NamedTag tag) {
        ByteArrayOutputStream res = new ByteArrayOutputStream();
        try {
            serializeStream(res, tag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res.toByteArray();
    }
}
