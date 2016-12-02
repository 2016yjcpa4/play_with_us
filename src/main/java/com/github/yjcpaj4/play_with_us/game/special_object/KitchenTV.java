package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.game.object.Light;
import com.github.yjcpaj4.play_with_us.game.LightWithGameObject;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.resource.SoundResource;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class KitchenTV extends LightWithGameObject {
    
    private static final int X = 345;
    private static final int Y = 320;
    
    private static final double LIGHT_ANGLE = Math.toRadians(90);
    private static final double LIGHT_EXTENT = 70;

    private int mFPS = 0;
    private BufferedImage mTurnOnImage; 
    private SoundResource mTurnOnSound;
    private Point2D mPos = new Point2D(X, Y);
    private boolean mSurprise = false;
    
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
