package com.github.yjcpaj4.play_with_us.game;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.resource.SoundResource;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Television extends LightWithGameObject {
    
    private static final int WIDTH = 40;
    private static final int HEIGHT = 25;
    
    private int mFPS = 0;
    private BufferedImage mTurnOnImage; 
    private SoundResource mTurnOnSound;
    private Point2D mPos = new Point2D();
    private boolean mSurprise = false;
    
    private Light mLight = new Light() {
        
        private final double ANGLE = Math.toRadians(90);
        
        @Override
        public Point2D getPosition() {
            return Television.this.getPosition();
        }
        
        @Override
        public double getExtent() {
            return 70;
        }

        @Override
        public double getAngle() {
            return ANGLE;
        }
    };
    
    public Television(Point2D p) {
        mLight.setTurnOff();
        
        mPos = p;
        mTurnOnImage = Application.getInstance().getResource().getImage("img.tv.noise");
        mTurnOnSound = Application.getInstance().getResource().getSound("snd.tv.noise");
    }
    
    public Point2D getPosition() {
        return mPos;
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        
        if (mLight.isTurnOff()) {
            mFPS = (int) (Math.random() * 1000 + 1000);
        }
        
        if ( ! mSurprise) {
            Point2D p1 = g.getPlayer().getPosition();
            Point2D p2 = getPosition();
            
            Vector2D v = new Vector2D(p1).subtract(p2);
            if (v.length() <= 150) {
                mSurprise = true;
            }
        }
        else {
            if (delta / mFPS % 2 == 0) {
                mLight.setTurnOn();
            } else {
                mLight.setTurnOff();
            }
        }
        
        if (mLight.isTurnOn() && g.getPlayer().getMap() == getMap()) {
            mTurnOnSound.play();
        }
        else {
            mTurnOnSound.stop();
        }
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        Point2D p = getPosition();
        int x = (int) p.getX();
        int y = (int) p.getY();
        
        if (mLight.isTurnOn()) {
            g2d.drawImage(mTurnOnImage, x - WIDTH / 2, y, null);
        }
    }

    @Override
    public Light getOwnedLight() {
        return mLight;
    }
}
