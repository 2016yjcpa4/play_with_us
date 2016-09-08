package org.game.geom;
 
import org.game.math.Point2D;

public class Circle implements Shape {
    
    private int rad;
    private Point2D pos = new Point2D(0, 0);

    public Circle(int rad) {
        this.rad = rad;
    }
    
    public int getRadius() {
        return rad;
    }
    
    public Point2D getPosition() {
        return pos;
    }

    @Override
    public boolean isCollide(Shape s) {
        return false;
    }
}
