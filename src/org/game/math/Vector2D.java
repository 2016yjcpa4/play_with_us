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
    
    public void set(double x, double y) {
        this.dx = x;
        this.dy = y;
    }
    
    public double angle() {
        return Math.atan2(dy, dx);
    }
    
    public double length() {
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public void normalize() {
        double len = length();
        dx = dx / len;
        dy = dy / len;
    }
    
    public double dotProduct(double x, double y) { return dx * x + dy * y; }
    public double dotProduct(Vector2D v) { return dotProduct(v.dx, v.dy); }
    public double dotProduct(Point2D p) { return dotProduct(p.getX(), p.getY()); }
    
    public double crossProduct(Vector2D v) { return crossProduct(v.dx, v.dy); }
    public double crossProduct(Point2D p) { return crossProduct(p.getX(), p.getY()); }
    public double crossProduct(double x, double y) { return dx * y - dy * x; }
    
    public Vector2D sum(double x, double y) { return new Vector2D(this.dx + x, this.dy + y); }
    public Vector2D sum(Vector2D v) { return sum(v.dx, v.dy); }
    public Vector2D sum(Point2D p) { return sum(p.getX(), p.getY()); }
    
    public Vector2D sub(double x, double y) { return new Vector2D(this.dx - x, this.dy - y); }
    public Vector2D sub(Vector2D v) { return sub(v.dx, v.dy); }
    public Vector2D sub(Point2D p) { return sub(p.getX(), p.getY()); }
    
    public Vector2D mult(double x, double y) { return new Vector2D(dx * x, dy * y); }
    public Vector2D mult(Point2D p) { return mult(p.getX(), p.getY()); }
    public Vector2D mult(Vector2D v) { return mult(v.dx, v.dy); }
}
