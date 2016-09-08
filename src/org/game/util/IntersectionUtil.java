/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.util;

import java.util.ArrayList;
import java.util.List;
import org.game.math.Line2D;
import org.game.math.Point2D;
import org.game.math.Vector2D;

public class IntersectionUtil {

    public static boolean hasPoint(Point2D p, List<Point2D> l) {

        if (l.isEmpty()) {
            return false;
        }
        
        int x = p.getX();
        int y = p.getY();
         
        Point2D p1 = l.get(0);
        Point2D p2 = l.get(l.size() - 1);
        
        boolean hasPoint = false;
        

        for (int n = 1; n < l.size(); n++) {            
            int x1 = p1.getX();
            int y1 = p1.getY();
            
            int x2 = p2.getX();
            int y2 = p2.getY();

            if ((( y1 > y) != (y2 > y)) && (x < (x2 - x1) * (y -  y1) / (y2 -  y1) + x1)) {
                hasPoint = ! hasPoint;
            }
            
            p1 = l.get(n);
            p2 = l.get(n - 1);
        }

        return hasPoint;
    }
    
    public static List<Point2D> getBresenhamLines(int x1, int y1, int x2, int y2) {
        List<Point2D> l = new ArrayList<>();

        int x = x1 < x2 ? 1 : -1;
        int y = y1 < y2 ? 1 : -1;
 
        int dx = Math.abs(x2 - x1);
        int dy = -Math.abs(y2 - y1);
        
        double e = dx + dy;

        while (true) {
            
            l.add(new Point2D(x1, y1));
            
            if (x1 == x2 && y1 == y2) {
                break;
            }
            
            double theta = 2 * e;
            
            if (theta > dy) {
                e += dy;
                x1 += x;
            } else if (theta < dx) {
                e += dx;
                y1 += y;
            }
        }

        return l;
    }
    
    private static IntersectionResult getIntersection(Point2D p, double angle, Line2D l) {
        Vector2D v1 = new Vector2D((p.getX() + Math.cos(angle)) - p.getX(), (p.getY() + Math.sin(angle)) - p.getY());
        Vector2D v2 = new Vector2D(l.getX2() - l.getX1(), l.getY2() - l.getY1());

        if (v1.getX() / v1.length() == v2.getX() / v2.length() && v1.getY() / v1.length() == v2.getY() / v2.length()) {
            return null;
        }

        double t2 = (v1.getX() * (l.getY1() - p.getY()) + v1.getY() * (p.getX() - l.getX1())) / (v2.getX() * v1.getY() - v2.getY() * v1.getX());
        double t1 = (l.getX1() + v2.getX() * t2 - p.getX()) / v1.getX();

        if (t1 < 0 || (t2 < 0 || t2 > 1)) {
            return null;
        }

        return new IntersectionResult(new Point2D((int) (p.getX() + v1.getX() * t1), 
                                                   (int) (p.getY() + v1.getY() * t1)), t1);
    }
    
    /**
     * 가상의 "레이저 빔" 을 원점으로부터 해당 빔을 따라 충돌체(collider)에 부딪힐 때까지 뻗어갑니다. 
     * 
     * @return 부딪힌 지점이 반환됩니다.
     */
    public static Point2D getIntersection(Point2D p, double angle, List<Line2D> l) {
        IntersectionResult r = null;
        
        for (int n = 0; n < l.size(); ++n){
            IntersectionResult _r = getIntersection(p, angle, l.get(n));

            if (_r == null) {
                continue;
            }

            if (r == null || _r.getTheta() < r.getTheta()) {
                r = _r;
            }

        }
        
        if (r == null) {
            return null;
        }
        
        return r.getHit();
    }
    
    
    private static class IntersectionResult {
        
        private final Point2D hit;
        private final double theta;
        
        public IntersectionResult(Point2D hit, double theta) {
            this.hit = hit;
            this.theta = theta;
        }
        
        public Point2D getHit() {
            return hit;
        }
        
        public double getTheta() {
            return theta;
        }
    }
}
