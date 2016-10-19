package org.game.geom;

import java.util.List;
import org.game.math.Point2D;
import org.game.math.Vector2D;

/**
 * Separating Axis Theorem.
 *
 * 축 분리 정리 두개의 오브젝트를 교차(충돌)하였는지 검사하는 클래스.
 *
 * @see https://github.com/jriecken/sat-js/blob/master/SAT.js
 * @author 차명도
 */
public class SAT {

    public static class Response {

        public Shape a;
        public Shape b;
        public Vector2D overlapN;
        public Vector2D overlapV;
        public float overlap;
        public boolean aInB;
        public boolean bInA;

        public Response() {
            this.a = null;
            this.b = null;
            this.overlapN = new Vector2D();
            this.overlapV = new Vector2D();
            this.clear();
        }

        public void clear() {

            this.aInB = true;
            this.bInA = true;
            this.overlap = Float.MAX_VALUE;
        }
    }
    // Calculates which Voronoi region a point is on a line segment.
    // It is assumed that both the line and the point are relative to `(0,0)`
    //
    //            |       (0)      |
    //     (-1)  [S]--------------[E]  (1)
    //            |       (0)      |

    /**
     * @param {Vector} line The line segment.
     * @param {Vector} point The point.
     * @return {number} LEFT_VORONOI_REGION (-1) if it is the left region,
     * MIDDLE_VORONOI_REGION (0) if it is the middle region,
     * RIGHT_VORONOI_REGION (1) if it is the right region.
     */
    private static int voronoiRegion(Vector2D line, Vector2D point) {
        double len2 = line.lengthSquared();
        double dp = point.scalar(line);
        // If the point is beyond the start of the line, it is in the
        // left voronoi region.
        if (dp < 0) {
            return LEFT_VORONOI_REGION;
        } // If the point is beyond the end of the line, it is in the
        // right voronoi region.
        else if (dp > len2) {
            return RIGHT_VORONOI_REGION;
        } // Otherwise, it's in the middle one.
        else {
            return MIDDLE_VORONOI_REGION;
        }
    }
    // Constants for Voronoi regions
    /**
     * @const
     */
    private static final int LEFT_VORONOI_REGION = -1;
    /**
     * @const
     */
    private static final int MIDDLE_VORONOI_REGION = 0;
    /**
     * @const
     */
    private static final int RIGHT_VORONOI_REGION = 1;

    public static boolean testPolygonCircle(Polygon polygon, Circle circle, Response response) {
        // Get the position of the circle relative to the polygon.
        Vector2D circlePos = new Vector2D(circle.getPosition());
        int radius = circle.getRadius();
        int radius2 = radius * radius;
        List<Point2D> points = polygon.getPoints();
        int len = points.size();
        Vector2D edge = new Vector2D();
        Vector2D point = new Vector2D();

        // For each edge in the polygon:
        for (int i = 0; i < len; i++) {
            int next = i == len - 1 ? 0 : i + 1;
            int prev = i == 0 ? len - 1 : i - 1;
            float overlap = 0;
            Vector2D overlapN = null;

            // Get the edge.
            edge = polygon.getEdge(i);
            // Calculate the center of the circle relative to the starting point of the edge.
            point = new Vector2D(circlePos).sub(points.get(i));

            // If the distance between the center of the circle and the point
            // is bigger than the radius, the polygon is definitely not fully in
            // the circle.
            if (response != null && point.lengthSquared() > radius2) {
                response.aInB = false;
            }

            // Calculate which Voronoi region the center of the circle is in.
            int region = voronoiRegion(edge, point);
            // If it's the left region:
            if (region == LEFT_VORONOI_REGION) {
                // We need to make sure we're in the RIGHT_VORONOI_REGION of the previous edge.
                edge = polygon.getEdge(prev);
                // Calculate the center of the circle relative the starting point of the previous edge
                Vector2D point2 = new Vector2D(circlePos).sub(points.get(prev));
                region = voronoiRegion(edge, point2);
                if (region == RIGHT_VORONOI_REGION) {
                    // It's in the region we want.  Check if the circle intersects the point.
                    double dist = point.length();
                    if (dist > radius) {
                        // No intersection 
                        return false;
                    } else if (response != null) {
                        // It intersects, calculate the overlap.
                        response.bInA = false;
                        overlapN = point.norm();
                        overlap = (float) (radius - dist);
                    }
                }
                // If it's the right region:
            } else if (region == RIGHT_VORONOI_REGION) {
                // We need to make sure we're in the left region on the next edge
                edge = polygon.getEdge(next);
                // Calculate the center of the circle relative to the starting point of the next edge.
                point = new Vector2D(circlePos).sub(points.get(next));
                region = voronoiRegion(edge, point);
                if (region == LEFT_VORONOI_REGION) {
                    // It's in the region we want.  Check if the circle intersects the point.
                    float dist = (float) point.length();
                    if (dist > radius) {
                        // No intersection 
                        return false;
                    } else if (response != null) {
                        // It intersects, calculate the overlap.
                        response.bInA = false;
                        overlapN = point.norm();
                        overlap = radius - dist;
                    }
                }
                // Otherwise, it's the middle region:
            } else {
                // Need to check if the circle is intersecting the edge,
                // Change the edge into its "edge normal".
                Vector2D normal = edge.perp().norm();
                // Find the perpendicular distance between the center of the 
                // circle and the edge.
                double dist = point.scalar(normal);
                double distAbs = Math.abs(dist);
                // If the circle is on the outside of the edge, there is no intersection.
                if (dist > 0 && distAbs > radius) {
                    // No intersection
                    return false;
                } else if (response != null) {
                    // It intersects, calculate the overlap.
                    overlapN = normal;
                    overlap = (float) (radius - dist);
                    // If the center of the circle is on the outside of the edge, or part of the
                    // circle is on the outside, the circle is not fully inside the polygon.
                    if (dist >= 0 || overlap < 2 * radius) {
                        response.bInA = false;
                    }
                }
            }

            // If this is the smallest overlap we've seen, keep it. 
            // (overlapN may be null if the circle was in the wrong Voronoi region).
            if (overlapN != null && response != null && Math.abs(overlap) < Math.abs(response.overlap)) {
                response.overlap = overlap;
                response.overlapN.set(overlapN);
            }
        }

        // Calculate the final overlap vector - based on the smallest overlap.
        if (response != null) {
            response.a = polygon;
            response.b = circle;
            response.overlapV.set(response.overlapN);
            response.overlapV = response.overlapV.mult(response.overlap);
        }
        return true;
    }
}
