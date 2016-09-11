package org.game.math;

import java.util.List;

public class Point2D {
    
    private int x;
    private int y;
    
    public Point2D() {
        this(0, 0);
    }
    
    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }

    public static int[] getXPoints(List<Point2D> l) {
        int[] x = new int[l.size()];
        
        for (int n = 0; n < l.size(); ++n) {
            x[n] = l.get(n).getX();
        }
        
        return x;
    }
    
    public static int[] getYPoints(List<Point2D> l) {
        int[] y = new int[l.size()];
        
        for (int n = 0; n < l.size(); ++n) {
            y[n] = l.get(n).getY();
        }
        
        return y;
    }
}
