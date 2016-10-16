package org.game.util;

public class MathUtil {

    public static double getNormalDegrees(double d) {
        return (d %= 360) >= 0 ? d : (d + 360);
    }

}
