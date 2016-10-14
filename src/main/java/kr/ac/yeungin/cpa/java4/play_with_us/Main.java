package kr.ac.yeungin.cpa.java4.play_with_us;
 
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
 * @author Use
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
    
    private JFrame f = new JFrame();
    private Game g = new Game();
    private Video v = new Video();
    
    private Main() {
        Container c = f.getContentPane();
        c.setLayout(new OverlayLayout(c));
        c.add(g.getCanvas());
        c.add(v.getCanvas());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1024, 768);
        //setResizable(false);
    }
    
    public void play(File f) {
        
        // http://stackoverflow.com/questions/16758346/how-pause-and-then-resume-a-thread
        
        v.open(f);
        v.play();
        
        new Timer().schedule(new TimerTask() {
            
            @Override
            public void run() {
                g.getCanvas().setVisible(true);
                v.getCanvas().setVisible(false);
                v.stop();
            }
        }, 7000);
        
        v.addListener(new VLC.Listener() {
            
            @Override
            public void start() {
                v.getCanvas().setVisible(true);
                g.getCanvas().setVisible(false);
            }

            @Override
            public void stop() {
                v.getCanvas().setVisible(false);
                g.getCanvas().setVisible(true);
                v.close();
            }

            @Override
            public void position(float pos) {
            }
        });
    }
    
    public void start() {
        f.setVisible(true);
        
        play(new File("./res/test.mov"));
    }
    
    public Game getGame() {
        return g;
    }
    
    public Video getVideo() {
        return v;
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
