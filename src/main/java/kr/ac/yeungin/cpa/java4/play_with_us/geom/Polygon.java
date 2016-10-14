package kr.ac.yeungin.cpa.java4.play_with_us.geom;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import kr.ac.yeungin.cpa.java4.play_with_us.math.Matrix2D;
import kr.ac.yeungin.cpa.java4.play_with_us.math.Point2D;
import kr.ac.yeungin.cpa.java4.play_with_us.math.Point2D;
import kr.ac.yeungin.cpa.java4.play_with_us.math.Vector2D;

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
            v = v.add(new Vector2D(p.get(n).x, p.get(n).y));
        }

        int x = (int) (v.getX() / len);
        int y = (int) (v.getY() / len);

        return new Point2D(x, y);
    }

    public Point2D getPoint(int n) {
        int len = p.size();

        return p.get(n < 0 ? n % len + len : n % len);
    }
    
    public void add(Point2D p) {
        this.p.add(p);
    }
}
