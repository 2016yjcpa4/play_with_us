package com.github.yjcpaj4.play_with_us.math;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Point2D {
    
    @SerializedName("x")
    private float mX;
    
    @SerializedName("y")
    private float mY;
    
    public Point2D() {
        this(0, 0);
    }
    
    public Point2D(float x, float y) {
        mX = x;
        mY = y;
    }
    
    public Point2D(Point2D p) {
        mX = p.getX();
        mY = p.getY();
    }
    
    public void setX(float x) {
        mX = x;
    }
    
    public void setY(float y) {
        mY = y;
    }
    
    public void set(float x, float y) {
        mX = x;
        mY = y;
    }
    
    public float getX() {
        return mX;
    }
    
    public float getY() {
        return mY;
    }
    
    public static int[] getXPoints(List<Point2D> l) {
        int[] r = new int[l.size()];
        
        for (int n = 0; n < l.size(); ++n) {
            r[n] = (int) l.get(n).getX();
        }
        
        return r;
    }
    
    public static int[] getYPoints(List<Point2D> l) {
        int[] r = new int[l.size()];
        
        for (int n = 0; n < l.size(); ++n) {
            r[n] = (int) l.get(n).getY();
        }
        
        return r;
    }
    
    @Override
    public String toString() {
        return String.format("[ %d, %d ]", (int) mX, (int) mY);
    }
}
