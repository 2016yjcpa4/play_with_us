package org.game.map;
 
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List; 
import javafx.scene.paint.RadialGradient;
import javax.imageio.ImageIO;
 
import org.game.map.Map;
import org.game.GameLoop;
import org.game.Game;
import org.game.Main;
import org.game.SpriteManager;
import org.game.geom.Circle;
import org.game.geom.EarCutTriangulator;
import org.game.geom.Polygon;
import static org.game.map.Map.MAP_HEIGHT;
import static org.game.map.Map.MAP_WIDTH; 
import org.game.geom.BresenhamLine;
import org.game.geom.Raycast;
import org.game.geom.SAT;
import org.game.math.Point2D;
import org.game.math.Vector2D;
import org.game.util.ColorUtil;
import org.game.GraphicObject;
 
public class Player extends Circle implements GraphicObject {
    
    private static final boolean DEBUG = true;
    
    private Map map;
 
    private boolean isTurnOnFlash = true;
     
    private Vector2D dir = new Vector2D(0, 0);    
    private Vector2D vel = new Vector2D();    
    
    
    private SpriteManager sp = new SpriteManager();
    
    public Player(Map m) {
        super(620, 700, 42);
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
    
    private Polygon projectLight(Point2D pos, double ang) { 
        if ( ! isTurnOnFlash) {
            return null;
        }
        
        List<Point2D> l = new ArrayList<>();
        l.add(pos);
        
        for (Point2D e : Raycast.getRaycast(pos, ang, map.getWall2())) {  
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
    
    private Area getArea(Point2D pos, double ang) {
        int w = 600;
        Area s = new Area(new Arc2D.Double(pos.getX() - w / 2, pos.getY() - w / 2, w, w, -Math.toDegrees(ang) - 25, 50, Arc2D.PIE));
        s.intersect(new Area(projectLight(pos, ang).toPolygon()));
        return s;
    }
    
    private static Paint getRadialGradientPaint(int x, int y) {
        return new RadialGradientPaint(x, 
                                       y,
                                       300f,
                                       new float[] { 0.5f, 1f },
                                       new Color[] {
                                           new Color(0, 0, 0, (int) (255 * 0.75f)),
                                           new Color(0, 0, 0, (int) (255 * 0))
                                       });
    }
    
    private BufferedImage createLayer(Point2D pos, double ang) {
        BufferedImage b = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = b.createGraphics();
        
        Area s = getArea(pos, ang);
        g2d.setClip(s); 
        g2d.setPaint(getRadialGradientPaint(pos.getX(), pos.getY())); // 그라데이션 삽입 
        g2d.fill(s);
        
        return b;
    }
    
    int s = 0;
    
    @Override
    public void draw(GameLoop c, Graphics2D g2d) { 
        
        Point2D p = getPosition();
        float x = p.getX();
        float y = p.getY();
         
        int rad = getRadius();
         
        //원본 맵을 그리고
        map.draw(c, g2d);
        
        Shape s = g2d.getClip();
        
        // 검은 마스크를 씌움...
        
        Point2D pos1 = getPosition();
        Point2D pos2 = new Point2D(620, 130);
        double angle1 = getAngle();
        double angle2 = Math.toRadians(90);
        
        if (isTurnOnFlash) {
             
            {
                // https://www.youtube.com/watch?v=KjfbzDGrRfI (ForeignGuyMike 감사합니다.)
                // 까만색 마스크를 씌웁니다... 
                BufferedImage bi = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                Graphics2D gg = bi.createGraphics();

                gg.setPaint(new Color(0, 0, 0, (int) (255 * 0.75f)));
                gg.fillRect(0, 0, MAP_WIDTH, MAP_HEIGHT);
                
                // 아직 이해를 못했는데 이작업을 하고...
                gg.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT));
                
                
                // 빛을 비추고...
                Area s1 = getArea(pos1, angle1);
                gg.setClip(s1); 
                gg.setPaint(getRadialGradientPaint(pos1.getX(), pos1.getY())); // 그라데이션 삽입 
                gg.fill(s1);
                
                // 빛을 또 비추고...
                Area s2 = getArea(pos2, angle2);
                gg.setClip(s2); 
                gg.setPaint(getRadialGradientPaint(pos2.getX(), pos2.getY())); // 그라데이션 삽입 
                gg.fill(s2);
                
                // 그렇게 작업된 image 를 드로잉
                g2d.drawImage(bi, 0, 0, null);
            }
            
        } 
        
        g2d.setClip(s);
        
        
        g2d.setColor(Color.red);
        
        g2d.drawImage(sp.getSprite("player.png", (int) (i % 3), getGridIndex(), 36, 82), (int) x - rad / 2, (int) y - rad, null);
        
        //g2d.setColor(Color.CYAN);
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
        
        if (vel.getX() != 0 || vel.getY() != 0) {
            i += 0.333;
        }
        
        Vector2D v = new Vector2D(vel);
        int x = (int) (p.getX() + vel.getX());
        int y = (int) (p.getY() + vel.getY());

        p.set(x, y);
        
        for(Wall w : map.getWall()) {

            SAT.Response r = new SAT.Response();
            if (SAT.testPolygonCircle(w, this, r)) {

                v = r.overlapV.add(p);

                p.set((int) (v.getX()), (int) (v.getY()));
            }
        }
        
    }
}