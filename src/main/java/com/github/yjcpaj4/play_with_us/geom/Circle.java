package com.github.yjcpaj4.play_with_us.geom;

import com.github.yjcpaj4.play_with_us.math.Point2D;

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
