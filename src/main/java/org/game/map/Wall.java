package org.game.map;

import java.awt.Color;
import java.awt.Graphics2D;
import org.game.GameLoop;
import org.game.DrawableObject;
import org.game.geom.Rect; 
import org.game.math.Point2D;

public class Wall extends Rect implements DrawableObject {
    
    public Wall(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    public void draw(GameLoop g, Graphics2D g2d) {
        int[] x = getXPoints();
        int[] y = getYPoints();
        
        Point2D p = getPosition(); 
         
        
        g2d.setColor(Color.BLACK);
        g2d.fillPolygon(x, y, x.length);
        
        g2d.setColor(Color.GRAY);
        g2d.drawPolygon(x, y, x.length);
        
        g2d.setColor(Color.RED);
        g2d.fillOval(p.getX() - 2, p.getY() - 2, 4, 4);
        
    }

    @Override
    public void update(GameLoop g) { 
    }
    
}