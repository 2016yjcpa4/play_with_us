package org.game;
 
import com.github.axet.play.VLC;
import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
 
/**
 * 메인 클래스는 비디오와 게임 캔버스를 관리합니다.
 * 
 * 외부에서 비디오를 재생시키면 게임스레드를 pause 하고
 * 비디오의 재생이 끝나면 게임스레드를 resume 시킵니다.
 * @author 차명도.
 */
public class Main {
    
    private static Main INSTANCE = null;
    
    public static Main getInstance() {
        if (INSTANCE == null) {
            synchronized(Main.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Main();
                }
            }
        }
        
        return INSTANCE;
    }
    
    private JFrame mWindow = new JFrame();
    private Game mGame = new Game();
    private Video mVideo = new Video();
    
    private Main() {
        Container c = mWindow.getContentPane();
        c.setLayout(new OverlayLayout(c));
        c.add(mGame.getCanvas());
        c.add(mVideo.getCanvas());
        mWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mWindow.setSize(1280, 800);
        //setResizable(false);
    }
    
    public void play(File f) {
        mVideo.open(f);
        mVideo.play();
        
        new Timer().schedule(new TimerTask() {
            
            @Override
            public void run() {
                mGame.getCanvas().setVisible(true);
                mVideo.getCanvas().setVisible(false);
                mVideo.stop();
            }
        }, 1000);
        
        mVideo.addListener(new VLC.Listener() {
            
            @Override
            public void start() {
                mVideo.getCanvas().setVisible(true);
                mGame.getCanvas().setVisible(false);
            }

            @Override
            public void stop() {
                mVideo.getCanvas().setVisible(false);
                mGame.getCanvas().setVisible(true);
                mVideo.close();
            }

            @Override
            public void position(float pos) {
            }
        });
    }
    
    public void start() {
        mWindow.setVisible(true);
        
        play(new File("./res/test.mov"));
    }
    
    public Game getGame() {
        return mGame;
    }
    
    public Video getVideo() {
        return mVideo;
    }
    
    public static void main(String[] args) { 
        
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                Main.getInstance().start();
            }
        });
    }

}
