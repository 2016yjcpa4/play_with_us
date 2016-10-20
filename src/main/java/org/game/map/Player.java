package org.game.map;
 
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import org.game.Game;
import org.game.InputManager;
import org.game.resource.ResourceManager;
import org.game.geom.Circle;
import org.game.geom.CollisionDetection;
import org.game.math.Point2D;
import org.game.math.Vector2D;
import org.game.resource.SpriteImageResource;
import org.game.util.GameUtil;
 
public class Player extends MapObject {
    
    private Circle mCollider;
    private Vector2D mDir = new Vector2D(0, 0);    
    private Vector2D mVel = new Vector2D();    
    
    /**
     * 플레이어의 손전등을 커스터마이징. 
     */
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
    
    /**
     * 플레이어가 가지고있는 손전등.
     * 
     * @return 
     */
    public Light getLight() {
        return mLight;
    }
    
    /**
     * 플레이어의 속도.
     * 
     * @return 
     */
    public Vector2D getVelocity() {
        return mVel;
    }     
    
    /**
     * 플레이어가 현재 바라보는 방향을 앵글값으로 나타냄.
     * 
     * @return 
     */
    public double getAngle() {
        return mDir.sub(getPosition()).angle();
    }
    
    /**
     * 플레이어의 현재위치.
     * 
     * @return 
     */
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
    
    @Override
    public void update(Game g) { 
        InputManager o = g.getInput();
        
        // 키를 눌렀을때 플레이어의 각종 처리들
        if (o.isKeyPressed(KeyEvent.VK_W)) mVel.setY(-4);
        if (o.isKeyPressed(KeyEvent.VK_S)) mVel.setY(4);
        if (o.isKeyPressed(KeyEvent.VK_A)) mVel.setX(-4);
        if (o.isKeyPressed(KeyEvent.VK_D)) mVel.setX(4);
        if ( ! (o.isKeyPressed(KeyEvent.VK_W) || o.isKeyPressed(KeyEvent.VK_S))) mVel.setY(0);
        if ( ! (o.isKeyPressed(KeyEvent.VK_A) || o.isKeyPressed(KeyEvent.VK_D))) mVel.setX(0);
        if (o.isMousePressed(MouseEvent.BUTTON3)) mLight.setTurnOn();
        if (o.isMouseReleased(MouseEvent.BUTTON3)) mLight.setTurnOff();
        
        Vector2D d = mDir;
        Point2D p = getPosition();
        
        // 현재 바라보는 방향, 위치를 업데이트
        d.set(o.getMousePosition());
        p.set((int) (p.getX() + mVel.getX()), 
              (int) (p.getY() + mVel.getY()));
        
        // 충돌에 대하여 처리를 합니다.
        for (Wall w : getMap().getAllWall()) {
            CollisionDetection.Response r = new CollisionDetection.Response();
            if (CollisionDetection.isCollides(w.getCollider(), mCollider, r)) {
                Vector2D v = new Vector2D(p).add(r.getOverlapVector());

                p.set((int) v.getX(), (int) v.getY());
            }
        }
    }
    
    /**
     * 현재 플레이어의 이미지를 가져옵니다.
     * 
     * 걷고, 서있고... 등등?
     * 
     * @return 프레임을 반환합니다.
     */
    private SpriteImageResource.SpriteImage.Frame getCurrentSpriteFrame(long d) {
        String k = String.join(".", "player", "walk", GameUtil.getDirectionByRadian(getAngle()));
        SpriteImageResource r = ResourceManager.getInstance().getSprite(k);
        
        SpriteImageResource.SpriteImage.Frame f = r.getFrame(0); // 기본 상태
        
        if (mVel.getX() != 0 || mVel.getY() != 0) { // 움직임이 발생하면
            f = r.getCurrentFrame(d); // 델타값을 넣어 현재 프레임을 뽑아옴
        }
        
        // TODO ... 걷고, 서있는것 이외에 무언가 처리해야할게 있는가???
        
        return f;
    }
    
    @Override
    public void draw(Game g, Graphics2D g2d) { 
        SpriteImageResource.SpriteImage.Frame f = getCurrentSpriteFrame(g.getDelta());
        Point2D p = getPosition();
        
        g2d.drawImage(f.getImage(), 
                      p.getX() - f.getWidth() / 2, 
                      p.getY() - f.getHeight() / 2, 
                      null);
    }
}