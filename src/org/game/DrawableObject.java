package org.game;

import java.awt.Graphics2D;

public interface DrawableObject {
    
    void update(GameCanvas g);

    void draw(GameCanvas g, Graphics2D g2d);
}
