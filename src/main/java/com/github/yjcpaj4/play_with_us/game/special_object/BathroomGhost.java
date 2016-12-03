package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.game.object.Player;
import com.github.yjcpaj4.play_with_us.ResourceManager;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.game.Map;
import com.github.yjcpaj4.play_with_us.game.object.Light;
import com.github.yjcpaj4.play_with_us.game.object.Portal;
import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Matrix2D;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.resource.SoundResource;
import com.github.yjcpaj4.play_with_us.resource.SpriteResource;
import com.github.yjcpaj4.play_with_us.util.AWTUtil;
import com.github.yjcpaj4.play_with_us.util.MathUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class BathroomGhost extends GameObject {
    
    private static final int X = 431;
    private static final int Y = 208;
    
    private long mDelta = 0;
    private Vector2D mVel = new Vector2D();
    private Polygon mCollider = new Circle(X, Y, 10);
    
    private long mDuration = 0;
    
    private boolean mLookingAtMe = false;
    
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
    
    /**
     * 플레이어와 마네킹 사이 각도
     * 
     * @param p
     * @return 
     */
    private double getAngleWithPlayer(Player p) {
        return new Vector2D(p.getPosition()).subtract(mCollider.getPosition()).toAngle();
    }
    
    private SpriteResource.Frame getCurrentSpriteFrame(GameLayer g, long d) {
        boolean isIdle = mVel.getX() == 0 && mVel.getY() == 0;
        
        List<String> l = new ArrayList<>(4);
        l.add("sprt");
        l.add("bathroom");
        l.add("ghost");
        
        if (isIdle) {
            l.add("idle");   
            l.add("n");
        } else {
            l.add("walk");
            l.add(MathUtil.getSimpleDirectionByRadian(getAngleWithPlayer(g.getPlayer())));
        }
        
        SpriteResource r = g.getResource().getSprite(String.join(".", l));
        int n = (int) (mDuration / r.getFPS() % r.getLength());
        
        mDuration += d;
        
        return r.getFrame(n);
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        final Player o = g.getPlayer();
        final Light l = o.getOwnedLight();
        if ( ! mLookingAtMe) {
            if (l.isTurnOn() && l.isCollide(mCollider)) {
                mLookingAtMe = true;
                return;
            }
        }
        
        if (mLookingAtMe) {
        }
        
        mDelta += delta;
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        SpriteResource.Frame f = getCurrentSpriteFrame(g, delta);
        Point2D p = mCollider.getPosition();
        
        int x = (int) (p.getX() - f.getWidth() / 2);
        int y = (int) (p.getY() - f.getHeight() + ((Circle) mCollider).getRadius());
        
        g2d.drawImage(f.getImage(), x, y, null);
    }
}
