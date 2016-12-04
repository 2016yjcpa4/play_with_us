package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import java.awt.Graphics2D;

public class BathroomWaterDrop extends GameObject {
    
    private boolean mPlaySound = false;
    
    @Override
    public void update(GameLayer g, long delta) {
        if (g.getMap() != getMap()) {
            mPlaySound = false;
            g.getResource().getSound("snd.bgm.bathroom.water_drop").stop();
        }
        else if ( ! mPlaySound) {
            g.getResource().getSound("snd.bgm.bathroom.water_drop").play(-1);
            mPlaySound = true;
        } 
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
    }
}
