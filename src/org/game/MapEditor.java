package org.game;

import org.game.map.Map;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MapEditor extends CanvasView implements MouseMotionListener, KeyListener {

    public static final boolean DEBUG = false;
            
    public Map map;

    public MapEditor() { 
        map = new Map();
        
        canvas.addMouseMotionListener(this);
        canvas.addKeyListener(this);
    }
    
    @Override
    protected void draw(Graphics2D g2d) {  
        super.draw(g2d);
         
        map.draw(this, g2d); 
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) { 
    }
}  