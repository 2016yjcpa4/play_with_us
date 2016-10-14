package kr.ac.yeungin.cpa.java4.play_with_us.object;

import java.awt.Color;
import java.awt.Graphics2D;
import kr.ac.yeungin.cpa.java4.GameLoop;
import kr.ac.yeungin.cpa.java4.play_with_us.DrawableObject;
import kr.ac.yeungin.cpa.java4.play_with_us.geom.Rect;
import kr.ac.yeungin.cpa.java4.play_with_us.math.Point2D;

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