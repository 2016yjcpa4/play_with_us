package com.github.yjcpaj4.play_with_us.stage;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.Stage;
import com.github.yjcpaj4.play_with_us.map.Map;
import com.github.yjcpaj4.play_with_us.resource.ResourceManager;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class GameStage extends Stage {
    
    private Map mMap;
    private boolean mIsGameOver = false;
    private ResourceManager mRes = ResourceManager.getInstance();
    
    public GameStage(Application c) {
        super(c);
        mMap = new Map();
    }
    
    public void setGameOver() {
        mIsGameOver = true;
    }
    
    public boolean isGameOver() {
        return mIsGameOver;
    }
    
    @Override
    protected void draw(long delta, Graphics2D g2d) {
        mMap.update(delta);
        mMap.draw(delta, g2d);
    }

    @Override
    protected void stop() {
        
    }

    @Override
    protected void init() {
        try {
            mRes.load("res/img_map.png",    "map");
            mRes.load("res/img_player.png", "player.walk.n");
            mRes.load("res/img_player.png", "player.walk.ne");
            mRes.load("res/img_player.png", "player.walk.e");
            mRes.load("res/img_player.png", "player.walk.se");
            mRes.load("res/img_player.png", "player.walk.s");
            mRes.load("res/img_player.png", "player.walk.sw");
            mRes.load("res/img_player.png", "player.walk.w");
            mRes.load("res/img_player.png", "player.walk.nw");
        }
        catch(IOException e) {
            e.printStackTrace();
            finish();
        }   
    }
}
