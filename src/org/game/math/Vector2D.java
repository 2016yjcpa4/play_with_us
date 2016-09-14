package org.game.math;

/**
 * 벡터는 A 와 B 에 대한 '방향'에 대한 정보를 연산할 때 사용합니다.
 */
public class Vector2D {

    private double x;
    private double y;
    
    public Vector2D() {
        this(0, 0);
    }

    public Vector2D(Point2D p) {
        this(p.getX(), p.getY());
    }

    public Vector2D(Vector2D v) {
        this(v.getX(), v.getY());
    }
    
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getAngle() {
        return Math.atan2(this.y, this.x);
    }
    
    public double getLengthSquared() {
        return scalar(this);
    }
    
    public double getLength() {
        return Math.sqrt(getLengthSquared());
    }
    
    public Vector2D setNormalize() {
        double len = getLength();
        
        if (len > 0) {
            this.x = this.x / len;
            this.y = this.y / len;
        }
        
        return this;
    }
    
    public Vector2D setNegative() {
        this.x = -this.x;
        this.y = -this.y;
        
        return this;
    }
    
    public Vector2D setPerpendicular() {
        double x = this.x; 
        double y = this.y;
        
        this.x = y;
        this.y = -x;
        
        return this;
    }
    
    public double scalar(double x, double y)    { return this.x * x + this.y * y; }
    public double scalar(double n)              { return scalar(n, n); }
    public double scalar(Vector2D v)            { return scalar(v.x, v.y); }
    public double scalar(Point2D p)             { return scalar(p.getX(), p.getY()); }
    
    public Vector2D add(double x, double y)     { return new Vector2D(this.x + x, this.y + y); }
    public Vector2D add(double n)               { return add(n, n); }
    public Vector2D add(Vector2D v)             { return add(v.x, v.y); }
    public Vector2D add(Point2D p)              { return add(p.getX(), p.getY()); }
    
    public Vector2D sub(double x, double y)     { return new Vector2D(this.x - x, this.y - y); }
    public Vector2D sub(double n)               { return sub(n, n); }
    public Vector2D sub(Vector2D v)             { return sub(v.x, v.y); }
    public Vector2D sub(Point2D p)              { return sub(p.getX(), p.getY()); }
    
    public Vector2D scale(double x, double y)   { return new Vector2D(this.x * x, this.y * y); }
    public Vector2D scale(double n)             { return scale(n, n); }
    public Vector2D scale(Point2D p)            { return scale(p.getX(), p.getY()); }
    public Vector2D scale(Vector2D v)           { return scale(v.x, v.y); }
}
