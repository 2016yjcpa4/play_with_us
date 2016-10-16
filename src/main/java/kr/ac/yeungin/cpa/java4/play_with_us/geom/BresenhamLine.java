package kr.ac.yeungin.cpa.java4.play_with_us.geom;

import java.util.ArrayList;
import java.util.List;
import kr.ac.yeungin.cpa.java4.play_with_us.math.Point2D;

public class BresenhamLine {
    
    private BresenhamLine() {
    }

    public static List<Point2D> getBresenhamLine(int x1, int y1, int x2, int y2) {
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
}
