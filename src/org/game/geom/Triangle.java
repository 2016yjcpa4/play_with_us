/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.geom;

import org.game.math.Point2D;

/**
 *
 * @author Administrator
 */
public class Triangle extends Polygon {
    
    public Triangle(Point2D p1, Point2D p2, Point2D p3) {
        addPoint(p1);
        addPoint(p2);
        addPoint(p3);
    }
    
    public double getArea() {
        Point2D p1 = getPoint(0);
        Point2D p2 = getPoint(1);
        Point2D p3 = getPoint(2);
        
        
        double a = p2.getX() - p1.getX();
        double b = p3.getY() - p1.getY();
        double c = p3.getX() - p1.getX();
        double d = p2.getY() - p1.getY();
        
        return a * b - c * d;
    }
     
    public static boolean isLeft(Point2D p1, Point2D p2, Point2D p3){
        return new Triangle(p1, p2, p3).getArea() > 0;
    }

    public static boolean isLeftOn(Point2D p1, Point2D p2, Point2D p3) {
        return new Triangle(p1, p2, p3).getArea() >= 0;
    }

    public static boolean isRight(Point2D p1, Point2D p2, Point2D p3) {
        return new Triangle(p1, p2, p3).getArea() < 0;
    }

    public static boolean isRightOn(Point2D p1, Point2D p2, Point2D p3) {
        return new Triangle(p1, p2, p3).getArea() <= 0;
    }
}
