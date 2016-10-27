package com.github.yjcpaj4.play_with_us;

import java.util.HashMap; 
import java.util.Map;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

/**
 * Input(키보드, 마우스) 를 관리하는 클래스.
 * 
 * @author 차명도.
 */
public class InputManager {
    
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
    private KeyBoardInput mMouseQueue = new KeyBoardInput();
    private KeyBoardInput mKeyboardQueue = new KeyBoardInput();
    
    public boolean isKeyPressed(int k) {
        return mKeyboardQueue.isPressed(k);
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
    
    public void setKeyPress(int k) {
        mKeyboardQueue.push(k, InputEvent.PRESSED);
    }
    
    public void setKeyRelease(int k) {
        mKeyboardQueue.push(k, InputEvent.RELEASED);
    }
    
    public void setMousePress(int k) {
        mMouseQueue.push(k, InputEvent.PRESSED);
    }
    
    public void setMouseRelease(int k) {
        mMouseQueue.push(k, InputEvent.RELEASED);
    }
    
    public void setMousePosition(int x, int y) {
        mMousePos.set(x, y);
    }
    
    public Point2D getMousePosition() {
        return mMousePos;
    }
    
    public void clear() {
        mMouseQueue.clear();
        mKeyboardQueue.clear();
    }
    
    private static final int PRESSED = 0;
    private static final int RELEASED = 1;
    private static final int ONCE = 2;
    
    private static class KeyBoardInput {
        
        private Integer[] mEvents = new Integer[256];
        private Queue<KeyEvent> mBuffer = new ArrayDeque<>();
        
        public KeyBoardInput() {
            for (int n = 0; n < mEvents.length; ++n) {
                mEvents[n] = -1;
            }
        }
        
        public void push(KeyEvent e) {
            mBuffer.offer(e);
        }
        
        public void update() {
            while (mBuffer.isEmpty()) {
                KeyEvent e = mBuffer.poll();
                
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    mEvents[ e.getKeyCode() ] = PRESSED;
                }
                else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    
                    if (mEvents[ e.getKeyCode() ] == KeyEvent.KEY_RELEASED) {
                        mEvents[ e.getKeyCode() ] = ONCE;
                    }
                    else {
                        mEvents[ e.getKeyCode() ] = RELEASED;
                    }
                }
            }
        }
        
        public void clear() {
            mBuffer.clear();
            
            for (int n = 0; n < mEvents.length; ++n) {
                mEvents[n] = null;
            }
        }
        
        public boolean isPressed(int k) {
            return mEvents[k] != null && mEvents[k].getType() == InputEvent.PRESSED;
        }
        
        public boolean isReleased(int k) {   
            return mEvents[k] == null || mEvents[k].getType() == InputEvent.RELEASED;
        }
    }
}