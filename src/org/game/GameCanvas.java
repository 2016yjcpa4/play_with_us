package org.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

public class GameCanvas extends Thread {

    private static final int FPS = 30;
    private static final int FRAME_DELAY = 1000 / FPS;

    private boolean isRunning = false;
    
    protected long delta;
    
    protected Canvas canvas = new Canvas() {
        
        @Override
        public void paint(Graphics g) {
            super.paint(g); //To change body of generated methods, choose Tools | Templates.
        
            if ( ! isRunning) {
                start();
            }
        }
    };

    public GameCanvas() {
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public synchronized void start() {
        isRunning = true;
        
        super.start();
    }

    @Override
    public void run() {
        long n = System.currentTimeMillis();

        canvas.createBufferStrategy(2);
        BufferStrategy bs = canvas.getBufferStrategy();

        while (isRunning) {
            update(bs);

            n += FRAME_DELAY;

            delta = n - System.currentTimeMillis();
            
            try {
                Thread.sleep(Math.max(0, delta));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update(BufferStrategy bs) {
        do {
            do {
                Graphics g = bs.getDrawGraphics();
                draw((Graphics2D) g);
                g.dispose();
            } 
            while (bs.contentsRestored());

            bs.show();
        } 
        while (bs.contentsLost());
    }
    
    protected void draw(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 안티앨리어싱
        
        g2d.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}  