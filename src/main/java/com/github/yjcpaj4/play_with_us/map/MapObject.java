package com.github.yjcpaj4.play_with_us.map;

import java.awt.Graphics2D;

public abstract class MapObject {
    
    private Map mMap;
    
    public void setMap(Map m) {
        mMap = m;
    }
    
    public Map getMap() {
        return mMap;
    }
    
    public abstract void update(long delta);

    public abstract void draw(long delta, Graphics2D g2d);
}
