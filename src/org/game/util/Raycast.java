/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.game.math.Line2D;
import org.game.math.LineF2D;
import org.game.math.Point2D;
import org.game.math.Vector2D;

public class Raycast {

    private Raycast() {
        
    }
    
    private static class IntersectionResult {
        
        public double x;
        public double y;
        public double param;
        public double angle;
        
        public IntersectionResult(double x, double y, double param) {
            this.x = x;
            this.y = y;
            this.param = param;
        }
    }
    
    
    private static IntersectionResult getIntersection2(LineF2D ray, Line2D segment) {

        // RAY in parametric: Point + Delta*T1
        double r_px = ray.getX1();
        double r_py = ray.getY1();
        double r_dx = ray.getX2() - ray.getX1();
        double r_dy = ray.getY2() - ray.getY1();

        // SEGMENT in parametric: Point + Delta*T2
        double s_px = segment.getX1();
        double s_py = segment.getY1();
        double s_dx = segment.getX2() - segment.getX1();
        double s_dy = segment.getY2() - segment.getY1();

        // Are they parallel? If so, no intersect
        double r_mag = Math.sqrt(r_dx * r_dx + r_dy * r_dy);
        double s_mag = Math.sqrt(s_dx * s_dx + s_dy * s_dy);
        if (r_dx / r_mag == s_dx / s_mag && r_dy / r_mag == s_dy / s_mag) {
            // Unit vectors are the same.
            return null;
        }

        // SOLVE FOR T1 & T2
        // r_px+r_dx*T1 = s_px+s_dx*T2 && r_py+r_dy*T1 = s_py+s_dy*T2
        // ==> T1 = (s_px+s_dx*T2-r_px)/r_dx = (s_py+s_dy*T2-r_py)/r_dy
        // ==> s_px*r_dy + s_dx*T2*r_dy - r_px*r_dy = s_py*r_dx + s_dy*T2*r_dx - r_py*r_dx
        // ==> T2 = (r_dx*(s_py-r_py) + r_dy*(r_px-s_px))/(s_dx*r_dy - s_dy*r_dx)
        double T2 = (r_dx * (s_py - r_py) + r_dy * (r_px - s_px)) / (s_dx * r_dy - s_dy * r_dx);
        double T1 = (s_px + s_dx * T2 - r_px) / r_dx;

        // Must be within parametic whatevers for RAY/SEGMENT
        if (T1 < 0)
            return null;
        if (T2 < 0 || T2 > 1)
            return null;

        return new IntersectionResult(r_px + r_dx * T1,
                                      r_py + r_dy * T1, 
                                      T1);

    }
    
    private static List<Point2D> getPoints(List<Line2D> l) {
        List<Point2D> r = new ArrayList<>();
        
        for (Line2D e : l) {
            
            Point2D p1 = new Point2D(e.getX1(), e.getY1());
            Point2D p2 = new Point2D(e.getX2(), e.getY2());
             
            r.add(p1);
            r.add(p2);
        }
        
        return r;
    }
    
    public static List<Double> getAngles(Point2D s, double ang2, List<Line2D> l) {
        List<Double> r = new ArrayList<>();
        
        List<Point2D> p = getPoints(l);
        
        double max = ang2 + Math.toRadians(25);
        double min = ang2 - Math.toRadians(25);

        
        r.add(min);
        r.add(max);

        for(Point2D e : p) {
            //http://stackoverflow.com/questions/12234574/calculating-if-an-angle-is-between-two-angles
            // TODO
            double ang = Math.atan2(e.getY() - s.getY(), e.getX() - s.getX());
            
        
            double anglediff = (ang2 - ang + 180 + 360) % 360 - 180;
        
            if (-25 <= anglediff && anglediff <= 25) {
                r.add(ang - 0.0001);
                r.add(ang);
                r.add(ang + 0.0001);
            }
        }
        
        return r;
    }
    
    public static List<Point2D> getRaycastPoints(Point2D s, double ang, List<Line2D> l) {
        
        List<IntersectionResult> intersects = new ArrayList();
        
        for (Double angle : getAngles(s, ang, l)) {

            // Calculate dx & dy from angle
            double dx = Math.cos(angle);
            double dy = Math.sin(angle);
            
            LineF2D ray = new LineF2D(s.getX(),
                                    s.getY(),
                                    (s.getX() + dx),
                                    (s.getY() + dy));



            // Find CLOSEST intersection
            IntersectionResult closestIntersect = null;
            for (Line2D e : l) {
                IntersectionResult intersect = getIntersection2(ray, e);
                if (intersect == null) {
                    continue;
                }
                
                if (closestIntersect == null || intersect.param < closestIntersect.param) {
                    closestIntersect = intersect;
                }
            }

            // Intersect angle
            if (closestIntersect == null)
                continue;
            
            closestIntersect.angle = angle;

            // Add to list of intersects
            intersects.add(closestIntersect);

        }
        // Sort intersects by angle
        intersects.sort(new Comparator<IntersectionResult>() {
            @Override
            public int compare(IntersectionResult a, IntersectionResult b) {
                if (a.angle > b.angle) {
                    return 1;
                } else if (a.angle < b.angle) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        
        List<Point2D> ll = new ArrayList<>();
        
        for(IntersectionResult r : intersects) {
            ll.add(new Point2D((int) r.x, (int) r.y));
        }
        
        return ll;
    }
}
