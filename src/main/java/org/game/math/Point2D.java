package org.game.math;

import java.util.List;

public class Point2D {
    
    private int mX;
    private int mY;
    
    public Point2D() {
        this(0, 0);
    }
    
    public Point2D(int x, int y) {
        mX = x;
        mY = y;
    }
    
    public void setX(int x) {
        mX = x;
    }
    
    public void setY(int y) {
        mY = y;
    }
    
    public void set(int x, int y) {
        mX = x;
        mY = y;
    }
    
    public int getX() {
        return mX;
    }
    
    public int getY() {
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
}
