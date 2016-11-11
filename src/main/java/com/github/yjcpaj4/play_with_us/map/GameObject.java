package com.github.yjcpaj4.play_with_us.map;

import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import java.awt.Graphics2D;

public abstract class GameObject {
    
    protected Map mMap;
    
    public void setMap(Map m) {
        mMap = m;
    }
    
    public Map getMap() {
        return mMap;
    }
    
    public abstract void update(GameLayer g, long delta);

    public abstract void draw(GameLayer g, long delta, Graphics2D g2d);
}
