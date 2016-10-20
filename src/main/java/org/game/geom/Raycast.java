/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.geom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.game.math.Line2D; 
import org.game.math.Point2D;
import org.game.math.Vector2D;

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

    /**
     * 들어온 Line2D 에서 시작점과 끝점을 분리하는작업.
     * 
     * @param l
     * @return 분리된 점을 목록형태로 가져옵니다.
     */
    private static List<Point2D> getUniquePoints(List<Line2D> l) {
        List<Point2D> r = new ArrayList<>();

        for (int n = 0; n < l.size(); ++n) {
            r.add(l.get(n).getStartPoint());
            r.add(l.get(n).getEndPoint());
        }

        return r;
    }

    private static double getDiff(double src, double dst) {
        return (src - dst + Math.PI + (Math.PI * 2)) % (Math.PI * 2) - Math.PI;
    }

    private static List<Double> getUniqueAngles(Point2D s, double dir, List<Line2D> l) {
        List<Double> r = new ArrayList<>();

        List<Point2D> p = getUniquePoints(l);

        double max = dir + Math.toRadians(25);
        double min = dir - Math.toRadians(25);

        r.add(min);
        r.add(max);

        final double d = Math.toRadians(25);
        
        for (Point2D e : p) {
            double ang = Math.atan2(e.getY() - s.getY(), e.getX() - s.getX());

            double anglediff = getDiff(dir, ang);

            // 양쪽으로 편차를 더 두어 벽이있는지 체크함
            if (-d <= anglediff && anglediff <= d) {
                r.add(ang - 0.0001); 
                r.add(ang); 
                r.add(ang + 0.0001);
            }
        }

        r.sort(new Comparator<Double>() {
            
            @Override
            public int compare(Double a, Double b) {
                double c = getDiff(a, b);
                
                if (c > 0) {
                    return 1;
                }else if (c < 0) {
                    return -1;
                }
                return 0;
            }
        });

        
        return r;
    }

    public static List<Point2D> getRaycast(Point2D s, double ang, List<Line2D> l) {

        List<IntersectionResult> intersects = new ArrayList();

        for (Double n : getUniqueAngles(s, ang, l)) {
            
            Line2D ray = new Line2D(s.getX(),
                                s.getY(),
                                s.getX() + (float) Math.cos(n),
                                s.getY() + (float) Math.sin(n));

            IntersectionResult r2 = null;
            
            for (Line2D e : l) {
                IntersectionResult r = getIntersection(ray, e);
                
                if (r == null || r.isNaN()) {
                    continue;
                }
                
                if (r2 == null || r.mParam < r2.mParam) {
                    r2 = r;
                }
            }

            if (r2 == null) {
                continue;
            }

            intersects.add(r2);
        }
  
        return new ArrayList<Point2D>() {
            {
                for (IntersectionResult e : intersects) {
                    add(e.toPoint());
                }
            }
        };
    }
}
