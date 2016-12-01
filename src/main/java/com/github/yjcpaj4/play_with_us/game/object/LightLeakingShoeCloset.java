package com.github.yjcpaj4.play_with_us.game.object;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.resource.SoundResource;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class LightLeakingShoeCloset extends LightWithGameObject {
    
    private static final double LIGHT_ANGLE = Math.toRadians(55);
    private static final double LIGHT_EXTENT = 30;
    private static final float LIGHT_LENGTH = 50;
    
    private BufferedImage mImage; 
    private Point2D mPos = new Point2D();
    
    private Light mLight = new Light() {
        
        @Override
        public Point2D getPosition() {
            return LightLeakingShoeCloset.this.getPosition();
        }
        
        @Override
        public double getExtent() {
            return LIGHT_EXTENT;
        }

        @Override
        public float getLength() {
            return LIGHT_LENGTH;
        }

        @Override
        public double getAngle() {
            return LIGHT_ANGLE;
        }
    };
    
    public LightLeakingShoeCloset(Point2D p) {
        mPos = p;
        mImage = Application.getInstance().getResource().getImage("img.shoecloset.door");
    }
    
    public Point2D getPosition() {
        return mPos;
    }
    
    @Override
    public void update(GameLayer g, long delta) {
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        if (mLight.isTurnOff()) {
            return;
        }
        
        Point2D p = getPosition();
        int x = (int) p.getX();
        int y = (int) p.getY();
        float d = g.getPlayer().getMap().getDarkness();
        
        g2d.drawImage(mImage, x, y, null);
    }

    @Override
    public Light getOwnedLight() {
        return mLight;
    }
}
