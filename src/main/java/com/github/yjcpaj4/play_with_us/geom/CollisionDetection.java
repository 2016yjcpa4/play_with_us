package com.github.yjcpaj4.play_with_us.geom;

import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
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

        public float min; // Are the polygons going to intersect forward in time?
        public float max; // Are the polygons currently intersecting
    }

    // Structure that stores the results of the PolygonCollision function
    public static class PolygonCollisionResult {
 
        public boolean Intersect; // Are the polygons currently intersecting
        public Vector2D MinimumTranslationVector; // The translation to apply to polygon A to push the polygons appart.
    }

    // Calculate the distance between [minA, maxA] and [minB, maxB]
    // The distance will be negative if the intervals overlap
    public static float IntervalDistance(float minA, float maxA, float minB, float maxB) {
        if (minA < minB) {
            return minB - maxA;
        } else {
            return minA - maxB;
        }
    }

    // Calculate the projection of a polygon on an axis and returns it as a [min, max] interval
    public static ProjectPolygon ProjectPolygon(Vector2D axis, Polygon polygon) {
        // To project a point on an axis use the dot product
        float d = axis.dot(polygon.getPoint(0));
        ProjectPolygon o = new ProjectPolygon();
        o.min = d;
        o.max = d;
        for (int i = 0; i < polygon.getPoints().size(); i++) {
            d = new Vector2D(polygon.getPoint(i)).dot(axis);
            if (d < o.min) {
                o.min = d;
            } else if (d > o.max) {
                o.max = d;
            }
        }

        return o;
    }

    // Check if polygon A is going to collide with polygon B for the given velocity
    public static PolygonCollisionResult PolygonCollision(Polygon polygonA, Polygon polygonB) {
        Vector2D d = new Vector2D(polygonA.getPosition()).sub(polygonB.getPosition());
        PolygonCollisionResult result = new PolygonCollisionResult();
        result.Intersect = true; 

        int edgeCountA = polygonA.getEdges().size();
        int edgeCountB = polygonB.getEdges().size();
        float minIntervalDistance = Float.MAX_VALUE;
        Vector2D translationAxis = new Vector2D();
        Vector2D edge;
 
        for (int edgeIndex = 0; edgeIndex < edgeCountA + edgeCountB; edgeIndex++) {
            if (edgeIndex < edgeCountA) {
                edge = polygonA.getEdge(edgeIndex);
            } else {
                edge = polygonB.getEdge(edgeIndex - edgeCountA);
            }
 
            Vector2D axis = edge.perp().norm();
 
            ProjectPolygon A = ProjectPolygon(axis, polygonA);
            ProjectPolygon B = ProjectPolygon(axis, polygonB);
 
            if (IntervalDistance(A.min, A.max, B.min, B.max) > 0) {
                result.Intersect = false; 
                break;
            }
 
            float intervalDistance = Math.abs(IntervalDistance(A.min, A.max, B.min, B.max));
            if (intervalDistance < minIntervalDistance) {
                minIntervalDistance = intervalDistance;
                translationAxis = axis;

                if (d.dot(translationAxis) < 0) {
                    translationAxis = translationAxis.neg();
                }
            }
        }
 
        if (result.Intersect) {
            result.MinimumTranslationVector = translationAxis.mult(minIntervalDistance);
        }

        return result;
    }

}
