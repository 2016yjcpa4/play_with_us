package com.github.yjcpaj4.play_with_us.layer;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.Layer;
import com.github.yjcpaj4.play_with_us.game.Map;
import com.github.yjcpaj4.play_with_us.game.object.LightLeakingShoeCloset;
import com.github.yjcpaj4.play_with_us.game.object.Player;
import com.github.yjcpaj4.play_with_us.game.object.Portal;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.resource.MapResource;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class GameLayer extends Layer {
    
    private static final String MAIN_MAP = "map.livingroom";
    
    private Timer mMessageTimer = new Timer();
    private Queue<String> mMessages = new ArrayDeque<>();
    
    private Player mPlayer;

    private Camera mCamera;
    
    public GameLayer(Application c) {
        super(c);

        /*
         * 게임은 플레이어의 중심으로 돌아가기때문에
         * 맵이아닌 플레이어와 맵을 생성하고 draw 시 플레이어가 속한 맵을 draw 합니다.
         */
        
        MapResource r = getResource().getMap(MAIN_MAP);
        Map m = r.toMap();
        
        if (MAIN_MAP.equals("map.livingroom")) {
            m.addObject(new LightLeakingShoeCloset(new Point2D(707, 990)));
        }
        
        if (r.hasPlayerSpawn()) {
            mPlayer = new Player(r.getPlayerSpwan());
            m.addObject(mPlayer);
        }
        
        mCamera = new Camera(c);
    }
    
    public void showMessage(String s) {
        showMessage(s, 2000);
    }
    
    public void showMessage(String s, int n) {
        final TimerTask t = new TimerTask() {
            
            @Override
            public void run() {
                mMessages.poll();
            }
        };
        
        mMessages.add(s);
        mMessageTimer.schedule(t, n);
    }
    
    public Camera getCamera() {
        return mCamera;
    }
    
    public Player getPlayer() {
        return mPlayer;
    }
    
    public Map getMap() {
        return mPlayer.getMap();
    }
    
    private int i ;

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        mCamera.update(mPlayer.getPosition());
        mCamera.draw(g2d);

        Map m = mPlayer.getMap();
        m.update(this, delta);
        m.draw(this, delta, g2d);
        
        if ( ! mMessages.isEmpty()) {

            g2d.setFont(new Font("굴림", Font.BOLD, 13));
            g2d.setColor(Color.WHITE);
            
            Point2D p = mCamera.getPosition();
            int w = mCamera.getWidth();
            int h = mCamera.getHeight();
            int x = (int) p.getX() + w - 10;
            int y = (int) p.getY() + 10;

            FontMetrics fm = g2d.getFontMetrics();

            for (String s : mMessages) {
                g2d.drawString(s, 
                               x - fm.stringWidth(s), 
                               y += (fm.getHeight() + 3));
            }
        }
        
    }
    
    public static class Camera {

        private Point2D mPos;
        private Application mContext;
        private float mZoom = 1.65f;

        public Camera(Application c) {
            mContext = c;
        }
        
        public int getWidth() {
            return (int) (mContext.getWidth() / mZoom);
        }
        
        public int getHeight() {
            return (int) (mContext.getHeight() / mZoom);
        }
        
        public float getZoom() {
            return mZoom;
        }
        
        public void update(Point2D p) {
            mPos = p;
        }
        
        public void draw(Graphics2D g2d) {
            float x = mPos.getX() * mZoom - mContext.getWidth() / 2;
            float y = mPos.getY() * mZoom - mContext.getHeight() / 2;
            
            g2d.translate(-x, -y);
            g2d.scale(mZoom, mZoom); 
        }
        
        public Point2D getPosition() {
            float x = mPos.getX() - getWidth() / 2;
            float y = mPos.getY() - getHeight() / 2;
            
            return new Point2D(x, y);
        }
    }
}
