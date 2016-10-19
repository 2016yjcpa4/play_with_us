package org.game.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;
import org.game.GameLoop;
import org.game.GraphicObject;
import org.game.geom.Polygon;
import org.game.geom.Raycast;
import org.game.math.Point2D;

public class Light implements GraphicObject {

    private Map mMap;
    
    private Point2D mPosition = new Point2D(); // 위치
    private double mAngle; // 각도
    private float mLength = 300f; // 빛의 길이
    private double mExtent = 50; // 빛의 범위
    
    public Point2D getPosition() {
        return mPosition;
    }
    
    public double getAngle() {
        return mAngle;
    }
    
    public float getLength() {
        return mLength;
    }
    
    public double getExtent() {
        return mExtent;
    }
    
    public Map getMap() {
        return mMap;
    }
    
    private Polygon getRaycast() {    
        List<Point2D> l = new ArrayList<>();
        l.add(getPosition());
        
        for (Point2D p : Raycast.getRaycast(getPosition(), getAngle(), getMap().getAllLineForProject())) {  
            l.add(p);    
        }
        
        return new Polygon(l); 
    }
    
    private Area getArea() {
        Point2D p = getPosition();
        double d = getLength();
        
        Area a = new Area(new Arc2D.Double(p.getX() - d, 
                                           p.getY() - d, 
                                           d * 2, 
                                           d * 2, 
                                           -Math.toDegrees(getAngle()) - getExtent() / 2, 
                                           getExtent(), 
                                           Arc2D.PIE));
        
        a.intersect(new Area(getRaycast().toPolygon()));
        
        return a;
    }

    @Override
    public void update(GameLoop g) {
        // 빛에서 업데이트 되는건 없을거같은데....???
    }

    @Override
    public void draw(GameLoop g, Graphics2D g2d) {
        Area a = getArea();
        Point2D p = getPosition();
        
        g2d.setClip(a); 
        g2d.setPaint(new RadialGradientPaint(p.getX(), 
                                             p.getY(),
                                             getLength(),
                                             new float[] { 0.5f, 1f },
                                             new Color[] {
                                                 new Color(0, 0, 0, (int) (255 * getMap().getDarkness())),
                                                 new Color(0, 0, 0, (int) (255 * 0))
                                             })); // 그라데이션 삽입 
        g2d.fill(a);
    }
}
