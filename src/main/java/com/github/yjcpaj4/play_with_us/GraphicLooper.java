package com.github.yjcpaj4.play_with_us;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

public class GraphicLooper implements Runnable {

    private static final int FPS = 30;
    private static final int FRAME_DELAY = 1000 / FPS;

    private boolean mRunning = false;
    private volatile boolean mPaused = false;
    private final Object mPauseLock = new Object();
    
    private long mClock;
    
    private Thread mThread;
    
    protected Canvas mCanvas = new Canvas();

    public GraphicLooper() {
        /**
         * 외부에서 스레드를 직접적으로 제어하지 않게 막기위해 
         * 상속이아닌 멤버변수로 할당함
         */
        mThread = new Thread(this);
        mThread.setName(getClass().getSimpleName());
    }

    public Canvas getCanvas() {
        return mCanvas;
    }
    
    public int getWidth() {
        return mCanvas.getWidth();
    }
    
    public int getHeight() {
        return mCanvas.getHeight();
    }
 
    public void start() {
        mRunning = true;
        
        mThread.start();
    }
    
    public void stop() {
        mRunning = false;
        
        mThread.interrupt();
    }

    public void pause() {
        mPaused = true;
    }

    public void resume() {
        synchronized (mPauseLock) {
            mClock = System.currentTimeMillis();
        
            mPaused = false;
            mPauseLock.notifyAll();
        }
    }
    
    @Override
    public void run() {
        mClock = System.currentTimeMillis();

        mCanvas.createBufferStrategy(2);
        BufferStrategy bs = mCanvas.getBufferStrategy();

        LOOP : while (mRunning) {
            
            synchronized (mPauseLock) {
                
                // pause 가 걸린상태면 계속 무한반복...
                while (mPaused) {
                    try {
                        // pause 되면 lock 변수를 이용해 wait 시켜 현재 스레드를 대기상태로 만듭니다.
                        mPauseLock.wait();
                    } 
                    catch (InterruptedException e) {
                        break LOOP;
                    }
                    
                    // 만약 현재 pause 걸려있는 상태에서 외부에서 stop 을 한경우 스레드를 종료시킵니다.
                    if ( ! mRunning) {
                        break LOOP;
                    }
                }
            }

            mClock += FRAME_DELAY;
            
            long delta = mClock - System.currentTimeMillis();
            
            update(Math.abs(delta), bs);
            
            try {
                Thread.sleep(Math.max(0, delta));
            } 
            catch (InterruptedException e) {
                break;
            }
        }
    }

    private void update(long delta, BufferStrategy bs) {
        do {
            do {
                Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
                draw(delta, g2d);
                g2d.dispose();
            } 
            while (bs.contentsRestored());

            bs.show();
        }
        while (bs.contentsLost());
    }
    
    protected void draw(long delta, Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 안티앨리어싱
        
        g2d.clearRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
    }
}  