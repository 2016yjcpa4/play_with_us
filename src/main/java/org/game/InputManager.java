package org.game;

import java.util.HashMap; 
import java.util.Map;

public class InputManager {
    
    enum KeyState {
        
        RELEASED,
        PRESSED
    }
    
    private Map<Integer, KeyState> mKeyStates = new HashMap<>();
    
    public boolean isPressed(int k) {
        KeyState o = mKeyStates.get(k);
        
        if (o == null) {
            return false;
        }
        
        return o.equals(KeyState.PRESSED);
    }
    
    public boolean isReleased(int k) {
        KeyState o = mKeyStates.get(k);
        
        if (o == null) {
            return true;
        }
        
        return o.equals(KeyState.RELEASED);
    }
    
    public void put(int k, KeyState o) {
        mKeyStates.put(k, o);
    }
}