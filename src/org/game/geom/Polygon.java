package org.game.geom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import org.game.math.Matrix2D;
import org.game.math.Point2D;
import org.game.math.Vector2D;

public class Polygon implements Shape {

    protected List<Point2D> p = new ArrayList<>();

    public Polygon() {
    }

    public Polygon(List<Point2D> p) {
        this.p.addAll(p);
    }

    public int[] getXPoints() {
        return Point2D.getXPoints(p);
    }

    public int[] getYPoints() {
        return Point2D.getYPoints(p);
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

    public double getArea() {
        throw new UnsupportedOperationException();
    }

    public Point2D getPoint(int n) {
        int len = p.size();

        return p.get(n < 0 ? n % len + len : n % len);
    }

    public void addPoint(Point2D p) {
        this.p.add(p);
    }

    private static final int MAX_LEVEL = 100;

    private void push(Point2D p) {
        this.p.add(p);
    }

    private static void polygonAppend(Polygon polygon, Polygon poly, int from, int to) {
        for (int i = from; i < to; i++) {
            polygon.push(poly.getPoint(i));
        }
    }

    public static List<Polygon> getSamples() {
        List<Point2D> l = new ArrayList<>();
        l.add(new Point2D(323, 41));
        l.add(new Point2D(372, 59));
        l.add(new Point2D(421, 77));
        l.add(new Point2D(474, 89));
        l.add(new Point2D(518, 114));
        l.add(new Point2D(561, 143));
        l.add(new Point2D(610, 159));
        l.add(new Point2D(664, 163));
        l.add(new Point2D(713, 152));
        l.add(new Point2D(744, 108));
        l.add(new Point2D(768, 61));
        l.add(new Point2D(801, 23));
        l.add(new Point2D(850, 0));
        l.add(new Point2D(917, 3));
        l.add(new Point2D(971, 29));
        l.add(new Point2D(972, 81));
        l.add(new Point2D(922, 87));
        l.add(new Point2D(874, 71));
        l.add(new Point2D(826, 86));
        l.add(new Point2D(823, 139));
        l.add(new Point2D(841, 190));
        l.add(new Point2D(890, 219));
        l.add(new Point2D(936, 193));
        l.add(new Point2D(975, 157));
        l.add(new Point2D(1023, 137));
        l.add(new Point2D(1076, 146));
        l.add(new Point2D(1120, 178));
        l.add(new Point2D(1121, 230));
        l.add(new Point2D(1098, 281));
        l.add(new Point2D(1060, 317));
        l.add(new Point2D(1006, 341));
        l.add(new Point2D(943, 360));
        l.add(new Point2D(885, 373));
        l.add(new Point2D(833, 374));
        l.add(new Point2D(776, 361));
        l.add(new Point2D(730, 335));
        l.add(new Point2D(689, 302));
        l.add(new Point2D(645, 271));
        l.add(new Point2D(598, 248));
        l.add(new Point2D(549, 231));
        l.add(new Point2D(486, 228));
        l.add(new Point2D(441, 258));
        l.add(new Point2D(394, 291));
        l.add(new Point2D(334, 319));
        l.add(new Point2D(278, 329));
        l.add(new Point2D(218, 317));
        l.add(new Point2D(184, 280));
        l.add(new Point2D(171, 230));
        l.add(new Point2D(175, 179));
        l.add(new Point2D(195, 125));
        l.add(new Point2D(224, 84));
        l.add(new Point2D(274, 52));

        return getDecomposedPolygons(new Polygon(l));
    }

    private static ArrayList<Polygon> getDecomposedPolygons(Polygon p) {
        return getDecomposedPolygons(p, new ArrayList<Polygon>(), 0);
    }

    private static ArrayList<Polygon> getDecomposedPolygons(Polygon polygon, ArrayList<Polygon> result, int level) {

        Point2D upperInt = new Point2D(), lowerInt = new Point2D(), p = new Point2D(); // Points
        double upperDist = 0, lowerDist = 0, d = 0, closestDist = 0; // scalars
        int upperIndex = 0, lowerIndex = 0, closestIndex = 0; // Integers
        Polygon lowerPoly = new Polygon();
        Polygon upperPoly = new Polygon(); // polygons

        if (polygon.getPoints().size() < 3) {
            return result;
        }

        if (level > MAX_LEVEL) {
            return result;
        }

        for (int i = 0; i < polygon.getPoints().size(); ++i) {
            if (Triangle.isRight(polygon.getPoint(i - 1), polygon.getPoint(i), polygon.getPoint(i + 1))) {
                upperDist = lowerDist = Double.MAX_VALUE;

                for (int j = 0; j < polygon.getPoints().size(); ++j) {
                    if (Triangle.isLeft(polygon.getPoint(i - 1), polygon.getPoint(i), polygon.getPoint(j)) && Triangle.isRightOn(polygon.getPoint(i - 1), polygon.getPoint(i), polygon.getPoint(j - 1))) { // if line intersects with an edge
                        p = getIntersectionPoint(polygon.getPoint(i - 1), polygon.getPoint(i), polygon.getPoint(j), polygon.getPoint(j - 1)); // find the point of intersection
                        if (Triangle.isRight(polygon.getPoint(i + 1), polygon.getPoint(i), p)) { // make sure it's inside the poly
                            d = new Vector2D(polygon.getPoint(i)).sub(p).getLengthSquared();
                            if (d < lowerDist) { // keep only the closest intersection
                                lowerDist = d;
                                lowerInt = p;
                                lowerIndex = j;
                            }
                        }
                    }
                    if (Triangle.isLeft(polygon.getPoint(i + 1), polygon.getPoint(i), polygon.getPoint(j + 1)) && Triangle.isRightOn(polygon.getPoint(i + 1), polygon.getPoint(i), polygon.getPoint(j))) {
                        p = getIntersectionPoint(polygon.getPoint(i + 1), polygon.getPoint(i), polygon.getPoint(j), polygon.getPoint(j + 1));
                        if (Triangle.isLeft(polygon.getPoint(i - 1), polygon.getPoint(i), p)) {
                            d = new Vector2D(polygon.getPoint(i)).sub(p).getLengthSquared();
                            if (d < upperDist) {
                                upperDist = d;
                                upperInt = p;
                                upperIndex = j;
                            }
                        }
                    }
                }

                // if there are no vertices to connect to, choose a point in the middle
                if (lowerIndex == (upperIndex + 1) % polygon.getPoints().size()) {
                    p.setX((lowerInt.getX() + upperInt.getX()) / 2);
                    p.setY((lowerInt.getY() + upperInt.getY()) / 2);

                    if (i < upperIndex) {
                        for (int k = i; k < upperIndex + 1; k++) {
                            lowerPoly.push(polygon.getPoint(k));
                        }

                        lowerPoly.push(p);
                        upperPoly.push(p);
                        if (lowerIndex != 0) {
                            for (int k = lowerIndex; k < polygon.getPoints().size(); k++) {
                                upperPoly.push(polygon.getPoint(k));
                            }

                        }
                        for (int k = 0; k < i + 1; k++) {
                            upperPoly.push(polygon.getPoint(k));
                        }
                    } else {
                        if (i != 0) {
                            for (int k = i; k < polygon.getPoints().size(); k++) {
                                lowerPoly.push(polygon.getPoint(k));
                            }
                        }

                        for (int k = 0; k < upperIndex + 1; k++) {
                            lowerPoly.push(polygon.getPoint(k));
                        }
                        lowerPoly.push(p);
                        upperPoly.push(p);

                        for (int k = lowerIndex; k < i + 1; k++) {
                            upperPoly.push(polygon.getPoint(k));
                        }
                    }
                } else {
                    if (lowerIndex > upperIndex) {
                        upperIndex += polygon.getPoints().size();
                    }
                    closestDist = Double.MAX_VALUE;

                    if (upperIndex < lowerIndex) {
                        return result;
                    }

                    for (int j = lowerIndex; j <= upperIndex; ++j) {
                        if (Triangle.isLeftOn(polygon.getPoint(i - 1), polygon.getPoint(i), polygon.getPoint(j))
                                && Triangle.isRightOn(polygon.getPoint(i + 1), polygon.getPoint(i), polygon.getPoint(j))) {
                            d = new Vector2D(polygon.getPoint(i)).sub(polygon.getPoint(j)).getLengthSquared();
                            if (d < closestDist) {
                                closestDist = d;
                                closestIndex = j % polygon.getPoints().size();
                            }
                        }
                    }

                    if (i < closestIndex) {
                        polygonAppend(lowerPoly, polygon, i, closestIndex + 1);
                        if (closestIndex != 0) {
                            polygonAppend(upperPoly, polygon, closestIndex, polygon.getPoints().size());
                        }
                        polygonAppend(upperPoly, polygon, 0, i + 1);
                    } else {
                        if (i != 0) {
                            polygonAppend(lowerPoly, polygon, i, polygon.getPoints().size());
                        }
                        polygonAppend(lowerPoly, polygon, 0, closestIndex + 1);
                        polygonAppend(upperPoly, polygon, closestIndex, i + 1);
                    }
                }

                if (lowerPoly.getPoints().size() < upperPoly.getPoints().size()) {
                    getDecomposedPolygons(lowerPoly, result, level + 1);
                    getDecomposedPolygons(upperPoly, result, level + 1);
                } else {
                    getDecomposedPolygons(upperPoly, result, level + 1);
                    getDecomposedPolygons(lowerPoly, result, level + 1);
                }

                return result;
            }
        }
        result.add(polygon);

        return result;
    }

    private static Point2D getIntersectionPoint(Point2D p1, Point2D p2, Point2D q1, Point2D q2) {
        double a1 = p2.getY() - p1.getY();
        double b1 = p1.getX() - p2.getX();
        double c1 = (a1 * p1.getX()) + (b1 * p1.getY());
        double a2 = q2.getY() - q1.getY();
        double b2 = q1.getX() - q2.getX();
        double c2 = (a2 * q1.getX()) + (b2 * q1.getY());
        double det = (a1 * b2) - (a2 * b1);

        if (Math.abs(det) >= 0) {
            return new Point2D((int) (((b2 * c1) - (b1 * c2)) / det), (int) (((a1 * c2) - (a2 * c1)) / det));
        } else {
            return new Point2D();
        }
    }
}
