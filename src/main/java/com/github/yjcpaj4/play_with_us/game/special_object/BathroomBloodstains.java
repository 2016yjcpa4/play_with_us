package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.ResourceManager;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Box2D;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.resource.SpriteResource;
import com.github.yjcpaj4.play_with_us.util.MathUtil;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BathroomBloodstains extends GameObject {
    
    private static final int X = 196;
    private static final int Y = 73;
    private static final int WIDTH = 64;
    private static final int HEIGHT = 173;
    
    private Polygon mCollider;
    private long mDuration;
    
    private boolean mShow;
    
    public BathroomBloodstains() {
        mCollider = new Box2D(X, Y, WIDTH, HEIGHT).toPolygon();
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        if (g.getInput().isKeyOnce(KeyEvent.VK_F)) {
            if (CollisionDetection.getCollision(g.getPlayer().getCollider(), mCollider) != null) {
                mShow = true;
            }
        }
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        if ( ! mShow) {
            return;
        }
        
        ResourceManager m = g.getResource();
        SpriteResource r = m.getSprite("sprt.bathroom.bloodstains");
        int n = (int) (mDuration / r.getFPS());
        if (n >= r.getLength()) {
            n = r.getLength() - 1;
        }
        BufferedImage b = r.getFrame(n).getImage();
        
        g2d.drawImage(b, X, Y, null);
        
        mDuration += delta;
    }
}
