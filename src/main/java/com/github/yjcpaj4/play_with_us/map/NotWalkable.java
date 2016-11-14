package com.github.yjcpaj4.play_with_us.map;

import java.awt.Color;
import java.awt.Graphics2D;
import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.GraphicLooper;
import com.github.yjcpaj4.play_with_us.geom.EarCutTriangulator;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.geom.Rect; 
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import java.util.ArrayList;
import java.util.List;

public class NotWalkable extends GameObject {
    
    private Polygon mCollider;
    
    public NotWalkable(List<Point2D> l) {
        mCollider = new Polygon(l);
    }
    
    public Polygon getCollider() {
        return mCollider;
    }
    
    public Point2D getPosition () {
        return mCollider.getPosition();
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        //g2d.setColor(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
        g2d.setColor(Color.RED);
        g2d.fillPolygon(mCollider.toAWTPolygon());
        
        g2d.setColor(Color.YELLOW);
        for (int n = 500; n < mCollider.getPoints().size(); ++n) {
            Vector2D v = mCollider.getEdge(n);
            g2d.drawOval((int) (v.getX() - 5), 
                         (int) (v.getY() - 5), 
                         10, 10);
        }
    }

    @Override
    public void update(GameLayer g, long delta) { 
    }
    
    public static List<NotWalkable> newInstances(List<Point2D> l) {
        List<NotWalkable> r = new ArrayList<>();
        r.add(new NotWalkable(l));
        return r;
    }
}