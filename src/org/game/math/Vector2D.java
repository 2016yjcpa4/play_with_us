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
    
    public void setX(double dx) {
        this.dx = dx;
    }
    
    public void setY(double dy) {
        this.dy = dy;
    }
    
    public double getAngle() {
        return Math.atan2(this.dy, this.dx);
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
            this.dx = this.dx / len;
            this.dy = this.dy / len;
        }
        
        return this;
    }
    
    public Vector2D setNegative() {
        this.dx = -this.dx;
        this.dy = -this.dy;
        
        return this;
    }
    
    public Vector2D setPerpendicular() {
        double dx = this.dx; 
        double dy = this.dy;
        
        this.dx = dy;
        this.dy = -dx;
        
        return this;
    }
    
    public double scalar(double dx, double dy)    { return this.dx * dx + this.dy * dy; }
    public double scalar(double d)              { return scalar(d, d); }
    public double scalar(Vector2D v)            { return scalar(v.dx, v.dy); }
    public double scalar(Point2D p)             { return scalar(p.getX(), p.getY()); }
    
    public Vector2D add(double dx, double dy)     { return new Vector2D(this.dx + dx, this.dy + dy); }
    public Vector2D add(double d)               { return add(d, d); }
    public Vector2D add(Vector2D v)             { return add(v.dx, v.dy); }
    public Vector2D add(Point2D p)              { return add(p.getX(), p.getY()); }
    
    public Vector2D sub(double dx, double dy)     { return new Vector2D(this.dx - dx, this.dy - dy); }
    public Vector2D sub(double d)               { return sub(d, d); }
    public Vector2D sub(Vector2D v)             { return sub(v.dx, v.dy); }
    public Vector2D sub(Point2D p)              { return sub(p.getX(), p.getY()); }
    
    public Vector2D scale(double dx, double dy)   { return new Vector2D(this.dx * dx, this.dy * dy); }
    public Vector2D scale(double d)             { return scale(d, d); }
    public Vector2D scale(Point2D p)            { return scale(p.getX(), p.getY()); }
    public Vector2D scale(Vector2D v)           { return scale(v.dx, v.dy); }
}
