package org.game.math;

/**
 * 벡터는 A 와 B 에 대한 '방향'에 대한 정보를 연산할 때 사용합니다.
 */
public class Vector2D {

    public float x;
    public float y;
    
    public Vector2D() {
        this(0, 0);
    }

    public Vector2D(Point2D p) {
        this(p.getX(), p.getY());
    }

    public Vector2D(Vector2D v) {
        this(v.getX(), v.getY());
    }
    
    public Vector2D(float dx, float dy) {
        this.x = dx;
        this.y = dy;
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public void setX(float dx) {
        this.x = dx;
    }
    
    public void setY(float dy) {
        this.y = dy;
    }
    
    public void set(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    public void set(Point2D v) {
        this.x = v.x;
        this.y = v.y;
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
        float n = (float) getLength();
        
        if (n > 0) {
            this.x = this.x / n;
            this.y = this.y / n;
        }
        
        return this;
    }
    
    public Vector2D setNegative() {
        this.x = -this.x;
        this.y = -this.y;
        
        return this;
    }
    
    public Vector2D setPerpendicular() {
        float dx = this.x; 
        float dy = this.y;
        
        this.x = dy;
        this.y = -dx;
        
        return this;
    }
    
    public double scalar(float dx, float dy)    { return this.x * dx + this.y * dy; }
    public double scalar(float d)              { return scalar(d, d); }
    public double scalar(Vector2D v)            { return scalar(v.x, v.y); }
    public double scalar(Point2D p)             { return scalar(p.getX(), p.getY()); }
    
    public Vector2D add(float dx, float dy)     { return new Vector2D(this.x + dx, this.y + dy); }
    public Vector2D add(float d)               { return add(d, d); }
    public Vector2D add(Vector2D v)             { return add(v.x, v.y); }
    public Vector2D add(Point2D p)              { return add(p.getX(), p.getY()); }
    
    public Vector2D sub(float dx, float dy)     { return new Vector2D(this.x - dx, this.y - dy); }
    public Vector2D sub(float d)               { return sub(d, d); }
    public Vector2D sub(Vector2D v)             { return sub(v.x, v.y); }
    public Vector2D sub(Point2D p)              { return sub(p.getX(), p.getY()); }
    
    public Vector2D scale(float dx, float dy)   { return new Vector2D(this.x * dx, this.y * dy); }
    public Vector2D scale(float d)             { return scale(d, d); }
    public Vector2D scale(Point2D p)            { return scale(p.getX(), p.getY()); }
    public Vector2D scale(Vector2D v)           { return scale(v.x, v.y); }
}
