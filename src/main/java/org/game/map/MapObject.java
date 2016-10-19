package org.game.map;

import java.awt.Graphics2D;
import org.game.Game;
import org.game.math.Point2D;

public abstract class MapObject {
    
    private Map mMap;
    
    public void setMap(Map m) {
        mMap = m;
    }
    
    public Map getMap() {
        return mMap;
    }
    
    abstract void update(Game g);

    abstract void draw(Game g, Graphics2D g2d);
}
