package org.game.geom;

import java.awt.Point;
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

    protected void addPoint(Point2D p) {
        this.p.add(p);
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

    private static class DecomposedPolygon {

        private Polygon o;
        private Point2D p;
        private double d;
        private int n;

        public DecomposedPolygon() {
            this.o = new Polygon();
            this.p = new Point2D();
            this.d = Double.MAX_VALUE;
            this.n = 0;
        }

        public Polygon getPolygon() {
            return o;
        }

        public Point2D getPoint() {
            return p;
        }

        public void setPoint(Point2D p) {
            this.p = p;
        }

        public void setDistance(double d) {
            this.d = d;
        }

        public double getDistance() {
            return d;
        }

        public int getIndex() {
            return n;
        }

        public void setIndex(int n) {
            this.n = n;
        }
    }

    public static ArrayList<Polygon> getDecomposedPolygons(Polygon p) {
        return getDecomposedPolygons(p, new ArrayList<Polygon>());
    }

    private static ArrayList<Polygon> getDecomposedPolygons(Polygon o, ArrayList<Polygon> r) {

        int len = o.getPoints().size();

        DecomposedPolygon u = new DecomposedPolygon(); // upper polygon
        DecomposedPolygon l = new DecomposedPolygon(); // lower polygon

        if (len < 3) {
            return r;
        }

        for (int n = 0; n < len; ++n) {

            if (Triangle.isRight(o.getPoint(n - 1), o.getPoint(n), o.getPoint(n + 1))) {

                for (int k = 0; k < o.getPoints().size(); ++k) {

                    if (Triangle.isLeft(o.getPoint(n - 1), o.getPoint(n), o.getPoint(k)) 
                     && Triangle.isRightOn(o.getPoint(n - 1), o.getPoint(n), o.getPoint(k - 1))) { // if line intersects with an edge

                        Point2D p = getIntersectionPoint(o.getPoint(n - 1)
                                                       , o.getPoint(n)
                                                       , o.getPoint(k)
                                                       , o.getPoint(k - 1)); // find the point of intersection

                        if (Triangle.isRight(o.getPoint(n + 1), o.getPoint(n), p)) { // make sure it's inside the poly

                            double d = new Vector2D(o.getPoint(n)).sub(p).getLengthSquared();
                            if (d < l.getDistance()) { // keep only the closest intersection
                                l.setDistance(d);
                                l.setPoint(p);
                                l.setIndex(k);
                            }
                        }
                    }

                    if (Triangle.isLeft(o.getPoint(n + 1), o.getPoint(n), o.getPoint(k + 1))
                     && Triangle.isRightOn(o.getPoint(n + 1), o.getPoint(n), o.getPoint(k))) {

                        Point2D p = getIntersectionPoint(o.getPoint(n + 1)
                                                       , o.getPoint(n)
                                                       , o.getPoint(k)
                                                       , o.getPoint(k + 1));

                        if (Triangle.isLeft(o.getPoint(n - 1), o.getPoint(n), p)) {

                            double d = new Vector2D(o.getPoint(n)).sub(p).getLengthSquared();
                            if (d < u.getDistance()) {
                                u.setDistance(d);
                                u.setPoint(p);
                                u.setIndex(k);
                            }
                        }
                    }
                }

                if (l.getIndex() == (u.getIndex() + 1) % len) {
                    Point2D p = new Point2D((l.getPoint().getX() + u.getPoint().getX()) / 2, (l.getPoint().getY() + u.getPoint().getY()) / 2);

                    if (n < u.getIndex()) {

                        for (int k = n; k < u.getIndex() + 1; k++) {
                            l.getPolygon().addPoint(o.getPoint(k));
                        }

                        l.getPolygon().addPoint(p);
                        u.getPolygon().addPoint(p);

                        if (l.getIndex() != 0) {
                            for (int k = l.getIndex(); k < len; k++) {
                                u.getPolygon().addPoint(o.getPoint(k));
                            }
                        }

                        for (int k = 0; k < n + 1; k++) {
                            u.getPolygon().addPoint(o.getPoint(k));
                        }
                    } else {
                        if (n != 0) {
                            for (int k = n; k < len; k++) {
                                l.getPolygon().addPoint(o.getPoint(k));
                            }
                        }

                        for (int k = 0; k < u.getIndex() + 1; k++) {
                            l.getPolygon().addPoint(o.getPoint(k));
                        }

                        l.getPolygon().addPoint(p);
                        u.getPolygon().addPoint(p);

                        for (int k = l.getIndex(); k < n + 1; k++) {
                            u.getPolygon().addPoint(o.getPoint(k));
                        }
                    }
                } else {
                    if (l.getIndex() > u.getIndex()) {
                        u.setIndex(u.getIndex() + len);
                    }

                    DecomposedPolygon c = new DecomposedPolygon(); // lower polygon 

                    if (u.getIndex() < l.getIndex()) {
                        return r;
                    }

                    for (int k = l.getIndex(); k <= u.getIndex(); ++k) {

                        if (Triangle.isLeftOn(o.getPoint(n - 1), o.getPoint(n), o.getPoint(k))
                         && Triangle.isRightOn(o.getPoint(n + 1), o.getPoint(n), o.getPoint(k))) {

                            double d = new Vector2D(o.getPoint(n)).sub(o.getPoint(k)).getLengthSquared();
                            if (d < c.getDistance()) {
                                c.setDistance(d);
                                c.setIndex(k % len);
                            }
                        }
                    }

                    if (n < c.getIndex()) {

                        for (int k = n; k < c.getIndex() + 1; k++) {
                            l.getPolygon().addPoint(o.getPoint(k));
                        }

                        if (c.getIndex() != 0) {
                            for (int k = c.getIndex(); k < len; k++) {
                                u.getPolygon().addPoint(o.getPoint(k));
                            }
                        }

                        for (int k = 0; k < n + 1; k++) {
                            u.getPolygon().addPoint(o.getPoint(k));
                        }
                    } else {

                        if (n != 0) {
                            for (int k = n; k < len; k++) {
                                l.getPolygon().addPoint(o.getPoint(k));
                            }
                        }

                        for (int k = 0; k < c.getIndex() + 1; k++) {
                            l.getPolygon().addPoint(o.getPoint(k));
                        }

                        for (int k = c.getIndex(); k < n + 1; k++) {
                            u.getPolygon().addPoint(o.getPoint(k));
                        }
                    }
                }

                if (l.getPolygon().getPoints().size() < u.getPolygon().getPoints().size()) {
                    getDecomposedPolygons(l.getPolygon(), r);
                    getDecomposedPolygons(u.getPolygon(), r);
                } else {
                    getDecomposedPolygons(u.getPolygon(), r);
                    getDecomposedPolygons(l.getPolygon(), r);
                }

                return r;
            }
        }

        r.add(o);

        return r;
    }

    // TODO 리팩토링
    private static Point2D getIntersectionPoint(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
        double a1 = p2.getY() - p1.getY();
        double b1 = p1.getX() - p2.getX();
        double c1 = (a1 * p1.getX()) + (b1 * p1.getY());

        double a2 = p4.getY() - p3.getY();
        double b2 = p3.getX() - p4.getX();
        double c2 = (a2 * p3.getX()) + (b2 * p3.getY());

        double d = (a1 * b2) - (a2 * b1);

        if (Math.abs(d) >= 0) {
            return new Point2D((int) (((b2 * c1) - (b1 * c2)) / d), (int) (((a1 * c2) - (a2 * c1)) / d));
        } else {
            return new Point2D();
        }
    }
}
