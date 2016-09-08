package org.game.geom;

import java.util.ArrayList;
import java.util.List;
import org.game.math.Matrix2D;
import org.game.math.Point2D;
import org.game.math.Vector2D;

public class Polygon {

    private List<Point2D> vtx = new ArrayList<>();
    private List<Point2D> edg = new ArrayList<>();

    public Polygon(List<Point2D> vtx) {
        this.vtx.addAll(vtx);

        this.setEdges();
    }

    private void setEdges() {
        edg.clear();

        for (int n = 0; n < vtx.size(); n++) {
            Point2D p1 = vtx.get(n);
            Point2D p2;

            if (n + 1 >= vtx.size()) {
                p2 = vtx.get(0);
            } else {
                p2 = vtx.get(n + 1);
            }

            Vector2D v = new Vector2D(p2).sub(p1);

            edg.add(new Point2D((int) v.getX(), (int) v.getY()));
        }
    }

    public void transform(Matrix2D m) {
        // TODO
    }

    private Point2D getCenterPosition() {

        float x = 0;
        float y = 0;
        for (int n = 0; n < vtx.size(); n++) {
            x += vtx.get(n).getX();
            y += vtx.get(n).getY();
        }

        return new Point2D((int) (x / (float) vtx.size()), (int) (y / (float) vtx.size()));
    }
    
    public class PolygonCollisionResult {
            public boolean isWillCollide; // Are the polygons going to intersect forward in time?
            public boolean isCollide; // Are the polygons currently intersecting
            public Vector2D minTranslVec; // The translation to apply to polygon A to push the polygons appart.
    }

    // min, max
    public void ProjectPolygon(Vector2D axis, Polygon polygon, double min, double max) {
        // To project a point on an axis use the dot product
        double d = axis.dotProduct(polygon.vtx.get(0));
        min = d;
        max = d;
        for (int n = 0; n < polygon.vtx.size(); n++) {
            d = new Vector2D(polygon.vtx.get(n)).dotProduct(axis);
            if (d < min) {
                min = d;
            } else if (d > max) {
                max = d;
            }
        }
    }

    public float IntervalDistance(float ma, float maxA, float minB, float maxB) {
        if (ma < minB) {
            return minB - maxA;
        } else {
            return ma - maxB;
        }
    }
}
