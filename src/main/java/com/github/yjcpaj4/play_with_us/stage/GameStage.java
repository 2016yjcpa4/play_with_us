package com.github.yjcpaj4.play_with_us.stage;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.Stage;
import com.github.yjcpaj4.play_with_us.map.Map;
import com.github.yjcpaj4.play_with_us.map.Player;
import com.github.yjcpaj4.play_with_us.resource.ResourceManager;
import java.awt.Graphics2D;

public class GameStage extends Stage {
    
    private Player mPlayer;
    private ResourceManager mRes = ResourceManager.getInstance(); 
    
    public GameStage(Application c) {
        super(c);
        
        mPlayer = new Player();
        
        Map m = new Map();
        m.addObject(mPlayer);
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        Map m = mPlayer.getMap();
        m.update(delta);
        m.draw(delta, g2d);
    }
}
