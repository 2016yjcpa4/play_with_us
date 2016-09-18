package org.game.geom;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static class SATOverlapResult {

        public Shape a;
        public Shape b;
        public boolean hasAInB;
        public boolean hasBInA;
        
        public double dist;
        public Vector2D dir;
        public Vector2D overlap;

        public SATOverlapResult() {
            clear();
        }
        
        public void clear() {
            this.dist = Double.MAX_VALUE;
            this.hasAInB = true;
            this.hasBInA = true;
            this.dir = null;
            this.overlap = null;
            this.a = null;
            this.b = null;
        }
        
        public void setDistance(double dist) {
            this.dist = dist;
        }
        
        public double getDistance() {
            return dist;
        }
        
        public Vector2D getDirection() {
            return dir;
        }
        
        public void setDirection(Vector2D dir) {
            this.dir = dir;
        }
        
        public Vector2D getOverlap() {
            return overlap;
        }
        
        public void setOverlap(Vector2D overlap) {
            this.overlap = overlap;
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

    public static boolean isCollidePolygonCircle(Polygon a, Circle b, SATOverlapResult r) {  
        if (r != null) {
            r.clear();
        }
        
        int rad = b.getRadius(); 
        int len = a.getPoints().size(); 
        
        for (int n = 0; n < len; n++) {
            double dist = 0;
            Vector2D dir = null; 
            Vector2D v = new Vector2D(b.getPosition()).sub(a.getPoints().get(n));
 
            if (v.getLengthSquared() > (rad * rad)) {
                r.hasAInB = false;
            }
            
            switch(getVornoiRegion(a.getEdge(n), v)) {
                case LEFT_VORNOI_REGION: {
                    int prv = (n - 1 + len) % len;

                    Vector2D v2 = new Vector2D(b.getPosition()).sub(a.getPoints().get(prv));

                    if (getVornoiRegion(a.getEdge(prv), v2) == RIGHT_VORNOI_REGION) {  
                        
                        if (v.getLength() > rad) { 
                            return false;
                        } 
                        else if (r != null) {
                            r.hasBInA = false;

                            dist = rad - v.getLength(); 
                            dir = v.setNormalize();
                        }
                    }
                    break;
                }
                case RIGHT_VORNOI_REGION: {    
                    int nxt = (n + 1) % len;

                    Vector2D v2 = new Vector2D(b.getPosition()).sub(a.getPoints().get(nxt));

                    if (getVornoiRegion(a.getEdge(nxt), v2) == LEFT_VORNOI_REGION) {  
                        
                        if (v2.getLength() > rad) { 
                            return false;
                        }  
                        else if (r != null) {
                            r.hasBInA = false;

                            dist = rad - v2.getLength();
                            dir = v2.setNormalize();
                        }
                    } 
                    break;
                }
                case MIDDLE_VORNOI_REGION: {
                    Vector2D norm = new Vector2D(a.getEdge(n)).setPerpendicular().setNormalize();

                    double d = v.scalar(norm); 

                    if (d > 0 && Math.abs(d) > rad) {
                        return false;
                    } 
                    else if (r != null) {
                        dir = norm;
                        dist = rad - d; 

                        if (d >= 0 || dist < (2 * rad)) {
                            r.hasBInA = false;
                        }
                    }
                }
            }
            
            if (dir != null && Math.abs(dist) < Math.abs(r.getDistance())) {
                r.setDistance(dist);
                r.setDirection(dir);
            }
        }
 
        if (r != null) {
            r.a = a;
            r.b = b;
            r.setOverlap(r.getDirection().scale(r.getDistance()));
        }
        
        return true;
    }
;

}
