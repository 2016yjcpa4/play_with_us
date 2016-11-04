package com.github.yjcpaj4.play_with_us;

import java.util.HashMap; 
import java.util.Map;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Input(키보드, 마우스) 를 관리하는 클래스.
 * 
 * @author 차명도.
 */
public class InputManager implements MouseMotionListener, MouseListener, KeyListener {
    
    private static InputManager INSTANCE;
    
    private InputManager() {
    }
    
    public static InputManager getInstance() {
        if (INSTANCE == null) {
            synchronized(InputManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new InputManager();
                }
            }  
        }
        
        return INSTANCE;
    }
    
    private Point2D mMousePos = new Point2D();
    private InputQueue mMouseQueue = new InputQueue(4);
    private InputQueue mKeyboardQueue = new InputQueue(512);
    
    public boolean isKeyPressed(int k) {
        return mKeyboardQueue.isPressed(k);
    }
    
    public boolean isKeyOnce(int k) {
        return mKeyboardQueue.isOnce(k);
    }
    
    public boolean isKeyReleased(int k) {
        return mKeyboardQueue.isReleased(k);
    }
    
    public boolean isMousePressed(int k) {
        return mMouseQueue.isPressed(k);
    }
    
    public boolean isMouseReleased(int k) {
        return mMouseQueue.isReleased(k);
    }
    
    public Point2D getMousePosition() {
        return mMousePos;
    }
    
    public void update() {
        mMouseQueue.update();
        mKeyboardQueue.update();
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        mKeyboardQueue.add(e.getKeyCode(), InputEvent.PRESSED);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        mKeyboardQueue.add(e.getKeyCode(), InputEvent.RELEASED);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mMousePos.set(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mMouseQueue.add(e.getButton(), InputEvent.PRESSED);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mMouseQueue.add(e.getButton(), InputEvent.RELEASED);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    private static class InputEvent {
        
        public static final int PRESSED = 0;
        public static final int RELEASED = 1;
        public static final int ONCE = 2;
        
        private int mCode;
        private int mType;
        
        public InputEvent(int c, int t) {
            mCode = c;
            mType = t;
        }
        
        public int getCode() {
            return mCode;
        }
        
        public int getType() {
            return mType;
        }
    }
    
    private static class InputQueue {
        
        private Map<Integer, Integer> mEvents = new HashMap<>();
        private int[] mBuffer;
        
        public InputQueue(int c) {
            mBuffer = new int[c];
            for (int n = 0; n < mBuffer.length; ++n) {
                mBuffer[n] = InputEvent.RELEASED;
            }
        }
        
        public void add(int c, int t) {
            add(new InputEvent(c, t));
        }
        
        public void add(InputEvent e) {
            mBuffer[ e.getCode() ] = e.getType();
        }
        
        public void update() {
            for (int k = 0; k < mBuffer.length; ++k) {
                int v = mBuffer[k];
                
                if (v == InputEvent.RELEASED) {
                    if (isPressed(k)) { // 이전값이 눌러져있엇다면
                        mEvents.put(k, InputEvent.ONCE);
                    }
                    else {
                        mEvents.put(k, InputEvent.RELEASED);
                    }
                }
                else if (v == InputEvent.PRESSED) {
                    mEvents.put(k, InputEvent.PRESSED);
                }
            }
        }
        
        public boolean isOnce(int k) { 
            return mEvents.containsKey(k) &&  mEvents.get(k) == InputEvent.ONCE;
        }
        
        public boolean isPressed(int k) {
            return mEvents.containsKey(k) && mEvents.get(k) == InputEvent.PRESSED;
        }
        
        public boolean isReleased(int k) {
            return ! mEvents.containsKey(k) || mEvents.get(k) == InputEvent.RELEASED;
        }
    }
}