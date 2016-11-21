package com.github.yjcpaj4.play_with_us.math;

/**
 * 벡터는 A 와 B 에 대한 '방향'에 대한 정보를 연산할 때 사용합니다.
 */
public class Vector2D {

    private float mX;
    private float mY;
    
    public Vector2D() {
        this(0, 0);
    }

    public Vector2D(Point2D p) {
        this(p.getX(), p.getY());
    }

    public Vector2D(Vector2D v) {
        this(v.getX(), v.getY());
    }
    
    public Vector2D(float x, float y) {
        set(x, y);
    }
    
    public void set(float x, float y) {
        mX = x;
        mY = y;
    }
    
    public void set(Vector2D v) {
        set(v.getX(), v.getY());
    }
    
    public void set(Point2D p) {
        set(p.getX(), p.getY());
    }
    
    public float getX() {
        return mX;
    }
    
    public float getY() {
        return mY;
    }
    
    public float toAngle() {
        return (float) Math.atan2(getY(), getX());
    }
    
    public float length() {
        return (float) Math.sqrt(dotProduct(this));
    }
    
    public Vector2D normalize() { 
        return divide(length()); 
    }
    
    public Vector2D negative() {
        return new Vector2D(-getX(), -getY());
    }
    
    public Vector2D perp() {
        return new Vector2D(getY(), -getX());
    }
    
    public Point2D toPoint2D() {
        return new Point2D(mX, mY);
    }
    
    public float dotProduct(float x, float y)   { return getX() * x + getY() * y; }
    public float dotProduct(float n)            { return dotProduct(n, n); }
    public float dotProduct(Vector2D v)         { return dotProduct(v.getX(), v.getY()); }
    public float dotProduct(Point2D p)          { return dotProduct(p.getX(), p.getY()); }
    
    public Vector2D add(float x, float y)        { return new Vector2D(getX() + x, getY() + y); }
    public Vector2D add(float n)                 { return add(n, n); }
    public Vector2D add(Vector2D v)              { return add(v.getX(), v.getY()); }
    public Vector2D add(Point2D p)               { return add(p.getX(), p.getY()); }
    
    public Vector2D subtract(float x, float y)  { return new Vector2D(getX() - x, getY() - y); }
    public Vector2D subtract(float n)           { return subtract(n, n); }
    public Vector2D subtract(Vector2D v)        { return subtract(v.getX(), v.getY()); }
    public Vector2D subtract(Point2D p)         { return subtract(p.getX(), p.getY()); }
    
    public Vector2D multiply(float x, float y)  { return new Vector2D(getX() * x, getY() * y); }
    public Vector2D multiply(float n)           { return multiply(n, n); }
    public Vector2D multiply(Point2D p)         { return multiply(p.getX(), p.getY()); }
    public Vector2D multiply(Vector2D v)        { return multiply(v.getX(), v.getY()); }
    
    public Vector2D divide(float x, float y)    { return new Vector2D(getX() / x, getY() / y); }
    public Vector2D divide(float n)             { return divide(n, n); }
    public Vector2D divide(Point2D p)           { return divide(p.getX(), p.getY()); }
    public Vector2D divide(Vector2D v)          { return divide(v.getX(), v.getY()); }
}
