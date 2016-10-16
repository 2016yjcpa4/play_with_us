package org.game;

import com.github.axet.play.VLC;
import java.awt.Canvas;
import java.awt.Graphics;
import java.io.File;
import javax.swing.SwingUtilities;

public class Video {
    
    private VLC v = new VLC();
    private Canvas c = new Canvas() {
        
        private boolean isPainted = false;
        
        @Override
        public void paint(Graphics g) {
            super.paint(g); //To change body of generated methods, choose Tools | Templates.
            
            if ( ! isPainted) {
                v.setVideoCanvas(this);
                isPainted = true;
            }
        }
    };
    
    public Video() {
    }
    
    public Canvas getCanvas() {
        return c;
    }
    
    public void addListener(VLC.Listener l) {
        v.addListener(l);
    }
    
    public void open(File f) {
        v.open(f);
    }
    
    public void play() {
        v.play();
    } 
    
    public void stop() {
        v.stop();
    } 
    
    public void close() {
        v.close();
    }
}
