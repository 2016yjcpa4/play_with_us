package com.github.yjcpaj4.play_with_us.game;

import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Television extends LightWithGameObject {
    
    private static final int WIDTH = 40;
    private static final int HEIGHT = 25;
    
    private BufferedImage mNoiseImage;
    private BufferedImage mImage;
    
    private Point2D mPos = new Point2D();
    
    private Light mLight = new Light() {
        
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
            return Math.toRadians(90);
        }
    };
    
    public Point2D getPosition() {
        return mPos;
    }
    
    public Television(Point2D p) {
        mLight.setTurnOff();
        try {
            mPos = p;
            mNoiseImage = ImageIO.read(new File("res/img.tv.noise.png"));
            mImage = ImageIO.read(new File("res/img.tv.png"));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private boolean mStartNoise = false;

    @Override
    public void update(GameLayer g, long delta) {
        
        if ( ! mStartNoise) {
            Vector2D v = new Vector2D(g.getPlayer().getPosition()).subtract(getPosition());

            if (v.length() <= 150) {
                mStartNoise = true;
            }
        }
        
        if (mStartNoise) {
            int n = (int) (Math.random() * 1000 + 500);
            if (delta / n % 2 == 0) {
                mLight.setTurnOn();
            } else {
                mLight.setTurnOff();
            }
        }
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        Point2D p = getPosition();
        int x = (int) p.getX() - WIDTH / 2;
        int y = (int) p.getY();
        
        if (mLight.isTurnOn()) {
            g2d.drawImage(mNoiseImage, x, y, null);
        }
    }

    @Override
    public Light getOwnedLight() {
        return mLight;
    }
}
