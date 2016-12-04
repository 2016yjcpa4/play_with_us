package com.github.yjcpaj4.play_with_us.game.object;

import com.github.yjcpaj4.play_with_us.game.GameObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;
import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.geom.Raycast;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import java.awt.Shape;

public class Light extends GameObject {

    private static final float DEFAULT_LENGTH = 300f;
    private static final double DEFAULT_EXTENT = 50;
    
    private Point2D mPos = new Point2D();
    private double mAngle; // 각도
    private float mLength = DEFAULT_LENGTH; // 빛의 길이
    private double mExtent = DEFAULT_EXTENT; // 빛의 범위
    
    private boolean mTurnOff = true;
    
    public Light() {
    }
    
    public Light(Point2D p, double a, float l, double e) {
        mPos = p;
        mAngle = a;
        mLength = l;
        mExtent = e;
    }
    
    public void setTurnOff() {
        mTurnOff = true;
    }
    
    public void setTurnOn() {
        mTurnOff = false;
    }
    
    public boolean isTurnOn() {
        return !mTurnOff;
    }
    
    public boolean isTurnOff() {
        return mTurnOff;
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
    
    public Point2D getPosition() {
        return mPos;
    }
    
    private Polygon getRaycast() {    
        List<Point2D> l = new ArrayList<>();
        l.add(getPosition());
        
        for (Point2D p : Raycast.getRaycast(getPosition(), getAngle(), getExtent(), getMap().getAllSideByDarknessObjects())) {  
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
        
        a.intersect(new Area(getRaycast().toAWTPolygon()));
        
        return a;
    }
    
    public boolean isCollide(Polygon p) {
        boolean b = false;
        for (Polygon o : getRaycast().getTriangulate()) {
            if (CollisionDetection.isCollide(o, p)) {
                b = true;
                break;
            }
        }
        
        if ( ! b) {
            return false;
        }
        
        Point2D p1 = p.getPosition();
        Point2D p2 = getPosition();
        
        Vector2D v = new Vector2D(p1).subtract(p2);
        if (v.length() > (getLength() * 0.70)) {
            return false;
        }
        
        return true;
    }

    @Override
    public void update(GameLayer g, long delta) {
        // 빛에서 업데이트 되는건 없을거같은데....???
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        if (mTurnOff) {
            return;
        }
        
        Shape s = g2d.getClip();
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
        
        g2d.setClip(s);
        if (Application.DEBUG) {
            g2d.setColor(Color.YELLOW);
            g2d.draw(a);
            
            g2d.setColor(Color.RED);
            g2d.drawPolygon(getRaycast().toAWTPolygon());
        }
    }
}
