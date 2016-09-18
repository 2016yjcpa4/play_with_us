package org.game.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import org.game.CanvasView;
import org.game.DrawableObject;
import org.game.Game;
import org.game.geom.Polygon;
import org.game.geom.Rect;
import org.game.math.Point2D;

public class Wall11 extends Polygon implements DrawableObject {
    
    public Wall11() {
        super(new ArrayList<Point2D>() {{
add(new Point2D(130, 300));
add(new Point2D(10, 140));
add(new Point2D(9, 138));
add(new Point2D(9, 136));
add(new Point2D(10, 134));
add(new Point2D(10, 131));
add(new Point2D(10, 129));
add(new Point2D(10, 127));
add(new Point2D(9, 125));
add(new Point2D(10, 122));
add(new Point2D(10, 120));
add(new Point2D(10, 117));
add(new Point2D(10, 115));
add(new Point2D(10, 112));
add(new Point2D(10, 110));
add(new Point2D(9, 107));
add(new Point2D(10, 104));
add(new Point2D(10, 101));
add(new Point2D(10, 99));
add(new Point2D(10, 96));
add(new Point2D(10, 93));
add(new Point2D(10, 90));
add(new Point2D(10, 87));
add(new Point2D(10, 83));
add(new Point2D(10, 80));
add(new Point2D(10, 77));
add(new Point2D(10, 74));
add(new Point2D(9, 70));
add(new Point2D(10, 66));
add(new Point2D(10, 63));
add(new Point2D(10, 59));
add(new Point2D(10, 55));
add(new Point2D(10, 51));
add(new Point2D(10, 47));
add(new Point2D(10, 43));
add(new Point2D(10, 39));
add(new Point2D(10, 35));
add(new Point2D(10, 30));
add(new Point2D(10, 25));
add(new Point2D(10, 21));
add(new Point2D(9, 16));
add(new Point2D(10, 11));
add(new Point2D(11, 9));
add(new Point2D(13, 10));
add(new Point2D(15, 9));
add(new Point2D(17, 9));
add(new Point2D(20, 9));
add(new Point2D(22, 9));
add(new Point2D(24, 10));
add(new Point2D(26, 10));
add(new Point2D(28, 9));
add(new Point2D(30, 9));
add(new Point2D(32, 10));
add(new Point2D(34, 9));
add(new Point2D(36, 9));
add(new Point2D(38, 9));
add(new Point2D(40, 9));
add(new Point2D(42, 10));
add(new Point2D(44, 10));
add(new Point2D(46, 9));
add(new Point2D(48, 10));
add(new Point2D(50, 10));
add(new Point2D(52, 9));
add(new Point2D(54, 10));
add(new Point2D(56, 9));
add(new Point2D(58, 10));
add(new Point2D(60, 9));
add(new Point2D(61, 10));
add(new Point2D(63, 10));
add(new Point2D(65, 10));
add(new Point2D(67, 10));
add(new Point2D(69, 9));
add(new Point2D(71, 10));
add(new Point2D(101, 151));
add(new Point2D(102, 153));
add(new Point2D(103, 155));
add(new Point2D(104, 157));
add(new Point2D(106, 159));
add(new Point2D(107, 161));
add(new Point2D(108, 162));
add(new Point2D(109, 164));
add(new Point2D(110, 166));
add(new Point2D(111, 167));
add(new Point2D(112, 169));
add(new Point2D(113, 170));
add(new Point2D(114, 172));
add(new Point2D(115, 173));
add(new Point2D(116, 175));
add(new Point2D(117, 176));
add(new Point2D(118, 178));
add(new Point2D(119, 179));
add(new Point2D(120, 180));
add(new Point2D(121, 181));
add(new Point2D(122, 183));
add(new Point2D(122, 184));
add(new Point2D(123, 185));
add(new Point2D(124, 186));
add(new Point2D(125, 187));
add(new Point2D(126, 189));
add(new Point2D(126, 190));
add(new Point2D(127, 191));
add(new Point2D(128, 192));
add(new Point2D(128, 193));
add(new Point2D(129, 194));
add(new Point2D(130, 195));
add(new Point2D(130, 196));
add(new Point2D(131, 197));
add(new Point2D(132, 198));
add(new Point2D(132, 199));
add(new Point2D(133, 200));
add(new Point2D(133, 200));
add(new Point2D(134, 201));
add(new Point2D(135, 202));
add(new Point2D(135, 203));
add(new Point2D(136, 204));
add(new Point2D(136, 205));
add(new Point2D(137, 205));
add(new Point2D(137, 206));
add(new Point2D(138, 207));
add(new Point2D(138, 208));
add(new Point2D(139, 209));
add(new Point2D(139, 209));
add(new Point2D(140, 208));
add(new Point2D(141, 207));
add(new Point2D(142, 205));
add(new Point2D(142, 203));
add(new Point2D(143, 201));
add(new Point2D(144, 199));
add(new Point2D(145, 197));
add(new Point2D(146, 195));
add(new Point2D(147, 192));
add(new Point2D(148, 190));
add(new Point2D(150, 188));
add(new Point2D(151, 185));
add(new Point2D(152, 182));
add(new Point2D(153, 179));
add(new Point2D(155, 176));
add(new Point2D(156, 173));
add(new Point2D(158, 169));
add(new Point2D(160, 166));
        }});  
    }

    @Override
    public void draw(CanvasView g, Graphics2D g2d) {
        int[] x = getXPoints();
        int[] y = getYPoints();
        
        Point2D p = getPosition(); 
         
        
        g2d.setColor(Color.BLACK);
        g2d.fillPolygon(x, y, x.length);
        
        g2d.setColor(Color.GRAY);
        g2d.drawPolygon(x, y, x.length);
        
        if (Game.DEBUG) {
            g2d.setColor(Color.RED);
            g2d.fillOval(p.getX() - 1, p.getY() - 1, 4, 4);
        }
    }
    
}