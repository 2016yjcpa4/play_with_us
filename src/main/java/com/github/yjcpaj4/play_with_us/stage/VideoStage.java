package com.github.yjcpaj4.play_with_us.stage;

import com.github.yjcpaj4.play_with_us.Stage;
import com.github.axet.play.VLC;
import com.github.yjcpaj4.play_with_us.Application;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent; 
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class VideoStage extends Stage {
    
    private final VLC mVLC;
    
    private final KeyListener mKeyListener = new KeyListener();
    private final VideoListener mVideoListener = new VideoListener();
    
    public VideoStage(Application ctx) {
        super(ctx);
        
        mVLC = new VLC();
        mVLC.addListener(mVideoListener);
    }
    
    public void load(File f) {
        mVLC.setVideoCanvas(getCanvas());
        mVLC.open(f);
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        // 여기는 구현하면 안됨.
    }

    @Override
    protected void stop() {
        mVLC.close();
        
        getCanvas().removeKeyListener(mKeyListener);
    }

    @Override
    protected void init() {
        getCanvas().addKeyListener(mKeyListener);
        
        mVLC.play();
    }
    
    private void startGame() {
        GameStage s = Application.getStage(GameStage.class);
        s.init();
        startScene(s);
        
        finish();
    }
    
    private class KeyListener implements java.awt.event.KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                startGame();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        } 
    }
    
    private class VideoListener implements VLC.Listener {

        @Override
        public void start() {
            
        }

        @Override
        public void stop() {
            startGame();
        }

        @Override
        public void position(float n) {
            
        }
    }
}
