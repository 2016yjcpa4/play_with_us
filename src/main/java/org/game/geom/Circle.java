package org.game.geom;

import java.util.ArrayList;
import java.util.List; 
import org.game.math.Point2D;

public class Circle implements Shape { 
    
    private Point2D mPos = new Point2D();
    private int mRadius;
     
    public Circle(int x, int y, int r) {
        mPos.set(x, y);
        mRadius = r;
    }
    
    public int getRadius() {
        return mRadius;
    }
    
    public Point2D getPosition() {
        return mPos;
    }
}
