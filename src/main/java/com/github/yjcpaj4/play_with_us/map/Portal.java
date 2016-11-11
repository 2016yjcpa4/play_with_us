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
public class Portal extends GameObject {
    
    private final String mID;
    private final String mLinkID;
    private final String mMapID;
    
    private Circle mCollider;
    
    private boolean mLocked = false;
    
    public Portal(String s1, String s2, String s3) {
        mID = s1;
        
        mMapID = s2;
        mLinkID = s3;
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        
        if (CollisionDetection.isCollides(g.getPlayer().getCollider(), mCollider)) { 
            
            
        }
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
    }
}