package com.github.yjcpaj4.play_with_us.map;
 
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.InputManager;
import com.github.yjcpaj4.play_with_us.ResourceManager;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.resource.SpriteResource;
import com.github.yjcpaj4.play_with_us.resource.VideoResource;
import com.github.yjcpaj4.play_with_us.layer.VideoLayer;
import com.github.yjcpaj4.play_with_us.math.Matrix2D;
import com.github.yjcpaj4.play_with_us.util.GameUtil;
import java.awt.Color;
 
public class Player extends PhysicsObject {
    
    private static final int SPEED = 3;
    
    private Circle mCollider;
    private boolean mIsFlashTurnOn;
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
        mCollider = new Circle(400, 400, 20);
    }
    
    public Circle getCollider() {
        return mCollider;
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
        return mDir.sub(getPosition()).toAngle();
    }
    
    public boolean isTurnOnLight() {
        return mLight.isTurnOn();
    }
    
    /**
     * 플레이어의 현재위치.
     * 
     * @return 
     */
    public Point2D getPosition() {
        return mCollider.getCenterPosition();
    }
    
    private int DEBUG = 0;
    
    @Override
    public void update(GameLayer g, long delta) {
        
        int x = 0;
        int y = 0;
        
        // 키를 눌렀을때 플레이어의 각종 처리들
        if (g.getInput().isKeyPressed(KeyEvent.VK_W)) y = (-SPEED);
        if (g.getInput().isKeyPressed(KeyEvent.VK_S)) y = (SPEED);
        if (g.getInput().isKeyPressed(KeyEvent.VK_A)) x = (-SPEED);
        if (g.getInput().isKeyPressed(KeyEvent.VK_D)) x = (SPEED);
        
        // 상하 or 좌우 키값에 안눌러져있다면 따라 보정처리
        if (g.getInput().isKeyReleased(KeyEvent.VK_W) && g.getInput().isKeyReleased(KeyEvent.VK_S)) y = (0);
        if (g.getInput().isKeyReleased(KeyEvent.VK_A) && g.getInput().isKeyReleased(KeyEvent.VK_D)) x = (0);
        
        mVel.set(x, y);
        
        if (g.getInput().isKeyOnce(KeyEvent.VK_F))  {
            if (mLight.isTurnOn()) {
                mLight.setTurnOff();
            } else {
                mLight.setTurnOn();
            }
            
            mIsFlashTurnOn = mLight.isTurnOn();
        }
        
        if ( ! mIsFlashTurnOn) {
            if (g.getInput().isMousePressed(MouseEvent.BUTTON3)) { mLight.setTurnOn(); }
            if (g.getInput().isMouseReleased(MouseEvent.BUTTON3)) { mLight.setTurnOff(); }
        }
        
        Vector2D d = mDir;
        
        // 현재 바라보는 방향, 위치를 업데이트
        d.set(g.getInput().getMousePosition());
        //p.set(p.getX() + mVel.getX(), 
        //      p.getY() + mVel.getY());
        
        mCollider.transform(Matrix2D.translate(mVel.getX(), mVel.getY()));
        
        // 충돌에 대하여 처리를 합니다.
        for (NotWalkable o : getMap().getAllNotWalkable()) {
            Vector2D r = CollisionDetection.getCollision(o.getCollider(), mCollider);
            if (r != null) {
                Vector2D v = r.neg();
 
                mCollider.transform(Matrix2D.translate(v.getX(), v.getY()));
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
    private SpriteResource.Frame getCurrentSpriteFrame(ResourceManager r, long d) {
        String k = String.join(".", "player", "walk", GameUtil.getDirectionByRadian(getAngle()));
        
        SpriteResource.Frame f = r.getSprite(k).getFrame(2); // 기본 상태
        
        if (mVel.getX() != 0 || mVel.getY() != 0) { // 움직임이 발생하면
            f = r.getSprite(k).getCurrentFrame(d); // 델타값을 넣어 현재 프레임을 뽑아옴
        }
        
        // TODO ... 걷고, 서있는것 이외에 무언가 처리해야할게 있는가???
        
        return f;
    }
    
    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) { 
        SpriteResource.Frame f = getCurrentSpriteFrame(g.getResource(), delta);
        Point2D p = getPosition();
        
        g2d.drawImage(f.getImage(), 
                      (int)p.getX() - f.getWidth() / 2, 
                      (int)p.getY() - f.getHeight() / 2, 
                      null);
        
        
        g2d.setColor(Color.RED);
        g2d.drawPolygon(mCollider.toAWTPolygon());
    }
}