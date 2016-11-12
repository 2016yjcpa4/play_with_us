package com.github.yjcpaj4.play_with_us.tool;

import com.github.yjcpaj4.play_with_us.GraphicLooper;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.math.Point2D;
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
    private boolean mReversed = false;
    
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
        f.getContentPane().add(mCanvas);
        f.setVisible(true);
        
        start();
    }
    
    private List<Point2D> getPoints() {
        if ( ! mReversed) {
            return mPoint;
        }
        
        Point2D p = mPoint.get(0); // 첫번째껄 가져와서

        // 시계방향으로 사각형 포인팅을 추가하면 폴리곤이 반대로 먹게됨.
        List<Point2D> l = new ArrayList(mPoint);
        l.add(p);
        l.add(new Point2D(p.getX(),          0));
        l.add(new Point2D(mImage.getWidth(), 0));
        l.add(new Point2D(mImage.getWidth(), mImage.getHeight()));
        l.add(new Point2D(0,                 mImage.getHeight()));
        l.add(new Point2D(0,                 0));
        l.add(new Point2D(p.getX(),          0));
        
        return l;
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        g2d.drawImage(mImage, 0, 0, null);
        
        g2d.setColor(new Color(255, 0, 0, (int) (255 * 0.5))); 
        g2d.fillPolygon(new Polygon(getPoints()).toAWTPolygon()); 
        
        g2d.setColor(Color.YELLOW);
        for(int n = 0; n < mPoint.size(); ++n) { 
            Point2D p = mPoint.get(n);

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
}
