package com.github.yjcpaj4.play_with_us.util;

import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

public class AWTUtil {

    private AWTUtil() {
    }
    
    public static Polygon getPolygon(Area a) {
        return new Polygon(getPoints(a));
    }
    
    public static List<Point2D> getPoints(Area a) {
        List<Point2D> l = new ArrayList<>();
        PathIterator p = a.getPathIterator(null);
        
        while ( ! p.isDone()) {
            float[] f = new float[6];
            if (p.currentSegment(f) != PathIterator.SEG_CLOSE) {
                l.add(new Point2D(f[0], f[1]));
            }
            
            p.next();
        }
        
        return l;
    }
}
