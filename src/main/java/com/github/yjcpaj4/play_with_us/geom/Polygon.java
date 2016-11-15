package com.github.yjcpaj4.play_with_us.geom;

import java.util.ArrayList;
import java.util.List; 
import com.github.yjcpaj4.play_with_us.math.Matrix2D;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.util.ArrayUtil;
import java.util.Collection;
import java.util.Collections;

public class Polygon {

    protected List<Point2D> mPoints = new ArrayList<>();
    private List<Vector2D> mEdges = new ArrayList<>();

    public Polygon() {
    }

    public Polygon(List<Point2D> p) {
        mPoints.addAll(p);
        
        init();
    }
    
    protected void init() {
        mEdges.clear();
        
        for (int n = 0; n < mPoints.size(); n++) {
            Point2D p1 = getPoint(n);
            Point2D p2 = getPoint(n + 1);
            
            mEdges.add(new Vector2D(p2).sub(p1));
        }
    }

    public List<Point2D> getPoints() {
        return Collections.unmodifiableList(mPoints);
    }

    public List<Vector2D> getEdges() {
        return Collections.unmodifiableList(mEdges);
    }

    public void transform(Matrix2D m) {
        transform(m, getCenterPosition());
    }

    public void transform(Matrix2D m, Point2D c) {
        m = Matrix2D.translate(c.getX(), c.getY())
                    .concat(m)
                    .concat(Matrix2D.translate(-c.getX(), -c.getY()));

        for (Point2D p : getPoints()) {
            double x = p.getX();
            double y = p.getY();

            p.setX((int) (x * m.getA() + y * m.getC() + m.getE()));
            p.setY((int) (x * m.getB() + y * m.getD() + m.getF()));
        }
        
        init();
    }

    public Point2D getCenterPosition() {
        int len = mPoints.size();

        Vector2D v = new Vector2D();

        for (int n = 0; n < len; ++n) {
            v = v.add(new Vector2D(mPoints.get(n).getX(), mPoints.get(n).getY()));
        }

        int x = (int) (v.getX() / len);
        int y = (int) (v.getY() / len);

        return new Point2D(x, y);
    }

    public Point2D getPoint(int n) {
        return new Point2D(mPoints.get(ArrayUtil.getFixedArrayIndex(n, mPoints.size())));
    }
    
    public Vector2D getEdge(int n) {
        return new Vector2D(mEdges.get(ArrayUtil.getFixedArrayIndex(n, mEdges.size())));
    }
    
    public void addPoint(Point2D p) {
        mPoints.add(p);
        
        init();
    }
    
    /**
     * 폴리곤 객체를 삼각형으로 쪼갭니다.
     * 
     * 폴리곤 객체를 만들때 볼록한 폴리곤이 만들어질수도 있고 오목한 폴리곤이 만들어질수도 있습니다.
     * 볼록한(convex) 폴리곤이 만들어진경우 SAT(Seprating Axis Theorem) 충돌 검사를 하여도 오류가 납니다. 
     * 이때 이 함수를 호출하여 삼각형으로 쪼개고 검사를 할경우 충돌 검사가 잘될것입니다.
     * 
     * @return 
     */
    public List<Polygon> getTriangulate() {
        return EarCutTriangulator.getTriangulate(this);
    }
    
    public java.awt.Polygon toAWTPolygon() {
        return new java.awt.Polygon(Point2D.getXPoints(mPoints), Point2D.getYPoints(mPoints), mPoints.size());
    }
}
