package com.github.yjcpaj4.play_with_us.util;

public class GameUtil {
    
    private GameUtil() {
    }
    
    public static String getDirectionByRadian(double d) {
        return getDirectionByDegree(Math.toDegrees(d));
    }
    
    public static String getDirectionByDegree(double d) {
        d = MathUtil.getNormalDegrees(d);
        
        if (22 <= d && d < 67)        return "se";
        else if (67 <= d && d < 112)  return "s"; 
        else if (112 <= d && d < 157) return "sw";
        else if (157 <= d && d < 202) return "w";
        else if (202 <= d && d < 247) return "nw";
        else if (247 <= d && d < 292) return "n";
        else if (292 <= d && d < 337) return "ne";
        else                          return "e"; // 오른쪽으로
    }
}
