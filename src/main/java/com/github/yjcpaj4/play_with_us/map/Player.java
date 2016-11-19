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
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.resource.SpriteResource;
import com.github.yjcpaj4.play_with_us.resource.VideoResource;
import com.github.yjcpaj4.play_with_us.layer.VideoLayer;
import com.github.yjcpaj4.play_with_us.math.Matrix2D;
import com.github.yjcpaj4.play_with_us.util.MathUtil;
import java.awt.Color;
 
public class Player extends PhysicsObject {
    
    private static final int SPEED = 3;
    
    private Vector2D mVel = new Vector2D();
    private Vector2D mDir = new Vector2D(); 
    
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
    
    public Player(Point2D p) {
        this(p.getX(), p.getY());
    }
    
    public Player(float x, float y) {
        mCollider = new Circle(x, y, 20);
    }
    
    /**
     * 플레이어가 가지고있는 손전등.
     * 
     * @return 
     */
    public Light getOwnedLight() {
        return mLight;
    }
    
    /**
     * 플레이어가 현재 바라보는 방향을 앵글값으로 나타냄.
     * 
     * @return 
     */
    public double getAngle() {
        return mDir.subtract(getPosition()).toAngle();
    }
    
    public boolean isTurnOnLight() {
        return mLight.isTurnOn();
    }
    
    private void setDirectionByInput(InputManager m) {
        mDir.set(m.getMousePosition());
    }
    
    private void setLightByInput(InputManager m) {
        if (m.isMousePressed(MouseEvent.BUTTON3)) { 
            mLight.setTurnOn(); 
        }
        else {
            mLight.setTurnOff();
        }
    }
    
    private void setVelocityByInput(InputManager m) {
        int x = 0;
        int y = 0;
        
        if (m.isKeyPressed(KeyEvent.VK_W)) {
            y = -SPEED;
        }
        else if (m.isKeyPressed(KeyEvent.VK_S)) {
            y = SPEED;
        }
        else {
            y = 0;
        }
        
        if (m.isKeyPressed(KeyEvent.VK_A)) {
            x = -SPEED;
        } 
        else if (m.isKeyPressed(KeyEvent.VK_D)) {
            x = SPEED;
        }
        else {
            x = 0;
        }
        
        mVel.set(x, y);
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        InputManager m = g.getInput();
        
        // 인풋 들어온걸 토대로 캐릭터의 속성 변화를 줍니다.
        setLightByInput(m); // 손전등의 상태
        setVelocityByInput(m); // 플레이어의 속도
        setDirectionByInput(m); // 플레이어가 바라보는 방향
        
        mCollider.transform(Matrix2D.translate(mVel.getX(), mVel.getY()));
        
        for (NotWalkable o : getMap().getAllNotWalkable()) {
            Vector2D v;
            if ((v = getCollisionWith(o)) != null) { 
 
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
    @Deprecated
    private SpriteResource.Frame getCurrentSpriteFrame(ResourceManager r, long d) {
        String k = String.join(".", "player", "walk", MathUtil.getDirectionByRadian(getAngle()));
        
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