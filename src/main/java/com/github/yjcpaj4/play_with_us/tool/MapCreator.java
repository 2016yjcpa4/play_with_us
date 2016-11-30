package com.github.yjcpaj4.play_with_us.tool;

import com.github.yjcpaj4.play_with_us.GraphicLooper;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.game.Lightless;
import com.github.yjcpaj4.play_with_us.game.NotWalkable;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.resource.MapResource;
import com.github.yjcpaj4.play_with_us.util.AWTUtil;
import com.github.yjcpaj4.play_with_us.util.FileUtil;
import com.github.yjcpaj4.play_with_us.util.MathUtil;
import com.google.gson.Gson;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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

public class MapCreator extends GraphicLooper implements MouseListener, MouseWheelListener, MouseMotionListener, KeyListener {
    
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
    private static final int SELECT_PORTAL = 3;
    
    private JFrame mFrame = new JFrame();
    private int mSelectMode = -1;
    private Selection mSelection = new Selection();
    private Vector2D mMousePos = new Vector2D();

    private MapResource mResource;
    
    public MapCreator() {
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
                                FileUtil.setContents(new File(f, "map.debug.json"), s);
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
                                        mResource = MapResource.loadFromJSON(fc.getSelectedFile());
                                    }
                                    catch(Exception e) {
                                        e.printStackTrace();
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
    
    public Point2D getMousePosition() {
        return new Vector2D(mMousePos).subtract(mTrans).toPoint2D();
    }
    
    private MapResource newStageResource() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return new MapResource(fc.getSelectedFile());
        }
        
        return null;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
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
                l.add(getMousePosition());
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
        
        g2d.translate(mTrans.getX(), mTrans.getY());
        
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
        
        for (NotWalkable o : mResource.getNotWalkable()) {
            g2d.setColor(new Color(255, 0, 0, (int) (255 * 0.5))); 
            g2d.fillPolygon(o.toAWTPolygon());
            
            g2d.setColor(new Color(255, 0, 0)); 
            g2d.drawPolygon(o.toAWTPolygon());
        }
        
        if (mResource.hasPlayerSpawn()) {
            final int SIZE = 40;
            
            Point2D p = mResource.getPlayerSpwan();
            g2d.setColor(new Color(0, 0, 255, (int) (255 * 0.5))); 
            g2d.fillOval((int) p.getX() - (SIZE / 2), (int) p.getY() - (SIZE / 2), SIZE, SIZE);
            
            g2d.setColor(new Color(0, 0, 255)); 
            g2d.drawOval((int) p.getX() - (SIZE / 2), (int) p.getY() - (SIZE / 2), SIZE, SIZE);
        }
        
        Polygon o = mSelection.toPolygon();
        
        g2d.setColor(new Color(34, 181, 0, (int) (255 * 0.5))); 
        g2d.fillPolygon(o.toAWTPolygon()); 

        g2d.setColor(new Color(34, 181, 0)); 
        g2d.drawPolygon(o.toAWTPolygon());
        
        Point2D p = getMousePosition();
        
        g2d.drawLine((int) p.getX() - 10, (int) p.getY(), (int) p.getX() + 10, (int) p.getY());
        g2d.drawLine((int) p.getX(), (int) p.getY() - 10, (int) p.getX(), (int) p.getY() + 10);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    private Point2D mPressed;
    private Vector2D mTrans = new Vector2D();
    
    @Override
    public void mousePressed(MouseEvent e) {
        mPressed = new Point2D(e.getX() - mTrans.getX(), e.getY() - mTrans.getY());
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (mPressed != null) {
            mTrans.set(e.getX() - mPressed.getX(), e.getY() - mPressed.getY());
        }
        
        if (e.isShiftDown() && mSelection.getOriginPoints().size() > 0) {
            List<Point2D> l = mSelection.getOriginPoints();
            Vector2D v = new Vector2D(l.get(l.size() - 1)).add(mTrans);
            String s = MathUtil.getSimpleDirectionByRadian(v.subtract(e.getX(), e.getY()).toAngle());
            
            if (Arrays.asList("n", "s").contains(s)) { // 북쪽 혹은 남쪽 방향이면
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
        mPressed = null;
        
        if (e.getButton() == MouseEvent.BUTTON1) {
            
            if (mResource == null) {
                mResource = newStageResource();
                return;
            }
            
            mSelection.addPoint(new Point2D(getMousePosition()));
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
        else if (e.getKeyCode() == KeyEvent.VK_3) {
            mFrame.setTitle(WINDOW_TITLE + " [선따기 : 포탈]");
            mSelectMode = SELECT_PORTAL;
        }
        else if (e.getKeyCode() == KeyEvent.VK_4) {
            mResource.setPlayerSpawn(new Point2D(getMousePosition()));
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            
            if (mSelectMode == SELECT_LIGHTLESS) {
                for(Polygon o : new Polygon(mSelection.getPoints(false)).getTriangulate()) {
                    mResource.addLightless(o.getPoints());
                }
            
                mSelection.reset();
            }
            else if (mSelectMode == SELECT_NOT_WALKABLE) {
                for(Polygon o : new Polygon(mSelection.getPoints(false)).getTriangulate()) { 
                    mResource.addNotWalkable(o.getPoints());
                }
            
                mSelection.reset();
            }
            else if (mSelectMode == SELECT_PORTAL) {
                
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                
                MapResource r = MapResource.loadFromJSON(fc.getSelectedFile());
                
                Point2D p = new Point2D();
                JFrame f = new JFrame();
                
                GraphicLooper l = new GraphicLooper() { 
                    
                    @Override
                    protected void draw(long delta, Graphics2D g2d) {
                        super.draw(delta, g2d);

                        g2d.drawImage(r.getImage(), 0, 0, null);

                        for (Lightless o : r.getLightless()) {
                            g2d.setColor(new Color(255, 255, 0, (int) (255 * 0.5))); 
                            g2d.fillPolygon(o.toAWTPolygon());

                            g2d.setColor(new Color(255, 255, 0)); 
                            g2d.drawPolygon(o.toAWTPolygon());
                        }

                        for (NotWalkable o : r.getNotWalkable()) {
                            g2d.setColor(new Color(255, 0, 0, (int) (255 * 0.5))); 
                            g2d.fillPolygon(o.toAWTPolygon());

                            g2d.setColor(new Color(255, 0, 0)); 
                            g2d.drawPolygon(o.toAWTPolygon());
                        }
                    }
                };
                
                l.getCanvas().addMouseListener(new MouseListener() {
                    
                    @Override
                    public void mouseClicked(MouseEvent e) { 
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        l.stop();
                        f.setVisible(false);
                        
                        p.set(e.getX(), e.getY());
                        
                        mResource.addPortal(fc.getName(), p, mSelection.getPoints());
            
                        mSelection.reset();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) { 
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) { 
                    }

                    @Override
                    public void mouseExited(MouseEvent e) { 
                    }
                });
                
                f.setTitle(WINDOW_TITLE);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
                f.setLocationRelativeTo(null);       
                f.getContentPane().add(l.getCanvas());
                f.setVisible(true);
                
                l.start();
            }
        }
    }
    
    public static void main(String args[]) {
        new MapCreator();
    }
}
