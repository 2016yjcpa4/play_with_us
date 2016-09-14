package org.game.geom;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.game.math.Matrix2D;
import org.game.math.Point2D;
import org.game.math.Vector2D;

public class Polygon implements Shape { 
    
    private List<Vector2D> edges = new ArrayList<>();
    private List<Vector2D> norm = new ArrayList<>();
    private List<Point2D> vtx = new ArrayList<>();

    public Polygon() {
    }

    public Polygon(List<Point2D> l) {
        
        vtx.addAll(l);
        
        reset();
    }
    
    private void reset() { 
        int len = vtx.size(); 
        
        edges.clear();
        norm.clear();
        
        for (int n = 0; n < len; ++n) {
            Point2D p1 = vtx.get(n);
            Point2D p2 = vtx.get(n < len - 1 ? n + 1 : 0);
            
            Vector2D e = new Vector2D(p2).sub(p1);
            Vector2D norm = new Vector2D(e).perp().normalize();
            
            this.edges.add(e);
            this.norm.add(norm);
        } 
    }

    public int[] getXPoints() {
        return Point2D.getXPoints(vtx);
    }

    public int[] getYPoints() {
        return Point2D.getYPoints(vtx);
    }

    public List<Point2D> getVertex() {
        return vtx;
    }

    public void transform(Matrix2D m) {
        transform(m, getPosition());
    }

    public void transform(Matrix2D m, Point2D about) {
        m = Matrix2D.translate(about.getX(), about.getY()).concat(m).concat(Matrix2D.translate(-about.getX(), -about.getY()));

        for (int n = 0; n < vtx.size(); ++n) {
            Point2D p = vtx.get(n);

            double x = p.getX();
            double y = p.getY();

            p.setX((int) (x * m.getA() + y * m.getC() + m.getE()));
            p.setY((int) (x * m.getB() + y * m.getD() + m.getF()));
        }
        
        reset();
    }
    
    public Point2D getPosition() {
        Vector2D v = new Vector2D();

        for (int n = 0; n < vtx.size(); n++) {
            v = v.add(vtx.get(n));
        }

        int x = (int) (v.getX() / (float) vtx.size());
        int y = (int) (v.getY() / (float) vtx.size());

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
        };
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
        for(; i >= 0; i--) { 
            double d = new Vector2D(points.get(i)).dot(norm);
            
            min = Math.min(min, d);
            max = Math.max(max, d);
        }
        
        return new RangeResult(min, max);
    }
    
    private static final int LEFT_VORNOI_REGION = -1; 
    private static final int MIDDLE_VORNOI_REGION = 0; 
    private static final int RIGHT_VORNOI_REGION = 1;
    
    private static int vornoiRegion(Vector2D line, Vector2D point) {
        double len2 = line.len2();
        double dp = point.dot(line);

        if (dp < 0) return LEFT_VORNOI_REGION;
        if (dp > len2) return RIGHT_VORNOI_REGION;
        return MIDDLE_VORNOI_REGION;
    } 
    
    
    public static boolean testPolygonCircle(Polygon polygon, Circle circle, SATResponse response) {
         
        Vector2D circlePos = new Vector2D(circle.getPosition());//.sub(polygon.getPosition());
        double radius = circle.getRadius();
        double radius2 = radius * radius;
        List<Point2D> points = polygon.vtx;
        int len = points.size();
        Vector2D edge = new Vector2D();
        Vector2D point = new Vector2D();

        // For each edge in the polygon
        for (int i = 0; i < len; i++) {
            int next = (i == len - 1) ? 0 : i + 1;
            int prev = (i == 0) ? len - 1 : i - 1;
            double overlap = 0;
            Vector2D overlapN = null;

            // Get the edge
            edge = new Vector2D(polygon.edges.get(i));

            // Calculate the center of the cirble relative to the starting point of the edge
            point = circlePos.sub(points.get(i));

            // If the distance between the center of the circle and the point
            // is bigger than the radius, the polygon is definitely not fully in
            // the circle.
            if (response != null && point.len2() > radius2) {
                response.aInB = false;
            }

            // Calculate which Vornoi region the center of the circle is in.
            int region = vornoiRegion(edge, point);
            if (region == LEFT_VORNOI_REGION) {

                // Need to make sure we're in the RIGHT_VORNOI_REGION of the previous edge.
                edge = polygon.edges.get(prev);

                // Calculate the center of the circle relative the starting point of the previous edge
                Vector2D point2 = circlePos.sub(points.get(prev));

                region = vornoiRegion(edge, point2);
                if (region == RIGHT_VORNOI_REGION) {

                    // It's in the region we want.  Check if the circle intersects the point.
                    double dist = point.len();
                    if (dist > radius) {
                        // No intersection 
                        return false;
                    } 
                    else if (response != null) {
                        // It intersects, calculate the overlap
                        response.bInA = false;
                        overlapN = point.normalize();
                        overlap = radius - dist;
                    }
                }
            } 
            else if (region == RIGHT_VORNOI_REGION) {

                // Need to make sure we're in the left region on the next edge
                edge = (polygon.edges.get(next));

                // Calculate the center of the circle relative to the starting point of the next edge
                point = circlePos.sub(points.get(next));

                region = vornoiRegion(edge, point);
                if (region == LEFT_VORNOI_REGION) {

                    // It's in the region we want.  Check if the circle intersects the point.
                    double dist = point.len();
                    if (dist > radius) {
                        // No intersection 
                        return false;
                    }
                    else if (response != null) {
                        // It intersects, calculate the overlap
                        response.bInA = false;
                        overlapN = point.normalize();
                        overlap = radius - dist;
                    }
                }
                // MIDDLE_VORNOI_REGION
            } else {

                // Need to check if the circle is intersecting the edge,
                // Change the edge into its "edge normal".
                Vector2D normal = edge.perp().normalize();

                // Find the perpendicular distance between the center of the 
                // circle and the edge.
                double dist = point.dot(normal);
                double distAbs = Math.abs(dist);

                // If the circle is on the outside of the edge, there is no intersection
                if (dist > 0 && distAbs > radius) { 
                    return false;
                } 
                else if (response != null) {
                    // It intersects, calculate the overlap.
                    overlapN = normal;
                    overlap = radius - dist;
                    // If the center of the circle is on the outside of the edge, or part of the
                    // circle is on the outside, the circle is not fully inside the polygon.
                    if (dist >= 0 || overlap < 2 * radius) {
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
    };
    
}
