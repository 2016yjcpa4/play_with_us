package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.game.object.Light;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import java.awt.Graphics2D;

public class BrokenLight extends GameObject {
    
    public static final int MAX_DURATION = 4000;
    
    private int mFPS;
    
    private long mDuration = 0;
    private long mTotalDuration = 0;
    
    private Runnable mFinishEvent;
    
    public void reset() {
        mDuration = 0;
        mTotalDuration = 0;
    }
    
    public void setFinishEvent(Runnable r) {
        mFinishEvent = r;
    }
    
    public void finish() {
        mTotalDuration = MAX_DURATION;
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        if (g.getMap() != getMap()) {
            g.getPlayer().setLightControllable(true);
            reset();
            return;
        }
        
        if (mTotalDuration > MAX_DURATION) {
            g.getPlayer().setLightControllable(true);
            if (mFinishEvent != null) {
                mFinishEvent.run();
            }
            return;
        }
        
        g.getPlayer().setLightControllable(false);
        
        Light l = g.getPlayer().getOwnedLight();
        boolean b = l.isTurnOn();
        
        if (mDuration >= mFPS) {
            if (b) {
                l.setTurnOff();
            } else {
                l.setTurnOn();
            }
        }
        
        if (b != l.isTurnOn()) {
            mFPS = (int) (Math.random() * 200 + 100);
            mDuration = 0;
        }
        
        mDuration += delta;
        mTotalDuration += delta;
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
    }
}
