package kr.ac.yeungin.cpa.java4.play_with_us.util;

public class MathUtil {

    public static double getNormalDegrees(double d) {
        return (d %= 360) >= 0 ? d : (d + 360);
    }

}
