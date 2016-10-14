package kr.ac.yeungin.cpa.java4.play_with_us.geom;

import java.util.ArrayList;
import java.util.List;
import kr.ac.yeungin.cpa.java4.play_with_us.math.Point2D;
import kr.ac.yeungin.cpa.java4.play_with_us.math.Vector2D;

public class Circle implements Shape { 
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
