package com.github.yjcpaj4.play_with_us.game;

import com.github.yjcpaj4.play_with_us.game.Map;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import java.awt.Graphics2D;

public abstract class SpecialGameObject extends GameObject {
    
    private boolean mRunning = false;
    
    public boolean isRunning() {
        return mRunning;
    }
    
    public  void pause(GameLayer g) {
        mRunning = false;
    }
    
    public void resume(GameLayer g) {
        mRunning = true;
    }
}
