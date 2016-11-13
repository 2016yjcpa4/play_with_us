package com.github.yjcpaj4.play_with_us.map;

import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import java.awt.Graphics2D;

public abstract class GameObject {
    
    private GameMap mMap;
    
    public void setMap(GameMap m) {
        mMap = m;
    }
    
    public GameMap getMap() {
        return mMap;
    }
    
    public abstract void update(GameLayer g, long delta);

    public abstract void draw(GameLayer g, long delta, Graphics2D g2d);
}
