package com.github.yjcpaj4.play_with_us.activity;

import java.awt.Graphics2D;
import java.util.Map;

public abstract class Activity {
    
    private Map<String, Object> mDatas;
    
    public Map<String, Object> getData() {
        return mDatas;
    }
    
    public abstract void init();
    
    public abstract void draw(long delta, Graphics2D g2d);

    public abstract void pause(); 
    
    public abstract void resume();
}
