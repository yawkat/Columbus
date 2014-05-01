package at.yawk.columbus;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Various hardcoded values used by minecraft.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Tables {
    static final int[] oppositeSide = new int[]{1, 0, 3, 2, 5, 4};
    static final int[] offsetsXForSide = new int[]{0, 0, 0, 0, -1, 1};
    static final int[] offsetsYForSide = new int[]{-1, 1, 0, 0, 0, 0};
    static final int[] offsetsZForSide = new int[]{0, 0, -1, 1, 0, 0};

    static void putDefaultLightValues(Lighter lighter) {
        lighter.putBlockLightData(1, 255, 0);
        lighter.putBlockLightData(2, 255, 0);
        lighter.putBlockLightData(3, 255, 0);
        lighter.putBlockLightData(4, 255, 0);
        lighter.putBlockLightData(5, 255, 0);
        lighter.putBlockLightData(7, 255, 0);
        lighter.putBlockLightData(8, 3, 0);
        lighter.putBlockLightData(9, 3, 0);
        lighter.putBlockLightData(10, 0, 15);
        lighter.putBlockLightData(11, 0, 15);
        lighter.putBlockLightData(12, 255, 0);
        lighter.putBlockLightData(13, 255, 0);
        lighter.putBlockLightData(14, 255, 0);
        lighter.putBlockLightData(15, 255, 0);
        lighter.putBlockLightData(16, 255, 0);
        lighter.putBlockLightData(17, 255, 0);
        lighter.putBlockLightData(18, 1, 0);
        lighter.putBlockLightData(19, 255, 0);
        lighter.putBlockLightData(21, 255, 0);
        lighter.putBlockLightData(22, 255, 0);
        lighter.putBlockLightData(23, 255, 0);
        lighter.putBlockLightData(24, 255, 0);
        lighter.putBlockLightData(25, 255, 0);
        lighter.putBlockLightData(30, 1, 0);
        lighter.putBlockLightData(35, 255, 0);
        lighter.putBlockLightData(39, 0, 1);
        lighter.putBlockLightData(41, 255, 0);
        lighter.putBlockLightData(42, 255, 0);
        lighter.putBlockLightData(43, 255, 0);
        lighter.putBlockLightData(44, 255, 0);
        lighter.putBlockLightData(45, 255, 0);
        lighter.putBlockLightData(46, 255, 0);
        lighter.putBlockLightData(47, 255, 0);
        lighter.putBlockLightData(48, 255, 0);
        lighter.putBlockLightData(49, 255, 0);
        lighter.putBlockLightData(50, 0, 14);
        lighter.putBlockLightData(51, 0, 15);
        lighter.putBlockLightData(53, 255, 0);
        lighter.putBlockLightData(56, 255, 0);
        lighter.putBlockLightData(57, 255, 0);
        lighter.putBlockLightData(58, 255, 0);
        lighter.putBlockLightData(60, 255, 0);
        lighter.putBlockLightData(61, 255, 0);
        lighter.putBlockLightData(62, 255, 13);
        lighter.putBlockLightData(67, 255, 0);
        lighter.putBlockLightData(73, 255, 0);
        lighter.putBlockLightData(74, 255, 9);
        lighter.putBlockLightData(76, 0, 7);
        lighter.putBlockLightData(79, 3, 0);
        lighter.putBlockLightData(80, 255, 0);
        lighter.putBlockLightData(82, 255, 0);
        lighter.putBlockLightData(84, 255, 0);
        lighter.putBlockLightData(86, 255, 0);
        lighter.putBlockLightData(87, 255, 0);
        lighter.putBlockLightData(88, 255, 0);
        lighter.putBlockLightData(89, 255, 15);
        lighter.putBlockLightData(90, 0, 11);
        lighter.putBlockLightData(91, 255, 15);
        lighter.putBlockLightData(94, 0, 9);
        lighter.putBlockLightData(95, 255, 15);
        lighter.putBlockLightData(97, 255, 0);
        lighter.putBlockLightData(98, 255, 0);
        lighter.putBlockLightData(99, 255, 0);
        lighter.putBlockLightData(100, 255, 0);
        lighter.putBlockLightData(103, 255, 0);
        lighter.putBlockLightData(108, 255, 0);
        lighter.putBlockLightData(109, 255, 0);
        lighter.putBlockLightData(110, 255, 0);
        lighter.putBlockLightData(112, 255, 0);
        lighter.putBlockLightData(114, 255, 0);
        lighter.putBlockLightData(117, 0, 1);
        lighter.putBlockLightData(119, 0, 15);
        lighter.putBlockLightData(120, 0, 1);
        lighter.putBlockLightData(121, 255, 0);
        lighter.putBlockLightData(122, 0, 1);
        lighter.putBlockLightData(123, 255, 0);
        lighter.putBlockLightData(124, 255, 15);
        lighter.putBlockLightData(125, 255, 0);
        lighter.putBlockLightData(126, 255, 0);
        lighter.putBlockLightData(128, 255, 0);
        lighter.putBlockLightData(129, 255, 0);
        lighter.putBlockLightData(130, 0, 7);
        lighter.putBlockLightData(133, 255, 0);
        lighter.putBlockLightData(134, 255, 0);
        lighter.putBlockLightData(135, 255, 0);
        lighter.putBlockLightData(136, 255, 0);
        lighter.putBlockLightData(137, 255, 0);
        lighter.putBlockLightData(138, 0, 15);
        lighter.putBlockLightData(150, 0, 9);
        lighter.putBlockLightData(152, 255, 0);
        lighter.putBlockLightData(153, 255, 0);
        lighter.putBlockLightData(155, 255, 0);
        lighter.putBlockLightData(156, 255, 0);
        lighter.putBlockLightData(158, 255, 0);
        lighter.putBlockLightData(159, 255, 0);
        lighter.putBlockLightData(170, 255, 0);
        lighter.putBlockLightData(172, 255, 0);
        lighter.putBlockLightData(173, 255, 0);
    }
}
