package com.github.yjcpaj4.play_with_us.tool;

import com.github.yjcpaj4.play_with_us.GraphicLooper;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class MapEditTool extends GraphicLooper implements MouseListener {
    
    private BufferedImage mImage;
    
    private List<Point2D> mPoint = new ArrayList<>();
    
    public MapEditTool() throws Exception {
        
        mImage = ImageIO.read(new File("res/img_map.png"));
        
        mCanvas.addMouseListener(this);
        mCanvas.setBackground(Color.BLACK);
        
        JFrame f = new JFrame();
        f.setTitle("PLAY with us - Map Edit Tool");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setUndecorated(true);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setLocationRelativeTo(null);        
        f.add(mCanvas, BorderLayout.CENTER);
        f.setVisible(true);
        
        start();
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        g2d.drawImage(mImage, 0, 0, null);
        
        g2d.setColor(Color.RED);

        if (mPoint.size() > 1) {
            g2d.setStroke(new BasicStroke(5));
                
            for(int n = 1; n < mPoint.size(); ++n) {
                Point2D p1 = mPoint.get(n - 1);
                Point2D p2 = mPoint.get(n);

                g2d.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                
                g2d.fillOval(p1.getX() - (10 / 2), p1.getY() - (10 / 2), 10, 10);
                g2d.fillOval(p2.getX() - (10 / 2), p2.getY() - (10 / 2), 10, 10);
            }
        } 
        else if (mPoint.size() == 1) {
            Point2D p = mPoint.get(0);
            
            g2d.fillOval(p.getX() - (10 / 2), p.getY() - (10 / 2), 10, 10);
        }
    }
    
    public static void main(String args[]) throws Exception {
        new MapEditTool();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mPoint.add(new Point2D(e.getX(), e.getY()));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
