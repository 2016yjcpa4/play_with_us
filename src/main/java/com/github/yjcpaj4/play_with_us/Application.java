package com.github.yjcpaj4.play_with_us;
 
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import com.github.yjcpaj4.play_with_us.resource.ImageResource;
import com.github.yjcpaj4.play_with_us.resource.ResourceManager;
import com.github.yjcpaj4.play_with_us.resource.SpriteImageResource;
import com.github.yjcpaj4.play_with_us.layer.ResourceLoaderLayer;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.util.EmptyStackException;
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
    
    public static final boolean DEBUG = true;
    
    private Stack<Layer> mLayers = new Stack<>();
    private ResourceManager mRes = ResourceManager.getInstance(); // 싱글톤으로 만들필요는 없을거같음...
    private InputManager mInput = InputManager.getInstance();
    
    private Application() {
    }
    
    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        mInput.update();
          
        final Layer s;
        
        try {
            s = mLayers.peek(); //  스테이지 중에 제일 위에있는놈을 선택해서
        }
        catch(EmptyStackException e) { // 스테이지가 없으면 자동으로 프로그램이 종료가 되야함
            e.printStackTrace();
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
    
    /*
     * 스타트를 한순간 프레임이 만들어지고 캔버스를 생성합니다.
     * 생성된 캔버스는 외부스레드인 GraphicLooper 스레드 에서 
     * 계속적으로 draw 하는작업이 시작됩니다.
     */
    @Override
    public void start() {
        final Runnable r = new Runnable() {

            @Override
            public void run() { 
                mCanvas.addMouseListener(mInput);
                mCanvas.addMouseMotionListener(mInput);
                mCanvas.addKeyListener(mInput);
                mCanvas.setFocusable(true);

                JFrame f = new JFrame();
                f.setTitle("PLAY with us");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setResizable(false);
                f.setUndecorated(true);
                f.setExtendedState(JFrame.MAXIMIZED_BOTH);
                f.setLocationRelativeTo(null);        
                f.add(mCanvas, BorderLayout.CENTER);
                f.setVisible(true);
        
                showResourceLoader();
            }
        };
        
        try {
            /*
             * Event Dispatch Thread 안에서 실행시키도록 합니다.
             * 하지만 thread 이므로 비동기로 일어날수 있어 
             * invokeAndWait 을 사용하여 동기적인 스레드를 요청합니다.
             */
            EventQueue.invokeAndWait(r);
            
            super.start();
        }
        catch (Exception e) {
            /*
             * 그럴일이 없겟지만 예외가 발생하여 최악의 경우
             * 프로그램은 비정상적으로 돌아가므로 그냥 종료 시킵니다.
             */
            stop();
        }
    }
    
    public <T extends Layer> T getLayer(Class<T> cls) {
        if ( ! mLayers.isEmpty()) {
            for (Layer l : mLayers) {
                if (cls == l.getClass()) {
                    return (T) l;
                }
            }
        }
        
        final Layer l;
        
        try {
            Constructor con = cls.getConstructor(Application.class);
            l = (Layer) con.newInstance(this);
        }
        catch(Exception e) {
            throw new RuntimeException("읍읍!!");
        }
        
        mLayers.push(l);
        return (T) l;
    }
    
    private void showResourceLoader() {
        showLayer(getLayer(ResourceLoaderLayer.class));
    }
    
    protected void finishLayer() { 
        finishLayer(mLayers.peek()); 
    }
    
    /**
     * 스테이지(무대) 를 정지시킵니다.
     * 
     * 스테이지 전환은 Event Dispatch Thread 에서 처리됩니다.
     * finishStage, showStage 를 하는순간 어떠한 인터렉티브(상호작용)에 의해 일어난것이므로
     * 반드시 외부 스레드에서 호출되게 해야합니다.
     *
     * @param l 
     */    
    protected void finishLayer(Layer l) {  
        final Runnable r = new Runnable() {

            @Override
            public void run() {
                pause();
                if (l.isRunning()) {
                    l.pause();
                }
                
                mLayers.remove(l);
                
                l.finish();
                
                resume();
                if ( ! mLayers.peek().isRunning()) {
                    mLayers.peek().resume();
                }
            }
        };
        
        if (EventQueue.isDispatchThread()) {
            r.run();
        } 
        else {
            try {
                EventQueue.invokeAndWait(r);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 스테이지(무대) 를 보여줍니다(시작합니다).
     * 
     * 스테이지 전환은 Event Dispatch Thread 에서 처리됩니다.
     * finishStage, showStage 를 하는순간 어떠한 인터렉티브(상호작용)에 의해 일어난것이므로
     * 반드시 외부 스레드에서 호출되게 해야합니다.
     * 
     * @param l 
     */
    protected void showLayer(Layer l) {
        final Runnable r = new Runnable() {

            @Override
            public void run() {
                /*
                 * 화면에 레이어를 추가하기전 GraphicLooper 상태를 일시정지 시킵니다.
                 * 동시에 레이어 스택에 있는것중 제일 위에있는 레이어만 일시정지 이벤트를 호출합니다.
                 */
                pause();
                if ( ! mLayers.isEmpty()) {
                    mLayers.peek().pause();
                }

                /*
                 * 스택에 쌓여잇는것중에 동일한 인스턴스가 존재한다면 삭제합니다.
                 * 삭제 처리후 push 를 하게되면 밑에있던 레이어는 위로 올라오게 될것입니다.
                 */
                if (mLayers.contains(l)) {
                    mLayers.remove(l);
                }
                mLayers.push(l);
 
                /*
                 * 위 작업이 모두 끝나면 일시정지 상태에 있던 GraphicLooper 와 Layer 의 
                 * 상태를 이어서 재생시킵니다.
                 */
                resume();
                l.resume();
            }
        };
        
        if (EventQueue.isDispatchThread()) {
            r.run();
        }
        else {
            try {
                EventQueue.invokeAndWait(r);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
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
