package org.game.map;
 
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import org.game.Game;
import org.game.InputManager;
import org.game.SpriteManager;
import org.game.geom.Circle;
import org.game.geom.CollisionDetection;
import org.game.math.Point2D;
import org.game.math.Vector2D;
 
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
    
    public Player() {
        mCollider = new Circle(620, 700, 42);
    }
    
    public Light getLight() {
        return mLight;
    }
    
    public Vector2D getDirection() {
        return mDir;
    }
    
    public Vector2D getVelocity() {
        return mVel;
    }     
    
    public double getAngle() {
        return mDir.sub(getPosition()).angle();
    }
    
    public Point2D getPosition() {
        return mCollider.getPosition();
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
        
        Vector2D d = getDirection();
        Point2D p = getPosition();
        
        d.set(m.getMousePosition());
        p.set((int) (p.getX() + mVel.getX()), 
              (int) (p.getY() + mVel.getY()));
        
        for(Wall w : getMap().getAllWall()) {
            CollisionDetection.Response r = new CollisionDetection.Response();
            if (CollisionDetection.isCollides(w.getCollider(), mCollider, r)) {
                Vector2D v = new Vector2D(p).add(r.getOverlapVector());

                p.set((int) v.getX(), (int) v.getY());
            }
        }
        
        if (mVel.getX() != 0 || mVel.getY() != 0) {
            i += 0.333;
        }
    }
    
    
    private SpriteManager sp = new SpriteManager();
    
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
}