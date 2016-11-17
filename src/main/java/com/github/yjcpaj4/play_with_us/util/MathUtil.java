package com.github.yjcpaj4.play_with_us.util;

public class MathUtil {

    public static double getNormalDegrees(double d) {
        return (d %= 360) >= 0 ? d : (d + 360);
    }
    
    public static char getSimpleDirectionByRadian(double d) {
        return getSimpleDirectionByDegree(Math.toDegrees(d));
    }

    public static char getSimpleDirectionByDegree(double d) {
        d = getNormalDegrees(d);
        
        if (45 <= d && d < 135) {
            return 's';
        }
        else if (135 <= d && d < 225) {
            return 'w';
        }
        else if (225 <= d && d < 315) {
            return 'n';
        }
        else {
            return 'e';
        }
    }
    
    public static String getDirectionByRadian(double d) {
        return getDirectionByDegree(Math.toDegrees(d));
    }
    
    public static String getDirectionByDegree(double d) {
        d = getNormalDegrees(d);
        
        if (22 <= d && d < 67) {
            return "se";
        }
        else if (67 <= d && d < 112) {
            return "s";
        } 
        else if (112 <= d && d < 157) {
            return "sw";
        }
        else if (157 <= d && d < 202) {
            return "w";
        }
        else if (202 <= d && d < 247) {
            return "nw";
        }
        else if (247 <= d && d < 292) {
            return "n";
        }
        else if (292 <= d && d < 337) {
            return "ne";
        }
        else {
            return "e";
        }
    }
}
