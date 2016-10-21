package org.game;
 
import org.game.resource.VideoResource;
//import com.github.axet.play.VLC;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.OverlayLayout;
import org.game.resource.ResourceManager;
 
/**
 * 메인 클래스는 비디오와 게임 캔버스를 관리합니다.
 * 
 * 외부에서 비디오를 재생시키면 게임스레드를 pause 하고
 * 비디오의 재생이 끝나면 게임스레드를 resume 시킵니다.
 * @author 차명도.
 */
public class MainBackup {
    
    private static MainBackup INSTANCE = null;
    
    public static MainBackup getInstance() {
        if (INSTANCE == null) {
            synchronized(MainBackup.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MainBackup();
                }
            }
        }
        
        return INSTANCE;
    }
    
    public static final boolean DEBUG = true;
    
    private JFrame mWindow = new JFrame();
    private JLayeredPane mLayer = new JLayeredPane();
    private Game mGame = new Game();
    private ResourceManager mRes = ResourceManager.getInstance();
    
    //private VLC mVLC = new VLC();
    private Canvas mVideoCanvas = new Canvas();
    
    private MainBackup() {
        mLayer.setLayout(new OverlayLayout(mLayer));
        mLayer.add(mGame.getCanvas(), 0);
        mLayer.add(mVideoCanvas, 1);
        mLayer.setVisible(true);
        
        mWindow.add(mLayer, BorderLayout.CENTER);
        mWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mWindow.setSize(1280, 800);
    }
    
    /*
    public void play(String f) {
        play(new File(f));
    }
    
    public void play(File f) {
        mGame.pause();        
        mLayer.moveToFront(mVideoCanvas);

        mVLC.open(f);
        mVLC.play();
        
        final KeyListener l = new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    mWindow.removeKeyListener(this);

                    mGame.resume();

                    mLayer.moveToFront(mGame.getCanvas());
                    mVLC.close();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
        
        mVLC.addListener(new VLC.Listener() {

            @Override
            public void stop() {
                l.keyPressed(new KeyEvent(mWindow, 0, 0, 0, KeyEvent.VK_ESCAPE));
            }
            
            @Override
            public void start() {
                if (DEBUG) {
                    mWindow.addKeyListener(l);
                }
            }

            @Override
            public void position(float n) {
                // 구현하지 않음
            }
        });
    }*/
    
    public void launch() {
        mWindow.setVisible(true);
        
        mRes.setup(new ResourceManager.SetupListener() {

            @Override
            public void onProgress(int success, int error, int total) {

            }

            @Override
            public void onComplete() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onReady() {
                mLayer.moveToFront(mGame.getCanvas());
            }
        }); 
        
        /*
        mVLC.setVideoCanvas(mVideoCanvas);
        
        play(VideoResource.MOV_INTRO);
        */
    }
    
    public static void main(String[] args) {
        MainBackup.getInstance().launch(); 
    }
}
