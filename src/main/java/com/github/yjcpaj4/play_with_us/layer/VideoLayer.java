package com.github.yjcpaj4.play_with_us.layer;

import com.github.yjcpaj4.play_with_us.Layer;
import com.github.axet.play.VLC;
import com.github.yjcpaj4.play_with_us.Application;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent; 
import java.io.File;

public class VideoLayer extends Layer {
    
    private File mFile;
    private VLC mVLC;
    
    private final KeyListener mKeyListener = new KeyListener();
    private final VideoListener mVideoListener = new VideoListener();
    
    public VideoLayer(Application c) {
        super(c);
    }
    
    public void load(File f) {
        mFile = f;
    }

    @Override
    protected void resume() {
        super.resume();
        
        if (mVLC != null) {
            mVLC.close();
            mVLC = null;
        }

        mVLC = new VLC();
        mVLC.setVideoCanvas(getApplicationCanvas());
        mVLC.addListener(mVideoListener);
        mVLC.open(mFile);
        mVLC.play();
        
        getApplicationCanvas().addKeyListener(mKeyListener);
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        // 여기는 구현하면 안됨.
    }

    @Override
    protected void pause() {
        super.pause();
        
        /*
         * VLC.Listener 에서 stop 후 pause 로 넘어올경우 
         * mVLC.close(); 호출이 되어야하는데 호출이 되지않습니다.
         * 아래처럼 별도의 Thread 를 생성하여 함수호출할시 해결은 되었습니다.
         * 원인은 파악하지 못하여 차후 원인을 밝혀야합니다.
         */
        
        final Runnable r = new Runnable() {
            
            @Override
            public void run() {
                 
                if (mVLC != null) {
                    mVLC.close();
                    mVLC = null;
                }
        
                getApplicationCanvas().removeKeyListener(mKeyListener);
            }
        };
        
        new Thread(r).start();
    }
    
    private class KeyListener implements java.awt.event.KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                showLayer(GameLayer.class);
                
                finishLayer();
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
            showLayer(GameLayer.class);
            
            finishLayer();
        }

        @Override
        public void position(float n) {
            
        }
    }
}
