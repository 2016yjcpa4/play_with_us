package kr.ac.yeungin.cpa.java4.play_with_us;

import kr.ac.yeungin.cpa.java4.GameLoop;
import java.awt.Graphics2D;

public interface DrawableObject {
    
    void update(GameLoop g);

    void draw(GameLoop g, Graphics2D g2d);
}
