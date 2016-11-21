package com.github.yjcpaj4.play_with_us.layer;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.Layer;
import com.github.yjcpaj4.play_with_us.map.Stage;
import com.github.yjcpaj4.play_with_us.map.Player;
import com.github.yjcpaj4.play_with_us.resource.StageResource;
import java.awt.Graphics2D;

public class GameLayer extends Layer {
    
    private Player mPlayer;
    
    public GameLayer(Application c) { 
        super(c);

        /*
         * 게임은 플레이어의 중심으로 돌아가기때문에
         * 맵이아닌 플레이어와 맵을 생성하고 draw 시 플레이어가 속한 맵을 draw 합니다.
         */
        
        StageResource r = StageResource.loadFromJSON("res/map.debug.json");
        Stage m = r.toStage();
        if (r.hasPlayerSpawn()) {
            mPlayer = new Player(r.getPlayerSpwan());
            m.addObject(mPlayer);
        }
    }
    
    public Player getPlayer() {
        return mPlayer;
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        Stage m = mPlayer.getStage();
        m.update(this, delta);
        m.draw(this, delta, g2d);
    }
}
