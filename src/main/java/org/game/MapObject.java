package org.game;

import org.game.GameLoop;
import java.awt.Graphics2D;
import org.game.map.Map;

public interface MapObject {
    
    void setMap(Map m);
    
    Map getMap();
    
    void update(GameLoop g);

    void draw(GameLoop g, Graphics2D g2d);
}
