package org.game.map;

import java.awt.Color;
import java.awt.Graphics2D;
import org.game.Game;
import org.game.GameLoop;
import org.game.geom.Polygon;
import org.game.geom.Rect; 
import org.game.math.Point2D;

public class Wall extends MapObject {
    
    private Polygon mCollider;
    
    public Wall(int x, int y, int w, int h) {
        mCollider = new Rect(x, y, w, h);
    }
    
    public Polygon getCollider() {
        return mCollider;
    }
    
    public Point2D getPosition () {
        return mCollider.getPosition();
    }

    @Override
    public void draw(Game g, Graphics2D g2d) {
    }

    @Override
    public void update(Game g) { 
    }
    
}