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

        /*
         * 게임은 플레이어의 중심으로 돌아가기때문에
         * 맵이아닌 플레이어와 맵을 생성하고 draw 시 플레이어가 속한 맵을 draw 합니다.
         */
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
