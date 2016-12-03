package com.github.yjcpaj4.play_with_us.math;

import com.github.yjcpaj4.play_with_us.geom.Polygon;
import java.util.ArrayList;
import java.util.List;

public class Box2D {
    
    private float mX;
    private float mY;
    private float mWidth;
    private float mHeight;
    
    public Box2D(float x, float y, float w, float h) {
        mX = x;
        mY = y;
        mWidth = w;
        mHeight = h;
    }
    
    public void setMargins(float m) {
        mX -= m / 2;
        mY -= m / 2;
        mWidth += m;
        mHeight += m;
    }
    
    public Polygon toPolygon() {
        List<Point2D> l = new ArrayList<>();
        l.add(new Point2D(mX, mY));
        l.add(new Point2D(mX + mWidth, mY));
        l.add(new Point2D(mX + mWidth, mY + mHeight));
        l.add(new Point2D(mX, mY + mHeight));
        return new Polygon(l);
    }
}
