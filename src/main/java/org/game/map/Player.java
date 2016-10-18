package org.game.map;
 
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
                                           new Color(0, 0, 0, (int) (255 * 0)),
                                           new Color(0, 0, 0, (int) (255 * 0.95f))
                                       });
    }
    
    private BufferedImage createLayer(Point2D pos, double ang) {
        BufferedImage b = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = b.createGraphics();
        
        Shape ss = g2d.getClip();

        Area s = getArea(pos, ang);
        g2d.setClip(s); 
        g2d.setPaint(getRadialGradientPaint(pos.getX(), pos.getY())); // 그라데이션 삽입 
        g2d.fill(s);
        
        g2d.setClip(ss);
        
        return b;
    }
    
    @Override
    public void draw(GameLoop c, Graphics2D g2d) { 
        
        Point2D p = getPosition();
        float x = p.getX();
        float y = p.getY();
         
        int rad = getRadius();
        
        float dark = 0.95f;
        
        //원본 맵을 그리고
        map.draw(c, g2d);
        g2d.setColor(new Color(0, 0, 0, (int) (255 * dark)));
        g2d.fillRect(0, 0, MAP_WIDTH, MAP_HEIGHT); // 채움 
            
        Shape s = g2d.getClip();
        
        // 검은 마스크를 씌움...
        
        Point2D pos = new Point2D(620, 130);
        double angle = getAngle();
        double ang2 = Math.toRadians(90);
        
        if (isTurnOnFlash) {
            
            Area a;
            
            // 빛을 비춰지는 부분 모든영역을 투명하게해서 clip 합니다.
            a = new Area();
            a.add(getArea(pos, ang2));
            a.add(getArea(getPosition(), angle));
            g2d.setClip(a);
            map.draw(c, g2d);
            g2d.setPaint(ColorUtil.TRANSPARENT);
            g2d.fill(a);
            
            // 빛발산 효과 (그라데이션)를 추가합니다.
            g2d.drawImage(createLayer(pos, ang2), 0, 0, null); // 채움
            g2d.drawImage(createLayer(getPosition(), angle), 0, 0, null); // 채움
            
            // 빛이 교차되는부분은 밝게
            a = new Area(getArea(pos, ang2));
            a.intersect(getArea(getPosition(), angle));
            g2d.setClip(a);
            map.draw(c, g2d);
            g2d.setPaint(ColorUtil.TRANSPARENT);
            g2d.fill(a);
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