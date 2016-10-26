package com.github.yjcpaj4.play_with_us.map;

import java.awt.Graphics2D;
import com.github.yjcpaj4.play_with_us.Game;

public abstract class MapObject {
    
    private Map mMap;
    
    public void setMap(Map m) {
        mMap = m;
    }
    
    public Map getMap() {
        return mMap;
    }
    
    public abstract void update(Game g);

    public abstract void draw(Game g, Graphics2D g2d);
}
