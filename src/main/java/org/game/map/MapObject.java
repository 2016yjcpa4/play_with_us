package org.game.map;

import org.game.GameLoop;
import java.awt.Graphics2D;
import org.game.GameLoop;
import org.game.map.Map;
import org.game.math.Point2D;

public abstract class MapObject {
    
    private Map mMap;
    private Point2D mPos = new Point2D();
    
    public void setMap(Map m) {
        mMap = m;
    }
    
    public Map getMap() {
        return mMap;
    }
    
    public Point2D getPosition() {
        return mPos;
    }
    
    abstract void update(GameLoop g);

    abstract void draw(GameLoop g, Graphics2D g2d);
}
