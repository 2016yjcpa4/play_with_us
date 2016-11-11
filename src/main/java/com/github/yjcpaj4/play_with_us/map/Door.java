package com.github.yjcpaj4.play_with_us.map;

import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 * Door.class
 * 
 * @author 차명도.
 */
public class Door extends GameObject {
    
    private boolean mLocked = false;
    private Circle mCollider;
    
    @Override
    public void update(GameLayer g, long delta) {
        if (CollisionDetection.isCollides(g.getPlayer().getCollider(), mCollider)) {
            //g.getInput().isKeyOnce(KeyEvent.VK_F)
        }
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
    }
    
    public static class LockedException extends Exception {
        
    }
}
