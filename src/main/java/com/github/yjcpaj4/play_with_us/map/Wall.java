package com.github.yjcpaj4.play_with_us.map;

import java.awt.Color;
import java.awt.Graphics2D;
import com.github.yjcpaj4.play_with_us.CanvasApplication;
import com.github.yjcpaj4.play_with_us.GraphicLooper;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.geom.Rect; 
import com.github.yjcpaj4.play_with_us.math.Point2D;

public class Wall extends MapObject {
    
    private Polygon mCollider;
    
    public Wall(int x, int y, int w, int h) {
        mCollider = new Rect(x, y, w, h);
    }
    
    public Polygon getCollider() {
        return mCollider;
    }
    
    public Point2D getPosition () {
        return mCollider.getPosition();
    }

    @Override
    public void draw(long delta, Graphics2D g2d) {
    }

    @Override
    public void update(long delta) { 
    }
    
}