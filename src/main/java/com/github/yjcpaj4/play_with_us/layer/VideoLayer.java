package com.github.yjcpaj4.play_with_us.layer;

import com.github.yjcpaj4.play_with_us.Layer;
import com.github.axet.play.VLC;
import com.github.yjcpaj4.play_with_us.CanvasApplication;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent; 
import java.io.File;

public class VideoLayer extends Layer {
    
    private File mFile;
    private VLC mVLC;
    
    private final KeyListener mKeyListener = new KeyListener();
    private final VideoListener mVideoListener = new VideoListener();
    
    public VideoLayer(CanvasApplication c) {
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
        // 여기는 구현하면 안됨.
    }

    @Override
    protected void pause() {
        super.pause();
        
        mVLC.close();
        mVLC = null;
        
        getApplicationCanvas().removeKeyListener(mKeyListener);
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
