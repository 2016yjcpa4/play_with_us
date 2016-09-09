package org.game.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List; 

import org.game.map.Map;
import org.game.CanvasView;
import org.game.DrawableObject;
import org.game.Game;
import org.game.geom.Circle;
import org.game.geom.Polygon;
import org.game.math.Matrix2D;
import org.game.math.Point2D;
import org.game.math.Vector2D;
import org.game.util.IntersectionUtil; 

public class Player implements DrawableObject {
    
    private static final boolean DEBUG = true;
    
    private Map map;

    private boolean isTurnOnFlash = false;
     
    private Vector2D dir = new Vector2D(0, 0);    
    private Vector2D vel = new Vector2D();    
    
    public Player(Map m) {
        this.map = m;
    }
    
    public void setMap(Map m) {
        this.map = m;
    }
    
    public boolean isTurnOnFlash() {
        return isTurnOnFlash;
    }
    
    public void setTurnOffFlash() {
        this.isTurnOnFlash = false;
    }
    
    public void setTurnOnFlash() {
        this.isTurnOnFlash = true;
    }
    
    public void toggleFlash() {
        this.isTurnOnFlash = ! this.isTurnOnFlash;
    }
    
    public List<Point2D> projectLight() {
        return projectLight(Math.toRadians(25));
    }
    
    private List<Point2D> projectLight(double rangeAngle) { 
        if ( ! isTurnOnFlash) {
            return Collections.emptyList();
        }
        
        List<Point2D> l = new ArrayList<>();
        l.add(getPosition());
        
        for (double ang = (getAngle() - rangeAngle); ang <= (getAngle() + rangeAngle); ang += (Math.PI * 2 / 1000)) { 
            l.add(IntersectionUtil.getIntersection(getPosition(), ang, map.getWall2()));
        }
        
        return l;
    }
    
    /**
     * 캐릭터가 가리키는 방향.
     * 
     * @return Vector2D 클래스로 반환됩니다. 
     */
    public Vector2D getDirection() {
        return dir;
    }
    
    /**
     * 캐릭터의 가속도.
     * 
     * @return 
     */
    public Vector2D getVelocity() {
        return vel;
    }
    
    public Point2D getPosition() {
        return a.getPosition();
    }
     
    
    // Math.atan2(dir - pos) = 각도
    public double getAngle() {
        return dir.sub(a.getPosition()).angle();
    }
    
    private Circle a = new Circle(250, 300, 20);
    private Circle b = new Circle(150, 150, 30); 
    
    @Override
    public void draw(CanvasView c, Graphics2D g2d) { 
        Game g= (Game) c;
        if (g.w) {
            vel.setY(-4);
        }
        
        if (g.s) {
            vel.setY(4);
        }
        
        if (g.a) {
            vel.setX(-4);
        }
        
        if (g.d) {
            vel.setX(4);
        }
        
        
        // 도형 충돌 처리
        for (Wall w : map.getWall()) {
            Polygon.PolygonCollisionResult r = w.PolygonCollision(a, w, vel);

            if (r.willHit) {
                vel = vel.sum(r.minTranslVec);
            }
        }
        
        if ( ! (g.w || g.s)) {
            vel.setY(0);
        }
        
        if ( ! (g.a || g.d)) {
            vel.setX(0);
        }
        
        a.transform(Matrix2D.translate(vel.getX(), vel.getY()));
        
        Point2D p = a.getPosition();
        int x = p.getX();
        int y = p.getY();
        
        g2d.setColor(new Color(255, 255, 0, (int) (255 * 0.20)));
        
        if (isTurnOnFlash) {
            List<Point2D> l = projectLight();
            
            g2d.fillPolygon(Point2D.getXPoints(l), Point2D.getYPoints(l), l.size());
        } 
        
        g2d.setColor(Color.CYAN);
        int dx = (int) (x + 20 * Math.cos(getAngle()));
        int dy = (int) (y + 20 * Math.sin(getAngle()));

        g2d.drawLine(x, y, dx, dy);
        
        
        
        g2d.drawPolygon(a.getXPoints(), a.getYPoints(), a.getXPoints().length);
    }

}
