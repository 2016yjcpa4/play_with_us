package org.game.math;

/**
 * 벡터는 A 와 B 에 대한 '방향'에 대한 정보를 연산할 때 사용합니다.
 */
public class Vector2D {

    private double dx;
    private double dy;
    
    public Vector2D() {
        this(0, 0);
    }

    public Vector2D(Point2D p) {
        this(p.getX(), p.getY());
    }

    public Vector2D(Vector2D v) {
        this(v.getX(), v.getY());
    }
    
    public Vector2D(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    public double getX() {
        return dx;
    }
    
    public double getY() {
        return dy;
    }
    
    public void setX(double x) {
        this.dx = x;
    }
    
    public void setY(double y) {
        this.dy = y;
    }
    
    public double angle() {
        return Math.atan2(dy, dx);
    }
    
    public double len2() {
        return dot(this);
    }
    
    public double len() {
        return Math.sqrt(len2());
    }
    
    public Vector2D normalize() {
        double len = len();
        
        if (len > 0) {
            dx = dx / len;
            dy = dy / len;
        }
        
        return this;
    }
    
    public Vector2D reverse() {
        dx = -dx;
        dy = -dy;
        
        return this;
    }
    
    public Vector2D perp() {
        double x = dx; 
        
        this.dx = dy;
        this.dy = -x;
        
        return this;
    }
    
    public double dot(double x, double y)        { return dx * x + dy * y; }
    public double dot(double n)                  { return dot(n, n); }
    public double dot(Vector2D v)                { return dot(v.dx, v.dy); }
    public double dot(Point2D p)                 { return dot(p.getX(), p.getY()); }
    
    public Vector2D add(double x, double y)      { return new Vector2D(this.dx + x, this.dy + y); }
    public Vector2D add(double n)                { return Vector2D.this.add(n, n); }
    public Vector2D add(Vector2D v)              { return Vector2D.this.add(v.dx, v.dy); }
    public Vector2D add(Point2D p)               { return Vector2D.this.add(p.getX(), p.getY()); }
    
    public Vector2D sub(double x, double y)      { return new Vector2D(this.dx - x, this.dy - y); }
    public Vector2D sub(double n)                { return sub(n, n); }
    public Vector2D sub(Vector2D v)              { return sub(v.dx, v.dy); }
    public Vector2D sub(Point2D p)               { return sub(p.getX(), p.getY()); }
    
    public Vector2D scale(double x, double y)    { return new Vector2D(dx * x, dy * y); }
    public Vector2D scale(double n)              { return Vector2D.this.scale(n, n); }
    public Vector2D scale(Point2D p)             { return Vector2D.this.scale(p.getX(), p.getY()); }
    public Vector2D scale(Vector2D v)            { return Vector2D.this.scale(v.dx, v.dy); }
}
