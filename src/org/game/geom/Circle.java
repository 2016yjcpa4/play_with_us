package org.game.geom;

import java.util.ArrayList;
import java.util.List;
import org.game.math.Point2D;

public class Circle extends Polygon {
    
    private int rad; 
    
    public Circle(int x, int y, int rad) {
        List<Point2D> l = new ArrayList<>();
        
        double ang = 0.0;
        
        l.add(new Point2D(x + rad, y));
        
        for (int n = 0; n < 10; ++n) {
            
            double dx = rad * Math.cos(ang);
            double dy = rad * Math.sin(ang);
            
            l.add(new Point2D((int) (x + dx), (int) (y + dy)));
            
            ang += 2.0 * Math.PI / 10.0;
        }
        
        l.add(new Point2D(x + rad, y));
        
        addAll(l);
    }
    
    public int getRadius() {
        return rad;
    }
}
