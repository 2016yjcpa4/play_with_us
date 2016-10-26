package com.github.yjcpaj4.play_with_us.geom;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.github.yjcpaj4.play_with_us.math.Line2D;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;

/**
 * 레이캐스트.
 * 
 * @see http://ncase.me/sight-and-light/
 */
public class Raycast {

    private Raycast() {
    }

    private static class IntersectionResult {

        public double mX;
        public double mY;
        public double mParam;

        public IntersectionResult(double x, double y, double n) {
            this.mX = x;
            this.mY = y;
            this.mParam = n;
        }
        
        public boolean isNaN() {
            return Double.isNaN(mX) || Double.isNaN(mY);
        }
        
        public Point2D toPoint() {
            return new Point2D((int) mX, (int) mY);
        }
    }

    /**
     * 빔을 쐈을때 벽면에 충돌하는 결과를 반환
     * 
     * @param l1 빔
     * @param l2 벽면
     * @return 
     */
    private static IntersectionResult getIntersection(Line2D l1, Line2D l2) {
        final Vector2D v1 = new Vector2D(l1.getX2(), l1.getY2()).sub(l1.getX1(), l1.getY1());
        final Vector2D v2 = new Vector2D(l2.getX2(), l2.getY2()).sub(l2.getX1(), l2.getY1());
        
        if (v1.getX() / v1.length() == v2.getX() / v2.length() && v1.getY() / v1.length() == v2.getY() / v2.length()) {
            return null;
        }

        double n1 = (v1.getX() * (l2.getY1() - l1.getY1()) + v1.getY() * (l1.getX1() - l2.getX1())) / (v2.getX() * v1.getY() - v2.getY() * v1.getX());
        double n2 = (l2.getX1() + v2.getX() * n1 - l1.getX1()) / v1.getX();

        if (n2 < 0) {
            return null;
        }
        
        if (n1 < 0 || n1 > 1) {
            return null;
        }

        return new IntersectionResult(
                        l1.getX1() + v1.getX() * n2,
                        l1.getY1() + v1.getY() * n2,
                        n2);
    }

    private static double getDiff(double n1, double n2) {
        return (n1 - n2 + Math.PI + (Math.PI * 2)) % (Math.PI * 2) - Math.PI;
    }

    private static List<Point2D> getUniquePoints(List<Line2D> l) {
        List<Point2D> r = new ArrayList<>();

        for (int n = 0; n < l.size(); ++n) {
            r.add(l.get(n).getStartPoint());
            r.add(l.get(n).getEndPoint());
        }

        return r;
    }

    private static List<Double> getUniqueAngles(Point2D s, double d1, double d2, List<Line2D> l) {
        List<Double> r = new ArrayList<>();

        r.add(d1 - d2);
        r.add(d1 + d2);

        for (Point2D p : getUniquePoints(l)) {
            double n1 = Math.atan2(p.getY() - s.getY(), p.getX() - s.getX());
            double n2 = getDiff(d1, n1);

            if (-d2 <= n2 && n2 <= d2) { // 양쪽으로 편차를 더 두어 벽이있는지 체크함
                r.add(n1 - 0.0001); 
                r.add(n1); 
                r.add(n1 + 0.0001);
            }
        }

        r.sort(new Comparator() {
            
            @Override
            public int compare(Object o1, Object o2) {
                double n = getDiff((Double) o1, (Double) o2);
                
                if (n > 0) {
                    return 1;
                }
                else if (n < 0) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });

        
        return r;
    }

    public static List<Point2D> getRaycast(Point2D p, double n, List<Line2D> l) {

        List<IntersectionResult> v = new ArrayList();

        for (Double n2 : getUniqueAngles(p, n, Math.toRadians(25), l)) {
            
            Line2D l2 = new Line2D(p.getX(),
                                    p.getY(),
                                    p.getX() + (float) Math.cos(n2),
                                    p.getY() + (float) Math.sin(n2));

            IntersectionResult v1 = null;
            
            for (Line2D l3 : l) {
                IntersectionResult v2 = getIntersection(l2, l3);
                
                if (v2 == null || v2.isNaN()) {
                    continue;
                }
                
                if (v1 == null || v2.mParam < v1.mParam) {
                    v1 = v2;
                }
            }

            if (v1 == null) {
                continue;
            }

            v.add(v1);
        }
  
        return new ArrayList<Point2D>() {
            {
                for (IntersectionResult v2 : v) {
                    add(v2.toPoint());
                }
            }
        };
    }
}
