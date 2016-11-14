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
        mX = x;
        mY = y;
    }
    
    public float getX() {
        return mX;
    }
    
    public float getY() {
        return mY;
    }
    
    public void setX(float x) {
        mX = x;
    }
    
    public void setY(float y) {
        mY = y;
    }
    
    public void set(Vector2D v) {
        mX = v.getX();
        mY = v.getY();
    }
    
    public void set(Point2D p) {
        mX = p.getX();
        mY = p.getY();
    }
    
    public float angle() {
        return (float) Math.atan2(getY(), getX());
    }
    
    public float lengthSq() {
        return dot(this);
    }
    
    public float length() {
        return (float) Math.sqrt(lengthSq());
    }
    
    public Vector2D norm() {
        return new Vector2D(getX() / length(), getY() / length());
    }
    
    public Vector2D neg() {
        return new Vector2D(-getX(), -getY());
    }
    
    public Vector2D perp() {
        return new Vector2D(getY(), -getX());
    }
    
    public Point2D toPoint2D() {
        return new Point2D(mX, mY);
    }
    
    public float dot(float x, float y)         { return getX() * x + getY() * y; }
    public float dot(float n)                  { return dot(n, n); }
    public float dot(Vector2D v)               { return dot(v.getX(), v.getY()); }
    public float dot(Point2D p)                { return dot(p.getX(), p.getY()); }
    
    public Vector2D add(float x, float y)       { return new Vector2D(getX() + x, getY() + y); }
    public Vector2D add(float n)                { return add(n, n); }
    public Vector2D add(Vector2D v)             { return add(v.getX(), v.getY()); }
    public Vector2D add(Point2D p)              { return add(p.getX(), p.getY()); }
    
    public Vector2D sub(float x, float y)       { return new Vector2D(getX() - x, getY() - y); }
    public Vector2D sub(float n)                { return sub(n, n); }
    public Vector2D sub(Vector2D v)             { return sub(v.getX(), v.getY()); }
    public Vector2D sub(Point2D p)              { return sub(p.getX(), p.getY()); }
    
    public Vector2D mult(float x, float y)      { return new Vector2D(getX() * x, getY() * y); }
    public Vector2D mult(float n)               { return mult(n, n); }
    public Vector2D mult(Point2D p)             { return mult(p.getX(), p.getY()); }
    public Vector2D mult(Vector2D v)            { return mult(v.getX(), v.getY()); }
    
    @Override
    public String toString() {
        return String.format("[ %d, %d ]", (int) mX, (int) mY);
    }
}
