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

public class Wall1 extends Polygon implements DrawableObject {
    
    public Wall1() {
        super(new ArrayList<Point2D>() {{
            add(new Point2D(100, 150));
            add(new Point2D(120, 50));
            add(new Point2D(200, 80));
            add(new Point2D(140, 210));
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