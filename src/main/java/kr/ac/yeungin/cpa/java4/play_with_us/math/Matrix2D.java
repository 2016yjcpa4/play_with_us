package kr.ac.yeungin.cpa.java4.play_with_us.math; 

public class Matrix2D {
    
    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final double e;
    private final double f;
    
    public Matrix2D() {
        this(1, 0, 0, 1, 0, 0);
    }
    
    public Matrix2D(double a,
                     double b,
                     double c,
                     double d,
                     double e,
                     double f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }
    
    public Matrix2D concat(Matrix2D o) {
        return new Matrix2D(
            a * o.a + c * o.b,
            b * o.a + d * o.b,
            a * o.c + c * o.d,
            b * o.c + d * o.d,
            a * o.e + c * o.f + e,
            b * o.e + d * o.f + f
        );
    } 
    
    public double getA() {
        return a;
    }
    
    public double getB() {
        return b;
    }
    
    public double getC() {
        return c;
    }
    
    public double getD() {
        return d;
    }
    
    public double getE() {
        return e;
    }
    
    public double getF() {
        return f;
    }
    
    public static Matrix2D translate(double x, double y) {
        return new Matrix2D(1, 0, 0, 1, x, y);
    }
    
    public static Matrix2D rotate(double rad) {
        return  new Matrix2D(Math.cos(rad), 
                             Math.sin(rad), 
                            -Math.sin(rad), 
                            Math.cos(rad), 
                            0, 
                            0);
    }
    
    public static Matrix2D scale(double s) {
        return scale(s, s);
    }
    
    public static Matrix2D scale(double sx, double sy) {
        return new Matrix2D(sx, 0, 0, sy, 0, 0);
    }
}