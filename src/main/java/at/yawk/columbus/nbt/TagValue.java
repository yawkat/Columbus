package at.yawk.columbus.nbt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Abstract class for all value tags (byte[], int[], byte, double, float, int, long, short, String).
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class TagValue extends Tag {}
