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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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
import org.game.InputManager;
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
 
public class Player extends MapObject {
    
    private Circle mCollider;
    private Vector2D mDir = new Vector2D(0, 0);    
    private Vector2D mVel = new Vector2D();    
    private Light mLight = new Light() {
        
        @Override
        public Point2D getPosition() {
            return Player.this.getPosition();
        }

        @Override
        public double getAngle() {
            return Player.this.getAngle();
        }
    };
    
    public Light getLight() {
        return mLight;
    }
    
    private SpriteManager sp = new SpriteManager();
    
    public Player() {
        mCollider = new Circle(620, 700, 42);
    }
    
    /**
     * 캐릭터가 가리키는 방향.
     * 
     * @return Vector2D 클래스로 반환됩니다. 
     */
    public Vector2D getDirection() {
        return mDir;
    }
    
    /**
     * 캐릭터의 가속도.
     * 
     * @return 
     */
    public Vector2D getVelocity() {
        return mVel;
    }     
    
    // Math.atan2(dir - pos) = 각도
    public double getAngle() {
        return mDir.sub(getPosition()).angle();
    }
    
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
    
    int s = 0;
    
    @Override
    public void draw(Game g, Graphics2D g2d) { 
        
        Point2D p = getPosition();
        float x = p.getX();
        float y = p.getY();
         
        int rad = mCollider.getRadius();
        
        g2d.drawImage(sp.getSprite("player.png", (int) (i % 3), getGridIndex(), 36, 82), (int) x - rad / 2, (int) y - rad, null);
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
    public void update(Game g) { 
        InputManager m = g.getInputManager();
        
        if (m.isKeyPressed(KeyEvent.VK_W)) mVel.setY(-4);
        if (m.isKeyPressed(KeyEvent.VK_S)) mVel.setY(4);
        if (m.isKeyPressed(KeyEvent.VK_A)) mVel.setX(-4);
        if (m.isKeyPressed(KeyEvent.VK_D)) mVel.setX(4);
        
        if ( ! (m.isKeyPressed(KeyEvent.VK_W) || m.isKeyPressed(KeyEvent.VK_S))) mVel.setY(0);
        if ( ! (m.isKeyPressed(KeyEvent.VK_A) || m.isKeyPressed(KeyEvent.VK_D))) mVel.setX(0);
        
        if (m.isMousePressed(MouseEvent.BUTTON3)) mLight.setTurnOn();
        if (m.isMouseReleased(MouseEvent.BUTTON3)) mLight.setTurnOff();
        
        mDir.set(m.getMousePosition());
        
        Point2D p = getPosition();
        
        if (mVel.getX() != 0 || mVel.getY() != 0) {
            i += 0.333;
        }
        
        Vector2D v = new Vector2D(mVel);
        int x = (int) (p.getX() + mVel.getX());
        int y = (int) (p.getY() + mVel.getY());

        p.set(x, y);
        
        for(Wall w : getMap().getAllWall()) {

            SAT.Response r = new SAT.Response();
            if (SAT.testPolygonCircle(w.getCollider(), mCollider, r)) {

                v = r.overlapV.add(p);

                p.set((int) (v.getX()), (int) (v.getY()));
            }
        }
        
    }
}