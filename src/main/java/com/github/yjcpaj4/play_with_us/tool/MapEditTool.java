package com.github.yjcpaj4.play_with_us.tool;

import com.github.yjcpaj4.play_with_us.GraphicLooper;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

public class MapEditTool extends GraphicLooper implements MouseListener, KeyListener {
    
    private BufferedImage mImage;
    
    private boolean mReversed = false;
    
    private static final int LIGHTLESS_POINTING = 0;
    private static final int NOT_WALKABLE_POINTING = 1;
    
    private int mPointingMode = -1;
    private List<Point2D> mCurrentPoint = new ArrayList<>();
    
    private List<Polygon> mNotWalkable = new ArrayList<>();
    private List<Polygon> mLightless = new ArrayList<>();
    
    public MapEditTool() throws Exception {
        
        JFileChooser fc = new JFileChooser();
        int n = fc.showOpenDialog(null);

        if (n == JFileChooser.APPROVE_OPTION) {
            mImage = ImageIO.read(fc.getSelectedFile());
        } else {
            System.exit(0);
        }
        
        mCanvas.addMouseListener(this);
        mCanvas.addKeyListener(this);
        mCanvas.setBackground(Color.BLACK);
        mCanvas.setFocusable(true);
        
        JFrame f = new JFrame();
        
        
        
        
        JMenuBar mb = new JMenuBar(); 
        {
            JMenu m = new JMenu("파일");
            
            JMenuItem m3 = new JMenuItem("저장");
            m3.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    
                }
            });

            m.add(m3);
            mb.add(m);
        }


        f.setJMenuBar(mb); 
        
        f.setTitle("PLAY with us - Map Edit Tool");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setSize(1280, 800);
        f.setLocationRelativeTo(null);        
        f.getContentPane().add(mCanvas);
        f.setVisible(true);
        
        
        start();
    }
    
    private List<Point2D> getPoints() {
        if ( ! mReversed) {
            return mCurrentPoint;
        }
        
        Point2D p = mCurrentPoint.get(0); // 첫번째껄 가져와서

        // 시계방향으로 사각형 포인팅을 추가하면 폴리곤이 반대로 먹게됨.
        List<Point2D> l = new ArrayList(mCurrentPoint);
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
        for(Polygon p : mNotWalkable) {
            g2d.fillPolygon(p.toAWTPolygon()); 
        }
        
        g2d.setColor(new Color(255, 255, 0, (int) (255 * 0.5)));    
        for(Polygon p : mLightless) {
            g2d.fillPolygon(p.toAWTPolygon()); 
        }
        
        if ( ! mCurrentPoint.isEmpty()) {
            g2d.setColor(new Color(34, 181, 0, (int) (255 * 0.5))); 
            g2d.fillPolygon(new Polygon(getPoints()).toAWTPolygon()); 

            g2d.setColor(new Color(34, 181, 0));
            for(int n = 0; n < mCurrentPoint.size(); ++n) { 
                Point2D p = mCurrentPoint.get(n);

                g2d.fillOval(p.getX() - (10 / 2), p.getY() - (10 / 2), 10, 10);
            }
        }
        
        g2d.setColor(Color.MAGENTA);
        
        Font f = new Font("돋움", Font.BOLD, 20);
        g2d.setFont(f);
        FontMetrics fm = g2d.getFontMetrics(f);
        
        String s1 = "숫자 키 1 => 비출수 없는 영역";
        String s2 = "숫자 키 2 => 걸어갈 수 없는 영역";
        String s3 = "Ctrl + Z => 한칸 되돌리기";
        
        if (mPointingMode == LIGHTLESS_POINTING) s1 += " [편집중]";
        if (mPointingMode == NOT_WALKABLE_POINTING) s2 += " [편집중]";
        
        g2d.drawString(s1, 10, fm.getHeight() * 1);
        g2d.drawString(s2, 10, fm.getHeight() * 2);
        g2d.drawString(s3, 10, fm.getHeight() * 3);
    }
    
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {
            e.printStackTrace();
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
        if (mPointingMode >= 0) {
            
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    mCurrentPoint.add(new Point2D(e.getX(), e.getY()));
                    break;
                    
                case MouseEvent.BUTTON3:
                    mReversed = !mReversed;
                    break;
            }
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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Z:
                if (e.isControlDown() && ! mCurrentPoint.isEmpty()) {
                    mCurrentPoint.remove(mCurrentPoint.size() - 1);
                }
                break;
            case KeyEvent.VK_1:
                mPointingMode = LIGHTLESS_POINTING;
                break;
            case KeyEvent.VK_2:
                mPointingMode = NOT_WALKABLE_POINTING;
                break;
            case KeyEvent.VK_ENTER:
                if (mPointingMode == NOT_WALKABLE_POINTING) {
                    mNotWalkable.add(new Polygon(getPoints()));
                }
                else if (mPointingMode == LIGHTLESS_POINTING) {
                    mLightless.add(new Polygon(getPoints()));
                }
                
                mCurrentPoint.clear();
                mReversed = false;
                break;
        }
    }
}
