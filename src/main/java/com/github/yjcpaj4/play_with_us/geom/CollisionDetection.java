package com.github.yjcpaj4.play_with_us.geom;

import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import java.util.ArrayList;
import java.util.List;

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

    // Structure that stores the results of the PolygonCollision function
    public static class ProjectPolygon {

        public float mMin = Float.MAX_VALUE; // Are the polygons going to intersect forward in time?
        public float mMax = Float.MIN_VALUE; // Are the polygons currently intersecting
    }

    // Calculate the distance between [minA, maxA] and [minB, maxB]
    // The distance will be negative if the intervals overlap
    public static float IntervalDistance(ProjectPolygon a, ProjectPolygon b) {
        if (a.mMin < b.mMin) {
            return b.mMin - a.mMax;
        } else {
            return a.mMin - b.mMax;
        }
    }

    // Calculate the projection of a polygon on an axis and returns it as a [min, max] interval
    public static ProjectPolygon ProjectPolygon(Vector2D v, Polygon p) {
        ProjectPolygon o = new ProjectPolygon();
        for (int i = 0; i < p.getPoints().size(); i++) {
            float d = new Vector2D(p.getPoint(i)).dot(v);
            o.mMax = Math.max(o.mMax, d);
            o.mMin = Math.min(o.mMin, d);
        }

        return o;
    }

    // Check if polygon A is going to collide with polygon B for the given velocity
    public static Vector2D PolygonCollision(Polygon a, Polygon b) {
        float n = Float.POSITIVE_INFINITY;
        Vector2D v = new Vector2D();
        Vector2D v2 = new Vector2D(a.getPosition()).sub(b.getPosition());

        List<Vector2D> l = new ArrayList<>();
        l.addAll(a.getEdges());
        l.addAll(b.getEdges());

        // Loop through all the edges of both polygons
        for (Vector2D edge : l) {

            // ===== 1. Find if the polygons are currently intersecting =====
            // Find the axis perpendicular to the current edge
            Vector2D axis = new Vector2D(-edge.getY(), edge.getX()).norm();

            // Find the projection of the polygon on the current axis 
            ProjectPolygon A = ProjectPolygon(axis, a);
            ProjectPolygon B = ProjectPolygon(axis, b);

            // Check if the polygon projections are currentlty intersecting
            if (IntervalDistance(A, B) > 0) {
                return null;
            }

            float intervalDistance = Math.abs(IntervalDistance(A, B));
            if (intervalDistance < n) {
                n = intervalDistance;
                v = axis;

                if (v2.dot(v) < 0) {
                    v = v.neg();
                }
            }
        }

        return v.mult(n);
    }

}
