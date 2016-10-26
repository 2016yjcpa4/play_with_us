package com.github.yjcpaj4.play_with_us;
 
import com.github.yjcpaj4.play_with_us.activity.GameActivity;
import com.github.yjcpaj4.play_with_us.activity.Param;
import com.github.yjcpaj4.play_with_us.map.Map;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import com.github.yjcpaj4.play_with_us.resource.ImageResource;
import com.github.yjcpaj4.play_with_us.resource.ResourceManager;
import com.github.yjcpaj4.play_with_us.resource.SpriteImageResource;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Stack;
import com.github.yjcpaj4.play_with_us.activity.Activity;
import com.github.yjcpaj4.play_with_us.activity.VideoActivity;
 
/**
 * 메인 클래스는 비디오와 게임 캔버스를 관리합니다.
 * 
 * 외부에서 비디오를 재생시키면 게임스레드를 pause 하고
 * 비디오의 재생이 끝나면 게임스레드를 resume 시킵니다.
 * @author 차명도.
 */
public class Application extends GraphicLooper implements MouseListener, MouseMotionListener, KeyListener {
    
    private static Application INSTANCE = null;
    
    public static Application getInstance() {
        if (INSTANCE == null) {
            synchronized(Application.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Application();
                }
            }
        }
        
        return INSTANCE;
    }
    
    public static final boolean DEBUG = true;
    
    private Stack<Activity> mActivities = new Stack<>();
    private JFrame mWindow = new JFrame();
    private ResourceManager mRes = ResourceManager.getInstance();
    private InputManager mInput = InputManager.getInstance();
    
    private Application() {
        mCanvas.addMouseListener(this);
        mCanvas.addMouseMotionListener(this);
        mCanvas.addKeyListener(this);
        
        mActivities.add(new GameActivity());
        mActivities.add(new VideoActivity(mCanvas));
        
        mWindow.setTitle("PLAY with us");
        mWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mWindow.setSize(1280, 800); 
        mWindow.add(mCanvas, BorderLayout.CENTER);
    }
    
    public void startActivity(Activity a) {
        startActivity(a, null);
    }
    
    public void startActivity(Activity a, Param p) {
        synchronized(mActivities) {
            pause(); // 게임화면을 잠시 일시정지...
            
            if (mActivities.contains(a)) {// 스택구조라 이미 있는경우
                mActivities.remove(a); 
            }

            for (Activity e : mActivities) {
                if (e != a) {
                    e.onPause();
                }
            }

            mActivities.push(a);

            a.onStart(p);
            
            resume(); // 위작업이 완료되면 다시 이어서 시작
        }
    }

    @Override
    protected void draw(Graphics2D g2d) {
        super.draw(g2d);
        
        mActivities.peek().onDraw(getDelta(), g2d);
    }
    
    @Override
    public void start() {
        try {
            loadResources();

            mWindow.setVisible(true);

            super.start();
        }
        catch (Exception e) {
            stop();
        }
    }
    
    private void loadResources() throws Exception {
        mRes.load("res/img_map.png",    "map");
        mRes.load("res/img_player.png", "player.walk.n");
        mRes.load("res/img_player.png", "player.walk.ne");
        mRes.load("res/img_player.png", "player.walk.e");
        mRes.load("res/img_player.png", "player.walk.se");
        mRes.load("res/img_player.png", "player.walk.s");
        mRes.load("res/img_player.png", "player.walk.sw");
        mRes.load("res/img_player.png", "player.walk.w");
        mRes.load("res/img_player.png", "player.walk.nw");
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        mInput.setKeyPress(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        mInput.setKeyRelease(e.getKeyCode());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mInput.setMousePosition(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mInput.setMousePress(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mInput.setMouseRelease(e.getButton());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    public InputManager getInput() {
        return mInput;
    }
    
    public BufferedImage getImage(String k) {
        return ((ImageResource) ResourceManager.getInstance().getImage(k)).getImageData();
    }
    
    public SpriteImageResource getSprite(String s) {
        return ((SpriteImageResource) ResourceManager.getInstance().getSprite(s));
    }
    
    public static void main(String[] args) throws Exception {
        Application.getInstance().start();
    }
}
