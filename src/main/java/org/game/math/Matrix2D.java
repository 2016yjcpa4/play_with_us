package org.game.math; 

public class Matrix2D {
    
    private final double mA;
    private final double mB;
    private final double mC;
    private final double mD;
    private final double mE;
    private final double mF;
    
    public Matrix2D() {
        this(1, 0, 0, 1, 0, 0);
    }
    
    public Matrix2D(double a,
                     double b,
                     double c,
                     double d,
                     double e,
                     double f) {
        mA = a;
        mB = b;
        mC = c;
        mD = d;
        mE = e;
        mF = f;
    }
    
    public Matrix2D concat(Matrix2D o) {
        return new Matrix2D(
            getA() * o.getA() + getC() * o.getB(),
            getB() * o.getA() + getD() * o.getB(),
            getA() * o.getC() + getC() * o.getD(),
            getB() * o.getC() + getD() * o.getD(),
            getA() * o.getE() + getC() * o.getF() + getE(),
            getB() * o.getE() + getD() * o.getF() + getF()
        );
    } 
    
    public double getA() {
        return mA;
    }
    
    public double getB() {
        return mB;
    }
    
    public double getC() {
        return mC;
    }
    
    public double getD() {
        return mD;
    }
    
    public double getE() {
        return mE;
    }
    
    public double getF() {
        return mF;
    }
    
    public static Matrix2D translate(double x, double y) {
        return new Matrix2D(1, 0, 0, 1, x, y);
    }
    
    public static Matrix2D rotate(double n) {
        return  new Matrix2D(Math.cos(n), 
                             Math.sin(n), 
                            -Math.sin(n), 
                            Math.cos(n), 
                            0, 
                            0);
    }
    
    public static Matrix2D scale(double n) {
        return scale(n, n);
    }
    
    public static Matrix2D scale(double x, double y) {
        return new Matrix2D(x, 0, 0, y, 0, 0);
    }
}