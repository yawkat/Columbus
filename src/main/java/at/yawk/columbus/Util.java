package at.yawk.columbus;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Util {
    public static boolean isAllZero(byte... array) {
        return isAll((byte) 0, array);
    }

    public static boolean isAll(byte value, byte... array) {
        for (byte checking : array) {
            if (value != checking) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllZero(short... array) {
        return isAll((short) 0, array);
    }

    public static boolean isAll(short value, short... array) {
        for (short checking : array) {
            if (value != checking) {
                return false;
            }
        }
        return true;
    }

    public static byte[] half(byte... original) {
        assert (original.length & 1) == 0;
        byte[] n = new byte[original.length >> 1];
        for (int i = 0; i < n.length; i++) {
            n[i] = (byte) (((original[(i << 1) + 1] & 0xf) << 4) | original[i << 1] & 0xf);
        }
        return n;
    }

    public static byte[] twice(byte... original) {
        byte[] n = new byte[original.length << 1];
        for (int i = 0; i < original.length; i++) {
            n[(i << 1) + 1] = (byte) ((original[i] >> 4) & 0xF);
            n[i << 1] = (byte) (original[i] & 0xF);
        }
        return n;
    }

    public static byte[][] split(short... original) {
        byte[][] result = new byte[2][original.length];
        for (int i = 0; i < original.length; i++) {
            result[0][i] = (byte) (original[i] & 0xff);
            result[1][i] = (byte) ((original[i] >> 8) & 0xff);
        }
        return result;
    }

    public static short[] merge(byte[] lower, byte[] upper) {
        assert lower.length == upper.length;
        short[] result = new short[lower.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (short) ((lower[i] & 0xff) | ((upper[i] & 0xff) << 8));
        }
        return result;
    }

    /*
     * http://stackoverflow.com/questions/779519/delete-files-recursively-in-
     * java/8685959#8685959
     */
    public static void removeRecursive(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                // try to delete the file anyway, even if its attributes
                // could not be read, since delete-only access is
                // theoretically possible
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    // directory iteration failed; propagate exception
                    throw exc;
                }
            }
        });
    }
}
