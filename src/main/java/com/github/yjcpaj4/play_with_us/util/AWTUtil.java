package com.github.yjcpaj4.play_with_us.util;

import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;

public class AWTUtil {

    public static Polygon toPolygon(Area a) {
        PathIterator p = a.getPathIterator(null);
        Polygon o = new Polygon();
        float[] b = new float[6];
        while ( ! p.isDone()) {
            
            if (p.currentSegment(b) != PathIterator.SEG_CLOSE) {
                o.addPoint(new Point2D((int) b[0], (int) b[1]));
            }
            
            p.next();
        }
        
        return o;
    }
    
}
