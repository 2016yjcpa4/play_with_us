package kr.ac.yeungin.cpa.java4.play_with_us.object;
 
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List; 
import javafx.scene.paint.RadialGradient;
import javax.imageio.ImageIO;
 
import kr.ac.yeungin.cpa.java4.play_with_us.map.Map;
import kr.ac.yeungin.cpa.java4.GameLoop;
import kr.ac.yeungin.cpa.java4.play_with_us.DrawableObject;
import kr.ac.yeungin.cpa.java4.play_with_us.Game;
import kr.ac.yeungin.cpa.java4.play_with_us.Main;
import kr.ac.yeungin.cpa.java4.play_with_us.SpriteManager;
import kr.ac.yeungin.cpa.java4.play_with_us.geom.Circle;
import kr.ac.yeungin.cpa.java4.play_with_us.geom.EarCutTriangulator;
import kr.ac.yeungin.cpa.java4.play_with_us.geom.Polygon;
import static kr.ac.yeungin.cpa.java4.play_with_us.map.Map.MAP_HEIGHT;
import static kr.ac.yeungin.cpa.java4.play_with_us.map.Map.MAP_WIDTH;
import kr.ac.yeungin.cpa.java4.play_with_us.math.Matrix2D;
import kr.ac.yeungin.cpa.java4.play_with_us.math.Point2D;
import kr.ac.yeungin.cpa.java4.play_with_us.math.Vector2D;
import kr.ac.yeungin.cpa.java4.play_with_us.util.BresenhamLineUtil;
import kr.ac.yeungin.cpa.java4.play_with_us.util.RaycastUtil;
 
public class Player extends Circle implements DrawableObject {
    
    private static final boolean DEBUG = true;
    
    private Map map;
 
    private boolean isTurnOnFlash = true;
     
    private Vector2D dir = new Vector2D(0, 0);    
    private Vector2D vel = new Vector2D();    
    
    
    private SpriteManager sp = new SpriteManager();
    
    public Player(Map m) {
        super(320, 150, 13);
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
    
    private java.awt.Polygon pr() {
        Polygon p = projectLight();
        
        return new java.awt.Polygon(p.getXPoints(), p.getYPoints(), p.getPoints().size());
    }
    
    @Override
    public void draw(GameLoop c, Graphics2D g2d) { 
        
        Point2D p = getPosition();
        float x = p.getX() + vel.getX();
        float y = p.getY() + vel.getY();
         
        int rad = 16;//getRadius();
        
        
        //원본 맵을 그리고
        map.draw(c, g2d);
        
        // 검은 마스크를 씌움...
        g2d.setColor(new Color(0, 0, 0, (int)(255 * 0.85)));
        g2d.fillRect(0, 0, MAP_WIDTH, MAP_HEIGHT);
        
        if (isTurnOnFlash) {
            RadialGradientPaint paint = new RadialGradientPaint(x, y, 300f,
                                                                new float[] { 0.7f, 1f },
                                                                new Color[] {
                                                                    new Color(0, 0, 0, (int) (255 * 0)),
                                                                    new Color(0, 0, 0, (int) (255 * 0.85))
                                                                });
        
            java.awt.Polygon arc = pr(); 
            
            g2d.clip(arc);
            map.draw(c, g2d); // 밝은부분만 그려짐
            g2d.setPaint(paint);
            g2d.fill(arc); 
            g2d.setClip(null);
            
            //g2d.drawImage(Main.draw(g2d, x, y, dx, dy, 0.3, Color.black, Color.black), 0, 0, null);
        } 
        
        g2d.setColor(Color.CYAN);
 
        g2d.drawImage(sp.getSprite((int) (i % 3), getGridIndex()), (int) x - rad, (int) y - rad, null);
        
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

    @Override
    public void update(GameLoop c) {
        Game g= (Game) c;
        if (g.w) vel.setY(-4);
        if (g.s) vel.setY(4);
        if (g.a) vel.setX(-4);
        if (g.d) vel.setX(4);
        
        if ( ! (g.w || g.s)) vel.setY(0);
        if ( ! (g.a || g.d)) vel.setX(0);
        
        Point2D p = getPosition();
        float x = p.getX() + vel.getX();
        float y = p.getY() + vel.getY();
         
        if (vel.getX() != 0 || vel.getY() != 0) {
            i += 0.333;
        }
        
        p.set((int) x, (int) y);
    }
}