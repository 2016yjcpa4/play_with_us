package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.game.object.Light;
import com.github.yjcpaj4.play_with_us.game.LightWithGameObject;
import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.layer.InterativeLayer;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.resource.SoundResource;
import com.github.yjcpaj4.play_with_us.util.SoundUtil;
import com.sun.glass.events.KeyEvent;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class KitchenTV extends LightWithGameObject {
    
    private static final int X = 345;
    private static final int Y = 320;
    
    private static final String YES = "살펴본다.";
    private static final String NO = "그만둔다.";
    
    private static final double LIGHT_ANGLE = Math.toRadians(90);
    private static final double LIGHT_EXTENT = 70;

    private int mFPS = 0;
    private BufferedImage mTurnOnImage; 
    private SoundResource mTurnOnSound;
    private Point2D mPos = new Point2D(X, Y);
    private boolean mSurprise = false;
    private long mDuration = 0;
    
    private boolean mPlaySound = false;
    
    private Circle mCollider = new Circle(X, Y, 45);
    
    private Light mLight = new Light() {
        
        @Override
        public Point2D getPosition() {
            Point2D p = new Point2D(KitchenTV.this.getPosition());
            p.setX(p.getX() + mTurnOnImage.getWidth() / 2);
            return p;
        }
        
        @Override
        public double getExtent() {
            return LIGHT_EXTENT;
        }

        @Override
        public double getAngle() {
            return LIGHT_ANGLE;
        }
    };
    
    public KitchenTV() {
        mTurnOnImage = Application.getInstance().getResource().getImage("img.obj.kitchen.tv");
        mTurnOnSound = Application.getInstance().getResource().getSound("snd.obj.kitchen.tv");
    }
    
    public Point2D getPosition() {
        return mPos;
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        
        if (getMap().equals(g.getPlayer().getMap()) 
        && CollisionDetection.isCollide(mCollider, g.getPlayer().getCollider())
        && g.getInput().isKeyOnce(KeyEvent.VK_F)) {
            
            InterativeLayer l = new InterativeLayer(Application.getInstance()) {
                
                @Override
                protected void pause() {
                    super.pause();
                    
                    if (getCurrentAnswer().equals(YES)) {
                        g.showMessage("아무것도 발견하지 못하였습니다.", 1000);
                    }
                }
            };
            l.setQuestion("살펴 보시겠습니까?");
            l.setAnswers(new String[] { YES, NO });
            l.setBackground(g.getResource().getImage("img.bg.kitchen.tv"));
            g.showLayer(l);
        }
        
        if (g.getPlayer().getMap() != getMap()) {
            mTurnOnSound.stop();
            mPlaySound = false;
            return;
        }
        
        Point2D p1 = g.getPlayer().getPosition();
        Point2D p2 = getPosition(); 

        if (new Vector2D(p1).subtract(p2).length() <= 150) {
            mSurprise = true;
        }
        
        if (mSurprise) {
            mTurnOnSound.setVolume(SoundUtil.getVolumeByDistance(g.getPlayer().getPosition(), getPosition(), 400));

            if ( ! mPlaySound) {
                mTurnOnSound.play(-1);
                mPlaySound = true;
            }
            
            boolean b = mLight.isTurnOn();
            if (mDuration >= mFPS) {
                if (b) {
                    mLight.setTurnOff();
                } else if(g.getPlayer().getMap() == getMap()) {
                    mLight.setTurnOn();
                }
            }

            if (b != mLight.isTurnOn()) {
                mFPS = (int) (Math.random() * 1000 + 1000);
                mDuration = 0;
                
                if (mLight.isTurnOff()) { 
                    mFPS = 100;
                }
            }

            mDuration += delta;
        }
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        if (mLight.isTurnOff()) {
            return;
        }
        
        Point2D p = getPosition();
        int x = (int) p.getX();
        int y = (int) p.getY();
        
        g2d.drawImage(mTurnOnImage, x, y, null);
    }

    @Override
    public Light getOwnedLight() {
        return mLight;
    }
}
