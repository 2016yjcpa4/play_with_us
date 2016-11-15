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

    private List<Point2D> mPoints = new ArrayList<>();
    private List<Vector2D> mEdges = new ArrayList<>();

    public Polygon(List<Point2D> l) {
        setPoints(l);
    }
    
    protected void setPoints(List<Point2D> l) {
        mPoints.clear();
        mPoints.addAll(l);
        
        setEdges();
    }
    
    private final void setEdges() {
        mEdges.clear();
        
        for (int n = 0; n < mPoints.size(); n++) {
            Point2D p1 = getPoint(n);
            Point2D p2 = getPoint(n + 1);
            
            mEdges.add(new Vector2D(p2).sub(p1));
        }
    }

    public final List<Point2D> getPoints() {
        return Collections.unmodifiableList(mPoints);
    }

    public final List<Vector2D> getEdges() {
        return Collections.unmodifiableList(mEdges);
    }

    public final void transform(Matrix2D m) {
        transform(m, getPosition());
    }

    public final void transform(Matrix2D m, Point2D c) {
        m = Matrix2D.translate(c.getX(), c.getY())
                    .concat(m)
                    .concat(Matrix2D.translate(-c.getX(), -c.getY()));

        for (Point2D p : getPoints()) {
            double x = p.getX();
            double y = p.getY();

            p.setX((int) (x * m.getA() + y * m.getC() + m.getE()));
            p.setY((int) (x * m.getB() + y * m.getD() + m.getF()));
        }
        
        setEdges();
    }

    public final Point2D getPosition() {
        int n = mPoints.size();

        Vector2D v = new Vector2D();
        for (Point2D p : mPoints) {
            v = v.add(new Vector2D(p.getX(), p.getY()));
        }

        int x = (int) (v.getX() / n);
        int y = (int) (v.getY() / n);
        return new Point2D(x, y);
    }

    public final Point2D getPoint(int n) {
        return new Point2D(mPoints.get(ArrayUtil.getFixedArrayIndex(n, mPoints.size())));
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
    public final List<Polygon> getTriangulate() {
        return EarCutTriangulator.getTriangulate(this);
    }
    
    public final java.awt.Polygon toAWTPolygon() {
        return new java.awt.Polygon(Point2D.getXPoints(mPoints), Point2D.getYPoints(mPoints), mPoints.size());
    }
}
