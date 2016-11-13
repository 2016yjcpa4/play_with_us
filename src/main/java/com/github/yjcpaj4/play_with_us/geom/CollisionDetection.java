package com.github.yjcpaj4.play_with_us.geom;

import com.github.yjcpaj4.play_with_us.math.Vector2D;

/**
 * Separating Axis Theorem.
 *
 * 축 분리 정리 두개의 오브젝트를 교차(충돌)하였는지 검사하는 클래스.
 *
 * @see https://github.com/jriecken/sat-js/blob/master/SAT.js
 */
public class CollisionDetection {

    private CollisionDetection() {
    }
    
    public static class Response {

        private Shape mA;
        private Shape mB;
        private Vector2D mOverlapNorm;
        private Vector2D mOverlapVec;
        private float mOverlap;
        private boolean mAInB;
        private boolean mBInA;

        public Response() {
            mA = null;
            mB = null;
            mAInB = true;
            mBInA = true;
            mOverlap = Float.MAX_VALUE;
            mOverlapNorm = new Vector2D();
            mOverlapVec = new Vector2D();
        }
        
        public Shape getA() {
            return mA;
        }
        
        public Shape getB() {
            return mB;
        }
        
        public boolean isAInB() {
            return mAInB;
        }
        
        public boolean isBInA() {
            return mBInA;
        }
        
        public float getOverlap() {
            return mOverlap;
        }
        
        public Vector2D getOverlapVector() {
            return mOverlapVec;
        }
        
        public Vector2D getOverlapNorm() {
            return mOverlapNorm;
        }
    }
    
    private static final int LEFT_VORONOI_REGION = -1;
    private static final int MIDDLE_VORONOI_REGION = 0; 
    private static final int RIGHT_VORONOI_REGION = 1;
    
    private static int getVoronoiRegion(Vector2D v1, Vector2D v2) {
        double n1 = v1.lengthSquared();
        double n2 = v2.dot(v1);
        
        if (n2 < 0) {
            return LEFT_VORONOI_REGION;
        }
        else if (n2 > n1) {
            return RIGHT_VORONOI_REGION;
        }
        else {
            return MIDDLE_VORONOI_REGION;
        }
    }
    
    public static boolean isCollides(Circle c, Polygon p) {
        return isCollides(c, p, null);
    }
    
    public static boolean isCollides(Circle c, Polygon p, Response r) {
        
        if (isCollides(p, c, r)) {
            
            if (r != null) {
                final Shape a = r.getA();
                final boolean b = r.isAInB();

                r.mA            = r.mB;
                r.mB            = a;
                r.mAInB         = r.isAInB();
                r.mBInA         = b;
                r.mOverlapVec   = r.mOverlapVec.neg();
                r.mOverlapNorm  = r.mOverlapNorm.neg();
            }
            
            return true;
        }
        
        return false;
    }
    
    public static boolean isCollides(Circle a, Circle b) {
        return isCollides(a, b, null);
    }
    
    public static boolean isCollides(Circle a, Circle b, Response r) {
        Vector2D v = new Vector2D(a.getPosition()).sub(b.getPosition());
        float f = a.getRadius() + b.getRadius();
        
        if (v.lengthSquared() > (f * f)) {
            return false;
        }
        
        if (r != null) {
            double d = v.length();
            r.mA = a;
            r.mB = b;
            r.mOverlap = (float) (f - d);
            r.mOverlapNorm.set(v.norm());
            r.mOverlapVec.set(v.mult(r.mOverlap));
            r.mAInB = a.getRadius() <= b.getRadius() && d <= b.getRadius() - a.getRadius();
            r.mBInA = b.getRadius() <= a.getRadius() && d <= a.getRadius() - b.getRadius();
        }
        
        return true;
    }
    
    public static boolean isCollides(Polygon p, Circle c) {
        return isCollides(p, c, null);
    }

    public static boolean isCollides(Polygon p, Circle c, Response r) {
        for (int l = 0; l < p.getPoints().size(); l++) {            
            float f = 0;
            Vector2D v1 = null;
            Vector2D v2 = p.getEdge(l);
            Vector2D v3 = new Vector2D(c.getPosition()).sub(p.getPoint(l));

            if (r != null && v3.lengthSquared() > (c.getRadius() * c.getRadius())) {
                r.mAInB = false;
            }

            int n = getVoronoiRegion(v2, v3);
            if (n == LEFT_VORONOI_REGION) {
                v2 = p.getEdge(l - 1);
                Vector2D p2 = new Vector2D(c.getPosition()).sub(p.getPoint(l - 1));
                if (getVoronoiRegion(v2, p2) == RIGHT_VORONOI_REGION) {
                    double d = v3.length();
                    if (d > c.getRadius()) {
                        return false;
                    }
                    else if (r != null) {
                        r.mBInA = false;
                        v1 = v3.norm();
                        f = (float) (c.getRadius() - d);
                    }
                }
            } else if (n == RIGHT_VORONOI_REGION) {
                v2 = p.getEdge(l + 1);
                v3 = new Vector2D(c.getPosition()).sub(p.getPoint(l + 1));
                n = getVoronoiRegion(v2, v3);
                if (n == LEFT_VORONOI_REGION) {
                    double d = v3.length();
                    if (d > c.getRadius()) {
                        return false;
                    }
                    else if (r != null) {
                        r.mBInA = false;
                        v1 = v3.norm();
                        f = c.getRadius() - (float) d;
                    }
                }
            } 
            else {
                Vector2D v4 = v2.perp().norm();
                double d = v3.dot(v4);
                if (d > 0 && Math.abs(d) > c.getRadius()) {
                    return false;
                }
                else if (r != null) {
                    v1 = v4;
                    f = (float) (c.getRadius() - d);
                    
                    if (d >= 0 || f < 2 * c.getRadius()) {
                        r.mBInA = false;
                    }
                }
            }
 
            if (v1 != null && r != null && Math.abs(f) < Math.abs(r.mOverlap)) {
                r.mOverlap = f;
                r.mOverlapNorm.set(v1);
            }
        }
        
        if (r != null) {
            r.mA = p;
            r.mB = c;
            r.mOverlapVec.set(r.mOverlapNorm);
            r.mOverlapVec = r.mOverlapVec.mult(r.mOverlap);
        }
        
        return true;
    }
}
