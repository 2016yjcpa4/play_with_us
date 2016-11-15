package com.github.yjcpaj4.play_with_us.geom;

import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import java.util.ArrayList;
import java.util.LinkedList;
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
    public static float IntervalDistance(ProjectPolygon a, ProjectPolygon b) {
        if (a.min < b.min) {
            return b.min - a.max;
        } else {
            return a.min - b.max;
        }
    }

    // Calculate the projection of a polygon on an axis and returns it as a [min, max] interval
    public static ProjectPolygon getProjectPolygon(Vector2D axis, Polygon polygon) {
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
    
    
    private static List<Vector2D> getEdges(Polygon ... p) {
        List l = new LinkedList();
        for(int n = 0; n < p.length; ++n) {
            l.addAll(p[n].getEdges());
        }
        return l;
    }
    
    // Check if polygon A is going to collide with polygon B for the given velocity
    public static PolygonCollisionResult PolygonCollision(Polygon polygonA, Polygon polygonB) {
        Vector2D d = new Vector2D(polygonA.getPosition()).sub(polygonB.getPosition());
        PolygonCollisionResult result = new PolygonCollisionResult();
        result.Intersect = true; 

        float minIntervalDistance = Float.MAX_VALUE;
        Vector2D translationAxis = new Vector2D();
 
        for (Vector2D edge : getEdges(polygonA, polygonB)) {
            
            Vector2D axis = edge.perp().norm();
 
            ProjectPolygon A = getProjectPolygon(axis, polygonA);
            ProjectPolygon B = getProjectPolygon(axis, polygonB);
            float intervalDistance = IntervalDistance(A, B);
 
            if (intervalDistance > 0) {
                result.Intersect = false; 
                break;
            }
 
            if (Math.abs(intervalDistance) < minIntervalDistance) {
                minIntervalDistance = Math.abs(intervalDistance);
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
