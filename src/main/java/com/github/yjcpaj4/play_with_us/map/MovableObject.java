package com.github.yjcpaj4.play_with_us.map;

import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;

public abstract class MovableObject extends GameObject {

    protected Vector2D mDir = new Vector2D();
    protected Point2D mPos = new Point2D(50, 50); 
    protected Vector2D mVel = new Vector2D(0, 0);
    
    public MovableObject() {
    }
    
    public Point2D getPosition() {
        return mPos;
    }
    
    public double getAngle() {
        return mDir.sub(getPosition()).angle();
    }
}
