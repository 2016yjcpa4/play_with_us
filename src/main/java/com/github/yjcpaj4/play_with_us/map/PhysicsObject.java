package com.github.yjcpaj4.play_with_us.map;

import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Matrix2D;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import java.awt.Graphics2D;

public abstract class PhysicsObject extends GameObject {
    
    protected transient Polygon mCollider;
    
    public Polygon getCollider() {
        return mCollider;
    }
    
    public void setPosition(float x, float y) {
        mCollider.setPosition(x, y);
    }
    public void setPosition(Point2D p) {
        setPosition(p.getX(), p.getY());
    }
    
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
}
