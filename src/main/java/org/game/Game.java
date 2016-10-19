package org.game;

import org.game.GameLoop;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import org.game.map.Light;
import org.game.map.Map;
import org.game.map.Player;
import org.game.math.Vector2D;

public class Game extends GameLoop implements MouseMotionListener, MouseListener, KeyListener {

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
        switch (e.getKeyCode()) 
        {
            case KeyEvent.VK_W:
            case KeyEvent.VK_S:
            case KeyEvent.VK_A:
            case KeyEvent.VK_D:
                mInput.put(e.getKeyCode(), InputManager.KeyState.PRESSED);
                break;
            case KeyEvent.VK_F:
                Light l = mMap.getPlayer().getLight();
                if(l.isTurnOff()) {
                    l.setTurnOn();
                } else {
                    l.setTurnOff();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) 
        {
            case KeyEvent.VK_W: 
            case KeyEvent.VK_S: 
            case KeyEvent.VK_A: 
            case KeyEvent.VK_D: 
                mInput.put(e.getKeyCode(), InputManager.KeyState.RELEASED);
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Vector2D v = mMap.getPlayer().getDirection();
        
        v.setX(e.getX());
        v.setY(e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3)
        {
            Light l = mMap.getPlayer().getLight();
            l.setTurnOn();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3)
        {
            Light l = mMap.getPlayer().getLight();
            l.setTurnOff();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}  