package org.game;

import java.util.HashMap; 
import java.util.Map;
import org.game.math.Point2D;

/**
 * Input(키보드, 마우스) 를 관리하는 클래스.
 * 
 * @author 차명도.
 */
public class InputManager {
    
    enum State {
        
        RELEASED,
        PRESSED
    }
     
    private Point2D mMousePos = new Point2D();
    private Map<Integer, State> mMouseStates = new HashMap<>();
    private Map<Integer, State> mKeyStates = new HashMap<>();
    
    public boolean isKeyReleased(int k) {
        return !mKeyStates.containsKey(k) || mKeyStates.get(k).equals(State.RELEASED);
    }
    
    public boolean isKeyPressed(int k) {
        return mKeyStates.containsKey(k) && mKeyStates.get(k).equals(State.PRESSED);
    }
    
    public boolean isMouseReleased(int k) {
        return !mMouseStates.containsKey(k) || mMouseStates.get(k).equals(State.RELEASED);
    }
    
    public boolean isMousePressed(int k) {
        return mMouseStates.containsKey(k) && mMouseStates.get(k).equals(State.PRESSED);
    }
    
    public void setMousePress(int k) {
        mMouseStates.put(k, State.PRESSED);
    }
    
    public void setMouseRelease(int k) {
        mMouseStates.put(k, State.RELEASED);
    }
    
    public void setKeyPress(int k) {
        mKeyStates.put(k, State.PRESSED);
    }
    
    public void setKeyRelease(int k) {
        mKeyStates.put(k, State.RELEASED);
    }
    
    public void setMousePosition(int x, int y) {
        mMousePos.set(x, y);
    }
    
    public Point2D getMousePosition() {
        return mMousePos;
    }
}