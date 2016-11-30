package com.github.yjcpaj4.play_with_us.game;
 
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
import com.github.yjcpaj4.play_with_us.resource.MovieResource;
import com.github.yjcpaj4.play_with_us.layer.VideoLayer;
import com.github.yjcpaj4.play_with_us.math.Matrix2D;
import com.github.yjcpaj4.play_with_us.util.MathUtil;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
 
public class Player extends PhysicsObject  {
    
    private static final int SPEED = 3;
    
    private Vector2D mVel = new Vector2D();
    private Vector2D mDir = new Vector2D(); 
    
    
    private Timer mTimer;
    
    private String mMessage;
    
    /**
     * 플레이어의 손전등을 커스터마이징. 
     */
    private Light mLight = new Light() {
        
        private static final int LIGHT_RELATIVE_Y = -15;
        
        @Override
        public Point2D getPosition() {
            Point2D p = new Point2D(Player.this.getPosition());
            p.setY(p.getY() + LIGHT_RELATIVE_Y);
            return p;
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
        mCollider = new Circle(x, y, 10);
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
    
    private void setDirectionByInput(GameLayer g, InputManager m) {
        mDir.set(new Vector2D(g.getCameraPosition()).divide(g.getCameraZoom()).add(m.getMousePosition().divide(g.getCameraZoom())));
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
        setDirectionByInput(g, m); // 플레이어가 바라보는 방향
        
        mCollider.transform(Matrix2D.translate(mVel.getX(), mVel.getY()));
        
        for (NotWalkable o : getMap().getAllNotWalkable()) {
            Vector2D v;
            if ((v = CollisionDetection.getCollision(mCollider, o.getCollider())) != null) {
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
        boolean isIdle = mVel.getX() == 0 && mVel.getY() == 0;
        
        List<String> l = new ArrayList<>(3);
        l.add("player");
        l.add(isIdle ? "idle" : "walk");
        l.add(MathUtil.getSimpleDirectionByRadian(getAngle()));
        
        SpriteResource.Frame f = r.getSprite(String.join(".", l)).getCurrentFrame(d);
        
        // TODO ... 걷고, 서있는것 이외에 무언가 처리해야할게 있는가???
        
        return f;
    }
    
    public void showMessage(String s, int n) {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        
        final TimerTask t = new TimerTask() {
            
            @Override
            public void run() {
                mMessage = null;
            }
        };
        
        mMessage = s;
        
        mTimer = new Timer();
        mTimer.schedule(t, n);
    }
    
    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) { 
        SpriteResource.Frame f = getCurrentSpriteFrame(g.getResource(), delta);
        Point2D p = getPosition();
        
        for(GameObject o : getMap().getAllObject()) {
            if (o instanceof Portal) {
                Vector2D v = new Vector2D(((Portal) o).getPosition()).subtract(p);
                if (v.length() <= 100) {
                    showMessage("문 발견", 1000);
                }
            }
        }
        
        int x = (int) (p.getX() - f.getWidth() / 2);
        int y = (int) (p.getY() - f.getHeight() + ((Circle) mCollider).getRadius());
        
        if (mMessage != null) {
            g2d.setFont(new Font("굴림", Font.PLAIN, 13));
            g2d.setColor(Color.WHITE);
            g2d.drawString(mMessage, x, y - 10);
        }
        
        g2d.drawImage(f.getImage(), x, y, null);
        
        if (Application.DEBUG) {
            g2d.setColor(Color.RED);
            g2d.drawPolygon(mCollider.toAWTPolygon());
        }
    }
}
