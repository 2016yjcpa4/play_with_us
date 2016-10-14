package kr.ac.yeungin.cpa.java4.play_with_us.math;

import java.util.List;

public class Point2D {
    
    public int x;
    public int y;
    
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
    
    public boolean equals(Object o) {
        
        Point2D p = (Point2D) o;
        
        return p.getX() == getX() && p.getY() == getY();
    }

    public static int[] getXPoints(List<Point2D> l) {
        int[] x = new int[l.size()];
        
        for (int n = 0; n < l.size(); ++n) {
            x[n] = (int) l.get(n).getX();
        }
        
        return x;
    }
    
    public static int[] getYPoints(List<Point2D> l) {
        int[] y = new int[l.size()];
        
        for (int n = 0; n < l.size(); ++n) {
            y[n] = (int) l.get(n).getY();
        }
        
        return y;
    }

    @Override
    public String toString() {
        return String.format("{x:%d, y:%d}", x, y);
    }
}
