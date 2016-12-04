package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.ResourceManager;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Box2D;
import com.github.yjcpaj4.play_with_us.math.Matrix2D;
import com.github.yjcpaj4.play_with_us.resource.SpriteResource;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class BathroomBloodstains extends GameObject {
    
    /*
     * 화장실이라는 맵에 한정되 있으므로 
     * X, Y, Width, Height 를 상수로 정의 합니다.
     */
    private static final int X = 196;
    private static final int Y = 73;
    private static final int WIDTH = 64;
    private static final int HEIGHT = 173;
    
    private static final int MARGINS = 100;
    
    private Polygon mCollider;
    private long mDuration;
    
    private boolean mShowAnim;
    
    public BathroomBloodstains() {
        Box2D b = new Box2D(X, Y, WIDTH, HEIGHT);
        b.setMargins(MARGINS);
        
        mCollider = b.toPolygon();
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        if (g.getPlayer().hasRoomKey("library")) {
            if (CollisionDetection.isCollide(g.getPlayer().getCollider(), mCollider)) {
                mShowAnim = true;
            }
        }
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        if (Application.DEBUG) {
            g2d.setColor(Color.red);
            g2d.drawPolygon(mCollider.toAWTPolygon());
            g2d.drawRect(X, Y, WIDTH, HEIGHT);
        }
        
        if ( ! mShowAnim) {
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
