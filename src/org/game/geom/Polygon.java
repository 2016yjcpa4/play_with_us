package org.game.geom;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.game.math.Matrix2D;
import org.game.math.Point2D;
import org.game.math.Vector2D;

public class Polygon implements Shape {

    private List<Vector2D> e = new ArrayList<>();
    private List<Vector2D> norm = new ArrayList<>();
    private List<Point2D> vtx = new ArrayList<>();

    public Polygon() {
    }

    public Polygon(List<Point2D> l) {
        int len = l.size();
        
        if(Polygon.LEFT_VORNOI_REGION == 0);
        
        for (int n = 0; n < len; ++n) {
            Point2D p1 = l.get(n);
            Point2D p2 = l.get(n < len - 1 ? n + 1 : 0);
            
            Vector2D e = new Vector2D(p2).sub(p1);
            Vector2D norm = new Vector2D(e).perp().normalize();
            
            this.e.add(e);
            this.norm.add(norm);
        }
        
        vtx.addAll(l);
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
    }

    public Point2D getPosition() {
        Vector2D v = new Vector2D();

        for (int n = 0; n < vtx.size(); n++) {
            v = v.sum(vtx.get(n));
        }

        int x = (int) (v.getX() / (float) vtx.size());
        int y = (int) (v.getY() / (float) vtx.size());

        return new Point2D(x, y);
    }
    
    private static Stack<List<Double>> T_ARRAYS = new Stack<>();
    private static Stack<Vector2D> T_VECTORS = new Stack<>();
    
    static {
        for (int n = 0; n < 5; ++n) {
            T_ARRAYS.push(new ArrayList());
        }
        
        for (int n = 0; n < 10; ++n) {
            T_VECTORS.push(new Vector2D());
        }
    }
    
    public static class SATResponse {
        
        public Shape a;
        public Shape b;
        
        public boolean aInB;
        public boolean bInA;
        
        public Vector2D overlapN = new Vector2D();
        public Vector2D overlapV = new Vector2D();
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
    
    public static void getFlattenPointsOn(List<Point2D> vtx, Vector2D norm, List l) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        
        for (int n = vtx.size() - 1; 0 >= n; --n) {
            
            double d = new Vector2D(vtx.get(n)).dot(norm);
            
            min = Math.min(min, d);
            max = Math.max(max, d);
        }
        
        l.set(0, min);
        l.set(1, max);
    }
    
    public static boolean isSeparatingAxis(Point2D aPos, Point2D bPos, List<Point2D> aPoints, List<Point2D> bPoints, Vector2D axis, SATResponse response) {
        List<Double> rangeA = T_ARRAYS.pop();
        List<Double> rangeB = T_ARRAYS.pop();
        
        Vector2D offsetV = T_VECTORS.pop().set(bPos).sub(aPos);
        double projectedOffset = offsetV.dot(axis);
        
        // Project the polygons onto the axis.
        getFlattenPointsOn(aPoints, axis, rangeA);
        getFlattenPointsOn(bPoints, axis, rangeB);

        // Move B's range to its position relative to A.
        rangeB.set(0, rangeB.get(0) + projectedOffset);
        rangeB.set(1, rangeB.get(1) + projectedOffset);
        
        // Check if there is a gap. If there is, this is a separating axis and we can stop
        if (rangeA.get(0) > rangeB.get(1) || rangeB.get(0) > rangeA.get(1)) {
            T_VECTORS.push(offsetV);
            T_ARRAYS.push(rangeA);
            T_ARRAYS.push(rangeB);
            return true;
        }
        
        
        // If we're calculating a response, calculate the overlap.
        if (response != null) {
            double overlap = 0;
            // A starts further left than B
            if (rangeA.get(0) < rangeB.get(0)) {
                response.aInB = false;
                // A ends before B does. We have to pull A out of B
                if (rangeA.get(1) < rangeB.get(1)) {
                    overlap = rangeA.get(1) - rangeB.get(0);
                    response.bInA = false;
                    // B is fully inside A.  Pick the shortest way out.
                } else {
                    double option1 = rangeA.get(1) - rangeB.get(0);
                    double option2 = rangeB.get(1) - rangeA.get(0);
                    overlap = option1 < option2 ? option1 : -option2;
                }
                // B starts further left than A
            } else {
                response.bInA = false;
                // B ends before A ends. We have to push A out of B
                if (rangeA.get(1) > rangeB.get(1)) {
                    overlap = rangeA.get(0) - rangeB.get(1);
                    response.aInB = false;
                    // A is fully inside B.  Pick the shortest way out.
                } else {
                    double option1 = rangeA.get(1) - rangeB.get(0);
                    double option2 = rangeB.get(1) - rangeA.get(0);
                    overlap = option1 < option2 ? option1 : -option2;
                }
            }

            // If this is the smallest amount of overlap we've seen so far, set it as the minimum overlap.
            double absOverlap = Math.abs(overlap);
            if (absOverlap < response.overlap) {
                response.overlap = absOverlap;
                response.overlapN.set(axis);
                if (overlap < 0) {
                    response.overlapN.reverse();
                }
            }
        }
        
        T_VECTORS.push(offsetV);
        T_ARRAYS.push(rangeA);
        T_ARRAYS.push(rangeB);
        return false;
    }
    
    
    public static boolean testCircleCircle(Circle a, Circle b, SATResponse response) {
        Vector2D differenceV = T_VECTORS.pop().set(b.getPosition()).sub(a.getPosition());
        double totalRadius = a.getRadius() + b.getRadius();
        double totalRadiusSq = totalRadius * totalRadius;
        double distanceSq = differenceV.len2();

        if (distanceSq > totalRadiusSq) {
            // They do not intersect 
            T_VECTORS.push(differenceV);
            return false;
        }

        // They intersect. If we're calculating a response, calculate the overlap.
        if (response != null) {
            double dist = Math.sqrt(distanceSq);
            response.a = a;
            response.b = b;
            response.overlap = totalRadius - dist;
            response.overlapN.set(differenceV.normalize());
            response.overlapV.set(differenceV).mult(response.overlap);
            response.aInB = a.getRadius() <= b.getRadius() && dist <= b.getRadius() - a.getRadius();
            response.bInA = b.getRadius() <= a.getRadius() && dist <= a.getRadius() - b.getRadius();
        }

        T_VECTORS.push(differenceV);
        return true;
    }
    /**
     * @const
     */
    private static final int LEFT_VORNOI_REGION = -1;

    /**
     * @const
     */
    private static final int MIDDLE_VORNOI_REGION = 0;

    /**
     * @const
     */
    private static final int RIGHT_VORNOI_REGION = 1;
    
    private static int vornoiRegion(Vector2D line, Vector2D point) {
        double len2 = line.len2();
        double dp = point.dot(line);

        if (dp < 0) return LEFT_VORNOI_REGION;
        if (dp > len2) return RIGHT_VORNOI_REGION;
        return MIDDLE_VORNOI_REGION;
    }
    
    public static boolean testPolygonCircle(Polygon polygon, Circle circle, SATResponse response) { 
        Vector2D circlePos = T_VECTORS.pop().set(circle.getPosition()).sub(polygon.getPosition());
        double radius = circle.getRadius();
        double radius2 = radius * radius;
        List<Point2D> points = polygon.vtx;
        int len = points.size();
        Vector2D edge = T_VECTORS.pop();
        Vector2D point = T_VECTORS.pop();

        // For each edge in the polygon
        for (int i = 0; i < len; i++) {
            int next = (i == len - 1) ? 0 : i + 1;
            int prev = (i == 0) ? len - 1 : i - 1;
            double overlap = 0;
            Vector2D overlapN = null;

            // Get the edge
            edge.set(polygon.e.get(i));

            // Calculate the center of the cirble relative to the starting point of the edge
            point.set(circlePos).sub(points.get(i));

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
                edge.set(polygon.e.get(prev));

                // Calculate the center of the circle relative the starting point of the previous edge
                Vector2D point2 = T_VECTORS.pop().set(circlePos).sub(points.get(prev));

                region = vornoiRegion(edge, point2);
                if (region == RIGHT_VORNOI_REGION) {

                    // It's in the region we want.  Check if the circle intersects the point.
                    double dist = point.len();
                    if (dist > radius) {
                        // No intersection
                        T_VECTORS.push(circlePos);
                        T_VECTORS.push(edge);
                        T_VECTORS.push(point);
                        T_VECTORS.push(point2);
                        return false;
                    } 
                    else if (response != null) {
                        // It intersects, calculate the overlap
                        response.bInA = false;
                        overlapN = point.normalize();
                        overlap = radius - dist;
                    }
                }
                T_VECTORS.push(point2);
            } 
            else if (region == RIGHT_VORNOI_REGION) {

                // Need to make sure we're in the left region on the next edge
                edge.set(polygon.e.get(next));

                // Calculate the center of the circle relative to the starting point of the next edge
                point.set(circlePos).sub(points.get(next));

                region = vornoiRegion(edge, point);
                if (region == LEFT_VORNOI_REGION) {

                    // It's in the region we want.  Check if the circle intersects the point.
                    double dist = point.len();
                    if (dist > radius) {
                        // No intersection
                        T_VECTORS.push(circlePos);
                        T_VECTORS.push(edge);
                        T_VECTORS.push(point);
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
                    T_VECTORS.push(circlePos);
                    T_VECTORS.push(normal);
                    T_VECTORS.push(point);
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
                response.overlapN.set(overlapN);
            }
        }

        // Calculate the final overlap vector - based on the smallest overlap.
        if (response != null) {
            response.a = polygon;
            response.b = circle;
            response.overlapV.set(response.overlapN).mult(response.overlap);
        }

        T_VECTORS.push(circlePos);
        T_VECTORS.push(edge);
        T_VECTORS.push(point);
        return true;
    }
}
