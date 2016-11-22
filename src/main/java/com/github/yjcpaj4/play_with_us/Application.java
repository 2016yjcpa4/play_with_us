package com.github.yjcpaj4.play_with_us;
 
import com.github.yjcpaj4.play_with_us.InputManager;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import com.github.yjcpaj4.play_with_us.layer.ResourceLoaderLayer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.lang.reflect.Constructor;
import java.util.EmptyStackException;
import java.util.Stack;
 
/**
 * 어플리케이션.
 * 
 * 어플리케이션 클래스는 프레임내에 한개의 캔버스만 존재하며 
 * 캔버스 내에서는 여러개의 레이어를 이용해 UI 를 제공하며
 * 동시에 레이어의 생명주기를 관리합니다.
 * 생명주기는 안드로이드 액티비티의 생명주기를 참고하여 제작되었습니다.
 * 
 * @see https://kairo96.gitbooks.io/android/content/pic2/2-4-1-1.jpg
 * @author 차명도.
 */
public class Application extends GraphicLooper {
    
    public static final boolean DEBUG = false;
    
    private Stack<Layer> mLayerStack = new Stack<>();
    private ResourceManager mRes = new ResourceManager(); // 싱글톤으로 만들필요는 없을거같음...
    private InputManager mInput = new InputManager();
    
    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        mInput.update();
          
        final Layer s;
        
        try {
            s = mLayerStack.peek(); //  스테이지 중에 제일 위에있는놈을 선택해서
        }
        catch (EmptyStackException e) { // 스테이지가 없으면 자동으로 프로그램이 종료가 되야함
            throw new RuntimeException(e);
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
                mCanvas.addMouseListener(mInput.getMouseListener());
                mCanvas.addMouseMotionListener(mInput.getMouseMotionListener());
                mCanvas.addKeyListener(mInput.getKeyListener());
                mCanvas.setBackground(Color.BLACK);
                mCanvas.setFocusable(true);

                JFrame f = new JFrame();
                f.setTitle("PLAY with us");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                //f.setResizable(false);
                //f.setUndecorated(true);
                f.setMinimumSize(new Dimension(700, 400));
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
            //stop();
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 클래스 값을 레이어 인스턴스로 가져옵니다.
     * 
     * 인스턴스의 생성빈도를 낮추기위해 레이어스택에 있는 인스턴스가 존재하면
     * 리턴 시키거나 없으면 새로 인스턴스를 생성하는 방식
     * 
     * @param <T>
     * @param cls
     * @return 
     */
    public <T extends Layer> T getLayer(Class<T> cls) {
        if ( ! mLayerStack.isEmpty()) {
            for (Layer l : mLayerStack) {
                if (cls == l.getClass()) {
                    return (T) l;
                }
            }
        }
        
        final Layer l;
        
        try {
            final Constructor c = cls.getConstructor(Application.class);
            l = (Layer) c.newInstance(this);
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        
        return (T) l;
    }
    
    private void showResourceLoader() {
        showLayer(getLayer(ResourceLoaderLayer.class));
    }
    
    protected void finishLayer() { 
        finishLayer(mLayerStack.peek()); 
    }
    
    /**
     * 레이어를 정지시킵니다. 
     *
     * finishLayer, showLayer 를 하는순간 어떠한 인터렉티브(상호작용)에 의해 일어난것이므로
     * 반드시 외부 스레드인 Event Dispatch Thread 에서 호출되게 해야합니다.
     * 
     * @param l 
     */
    protected void finishLayer(Layer l) {  
        final Runnable r = new Runnable() {

            @Override
            public void run() {
                /*
                 * 화면을 정지한순간 GraphicLooper 는 일시정지상태로 돌아갑니다.
                 * 동시에 finish 하려는 레이어의 상태가 Running 상태라면 pause 이벤트를 호출합니다.
                 */
                pause();
                if (l.isRunning()) {
                    l.pause();
                }
                
                /*
                 * 그래픽과 레이어를 일시정지상태로 만든후 
                 * 레이어를 finish 하기위해 레이어 스택에서 제외시키고
                 * finish 를 이벤트를 호출합니다.
                 */
                mLayerStack.remove(l);
                l.finish();
                
                /*
                 * 위 작업이 완료되면 그래픽을 이어서 재생시키고
                 * 쌓여있는 레이어중 제일 위에 있는 레이어를 호출시킵니다.
                 */
                resume();
                if ( ! mLayerStack.peek().isRunning()) {
                    mLayerStack.peek().resume();
                }
            }
        };
        
        /*
         * 현재 스레드가 Event Dispatch Thread 내에서 동작한다면
         * Runnable 을 그냥 run 호출로 끝냅니다.
         * 하지만 그렇지 않다면 Event Dispatch Thread 에 동기적으로 호출합니다.
         */
        if (EventQueue.isDispatchThread()) {
            r.run();
        } 
        else {
            try {
                EventQueue.invokeAndWait(r);
            }
            catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    protected void showLayer(Class<? extends Layer> cls) {
        showLayer(getLayer(cls));
    }
    
    /**
     * 레이어 를 보여줍니다(시작합니다).
     *
     * finishLayer, showLayer 를 하는순간 어떠한 인터렉티브(상호작용)에 의해 일어난것이므로
     * 반드시 외부 스레드인 Event Dispatch Thread 에서 호출되게 해야합니다.
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
                if ( ! mLayerStack.isEmpty()) {
                    mLayerStack.peek().pause();
                }

                /*
                 * 스택에 쌓여잇는것중에 동일한 인스턴스가 존재한다면 삭제합니다.
                 * 삭제 처리후 push 를 하게되면 밑에있던 레이어는 위로 올라오게 될것입니다.
                 */
                if (mLayerStack.contains(l)) {
                    mLayerStack.remove(l);
                }
                mLayerStack.push(l);
 
                /*
                 * 위 작업이 모두 끝나면 일시정지 상태에 있던 GraphicLooper 와 Layer 의 
                 * 상태를 이어서 재생시킵니다.
                 */
                resume();
                if ( ! l.isRunning()) {
                    l.resume();
                }
            }
        };
        
        /*
         * 현재 스레드가 Event Dispatch Thread 내에서 동작한다면
         * Runnable 을 그냥 run 호출로 끝냅니다.
         * 하지만 그렇지 않다면 Event Dispatch Thread 에 동기적으로 호출합니다.
         */
        if (EventQueue.isDispatchThread()) {
            r.run();
        }
        else {
            try {
                EventQueue.invokeAndWait(r);
            }
            catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public InputManager getInput() {
        return mInput;
    }
    
    public ResourceManager getResource() {
        return mRes;
    }
    
    public static void main(String[] args) throws Exception {
        new Application().start(); // 생성하고 시작!
    }
}
