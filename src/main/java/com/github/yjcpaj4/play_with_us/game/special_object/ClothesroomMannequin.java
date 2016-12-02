package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import java.awt.Graphics2D;

public class ClothesroomMannequin extends GameObject {
    
    private boolean mSuprise = false;
    
    public void setSuprise() {
        mSuprise = true;
    }
    
    @Override
    public void update(GameLayer g, long delta) {
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
    }
}
