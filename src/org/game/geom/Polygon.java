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
        m = Matrix2D.translate(about.getX(), about.getY()).concat(m).concat(Matrix2D.translate(-about.getX(), -about.getY()));

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
        public Vector2D overlapN = new Vector2D();
        public Vector2D overlapV = new Vector2D();

        public boolean aInB;
        public boolean bInA;
        public double overlap;

        public SATResponse() {
            clear();
        }

        public SATResponse clear() {
            this.aInB = true; // Is a fully inside b?
            this.bInA = true; // Is b fully inside a?
            this.overlap = Double.MAX_VALUE; // Amount of overlap (magnitude of overlapV). Can be 0 (if a and b are touching)
            return this;
        }
    ;

    } 
    
    private static class RangeResult {

        public double min;
        public double max;

        public RangeResult(double min, double max) {
            this.min = min;
            this.max = max;
        }
    }

    public static RangeResult flattenPointsOn(List<Point2D> points, Vector2D norm) {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        int i = points.size();
        for (; i >= 0; i--) {
            double d = new Vector2D(points.get(i)).scalar(norm);

            min = Math.min(min, d);
            max = Math.max(max, d);
        }

        return new RangeResult(min, max);
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

    public static boolean isCollidePolygonCircle(Polygon polygon, Circle circle, SATResponse response) {

        Vector2D circlePos = new Vector2D(circle.getPosition());//.sub(polygon.getPosition());
        double rad = circle.getRadius();
        List<Point2D> points = polygon.getPoints();
        int len = points.size();
        Vector2D edge = new Vector2D();
        Vector2D point = new Vector2D();

        // For each edge in the polygon
        for (int i = 0; i < len; i++) {
            double overlap = 0;
            Vector2D overlapN = null;

            // Calculate the center of the cirble relative to the starting point of the edge
            point = circlePos.sub(points.get(i));

            // If the distance between the center of the circle and the point
            // is bigger than the radius, the polygon is definitely not fully in
            // the circle.
            if (response != null && point.getLengthSquared() > (rad * rad)) {
                response.aInB = false;
            }

            // Calculate which Vornoi region the center of the circle is in.
            int region = getVornoiRegion(polygon.getEdge(i), point);
            if (region == LEFT_VORNOI_REGION) {
                int prev = (i == 0) ? len - 1 : i - 1;

                // Need to make sure we're in the RIGHT_VORNOI_REGION of the previous edge.
                edge = polygon.e.get(prev);

                // Calculate the center of the circle relative the starting point of the previous edge
                Vector2D point2 = circlePos.sub(points.get(prev));

                region = getVornoiRegion(edge, point2);
                if (region == RIGHT_VORNOI_REGION) {

                    // It's in the region we want.  Check if the circle intersects the point.
                    double dist = point.getLength();
                    if (dist > rad) {
                        // No intersection 
                        return false;
                    } else if (response != null) {
                        // It intersects, calculate the overlap
                        response.bInA = false;
                        overlapN = point.setNormalize();
                        overlap = rad - dist;
                    }
                }
            } else if (region == RIGHT_VORNOI_REGION) {
                int next = (i == len - 1) ? 0 : i + 1;

                // Need to make sure we're in the left region on the next edge
                edge = polygon.e.get(next);

                // Calculate the center of the circle relative to the starting point of the next edge
                point = circlePos.sub(points.get(next));

                region = getVornoiRegion(edge, point);
                if (region == LEFT_VORNOI_REGION) {

                    // It's in the region we want.  Check if the circle intersects the point.
                    double dist = point.getLength();
                    if (dist > rad) {
                        // No intersection 
                        return false;
                    } else if (response != null) {
                        // It intersects, calculate the overlap
                        response.bInA = false;
                        overlapN = point.setNormalize();
                        overlap = rad - dist;
                    }
                }
                // MIDDLE_VORNOI_REGION
            } else { 
                // Get the edge
                edge = new Vector2D(polygon.getEdge(i));

                // Need to check if the circle is intersecting the edge,
                // Change the edge into its "edge normal".
                Vector2D normal = edge.setPerpendicular().setNormalize();

                // Find the perpendicular distance between the center of the 
                // circle and the edge.
                double dist = point.scalar(normal);
                double distAbs = Math.abs(dist);

                // If the circle is on the outside of the edge, there is no intersection
                if (dist > 0 && distAbs > rad) {
                    return false;
                } else if (response != null) {
                    // It intersects, calculate the overlap.
                    overlapN = normal;
                    overlap = rad - dist;
                    // If the center of the circle is on the outside of the edge, or part of the
                    // circle is on the outside, the circle is not fully inside the polygon.
                    if (dist >= 0 || overlap < 2 * rad) {
                        response.bInA = false;
                    }
                }
            }

            // If this is the smallest overlap we've seen, keep it. 
            // (overlapN may be null if the circle was in the wrong Vornoi region)
            if (overlapN != null && response != null && Math.abs(overlap) < Math.abs(response.overlap)) {
                response.overlap = overlap;
                response.overlapN = new Vector2D(overlapN);
            }
        }

        // Calculate the final overlap vector - based on the smallest overlap.
        if (response != null) {
            response.a = polygon;
            response.b = circle;
            response.overlapV = response.overlapN.scale(response.overlap);
        }

        return true;
    }
;

}
