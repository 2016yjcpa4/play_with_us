package org.game;

import org.game.GraphicLooper;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import org.game.map.Map;

// http://stackoverflow.com/questions/16758346/how-pause-and-then-resume-a-thread
public class Game extends GraphicLooper implements MouseMotionListener, MouseListener, KeyListener {

    public static final boolean DEBUG = true;
               
    private Map mMap;
    private InputManager mInput = new InputManager();

    public Game() { 
        mCanvas.addMouseListener(this);
        mCanvas.addMouseMotionListener(this);
        mCanvas.addKeyListener(this);
        
        mMap = new Map();
    }
    
    @Override
    protected void draw(Graphics2D g2d) {  
        super.draw(g2d);
          
        mMap.update(this);
        mMap.draw(this, g2d);
    } 
    
    public InputManager getInputManager() {
        return mInput;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        mInput.setKeyPress(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        mInput.setKeyRelease(e.getKeyCode());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mInput.setMousePosition(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mInput.setMousePress(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mInput.setMouseRelease(e.getButton());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}  