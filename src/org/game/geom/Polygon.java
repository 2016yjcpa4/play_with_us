package org.game.geom;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import org.game.math.Matrix2D;
import org.game.math.Point2D;
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
            v = v.add(new Vector2D(p.get(n).x, p.get(n).y));
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

    protected void push(Point2D p) {
        this.p.add(p);
    }
    protected void addPoint(Point2D p) {
        this.p.add(p);
    }
 


    // ----------------------------------------------------------
    /**
     * Adds a sequence of vertices from another polygon to the end of this one.
     *
     * @param source the polygon to copy the vertices from
     * @param start the starting index of the vertices to copy
     * @param end the ending index of the vertices to copy (this vertex is NOT
     *     included)
     */
    public void addFrom(Polygon source, int start, int end)
    {
        for (int i = start; i < end; i++)
        {
            p.add(source.get(i));
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the vertex at the specified index. This accessor is circular;
     * negative indices and indices greater than the size of the polygon are
     * wrapped appropriately.
     *
     * @param index the index of the vertex to get
     * @return the vertex at the specified index
     */
    public Point2D get(int index)
    {
        int realIndex = wrap(index, size());
        return (p.get(realIndex));
    }

    private static int wrap(int a, int b)
    {
        return (a < 0) ? (a % b + b) : (a % b);
    }
    public int size()
    {
        return p.size();
    }
    
    
    
}
