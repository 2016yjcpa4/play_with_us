package com.github.yjcpaj4.play_with_us.game.object;
 
import com.github.yjcpaj4.play_with_us.game.LightWithGameObject;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.InputManager;
import com.github.yjcpaj4.play_with_us.ResourceManager;
import com.github.yjcpaj4.play_with_us.game.TileNode;
import com.github.yjcpaj4.play_with_us.game.object.Light;
import com.github.yjcpaj4.play_with_us.game.object.Wall;
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
import com.github.yjcpaj4.play_with_us.resource.SoundResource;
import com.github.yjcpaj4.play_with_us.util.MathUtil;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
 
public class Player extends LightWithGameObject  {
    
    private static final int INTERVAL_GHOST = 1000 * 60 * 3;
    
    private static final float LIGHT_LENGTH = 210f;
    
    protected transient Polygon mCollider;
    
    public static final int SPEED = 4;
    
    private Vector2D mVel = new Vector2D();
    private Vector2D mDir = new Vector2D(); 
    
    private boolean mOwnedLight = false;
    
    private Timer mMessageTimer;
    private String mMessage;
    
    private Set<String> mRoomKey = new HashSet<>();
    
    private boolean mInputEnable = true;
    
    private boolean mLightControllable = true;
    
    private long mSpriteDuration = 0;
    
    private long mRemainGhostTime = INTERVAL_GHOST;
    
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
        public void setTurnOff() {
            super.setTurnOff();
        }

        @Override
        public void setTurnOn() {
            super.setTurnOn();
        }

        @Override
        public float getLength() {
            return LIGHT_LENGTH;
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
    
    public boolean isCollide(Polygon p) {
        return CollisionDetection.isCollide(mCollider, p);
    }
    
    public boolean hasRoomKey(String s) {
        return mRoomKey.contains(s);
    }
    
    public void addRoomKey(String s) {
        mRoomKey.add(s);
    }
    
    public boolean isInputEnable() {
        return mInputEnable;
    }
    
    public void setInputEnable() {
        mInputEnable = true;
    }
    
    public boolean isLightControllable() {
        return mLightControllable;
    }
    
    public void setLightControllable(boolean b) {
        mLightControllable = b;
    }
    
    public void setIdle() {
        mVel.set(0, 0);
    }
    
    public void setInputDisable() {
        mInputEnable = false;
    }
    
    public Polygon getCollider() {
        return mCollider;
    }
    
    public void setPosition(float x, float y) {
        mCollider.setPosition(x, y);
    }
    
    public void setPosition(Point2D p) {
        mCollider.setPosition(p.getX(), p.getY());
    }
    
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
    
    /**
     * 플레이어가 가지고있는 손전등.
     * 
     * @return 
     */
    @Override
    public Light getOwnedLight() {
        return mLight;
    }
    
    /**
     * 플레이어가 현재 바라보는 방향을 앵글값으로 나타냄.
     * 
     * @return 
     */
    public double getAngle() {
        return mDir.subtract(mLight.getPosition()).toAngle();
    }
    
    public boolean isOwnedLight() {
        return mOwnedLight;
    }
    
    public void setOwnedLight() {
        mOwnedLight = true;
    }
    
    public boolean isTurnOnLight() {
        return mLight.isTurnOn();
    }
    
    public void setTurnOnLight() {
        mLight.setTurnOn();
    }
    
    public void setTurnOffLight() {
        mLight.setTurnOff();
    }
    
    private void setDirectionByInput() {
        GameLayer g = Application.getInstance().getLayer(GameLayer.class);
        InputManager m = g.getInput();
        
        if ( ! isInputEnable()) {
            return;
        }
        
        Vector2D v = m.getMousePosition().divide(g.getCamera().getZoom());
        Point2D p = g.getCamera().getPosition();
        
        mDir.set(v.add(p));
    }
    
    private void setLightByInput() {
        InputManager m1 = Application.getInstance().getInput();
        ResourceManager m2 = Application.getInstance().getResource();
        
        if ( ! isInputEnable() || ! isLightControllable()) {
            return;
        }
        
        if ( ! mOwnedLight) {
            return;
        }
        
        if (m1.isMouseOnce(MouseEvent.BUTTON3)) { 
            if (mLight.isTurnOn()) {
                mLight.setTurnOff();
            } else {
                mLight.setTurnOn();
            }
            
            m2.getSound("snd.player.light.switch").play();
        }
    }
    
    private void setVelocityByInput() {
        InputManager m = Application.getInstance().getInput();
        
        if ( ! isInputEnable()) {
            return;
        }
        
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
    
    public Vector2D getVelocity() {
        return mVel;
    }
    
    public Vector2D getDirection() {
        return mDir;
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        
        if (mRemainGhostTime <= 0) {
            SoundResource r = g.getResource().getSound("snd.bgm.ghost.begin");
            r.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    showGhost();
                }
            });
            r.play();
            
            mRemainGhostTime = INTERVAL_GHOST;
        }
        
        // 인풋 들어온걸 토대로 캐릭터의 속성 변화를 줍니다.
        setLightByInput(); // 손전등의 상태
        setVelocityByInput(); // 플레이어의 속도
        setDirectionByInput(); // 플레이어가 바라보는 방향
        
        mCollider.transform(Matrix2D.translate(mVel.getX(), mVel.getY()));
        
        for (Wall o : getMap().getAllWallObjects()) {
            Vector2D v;
            if ((v = CollisionDetection.getCollision(mCollider, o.getCollider())) != null) {
                mCollider.transform(Matrix2D.translate(v.getX(), v.getY()));
            }
        }
        
        if (isInputEnable()) {
            mRemainGhostTime -= delta;
        }
    }
    
    public void showMessage(String s, int n) {
        if (mMessageTimer != null) {
            mMessageTimer.cancel();
            mMessageTimer.purge();
            mMessageTimer = null;
        }
        
        final TimerTask t = new TimerTask() {
            
            @Override
            public void run() {
                mMessage = null;
            }
        };
        
        mMessage = s;
        
        mMessageTimer = new Timer();
        mMessageTimer.schedule(t, n);
    }
    
    /**
     * 현재 플레이어의 이미지를 가져옵니다.
     * 
     * 걷고, 서있고... 등등?
     * 
     * @return 프레임을 반환합니다.
     */
    private SpriteResource.Frame getCurrentSpriteFrame(ResourceManager m, long d) {
        boolean isIdle = mVel.getX() == 0 && mVel.getY() == 0;
        
        List<String> l = new ArrayList<>(4);
        l.add("sprt");
        l.add("player");
        l.add(isIdle ? "idle" : "walk");
        l.add(MathUtil.getSimpleDirectionByRadian(getAngle()));
        
        SpriteResource r = m.getSprite(String.join(".", l));
        int n = (int) (mSpriteDuration / r.getFPS() % r.getLength());
        
        mSpriteDuration += d;
        
        return r.getFrame(n);
    }
    
    private void showGhost() {
        
        Point2D p = getPosition();
        
        float x;
        float y;
        
        while (true) { // 적절한 위치를 찾을때까지 좌표값을 찾습니다. (플레이어에게 가로막는 장애물없이 걸어올수 있는 좌표인가)
            double n2 = Math.toRadians(Math.random() * 360);
            
            x = (float) (p.getX() + 200 * Math.cos(n2));
            y = (float) (p.getY() + 200 * Math.sin(n2));
            
            try {
                if ( ! getMap().getPath(p.getX(), p.getY(), x, y).isEmpty()) {
                    break;
                }
            } 
            catch(ArrayIndexOutOfBoundsException e) {
                // 그냥 맵밖으로 튀어나간경우... 따로 예외처리는 없음.
            }
        }
        
        SoundResource r = Application.getInstance().getResource().getSound("snd.bgm.ghost.scream");
        r.play(-1);

        Ghost o = new Ghost(x, y);
        
        getMap().addObject(o);
        
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                r.stop();
                
                o.getMap().removeObject(o);
            }
        }, 8000);
    }
    
    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) { 
        SpriteResource.Frame f = getCurrentSpriteFrame(g.getResource(), delta);
        Point2D p1 = getPosition();
        Point2D p2 = mLight.getPosition();
        
        int x = (int) (p1.getX() - f.getWidth() / 2);
        int y = (int) (p1.getY() - f.getHeight() + ((Circle) mCollider).getRadius());
        
        if (mMessage != null) {
            g2d.setFont(new Font("굴림", Font.PLAIN, 13));
            g2d.setColor(Color.WHITE);
            g2d.drawString(mMessage, x, y - 10);
        }
        
        g2d.drawImage(f.getImage(), x, y, null);
        
        
        if (Application.DEBUG) {
            g2d.setColor(Color.RED);
            g2d.drawPolygon(mCollider.toAWTPolygon());
            g2d.drawString((mRemainGhostTime / 1000) + "초 후 귀신등장", x, y);
            
            g2d.drawLine((int) p1.getX(), (int) p2.getY(), (int) mDir.getX(), (int) mDir.getY());
        }
    }
}
