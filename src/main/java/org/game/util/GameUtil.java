package org.game.util;

public class GameUtil {
    
    private GameUtil() {
    }
    
    public static String getDirectionByRadian(double d) {
        return getDirectionByDegree(Math.toDegrees(d));
    }
    
    public static String getDirectionByDegree(double d) {
        d = MathUtil.getNormalDegrees(d);
        
        if (45 <= d && d < 135) {
            return "south"; // 밑으로
        }
        
        if (135 <= d && d < 225) {
            return "west"; // 왼쪽으로
        }
        
        if (225 <= d && d < 315) {
            return "north"; // 위로
        }
        
        return "east"; // 오른쪽으로
    }
}
