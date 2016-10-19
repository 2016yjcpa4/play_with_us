package org.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

public class GameLoop extends Thread {

    private static final int FPS = 30;
    private static final int FRAME_DELAY = 1000 / FPS;

    private boolean mIsRunning = false;
    
    private long mDelta;
    
    protected Canvas mCanvas = new Canvas() {
        
        @Override
        public void paint(Graphics g) {
            super.paint(g); //To change body of generated methods, choose Tools | Templates.
        
            if ( ! mIsRunning) {
                start();
            }
        }
    };

    public GameLoop() {
    }

    public Canvas getCanvas() {
        return mCanvas;
    }
    
    public long getDelta() {
        return mDelta;
    }

    @Override
    public synchronized void start() {
        mIsRunning = true;
        
        super.start();
    }

    @Override
    public void run() {
        long n = System.currentTimeMillis();

        mCanvas.createBufferStrategy(2);
        BufferStrategy bs = mCanvas.getBufferStrategy();

        while (mIsRunning) {
            update(bs); 

            n += FRAME_DELAY;

            mDelta = n - System.currentTimeMillis();
            
            try {
                Thread.sleep(Math.max(0, mDelta));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update(BufferStrategy bs) {
        do {
            do {
                Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
                draw(g2d);
                g2d.dispose();
            } 
            while (bs.contentsRestored());

            bs.show();
        } 
        while (bs.contentsLost());
    }
    
    protected void draw(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 안티앨리어싱
        
        g2d.clearRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
    }
}  