package com.github.yjcpaj4.play_with_us.tool;

import com.github.yjcpaj4.play_with_us.GraphicLooper;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.resource.StageResource;
import com.github.yjcpaj4.play_with_us.util.AWTUtil;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class MapEditTool extends GraphicLooper implements MouseListener, MouseMotionListener, KeyListener {
    
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private Selection mSelection = new Selection();
    private Point2D mMousePos = new Point2D();
    
    private BufferedImage mImage;
    private StageResource mResource;
    
    public MapEditTool() throws Exception {
        mImage = ImageIO.read(new File("res/img_map.png"));
        
        mCanvas.addMouseMotionListener(this);
        mCanvas.addMouseListener(this);
        mCanvas.addKeyListener(this);
        mCanvas.setBackground(Color.BLACK);
        mCanvas.setFocusable(true);
        
        JFrame f = new JFrame();
        f.setTitle("PLAY with us - Map Edit Tool");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setSize(1280, 800);
        f.setLocationRelativeTo(null);        
        f.getContentPane().add(mCanvas);
        f.setVisible(true);
        
        start();
    }
    
    public class Selection {
        
        private boolean mReversed;
        private List<Point2D> mPoint = new ArrayList<>();
        
        public Polygon toPolygon() {
            return new Polygon(getPoints());
        }
        
        public List<Point2D> getPoints() {
            final List<Point2D> l = new ArrayList(mPoint);
            l.add(mMousePos);
            
            /**
             * 선택 영역을 반전시킵니다.
             * 
             * 이 경우 자바 내장 API 에서 Area 라는 클래스가 존재하는데
             * Area 를 이용하여 다각형과 다각형을 연산할수 있습니다.
             * 현재 아래 소스에서는 이미지크기 만큼의 사각형 도형을 하나 선언하고
             * 선택된 영역을 빼기(subtract)연산 합니다. 그렇게되면 사각형에서 선택영역의 구멍이 뚫린것처럼 나오게됩니다.
             */
            if (mReversed) {
                Area a = new Area(new Rectangle2D.Float(0, 0, mImage.getWidth(), mImage.getHeight()));
                a.subtract(new Area(new Polygon(l).toAWTPolygon()));
                
                Polygon p = new Polygon(AWTUtil.getPoints(a));
                
                a.subtract(new Area(p.toAWTPolygon()));
                
                l.clear();
                l.addAll(p.getPoints());
                l.addAll(AWTUtil.getPoints(a));
                return l;
            }
            
            return l;
        }
        
        public void addPoint(int x, int y) {
            mPoint.add(new Point2D(x, y));
        }
        
        public boolean isEmpty() {
            return getPoints().isEmpty();
        }
        
        public void setReverse() {
            mReversed = !mReversed;
        }
    }
    
    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        g2d.drawImage(mImage, 0, 0, null);
        
        if ( ! mSelection.isEmpty()) {
            g2d.setColor(new Color(34, 181, 0, (int) (255 * 0.5))); 
            for (Polygon p : mSelection.toPolygon().getTriangulate()) {
                g2d.fillPolygon(p.toAWTPolygon()); 
            }
            
            g2d.setColor(new Color(34, 181, 0));
            for (Point2D p : mSelection.getPoints()) {
                g2d.fillOval((int) p.getX() - (10 / 2), (int) p.getY() - (10 / 2), 10, 10);
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
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mMousePos.set(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mSelection.addPoint(e.getX(), e.getY());
        }
        else if (e.getButton() == MouseEvent.BUTTON3) {
            mSelection.setReverse();
        }
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
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            
        }
    }
}
