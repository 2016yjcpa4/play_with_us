package com.github.yjcpaj4.play_with_us.stage;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.Stage;
import com.github.yjcpaj4.play_with_us.map.Map;
import com.github.yjcpaj4.play_with_us.resource.ResourceManager;
import java.awt.Graphics2D;
import java.io.IOException;

public class GameStage extends Stage {
    
    private Map mMap;
    private boolean mIsGameOver = false;
    private ResourceManager mRes = ResourceManager.getInstance();
    
    public GameStage(Application c) {
        super(c);
        mMap = new Map();
    }

    @Override
    protected void init() {
        super.init(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        mMap.update(delta);
        mMap.draw(delta, g2d);
    }
}
