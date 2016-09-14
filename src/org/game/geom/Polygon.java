package org.game.geom;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.game.math.Matrix2D;
import org.game.math.Point2D;
import org.game.math.Vector2D;

public class Polygon implements Shape {

    private List<Vector2D> norm = new ArrayList<>();
    private List<Vector2D> e = new ArrayList<>();
    private List<Point2D> p = new ArrayList<>();

    public Polygon() {
    }

    public Polygon(List<Point2D> p) {
        this.p.addAll(p);

        reset();
    }

    private void reset() {
        int len = p.size();

        e.clear();
        norm.clear();

        for (int n = 0; n < len; ++n) {
            Point2D p1 = p.get(n);
            Point2D p2 = p.get((n + 1) % len);

            Vector2D e = new Vector2D(p2).sub(p1);
            Vector2D norm = new Vector2D(e).setPerpendicular().setNormalize();

            this.e.add(e);
            this.norm.add(norm);
        }
    }

    public int[] getXPoints() {
        return Point2D.getXPoints(p);
    }

    public int[] getYPoints() {
        return Point2D.getYPoints(p);
    }
    
    private Vector2D getEdge(int n) {
        return e.get(n);
    }
    
    private Vector2D getNormal(int n) {
        return norm.get(n);
    }

    public List<Point2D> getPoints() {
        return p;
    }

    public void transform(Matrix2D m) {
        transform(m, getPosition());
    }

    public void transform(Matrix2D m, Point2D about) {
        m = Matrix2D.translate(about.getX(), about.getY())
                    .concat(m)
                    .concat(Matrix2D.translate(-about.getX(), -about.getY()));

        for (Point2D p : getPoints()) {
            double x = p.getX();
            double y = p.getY();

            p.setX((int) (x * m.getA() + y * m.getC() + m.getE()));
            p.setY((int) (x * m.getB() + y * m.getD() + m.getF()));
        }

        reset();
    }

    public Point2D getPosition() {
        int len = p.size();

        Vector2D v = new Vector2D();

        for (int n = 0; n < len; ++n) {
            v = v.add(p.get(n));
        }

        int x = (int) (v.getX() / len);
        int y = (int) (v.getY() / len);

        return new Point2D(x, y);
    }

    public static class SATResponse {

        public Shape a;
        public Shape b;
        public boolean hasAInB;
        public boolean hasBInA;
        
        public double overlap;
        public Vector2D overlapN = new Vector2D();
        public Vector2D overlapV = new Vector2D();

        public SATResponse(Shape a, Shape b) {
            this.a = a;
            this.b = b;
            clear();
        }

        public SATResponse clear() {
            this.hasAInB = true; // Is a fully inside b?
            this.hasBInA = true; // Is b fully inside a?
            this.overlap = Double.MAX_VALUE; // Amount of overlap (magnitude of overlapV). Can be 0 (if a and b are touching)
            return this;
        }
    }

    private static final int LEFT_VORNOI_REGION = -1;
    private static final int MIDDLE_VORNOI_REGION = 0;
    private static final int RIGHT_VORNOI_REGION = 1;

    private static int getVornoiRegion(Vector2D v1, Vector2D v2) { 
        double d = v2.scalar(v1);

        if (d < 0) {
            return LEFT_VORNOI_REGION;
        }
        
        if (d > v1.getLengthSquared()) {
            return RIGHT_VORNOI_REGION;
        }
        
        return MIDDLE_VORNOI_REGION;
    }

    public static SATResponse isCollidePolygonCircle(Polygon a, Circle b) { 
        Vector2D pos = new Vector2D(b.getPosition()); 
        double rad = b.getRadius();
        List<Point2D> points = a.getPoints();
        int len = points.size();  
        
        SATResponse res = new SATResponse(a, b);
 
        for (int n = 0; n < len; n++) {
            double overlap = 0;
            Vector2D overlapN = null; 
            Vector2D v1 = pos.sub(points.get(n));
 
            if (v1.getLengthSquared() > (rad * rad)) {
                res.hasAInB = false;
            }
            
            int r = getVornoiRegion(a.getEdge(n), v1);
            if (r == LEFT_VORNOI_REGION) {
                int prev = (n == 0) ? len - 1 : n - 1;
  
                Vector2D v2 = pos.sub(points.get(prev));
 
                if (getVornoiRegion(a.getEdge(prev), v2) == RIGHT_VORNOI_REGION) { 
                    double dist = v1.getLength();
                    if (dist > rad) { 
                        return null;
                    } 
                     
                    res.hasBInA = false;
                    overlapN = v1.setNormalize();
                    overlap = rad - dist; 
                }
            } 
            else if (r == RIGHT_VORNOI_REGION) {
                int next = (n == len - 1) ? 0 : n + 1; 
                
                Vector2D v2 = pos.sub(points.get(next));
 
                if (getVornoiRegion(a.getEdge(next), v2) == LEFT_VORNOI_REGION) { 
                    double dist = v2.getLength();
                    if (dist > rad) { 
                        return null;
                    } 
                    
                    res.hasBInA = false;
                    overlapN = v2.setNormalize();
                    overlap = rad - dist;
                } 
            } 
            else { 
                Vector2D norm = new Vector2D(a.getEdge(n)).setPerpendicular().setNormalize();
 
                double dist = v1.scalar(norm); 
 
                if (dist > 0 && Math.abs(dist) > rad) {
                    return null;
                }
                
                overlapN = norm;
                overlap = rad - dist; 
                if (dist >= 0 || overlap < (2 * rad)) {
                    res.hasBInA = false;
                }
            } 
            
            if (overlapN != null && Math.abs(overlap) < Math.abs(res.overlap)) {
                res.overlap = overlap;
                res.overlapN = overlapN;
            }
        }
 
        res.overlapV = res.overlapN.scale(res.overlap);

        return res;
    }
;

}
