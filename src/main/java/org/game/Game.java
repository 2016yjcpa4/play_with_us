package org.game;

import org.game.GameLoop;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import kr.ac.yeungin.cpa.java4.play_with_us.object.Player;
import kr.ac.yeungin.cpa.java4.play_with_us.map.Map;
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
import kr.ac.yeungin.cpa.java4.play_with_us.geom.Polygon;
import kr.ac.yeungin.cpa.java4.play_with_us.object.Ghost;

public class Game extends GameLoop implements MouseMotionListener, MouseListener, KeyListener {

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
    
    @Override
    protected void draw(Graphics2D g2d) {  
        super.draw(g2d);
         
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
            map.update(this);
            
            player.update(this);
            player.draw(this, g2d);
            
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("굴림", Font.PLAIN, 40)); 
            g2d.drawString("게임시작", 80, canvas.getHeight() - 190);
            g2d.drawString("게임종료", 80, canvas.getHeight() - 100);
        }
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