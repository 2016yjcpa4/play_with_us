package org.game.geom;

import java.util.ArrayList;
import java.util.List;
import org.game.math.Point2D;
import org.game.math.Vector2D;

public class Circle implements Shape {
    
    private Point2D pos = new Point2D();
    private int rad;
     
    public Circle(int x, int y, int rad) {
        this.pos.set(x, y);
        this.rad = rad;
    }
    
    public int getRadius() {
        return rad;
    }
    
    public Point2D getPosition() {
        return pos;
    }
    
    public static boolean isCollideCircleCircle(Circle a, Circle b) {
        Vector2D differenceV = new Vector2D(b.pos).sub(a.pos);
        double totalRadius = a.rad + b.rad;
        double totalRadiusSq = totalRadius * totalRadius;
        double distanceSq = differenceV.getLengthSquared();

        if (distanceSq > totalRadiusSq) {
            // They do not intersect 
            return false;
        }
        
        return true;
    }
//    var testCircleCircle = function (a, b, response) {
//        var differenceV = new Vector().copy(b.pos).sub(a.pos);
//        var totalRadius = a.r + b.r;
//        var totalRadiusSq = totalRadius * totalRadius;
//        var distanceSq = differenceV.len2();
//
//        if (distanceSq > totalRadiusSq) {
//            // They do not intersect 
//            return false;
//        }
//
//        // They intersect. If we're calculating a response, calculate the overlap.
//        if (response) {
//            var dist = sqrt(distanceSq);
//            response.a = a;
//            response.b = b;
//            response.overlap = totalRadius - dist;
//            response.overlapN.copy(differenceV.normalize());
//            response.overlapV.copy(differenceV).scale(response.overlap);
//            response.aInB = a.r <= b.r && dist <= b.r - a.r;
//            response.bInA = b.r <= a.r && dist <= a.r - b.r;
//        }
//
//        return true;
//    };
}
