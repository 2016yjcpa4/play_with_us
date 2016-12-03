package com.github.yjcpaj4.play_with_us.game.object;
 
import com.github.yjcpaj4.play_with_us.game.LightWithGameObject;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.InputManager;
import com.github.yjcpaj4.play_with_us.ResourceManager;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.game.object.Light;
import com.github.yjcpaj4.play_with_us.game.object.NotWalkable;
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
import com.github.yjcpaj4.play_with_us.util.MathUtil;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
 
public class Ghost extends GameObject  {
    
    protected transient Polygon mCollider;
    
    private static final int SPEED = 2;
    
    private long mSpriteDuration = 0;
    
    private long mDuration = 0;
    
    private Vector2D mVel = new Vector2D(SPEED, SPEED);
    private Vector2D mDir = new Vector2D(); 
    
    public Ghost(Point2D p) {
        this(p.getX(), p.getY());
    }
    
    public Ghost(float x, float y) {
        mCollider = new Circle(x, y, 10);
    } 
    
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
    
    private double getAngleWithPlayer(Player p) {
        return new Vector2D(p.getPosition()).subtract(mCollider.getPosition()).toAngle();
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        if (mDuration > 4000) {
            getMap().removeObject(this);
            return;
        }
        
        Point2D p1 = mCollider.getPosition();
        Point2D p2 = g.getPlayer().getPosition();

        List<Point2D> l = g.getMap().getPath(p1, p2);
        if ( ! l.isEmpty()) {
            double n = new Vector2D(l.get(0)).subtract(p1).toAngle();
            double tx = mVel.getX() * Math.cos(n);
            double ty = mVel.getY() * Math.sin(n);

            mCollider.transform(Matrix2D.translate(tx, ty));
        }
        
        mDuration += delta;
    }
    
    /**
     * 현재 플레이어의 이미지를 가져옵니다.
     * 
     * 걷고, 서있고... 등등?
     * 
     * @return 프레임을 반환합니다.
     */
    private SpriteResource.Frame getCurrentSpriteFrame(GameLayer g, long d) {
        List<String> l = new ArrayList<>(4);
        l.add("sprt");
        l.add("ghost");
        l.add("walk");
        l.add(MathUtil.getSimpleDirectionByRadian(getAngleWithPlayer(g.getPlayer())));
        
        SpriteResource r = g.getResource().getSprite(String.join(".", l));
        int n = (int) (mSpriteDuration / r.getFPS() % r.getLength());
        
        mSpriteDuration += d;
        
        return r.getFrame(n);
    }
    
    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) { 
        SpriteResource.Frame f = getCurrentSpriteFrame(g, delta);
        Point2D p = getPosition();
        int x = (int) (p.getX() - f.getWidth() / 2);
        int y = (int) (p.getY() - f.getHeight() + ((Circle) mCollider).getRadius());
        
        g2d.drawImage(f.getImage(), x, y, null);
        
        if (Application.DEBUG) {
            g2d.setColor(Color.RED);
            g2d.drawPolygon(mCollider.toAWTPolygon());
        }
    }
}
