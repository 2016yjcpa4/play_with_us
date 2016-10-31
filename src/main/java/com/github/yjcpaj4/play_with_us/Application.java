package com.github.yjcpaj4.play_with_us;
 
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import com.github.yjcpaj4.play_with_us.resource.ImageResource;
import com.github.yjcpaj4.play_with_us.resource.ResourceManager;
import com.github.yjcpaj4.play_with_us.resource.SpriteImageResource;
import com.github.yjcpaj4.play_with_us.stage.GameStage;
import com.github.yjcpaj4.play_with_us.stage.ResourceLoaderStage;
import com.github.yjcpaj4.play_with_us.stage.VideoStage;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
public class Application extends GraphicLooper {
    
    private static final List<Stage> mStages = new LinkedList<>();
    
    {
        mStages.add(new GameStage(this));
        mStages.add(new VideoStage(this));
        mStages.add(new ResourceLoaderStage(this));
    }
    
    public static <T extends Stage> T getStageByClass(Class<T> c) {
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
    
    private final Stack<Stage> mLayers = new Stack<>();
    private final JFrame mWindow;
    private final ResourceManager mRes = ResourceManager.getInstance();
    private final InputManager mInput = InputManager.getInstance();
    
    private Application() {
        mCanvas.addMouseListener(mInput);
        mCanvas.addMouseMotionListener(mInput);
        mCanvas.addKeyListener(mInput);
        mCanvas.setFocusable(true);
        
        mWindow = new JFrame();
        mWindow.setTitle("PLAY with us");
        mWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mWindow.setResizable(false);
        mWindow.setUndecorated(true);
        mWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mWindow.setLocationRelativeTo(null);        
        mWindow.add(mCanvas, BorderLayout.CENTER);
    }
    
    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        mInput.update();
          
        final Stage s;
        
        try {
            s = mLayers.peek(); //  스테이지 중에 제일 위에있는놈을 선택해서
        }
        catch(EmptyStackException e) { // 스테이지가 없으면 자동으로 프로그램이 종료가 되야함
            stop();
            return;
        }
        
        s.draw(delta, g2d); // 스테이지 그리기
    }

    @Override
    public void stop() {
        super.stop(); // 스레드 종료후
        
        System.exit(0); // 프로세스 종료
        
        // 굳이 위에서 스레드 종료할것없이 프로세스만 종료 할수 있지만 걍 형식적인 흐름을 위해 호출
    }
    
    @Override
    public void start() {
        mWindow.setVisible(true);

        showResourceLoader();

        super.start();
    }
    
    private void showResourceLoader() {
        showStage(ResourceLoaderStage.class);
    }
    
    protected void stopStage() { 
        stopStage(mLayers.peek()); 
    }
    
    /**
     * 스테이지(무대) 를 정지시킵니다.
     * 
     * 스테이지 전환은 별도의 스레드에서 처리됩니다.
     *
     * @param s 
     */    
    protected void stopStage(Stage s) {  
        EventQueue.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                pause(); // 스톱되는 순간 화면을 정지시키고

                mLayers.remove(s); // 젤 위에있는 화면을 가져와
                s.finish(); // finish 호출시키고

                resume(); // GraphicLooper 는 다시 재생  
            }
        });
    }
    
    protected void showStage(Class<? extends Stage> c) {
        showStage(getStageByClass(c));
    }
    
    /**
     * 스테이지(무대) 를 보여줍니다(시작합니다).
     * 
     * TODO 콜백개념을 넣어줘야함.
     * 
     * showStage 는 GraphicLooper 를 제어해야 하는데 외부스레드에서 제어를 걸어줘야 하므로 EventQueue 를 이용하여 제어합니다.
     * 
     * @param s 
     */
    protected void showStage(Stage s) { 
        EventQueue.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                pause(); // 화면을 일시정지시키고

                if (mLayers.size() > 0) { // 쌓여있는 스테이지중 제일 위에있는걸 피니쉬
                    mLayers.peek().finish();
                }

                if (mLayers.contains(s)) { // 이미 있는놈이면
                    mLayers.remove(s); // 지우고
                }

                mLayers.push(s); // 마지막으로 이동

                s.init(); // Stage 는 초기화작업
                resume(); // GraphicLooper 는 다시 재생
            }
        });
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
