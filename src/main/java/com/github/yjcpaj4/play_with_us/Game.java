package com.github.yjcpaj4.play_with_us;

import com.github.yjcpaj4.play_with_us.resource.ResourceManager;
import com.github.yjcpaj4.play_with_us.resource.ImageResource;
import com.github.yjcpaj4.play_with_us.resource.SpriteImageResource;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import com.github.yjcpaj4.play_with_us.map.Map;

public class Game extends GraphicLooper implements MouseMotionListener, MouseListener, KeyListener {

    public static final boolean DEBUG = true;
               
    private Map mMap;
    private InputManager mInput = InputManager.getInstance();

    public Game() { 
        super();
        
        // 게임은 생성시 바로 일시정지 상태.
        
        pause(); // 이 방법 말고 다른방법도 있을건데
        
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
    
    public InputManager getInput() {
        return mInput;
    }
    
    public BufferedImage getImage(String k) {
        return ((ImageResource) ResourceManager.getInstance().getImage(k)).getImageData();
    }
    
    public SpriteImageResource getSprite(String s) {
        return ((SpriteImageResource) ResourceManager.getInstance().getSprite(s));
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