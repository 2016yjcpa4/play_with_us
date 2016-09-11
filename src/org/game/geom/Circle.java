package org.game.geom;

import java.util.ArrayList;
import java.util.List;
import org.game.math.Point2D;

public class Circle {
    
    private Point2D pos = new Point2D();
    private int rad;
     
    public Circle(int x, int y, int rad) {
        this.pos.set(x, y);
        this.rad = rad;
    }
    
    public int getRadius() {
        return rad;
    }
    
    public Point2D getPosition() {
        return pos;
    }
}
