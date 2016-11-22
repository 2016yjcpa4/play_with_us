package com.github.yjcpaj4.play_with_us.layer;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.Layer;
import com.github.yjcpaj4.play_with_us.map.Map;
import com.github.yjcpaj4.play_with_us.map.Player;
import com.github.yjcpaj4.play_with_us.map.Portal;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.resource.MapResource;
import java.awt.Graphics2D;

public class GameLayer extends Layer {
    
    public static final float ZOOM = 1.75f;
    
    private Player mPlayer;
    private Vector2D mCameraPos = new Vector2D();
    
    public GameLayer(Application c) {
        super(c);

        /*
         * 게임은 플레이어의 중심으로 돌아가기때문에
         * 맵이아닌 플레이어와 맵을 생성하고 draw 시 플레이어가 속한 맵을 draw 합니다.
         */
        
        MapResource r = getResource().getMap("kitchen");
        Map m = r.toMap();
        if (r.hasPlayerSpawn()) {
            mPlayer = new Player(r.getPlayerSpwan());
            m.addObject(mPlayer);
        }
    }
    
    public Vector2D getCamera() {
        return mCameraPos;
    }
    
    public Player getPlayer() {
        return mPlayer;
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);

        // 항상 사용자 위치를 기준으로
        Point2D p = mPlayer.getPosition();
        
        // 카메라가 이동
        float x = (p.getX() * ZOOM - getContext().getWidth() / 2);
        float y = (p.getY() * ZOOM - getContext().getHeight() / 2);
        mCameraPos.set(x, y);
        g2d.translate(-mCameraPos.getX(), -mCameraPos.getY());
        g2d.scale(ZOOM, ZOOM);

        Map m = mPlayer.getMap();
        m.update(this, delta);
        m.draw(this, delta, g2d);
    }
}
