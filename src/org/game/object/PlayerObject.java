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
import org.game.math.Point2D;
import org.game.math.Vector2D;
import org.game.util.IntersectionUtil; 

public class PlayerObject implements DrawableObject {
    
    private static final boolean DEBUG = true;
    
    private Map map;

    private boolean isTurnOnFlash = false;
    
    private Point2D pos = new Point2D(400, 300);
    private Point2D dir = new Point2D(0, 0);    
    private Point2D vel = new Point2D(0, 0);    
    
    public PlayerObject(Map m) {
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
        l.add(pos);
        
        double angle = getAngle();
        
        for (double n = (angle - rangeAngle); n <= (angle + rangeAngle); n += (Math.PI * 2 / 1000)) {
            
            l.add(IntersectionUtil.getIntersection(pos, n, map.getWall()));
        }
        
        return l;
    }
    
    /**
     * 캐릭터가 가리키는 방향.
     * 
     * @return Vector2D 클래스로 반환됩니다. 
     */
    public Point2D getDirection() {
        return dir;
    }
    
    /**
     * 캐릭터의 가속도.
     * 
     * @return 
     */
    public Point2D getVelocity() {
        return vel;
    }
    
    public Point2D getPosition() {
        return pos;
    }
     
    
    // Math.atan2(dir - pos) = 각도
    public double getAngle() {
        return new Vector2D(dir).sub(pos).angle();
    }
    
    @Override
    public void draw(CanvasView c, Graphics2D g2d) {
        int x = pos.getX() + vel.getX();
        int y = pos.getY() + vel.getY();
        
        pos.setX(x);
        pos.setY(y);
        
        g2d.setColor(new Color(255, 255, 0, (int) (255 * 0.20)));
        
        if (isTurnOnFlash) {
            List<Point2D> l = projectLight();
            
            g2d.fillPolygon(Point2D.getXPoints(l), Point2D.getYPoints(l), l.size());
        }
        
        g2d.setColor(Color.MAGENTA);
        g2d.drawOval(x - 5, y - 5, 10, 10);
        
        if (Game.DEBUG && PlayerObject.DEBUG) {
            int dx = (int) (x + 20 * Math.cos(getAngle()));
            int dy = (int) (y + 20 * Math.sin(getAngle()));

            g2d.drawLine(x, y, dx, dy);
        }
    }

}
