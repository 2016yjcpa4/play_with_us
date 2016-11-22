package com.github.yjcpaj4.play_with_us.map;

import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import java.awt.Graphics2D;
import java.util.List; 

import com.github.yjcpaj4.play_with_us.ResourceManager;
import com.github.yjcpaj4.play_with_us.resource.SpriteResource;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.util.MathUtil;

public class Mob extends PhysicsObject {
    
    private Vector2D mDir = new Vector2D();
    private Point2D mPos = new Point2D(50, 50); 
    private Vector2D mVel = new Vector2D(0, 0);
    private int mSpeed = 3;
    
    public Mob(int x, int y) {
        mPos.set(x, y);
    }
    
    public Point2D getPosition() {
        return mPos;
    }
    
    public double getAngle() {
        return mDir.subtract(getPosition()).toAngle();
    }
    
    public double getDistanceToPlayer() {
        return new Vector2D(mPos).subtract(getMap().getPlayer().getPosition()).length();
    }
    
    /**
     * 현재 플레이어의 이미지를 가져옵니다.
     * 
     * 걷고, 서있고... 등등?
     * 
     * @return 프레임을 반환합니다.
     */
    private SpriteResource.Frame getCurrentSpriteFrame(ResourceManager r, long d) {
        String k = String.join(".", "player", "walk", MathUtil.getDirectionByRadian(getAngle()));
        
        SpriteResource.Frame f = r.getSprite(k).getFrame(2); // 기본 상태
        
        if (mVel.getX() != 0 || mVel.getY() != 0) { // 움직임이 발생하면
            f = r.getSprite(k).getCurrentFrame(d); // 델타값을 넣어 현재 프레임을 뽑아옴
        }
        
        // TODO ... 걷고, 서있는것 이외에 무언가 처리해야할게 있는가???
        
        return f;
    }
    
    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) { 
        SpriteResource.Frame f = getCurrentSpriteFrame(g.getResource(), delta);
        Point2D p = getPosition();
        
        g2d.drawImage(f.getImage(), 
                      (int)p.getX() - f.getWidth() / 2, 
                      (int)p.getY() - f.getHeight() / 2, 
                      null);
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        /**
         * 플레이어와의 거리를 직선거리로 판정하고 있습니다
         * 플레이어와의 직선거리가 아닌 걸음수로 판정해야 할것같습니다
         * 걸음수는 AStar 알고리즘으로 나온 리스트 크기로 판별하면 될것입니다
         */
        
        /*
        boolean isNearPlayer = getDistanceToPlayer() < 200;
        boolean isLightProjected = false;
        
        mVel.setX(0);
        mVel.setY(0);
        
        if (isNearPlayer) { // 플레이어가 근처에 있는경우
            mVel.setX(mSpeed);
            mVel.setY(mSpeed); 
        }
        
        if (isLightProjected || (isNearPlayer && getMap().getPlayer().isTurnOnLight())) { // 플레이어가 근처에있으면서 플래시가 켜진 경우
            mVel.setX(mSpeed * 2);
            mVel.setY(mSpeed * 2); 
        }
        
        if (mVel.getX() != 0 && mVel.getY() != 0) {
            
            mDir.set(getMap().getPlayer().getPosition());
            
            List<Point2D> l = getMap().getPath(mPos, getMap().getPlayer().getPosition());
            
            if ( ! l.isEmpty()) {
                
                double n = new Vector2D(l.get(0)).sub(mPos).angle();
                int x = (int) (mPos.getX() + mVel.getX() * Math.cos(n));
                int y = (int) (mPos.getY() + mVel.getY() * Math.sin(n));
                
                mPos.setX(x);
                mPos.setY(y);
            }
        }*/
    }
}
