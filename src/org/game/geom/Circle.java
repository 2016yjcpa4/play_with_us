package org.game.geom;

import org.game.math.Point2D;
import org.game.math.Vector2D;

public class Circle implements Shape {
    
    protected Point2D pos = new Point2D();
    protected int rad;
     
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
