package org.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import org.game.object.Player;
import org.game.map.Map;
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
import org.game.geom.Polygon;

public class Game extends CanvasView implements MouseMotionListener, MouseListener, KeyListener {

    public static final boolean DEBUG = true;
              
    private boolean isGameOver = false; 
    
    private Player player;  
    private Map map;
    
    public boolean w, s, a, d;

    public Game() { 
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addKeyListener(this);
        
        map = new Map();
        player = new Player(map);
        
        map.setPlayer(player);
    }
    
    private List<Polygon> p = Polygon.getSamples();
    
    @Override
    protected void draw(Graphics2D g2d) {  
        super.draw(g2d);
        
        for(Polygon e : p) {
            
            g2d.setColor(Color.red);
            g2d.drawPolygon(e.getXPoints(), e.getYPoints(), e.getXPoints().length);
        }
        
        /*
        if (isGameOver) {
            // TODO 게임오버 처리
            String s = "게임오버";
            
            Font f = new Font("굴림", Font.PLAIN, 70);
            FontMetrics fm = g2d.getFontMetrics(f);
            
            int x = (canvas.getWidth() - fm.stringWidth(s)) / 2;
            int y = ((canvas.getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            
            g2d.setFont(f); 
            g2d.setColor(Color.RED);
            g2d.drawString(s, x, y);
        } 
        else {
            map.draw(this, g2d);
            player.draw(this, g2d);
        }*/
    } 
    
    public void setGameOver() {
        isGameOver = true;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) 
        {
            case KeyEvent.VK_W:
                //player.getVelocity().setY(-4);
                w = true;
                break;
            case KeyEvent.VK_S:
                //player.getVelocity().setY(4);
                s = true;
                break;
            case KeyEvent.VK_A:
                player.getVelocity().setX(-4);
                a = true;
                break;
            case KeyEvent.VK_D:
                //player.getVelocity().setX(4);
                d = true;
                break;
            case KeyEvent.VK_F:
                player.toggleFlash();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) 
        {
            case KeyEvent.VK_W:
                w = false;
                break;
            case KeyEvent.VK_S:
                s = false;
                break;
            case KeyEvent.VK_A:
                a = false;
                break;
            case KeyEvent.VK_D:
                d = false;
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        player.getDirection().setX(e.getX());
        player.getDirection().setY(e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3)
        {
            player.setTurnOnFlash();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3)
        {
            player.setTurnOffFlash();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}  