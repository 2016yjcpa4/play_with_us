package com.github.yjcpaj4.play_with_us.stage;

import com.github.yjcpaj4.play_with_us.Stage;
import com.github.axet.play.VLC;
import com.github.yjcpaj4.play_with_us.Application;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent; 
import java.io.File;

public class VideoStage extends Stage {
    
    private VLC mVLC;
    
    private final KeyListener mKeyListener = new KeyListener();
    private final VideoListener mVideoListener = new VideoListener();
    
    public VideoStage(Application c) {
        super(c);
    }
    
    public void load(File f) {
        if (mVLC != null) {
            mVLC.close();
            mVLC = null;
        }

        // 간혹 생성시 오류가 발생합니다.
        mVLC = new VLC();
        mVLC.setVideoCanvas(getCanvas());
        mVLC.open(f);
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        // 여기는 구현하면 안됨.
    }

    @Override
    protected void finish() {
        mVLC.close();
        mVLC = null;
        
        getCanvas().removeKeyListener(mKeyListener);
    }

    @Override
    protected void init() {        
        mVLC.addListener(mVideoListener);
        mVLC.play();
        
        getCanvas().addKeyListener(mKeyListener);
    }
    
    private class KeyListener implements java.awt.event.KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                showStage(GameStage.class);
                
                stopStage();
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
            showStage(GameStage.class);
            
            stopStage();
        }

        @Override
        public void position(float n) {
            
        }
    }
}
