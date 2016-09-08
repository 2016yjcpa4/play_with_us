package org.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

public class CanvasView extends Thread {

    private static final int FPS = 30;
    private static final int FRAME_DELAY = 1000 / FPS;

    private boolean isRunning = true;
    
    protected Canvas canvas = new Canvas();

    public CanvasView() {
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void run() {
        long delta = System.currentTimeMillis();

        canvas.createBufferStrategy(2);
        BufferStrategy bs = canvas.getBufferStrategy();

        while (isRunning) {
            update(bs);

            delta += FRAME_DELAY;

            try {
                Thread.sleep(Math.max(0, delta - System.currentTimeMillis()));
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