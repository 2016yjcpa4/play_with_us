package org.game;

import com.github.axet.play.VLC;
import java.awt.Canvas;
import java.awt.Graphics;
import java.io.File;
import javax.swing.SwingUtilities;

public class Video {
    
    private VLC mVLC = new VLC();
    private Canvas mCanvas = new Canvas() {
        
        private boolean mIsPaint = false;
        
        @Override
        public void paint(Graphics g) {
            super.paint(g); //To change body of generated methods, choose Tools | Templates.
            
            if ( ! mIsPaint) {
                mVLC.setVideoCanvas(this);
                mIsPaint = true;
            }
        }
    };
    
    public Video() {
    }
    
    public Canvas getCanvas() {
        return mCanvas;
    }
    
    public void addListener(VLC.Listener l) {
        mVLC.addListener(l);
    }
    
    public void open(File f) {
        mVLC.open(f);
    }
    
    public void play() {
        mVLC.play();
    } 
    
    public void stop() {
        mVLC.stop();
    } 
    
    public void close() {
        mVLC.close();
    }
}
