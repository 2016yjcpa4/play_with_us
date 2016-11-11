package com.github.yjcpaj4.play_with_us.tool;

import com.github.yjcpaj4.play_with_us.GraphicLooper;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.util.AWTUtil;
import com.github.yjcpaj4.play_with_us.util.ArrayUtil;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class MapEditTool extends GraphicLooper implements MouseListener, KeyListener {
    
    private BufferedImage mImage;
    
    private boolean mReversed = true;
    
    private List<Point2D> mPoint = new ArrayList<>();
    
    public MapEditTool() throws Exception {
        
        mImage = ImageIO.read(new File("res/img_map.png"));
        
        mCanvas.addKeyListener(this);
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

    public Point2D getPoint(int n) {
        return mPoint.get(ArrayUtil.getFixedArrayIndex(n, mPoint.size()));
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        final int OVAL_WIDTH = 6;
        
        g2d.drawImage(mImage, 0, 0, null);
        
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(3));
        
        for(int n = 0; n < mPoint.size(); ++n) {
            Point2D p = getPoint(n);

            g2d.fillOval(p.getX() - (OVAL_WIDTH / 2), p.getY() - (OVAL_WIDTH / 2), OVAL_WIDTH, OVAL_WIDTH);
            
            Point2D p1 = getPoint(n);
            Point2D p2 = getPoint(n + 1);
            g2d.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
        
        if (mReversed) {
            g2d.fillPolygon(new Polygon(mPoint).toAWTPolygon());
        } else {
            Area a = new Area(new Rectangle2D.Float(0, 0, mImage.getWidth(), mImage.getHeight()));
            a.subtract(new Area(new Polygon(mPoint).toAWTPolygon()));
            
            g2d.fillPolygon(AWTUtil.toPolygon(a).toAWTPolygon());
        }
        
        if ( ! mPoint.isEmpty()) {
            Point2D s = mPoint.get(0);
            
            g2d.setColor(Color.WHITE);
            g2d.drawString("시작점", s.getX(), s.getY());
            
            if (mPoint.size() > 1) {
                Point2D e = mPoint.get(mPoint.size() - 1);
                g2d.drawString("끝점", e.getX(), e.getY());
            }
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
        if (e.getButton() == MouseEvent.BUTTON3) {
            mReversed = !mReversed;
            return;
        }
        mPoint.add(new Point2D(e.getX(), e.getY()));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            mPoint.remove(mPoint.size() - 1); // Ctrl + Z 키 눌렀을경우 Un-do 수행
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
