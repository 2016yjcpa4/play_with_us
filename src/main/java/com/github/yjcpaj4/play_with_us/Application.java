package com.github.yjcpaj4.play_with_us;
 
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import com.github.yjcpaj4.play_with_us.resource.ImageResource;
import com.github.yjcpaj4.play_with_us.resource.ResourceManager;
import com.github.yjcpaj4.play_with_us.resource.SpriteImageResource;
import com.github.yjcpaj4.play_with_us.resource.VideoResource;
import com.github.yjcpaj4.play_with_us.stage.GameStage;
import com.github.yjcpaj4.play_with_us.stage.VideoStage;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
 
/**
 * 메인 클래스는 비디오와 게임 캔버스를 관리합니다.
 * 
 * 외부에서 비디오를 재생시키면 게임스레드를 pause 하고
 * 비디오의 재생이 끝나면 게임스레드를 resume 시킵니다.
 * 
 * @author 차명도.
 */
public class Application extends GraphicLooper
    implements MouseListener
             , MouseMotionListener
             , KeyListener {
        
    private static final List<Stage> mStages = new LinkedList<>();
    
    {
        mStages.add(new GameStage(this));
        mStages.add(new VideoStage(this));
    }
    
    public static <T extends Stage> T getStage(Class<T> c) {
        if (mStages.isEmpty()) {
            return null;
        }
        
        for(Stage s : mStages) {
            if (s.getClass() == c) {
                return (T) s;
            }
        }
        
        return null;
    }
    
    public static final boolean DEBUG = true;

    private final Stack<Stage> mPool = new Stack<>();
    private final JFrame mWindow;
    private final ResourceManager mRes = ResourceManager.getInstance();
    private final InputManager mInput = InputManager.getInstance();
    
    private Application() {
        mWindow = new JFrame();
        mWindow.setTitle("PLAY with us");
        mWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mWindow.setSize(1280, 800); 
        
        mCanvas.addMouseListener(this);
        mCanvas.addMouseMotionListener(this);
        mCanvas.addKeyListener(this);
        mCanvas.setFocusable(true);
        
        mWindow.add(mCanvas, BorderLayout.CENTER);
    }
    
    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        final Stage s;
        
        try {
            s = mPool.peek(); //  씬중에 제일 위에있는놈을 선택해서
        }
        catch(EmptyStackException e) { // 없으면 이건 프로그램 오류거나 혹은 자동으로 종료가 되야함
            stop();
            return;
        }
        
        s.draw(delta, g2d); // 그리기
    }

    @Override
    public void stop() {
        super.stop(); // 스레드 종료후
        
        System.exit(0); // 프로세스 종료, 굳이 위에서 스레드 종료할것없이 프로세스만 종료 할수 있지만 걍 형식적인 흐름을 위해 호출
    }
    
    @Override
    public void start() {
        mWindow.setVisible(true);
        
        showIntro();
        
        super.start(); 
    }
    
    private void showIntro() {
        VideoStage s = getStage(VideoStage.class);
        s.load(VideoResource.MOV_INTRO);
        showStage(s);
    }
    
    protected void stopFrontStage() {
        synchronized(mPool) {
            stopStage(mPool.peek());
        }
    }
    
    protected void stopStage(Stage s) {
        synchronized(mPool) {
            mPool.remove(s);
        }
    }
    
    protected void showStage(Stage s) {
        synchronized(mPool) {
            pause(); // 화면을 일시정지시키고
            
            for (int n = 0; n < mPool.size(); ++n) {
                if (mPool.get(n) != s) {
                    mPool.get(n).stop();
                }
            }
            
            if (mPool.contains(s)) { // 이미 있는 씬이 있다면
                mPool.remove(s); // 지우고
            }
            
            mPool.push(s); // 마지막으로 이동
            
            resume(); // GraphicLooper 는 다시 재생시키고
            
            s.init(); // Stage 는 초기화작업
        }
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
        return ((ImageResource) mRes.getImage(k)).getImageData();
    }
    
    public SpriteImageResource getSprite(String s) {
        return ((SpriteImageResource) mRes.getSprite(s));
    }
    
    public static void main(String[] args) throws Exception {
        new Application().start(); // 생성하고 시작!
    }
}
