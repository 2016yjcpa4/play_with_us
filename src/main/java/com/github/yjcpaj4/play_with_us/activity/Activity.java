package com.github.yjcpaj4.play_with_us.activity;

import java.awt.Graphics2D;

public abstract class Activity {
    
    public void finish() {
        
    }
    
    public void startActivity() {
        
    }
    
    public abstract void onStart(Param o);
    
    public abstract void onDraw(long delta, Graphics2D g2d);

    public abstract void onPause(); 
}
