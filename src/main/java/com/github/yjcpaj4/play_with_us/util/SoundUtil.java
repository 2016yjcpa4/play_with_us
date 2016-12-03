package com.github.yjcpaj4.play_with_us.util;

import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class SoundUtil {

    private SoundUtil() {
    }

    public static double getVolumeByDistance(Point2D p1, Point2D p2, float n) {
        return getVolumeByDistance(p1.getX(), p1.getY(), p2.getX(), p2.getY(), n);
    }
    
    public static double getVolumeByDistance(float x1, float y1, float x2, float y2, float n) {
        Vector2D v1 = new Vector2D(x1, y1);
        Vector2D v2 = new Vector2D(x2, y2);
        float l = v1.subtract(v2).length();
        
        return 1.0 - Math.max(0, Math.min(1, l / n));
    }
}
