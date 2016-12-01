package com.github.yjcpaj4.play_with_us.layer;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.Layer;
import com.github.yjcpaj4.play_with_us.game.Camera;
import com.github.yjcpaj4.play_with_us.game.Map;
import com.github.yjcpaj4.play_with_us.game.object.LightLeakingShoeCloset;
import com.github.yjcpaj4.play_with_us.game.object.Player;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.resource.MapResource;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
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
    
    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        mCamera.update(mPlayer.getPosition());
        mCamera.draw(g2d);

        Map m = mPlayer.getMap();
        m.update(this, delta);
        m.draw(this, delta, g2d);
        
        if (getInput().isKeyOnce(KeyEvent.VK_F1)) {
            showLayer(new HelperLayer(getContext()));
            return;
        }
        
        g2d.setFont(new Font("굴림", Font.BOLD, 13));
        g2d.setColor(Color.WHITE);

        Point2D p = mCamera.getPosition();
        int w = mCamera.getWidth();
        int h = mCamera.getHeight();
        int x = (int) p.getX();
        int y = (int) p.getY();

        FontMetrics fm = g2d.getFontMetrics();

        g2d.drawString("도움말 F1", x + 10, y + fm.getHeight() + 10);
        
        if ( ! mMessages.isEmpty()) {
            
            for (String s : mMessages) {
                g2d.drawString(s, 
                               x - fm.stringWidth(s) + w - 10, 
                               y + fm.getHeight() + 10);
                
                y += (fm.getHeight() + 3);
            }
        }
    }
}

class HelperLayer extends Layer {
    
    private BufferedImage mImage;
    
    public HelperLayer(Application c) {
        super(c);
        
        //mImage = getResource().getImage("img.help");
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        if (getInput().isKeyOnce(KeyEvent.VK_ESCAPE)) {
            finishLayer();
            return;
        }
        
        //g2d.drawImage(mImage, 0, 0, mImage.getWidth(), mImage.getHeight(), null);
    }
}
