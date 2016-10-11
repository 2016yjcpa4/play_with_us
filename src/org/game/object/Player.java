package org.game.object;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List; 
import javafx.scene.paint.RadialGradient;
import javax.imageio.ImageIO;

import org.game.map.Map;
import org.game.CanvasView;
import org.game.DrawableObject;
import org.game.Game;
import org.game.Sprite;
import org.game.geom.Circle; 
import org.game.geom.EarCutTriangulator;
import org.game.geom.Polygon;
import org.game.math.Matrix2D;
import org.game.math.Point2D;
import org.game.math.Vector2D;
import org.game.util.BresenhamLineUtil; 
import org.game.util.RaycastUtil;

public class Player extends Circle implements DrawableObject {
    
    private static final boolean DEBUG = true;
    
    private Map map;

    private boolean isTurnOnFlash = false;
     
    private Vector2D dir = new Vector2D(0, 0);    
    private Vector2D vel = new Vector2D();    
    
    
    private Sprite sp = new Sprite();
    
    public Player(Map m) {
        super(250, 300, 13);
        this.map = m;
        
        sp.loadSprite("player");
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
    
    public Polygon projectLight() {
        return projectLight(Math.toRadians(25));
    }
    
    private Polygon projectLight(double rangeAngle) { 
        if ( ! isTurnOnFlash) {
            return null;
        }
        
        List<Point2D> l = new ArrayList<>();
        l.add(getPosition());
        
        for (Point2D e : RaycastUtil.getRaycast(getPosition(), getAngle(), map.getWall2())) {  
            l.add(e);    
        }
        
        return new Polygon(l); 
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
    
    // Math.atan2(dir - pos) = 각도
    public double getAngle() {
        return dir.sub(getPosition()).getAngle();
    }
    
    @Override
    public void draw(CanvasView c, Graphics2D g2d) { 
        Game g= (Game) c;
        if (g.w) vel.setY(-4);
        if (g.s) vel.setY(4);
        if (g.a) vel.setX(-4);
        if (g.d) vel.setX(4);
        
        if ( ! (g.w || g.s)) vel.setY(0);
        if ( ! (g.a || g.d)) vel.setX(0);
        
        if (vel.getX() != 0 || vel.getY() != 0) {
            i += 0.333;
        }
        
        Point2D p = getPosition();
        float x = p.getX() + vel.getX();
        float y = p.getY() + vel.getY();
        
        float dx = (int) (x + 200 * Math.cos(getAngle()));
        float dy = (int) (y + 200 * Math.sin(getAngle()));
        int rad = 16;//getRadius();
        
        
        if (isTurnOnFlash) {
            Polygon e = projectLight();
            
            RadialGradientPaint paint = new RadialGradientPaint(x, y, 300f,
                                                                new float[] { 0.8f, 1f },
                                                                new Color[] {
                                                                    new Color(255, 255, 0, (int) (255 * 0.3)),
                                                                    new Color(255, 255, 0, (int) (255 * 0))
                                                                });
        
            g2d.setPaint(paint);
            
            g2d.fillPolygon(e.getXPoints(), e.getYPoints(), e.getPoints().size()); 
            
        } 
        
        g2d.setColor(Color.CYAN);

        g2d.drawImage(sp.getSprite((int) (i % 3), getGridIndex()), (int) x - rad, (int) y - rad, null);
        
        p.set((int) x, (int) y);
        
        //g2d.drawLine((int) x , (int) y, dx, dy);
        //g2d.drawOval((int) x - rad, (int) y - rad, rad * 2, rad * 2);
    }
    
    private double i = 0;

    public static double normalAbsoluteAngleDegrees(double angle) {
        return (angle %= 360) >= 0 ? angle : (angle + 360);
    }
    
    private int getGridIndex() {
        double ang = normalAbsoluteAngleDegrees(Math.toDegrees(getAngle()));
        
        if (45 <= ang && ang < 135) {
            return 0;
        }
        
        if (135 <= ang && ang < 225) {
            return 1;
        }
        
        if (225 <= ang && ang < 315) {
            return 3;
        }
        
        return 2;
    }
}
