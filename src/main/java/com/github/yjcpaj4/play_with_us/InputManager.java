package com.github.yjcpaj4.play_with_us;

import java.util.HashMap; 
import java.util.Map;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    private InputQueue mMouseQueue = new InputQueue();
    private InputQueue mKeyboardQueue = new InputQueue();
    
    public boolean isKeyPressed(int k) {
        return mKeyboardQueue.isPressed(k);
    }
    
    public boolean isKeyDown(int k) {
        return mKeyboardQueue.isDown(k);
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
        mKeyboardQueue.add(k, InputEvent.PRESSED);
    }
    
    public void setKeyRelease(int k) {
        mKeyboardQueue.add(k, InputEvent.RELEASED);
    }
    
    public void setMousePress(int k) {
        mMouseQueue.add(k, InputEvent.PRESSED);
    }
    
    public void setMouseRelease(int k) {
        mMouseQueue.add(k, InputEvent.RELEASED);
    }
    
    public void setMousePosition(int x, int y) {
        mMousePos.set(x, y);
    }
    
    public Point2D getMousePosition() {
        return mMousePos;
    }
    
    public void poll() {
        mMouseQueue.poll();
        mKeyboardQueue.poll();
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
        private int[] mBuffer = new int[256];
        
        public InputQueue() {
        }
        
        public void add(int c, int t) {
            add(new InputEvent(c, t));
        }
        
        public void add(InputEvent e) {
            mBuffer[ e.getCode() ] = e.getType();
        }
        
        public void poll() {
            for (int k = 0; k < mBuffer.length; ++k) {
                int v = mBuffer[k];
                
                if (v == InputEvent.RELEASED) {
                    mEvents.put(k, InputEvent.RELEASED);
                }
                else if (v == InputEvent.PRESSED) {
                    
                    if ( ! mEvents.containsKey(k) || mEvents.get(k) == InputEvent.RELEASED) {
                        mEvents.put(k, InputEvent.ONCE);
                    }
                    else {
                        mEvents.put(k, InputEvent.PRESSED);
                    }
                }
            }
        }
        
        public boolean isDown(int k) {
            return mEvents.containsKey(k) && mEvents.get(k) == InputEvent.ONCE;
        }
        
        public boolean isPressed(int k) {
            return mEvents.containsKey(k) && mEvents.get(k) == InputEvent.PRESSED;
        }
        
        public boolean isReleased(int k) {
            return ! mEvents.containsKey(k) || mEvents.get(k) == InputEvent.RELEASED;
        }
    }
}