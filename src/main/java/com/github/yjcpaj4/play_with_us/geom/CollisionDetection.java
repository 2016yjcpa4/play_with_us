package com.github.yjcpaj4.play_with_us.geom;

import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import java.util.LinkedList;
import java.util.List;

/**
 * Separating Axis Theorem.
 *
 * 축 분리 정리 두개의 오브젝트를 교차(충돌)하였는지 검사하는 클래스.
 */
public class CollisionDetection {

    private CollisionDetection() {
    }

    public static class ProjectPolygon {

        private float mMin = Float.POSITIVE_INFINITY;
        private float mMax = Float.NEGATIVE_INFINITY;

        public ProjectPolygon(Vector2D v, Polygon o) {
            for (Point2D p : o.getPoints()) {
                float n = v.dotProduct(p);

                mMin = Math.min(mMin, n);
                mMax = Math.max(mMax, n);
            }
        }

        public float getIntervalDistance(ProjectPolygon o) {
            if (mMin < o.mMin) {
                return o.mMin - mMax;
            } else {
                return mMin - o.mMax;
            }
        }
    }

    private static List<Vector2D> getEdges(Polygon p1, Polygon p2) {
        List l = new LinkedList();
        l.addAll(p1.getEdges());
        l.addAll(p2.getEdges());
        return l;
    }

    public static Vector2D getCollision(Polygon p1, Polygon p2) {
        float n1 = Float.POSITIVE_INFINITY;
        Vector2D v1 = new Vector2D(p1.getPosition()).subtract(p2.getPosition());
        Vector2D v2 = new Vector2D();

        for (Vector2D v3 : getEdges(p1, p2)) {
            Vector2D v4 = v3.perp().normalize();

            ProjectPolygon o1 = new ProjectPolygon(v4, p1);
            ProjectPolygon o2 = new ProjectPolygon(v4, p2);
            float n2 = o1.getIntervalDistance(o2);
            if (n2 > 0) {
                return null;
            }

            if (Math.abs(n2) < n1) {
                n1 = Math.abs(n2);
                v2 = v4;

                if (v1.dotProduct(v2) < 0) {
                    v2 = v2.negative();
                }
            }
        }

        return v2.multiply(n1);
    }

}
