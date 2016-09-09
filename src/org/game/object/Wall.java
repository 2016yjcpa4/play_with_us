package org.game.object;

import java.awt.Color;
import java.awt.Graphics2D;
import org.game.CanvasView;
import org.game.DrawableObject;
import org.game.geom.Rect;

public class Wall extends Rect implements DrawableObject {
    
    public Wall(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    public void draw(CanvasView g, Graphics2D g2d) {
        int[] x = getXPoints();
        int[] y = getYPoints();
        
        g2d.setColor(Color.BLACK);
        g2d.fillPolygon(x, y, x.length);
        
        g2d.setColor(Color.GRAY);
        g2d.drawPolygon(x, y, x.length);
    }
    
}