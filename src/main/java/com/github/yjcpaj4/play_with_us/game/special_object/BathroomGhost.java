package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.Layer;
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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BathroomGhost extends GameObject {
    
    private static final int X = 398;
    private static final int Y = 155;
    
    private static final int SPEED = 3;
    
    private Polygon mCollider = new Circle(X, Y, 10);
    
    private long mSpriteDuration = 0;
    private long mAnimDuration = 0;
    
    private boolean mLookingAtMe = false;
    private boolean mGhostHide = false;
    private boolean mBrokenLight = false;
    private boolean mFound = false;
    private int mSpeed = 0;
    
    private boolean mShowGhost = false;
    
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
        List<String> l = new ArrayList<>(4);
        l.add("sprt");
        l.add("bathroom");
        l.add("ghost");
        
        if (mSpeed == 0) {
            l.add("idle");   
        } else {
            l.add("walk");
        }
        
        if (mFound) {
            l.add(MathUtil.getSimpleDirectionByRadian(getAngleWithPlayer(g.getPlayer())));
        } else {
            l.add("n");
        }
        
        SpriteResource r = g.getResource().getSprite(String.join(".", l));
        int n = (int) (mSpriteDuration / r.getFPS() % r.getLength());
        
        mSpriteDuration += d;
        
        return r.getFrame(n);
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        if (mGhostHide) {// 귀신이 사라지고
            if ( ! mShowGhost) {
                if (g.getPlayer().isTurnOnLight()) { // 손전등을 최초 한번 키는경우 귀신 깝툭튀 ㅎㅎ
                    Layer l = new Layer(Application.getInstance()) {

                        @Override
                        protected void resume() {
                            super.resume();

                            new Timer().schedule(new TimerTask() {

                                @Override
                                public void run() {
                                    finishLayer();
                                }
                            }, 1500);
                        }

                        @Override
                        protected void draw(long delta, Graphics2D g2d) {
                            super.draw(delta, g2d);

                            BufferedImage b = getResource().getImage("img.bg.bathroom.ghost");

                            g2d.drawImage(b, 0, 0, getContext().getWidth(), getContext().getHeight(), null);
                        }
                    };
                    g.showLayer(l);

                    mShowGhost = true;
                }
            }
        
            return;
        }
        
        final Player o = g.getPlayer();
        
        if ( ! mLookingAtMe) {
            final Light l = o.getOwnedLight();
            if (l.isTurnOn() && l.isCollide(mCollider)) {
                mLookingAtMe = true;
                
                o.setInputDisable();
                o.setIdle();
            }
            return;
        }

        Point2D p1 = mCollider.getPosition();
        Point2D p2 = o.getPosition();

        o.getDirection().set(p1);

        if (mAnimDuration > 1000) {
            
            mFound = true;
            
            if (mAnimDuration > 2000) {
                
                mSpeed = SPEED;

                if ( ! mBrokenLight) {
                    g.getMap().getFirstObjectByClass(BathroomBrokenLight.class).reset();
                    mBrokenLight = true;
                }

                List<Point2D> l = g.getMap().getPath(p1, p2);
                if ( ! l.isEmpty()) {
                    double n = new Vector2D(l.get(0)).subtract(p1).toAngle();
                    double tx = mSpeed * Math.cos(n);
                    double ty = mSpeed * Math.sin(n);

                    mCollider.transform(Matrix2D.translate(tx, ty));
                }
                
                Vector2D v = new Vector2D(p1).subtract(p2);
                if (v.length() <= 50) {
                    mGhostHide = true;
                    g.getMap().getFirstObjectByClass(BathroomBrokenLight.class).finish();
                    
                    o.setInputEnable();
                    o.setTurnOffLight();
                }
            }
        }
        
        mAnimDuration += delta;
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        if (Application.DEBUG) {
            g2d.setColor(Color.RED);
            g2d.drawPolygon(mCollider.toAWTPolygon());
        }
        
        if (mGhostHide) {
            return;
        }
        
        SpriteResource.Frame f = getCurrentSpriteFrame(g, delta);
        Point2D p = mCollider.getPosition();
        
        int x = (int) (p.getX() - f.getWidth() / 2);
        int y = (int) (p.getY() - f.getHeight() + ((Circle) mCollider).getRadius());
        
        g2d.drawImage(f.getImage(), x, y, null);
    }
}
