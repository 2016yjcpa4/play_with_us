package org.game.geom;

import java.util.ArrayList;
import java.util.List; 
import org.game.math.Point2D;

/**
 * 브레젠헴의 직선 알고리즘.
 * 
 * 타일에서 직선을 그 었을경우, 교차되는 타일 인덱스를 가져옵니다.
 * 
 * @author 차명도.
 */
public class BresenhamLine {
    
    private BresenhamLine() {
    }

    public static List<Point2D> getBresenhamLine(int x1, int y1, int x2, int y2) {
        List<Point2D> l = new ArrayList<>();

        int x = x1 < x2 ? 1 : -1;
        int y = y1 < y2 ? 1 : -1;
 
        int dx = Math.abs(x2 - x1);
        int dy = -Math.abs(y2 - y1);
        
        double n1 = dx + dy;

        while (true) {
            
            l.add(new Point2D(x1, y1));
            
            if (x1 == x2 && y1 == y2) {
                break;
            }
            
            double n2 = 2 * n1;
            
            if (n2 > dy) {
                n1 += dy;
                x1 += x;
            } else if (n2 < dx) {
                n1 += dx;
                y1 += y;
            }
        }

        return l;
    } 
}
