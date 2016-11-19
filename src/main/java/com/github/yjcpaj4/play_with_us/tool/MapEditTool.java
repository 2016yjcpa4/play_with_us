package com.github.yjcpaj4.play_with_us.tool;

import com.github.yjcpaj4.play_with_us.GraphicLooper;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.map.Lightless;
import com.github.yjcpaj4.play_with_us.map.NotWalkable;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.resource.StageResource;
import com.github.yjcpaj4.play_with_us.util.AWTUtil;
import com.github.yjcpaj4.play_with_us.util.FileUtil;
import com.github.yjcpaj4.play_with_us.util.MathUtil;
import com.google.gson.Gson;
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
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
    
    private static final String WINDOW_TITLE = "PLAY with us - Map Edit Tool";
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 800;
    
    private static final int SELECT_LIGHTLESS = 1;
    private static final int SELECT_NOT_WALKABLE = 2;
    
    private JFrame mFrame = new JFrame();
    private int mSelectMode = -1;
    private Selection mSelection = new Selection();
    private Point2D mMousePos = new Point2D();

    private StageResource mResource;
    
    public MapEditTool() {
        mCanvas.addMouseMotionListener(this);
        mCanvas.addMouseListener(this);
        mCanvas.addKeyListener(this);
        mCanvas.setBackground(Color.BLACK);
        mCanvas.setFocusable(true);
        
        
        JMenuBar m = new JMenuBar();
        m.add(new JMenu("파일") {
            {
                add(new JMenuItem("새로 만들기") {
                    {
                        addActionListener(new ActionListener() {
                            
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                mResource = newStageResource();
                            }
                        });
                    }
                });
                
                add(new JMenuItem("저장하기") {
                    {
                        addActionListener(new ActionListener() {
                            
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String s = new Gson().toJson(mResource);
                                
                                // 리소스 폴더로 옮겨집니다.
                                File f = new File("res");
                                mResource.getImageFile().renameTo(new File(f, mResource.getImageFile().getName()));
                                FileUtil.setContents(new File(f, "map.json"), s);
                            }
                        });
                    }
                });
                
                add(new JMenuItem("불러오기") {
                    {
                        addActionListener(new ActionListener() {
                            
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                
                                JFileChooser fc = new JFileChooser();
                                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                                    try {
                                        mResource = new Gson().fromJson(FileUtil.getContents(fc.getSelectedFile()), StageResource.class);
                                    }
                                    catch(Exception e) {
                                        JOptionPane.showMessageDialog(mFrame, "맵 파일을 불러오라고욧!");
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
        
        mFrame.setJMenuBar(m);
        mFrame.setTitle(WINDOW_TITLE);
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mFrame.setLocationRelativeTo(null);       
        mFrame.getContentPane().add(mCanvas);
        mFrame.setVisible(true);
        
        start();
    }
    
    private StageResource newStageResource() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return new StageResource(fc.getSelectedFile());
        }
        
        return null;
    }
    
    private class Selection {
        
        private boolean mReversed = false;
        private Stack<Point2D> mPoints = new Stack<>();
        
        public Selection() {
            reset();
        }
        
        public void undo() {
            if ( ! mPoints.isEmpty()) {
                mPoints.pop();
            }
        }
        
        public void reset() {
            mPoints.clear();
            mReversed = false;
        }
        
        public Polygon toPolygon() {
            return new Polygon(getPoints(true));
        }
        
        public List<Point2D> getOriginPoints() {
            return mPoints;
        }
        
        public List<Point2D> getPoints() {
            return getPoints(true);
        }
        
        public List<Point2D> getPoints(boolean withCurrentMousePosition) {
            final List<Point2D> l = new ArrayList(mPoints);
            if (withCurrentMousePosition) {
                l.add(mMousePos);
            }
            
            /**
             * 선택 영역을 반전시킵니다.
             * 
             * 이 경우 자바 내장 API 에서 Area 라는 클래스가 존재하는데
             * Area 를 이용하여 다각형과 다각형을 연산할수 있습니다.
             * 현재 아래 소스에서는 이미지크기 만큼의 사각형 도형을 하나 선언하고
             * 선택된 영역을 빼기(subtract)연산 합니다. 그렇게되면 사각형에서 선택영역의 구멍이 뚫린것처럼 나오게됩니다.
             */
            if (mReversed) {
                Area a = new Area(new Rectangle2D.Float(0, 0, mResource.getWidth(), mResource.getHeight()));
                a.subtract(new Area(new Polygon(l).toAWTPolygon()));
                
                Polygon p = new Polygon(AWTUtil.getPoints(a));
                
                a.subtract(new Area(p.toAWTPolygon()));
                
                l.clear();
                l.addAll(p.getPoints());
                l.addAll(AWTUtil.getPoints(a));
            }
            
            return l;
        }
        
        public void addPoint(int x, int y) {
            addPoint(new Point2D(x, y));
        }
        
        public void addPoint(Point2D p) {
            mPoints.add(p);
        }
        
        public void setReverse() {
            mReversed = !mReversed;
        }
    }
    
    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        if (mResource == null) {
            Font f = new Font("돋움", Font.BOLD, 40);
            g2d.setColor(Color.RED);
            g2d.setFont(f);
            
            FontMetrics fm = g2d.getFontMetrics(f);

            String s = "여기를 클릭하여 맵 이미지를 불러옵니다.";
            g2d.drawString(s, getWidth() / 2 - fm.stringWidth(s) / 2, getHeight() / 2 - fm.getHeight() / 2);
            return;
        }
        
        g2d.drawImage(mResource.getImage(), 0, 0, null);
        
        for (Lightless o : mResource.getLightless()) {
            g2d.setColor(new Color(255, 255, 0, (int) (255 * 0.5))); 
            g2d.fillPolygon(o.toAWTPolygon());
            
            g2d.setColor(new Color(255, 255, 0)); 
            g2d.drawPolygon(o.toAWTPolygon());
        }
        
        for (NotWalkable o : mResource.getNotWalkable()) {
            g2d.setColor(new Color(255, 0, 0, (int) (255 * 0.5))); 
            g2d.fillPolygon(o.toAWTPolygon());
            
            g2d.setColor(new Color(255, 0, 0)); 
            g2d.drawPolygon(o.toAWTPolygon());
        }
         
        for (Polygon o : mSelection.toPolygon().getTriangulate()) {
            g2d.setColor(new Color(34, 181, 0, (int) (255 * 0.5))); 
            g2d.fillPolygon(o.toAWTPolygon()); 
            
            g2d.setColor(new Color(34, 181, 0)); 
            g2d.drawPolygon(o.toAWTPolygon()); 
        }

        g2d.setColor(new Color(34, 181, 0));
        for (Point2D o : mSelection.getPoints()) {
            g2d.fillOval((int) o.getX() - (10 / 2), (int) o.getY() - (10 / 2), 10, 10);
        }
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
        if (e.isShiftDown() && mSelection.getOriginPoints().size() > 0) {
            List<Point2D> l = mSelection.getOriginPoints();
            Vector2D v = new Vector2D(l.get(l.size() - 1));
            char s = MathUtil.getSimpleDirectionByRadian(v.subtract(e.getX(), e.getY()).toAngle());
            
            if (s == 'n' || s == 's') { // 북쪽 혹은 남쪽 방향이면
                mMousePos.set(v.getX(), e.getY()); // 수직으로 
            } 
            else { // 그외 동쪽 혹은 서쪽방향이면
                mMousePos.set(e.getX(), v.getY()); // 수평으로
            }
        }
        else {
            mMousePos.set(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            
            if (mResource == null) {
                mResource = newStageResource();
                return;
            }
            
            mSelection.addPoint(new Point2D(mMousePos));
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
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            mSelection.undo();
        }
        else if (e.getKeyCode() == KeyEvent.VK_1) {
            mFrame.setTitle(WINDOW_TITLE + " [선따기 : 빛]");
            mSelectMode = SELECT_LIGHTLESS;
        }
        else if (e.getKeyCode() == KeyEvent.VK_2) {
            mFrame.setTitle(WINDOW_TITLE + " [선따기 : 벽]");
            mSelectMode = SELECT_NOT_WALKABLE;
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            
            if ( ! Arrays.asList(SELECT_LIGHTLESS, SELECT_NOT_WALKABLE).contains(mSelectMode)) {
                return;
            }
            
            if (mSelectMode == SELECT_LIGHTLESS) {
                mResource.addLightless(mSelection.getPoints(false));
            }
            else {
                mResource.addNotWalkable(mSelection.getPoints(false));
            }
            
            mSelection.reset();
        }
    }
    
    public static void main(String args[]) {
        new MapEditTool();
    }
}
