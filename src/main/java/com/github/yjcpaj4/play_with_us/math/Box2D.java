package com.github.yjcpaj4.play_with_us.math;

import com.github.yjcpaj4.play_with_us.geom.Polygon;
import java.util.ArrayList;
import java.util.List;

public class Box2D {
    
    private float mX;
    private float mY;
    private float mWidth;
    private float mHeight;
    
    public Box2D(int x, int y, int w, int h) {
        mX = x;
        mY = y;
        mWidth = w;
        mHeight = h;
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
