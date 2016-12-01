package com.github.yjcpaj4.play_with_us.game.object;

import com.github.yjcpaj4.play_with_us.Application;
import java.awt.Color;
import java.awt.Graphics2D;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Line2D;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Lightless extends GameObject {
    
    private Polygon mCollider;
    
    public Lightless(List<Point2D> l) {
        mCollider = new Polygon(l);
    }
    
    public List<Line2D> getAllSide() {
        List<Line2D> l = new ArrayList<>();
        
        for(int n = 0; n < mCollider.getPoints().size(); ++n) {
            
            Point2D p1 = mCollider.getPoint(n);
            Point2D p2 = mCollider.getPoint(n + 1);

            l.add(new Line2D(p1.getX(), p1.getY(), p2.getX(), p2.getY()));
        }
        
        return l;
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        if (Application.DEBUG) {
            g2d.setColor(new Color(255, 255, 0, (int) (255 * 0.4)));
            g2d.fillPolygon(mCollider.toAWTPolygon());
            
            g2d.setColor(new Color(255, 255, 0));
            g2d.drawPolygon(mCollider.toAWTPolygon());
        }
    }

    @Override
    public void update(GameLayer g, long delta) { 
    }

    public java.awt.Polygon toAWTPolygon() {
        return mCollider.toAWTPolygon();
    }
}