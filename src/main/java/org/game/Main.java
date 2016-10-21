package org.game;
 
import com.github.axet.play.VLC;
import org.game.resource.VideoResource;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
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
    
    public static final boolean DEBUG = true;
    
    private JFrame mWindow = new JFrame();
    private JLayeredPane mLayer = new JLayeredPane();
    private Game mGame = new Game();
    private ResourceManager mRes = ResourceManager.getInstance();
    
    private VLC mVLC = new VLC();
    private Canvas mVideoCanvas = new Canvas();
    
    private Main() {        
        mLayer.add(mGame.getCanvas(), 0);
        mLayer.add(mVideoCanvas, 1);
        mLayer.setLayout(new OverlayLayout(mLayer));
        mLayer.setVisible(true);
        
        mWindow.add(mLayer, BorderLayout.CENTER);
        mWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mWindow.setSize(1280, 800);
        
        mGame.pause();
    }
    
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
    }
    
    private void loadResources() throws Exception {
        mRes.load("./res/img_map.png",    "map");
        mRes.load("./res/img_player.png", "player.walk.n");
        mRes.load("./res/img_player.png", "player.walk.ne");
        mRes.load("./res/img_player.png", "player.walk.e");
        mRes.load("./res/img_player.png", "player.walk.se");
        mRes.load("./res/img_player.png", "player.walk.s");
        mRes.load("./res/img_player.png", "player.walk.sw");
        mRes.load("./res/img_player.png", "player.walk.w");
        mRes.load("./res/img_player.png", "player.walk.nw");
    }
    
    public void launch() {
        mWindow.setVisible(true);
        
        mVLC.setVideoCanvas(mVideoCanvas);
        
        ResourceDownload r = new ResourceDownload();
        
        mRes.setup(new ResourceManager.SetupListener() {

            @Override
            public void onProgress(int progress, int total) {
                float n = (float) progress / total;
                
                r.setProgress(n);
                r.setMessage(String.format("게임에 필요한 자원을 내려받고 있습니다. (%.1f%%)", n * 100));
            }

            @Override
            public void onComplete() {
                r.setMessage("게임에 필요한 자원을 불러오는중 입니다.");
                
                try {
                    loadResources();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                
                r.stop();
                
                mLayer.remove(r.getCanvas());
                
                play(VideoResource.MOV_INTRO);
            }
        });
        
        r.setMessage("게임에 필요한 자원을 검사합니다.");
        
        mLayer.add(r.getCanvas(), 0);
    }
    
    private static class ResourceDownload extends GraphicLooper {
        
        private static final int PADDING = 25;
            
        private float mProgress;
        private String mMessage;
        
        public void setProgress(float n) {
            mProgress = n;
        }
        
        public void setMessage(String s) {
            mMessage = s;
        }
        
        private void drawMessage(Graphics2D g2d) {
            Font f = new Font(getCanvas().getFont().getName(), Font.PLAIN, 30);
            FontMetrics m = g2d.getFontMetrics(f);
            
            int w = getCanvas().getWidth();
            int h = getCanvas().getHeight();
            
            g2d.setFont(f);
            g2d.drawString(mMessage, 
                           w / 2 - m.stringWidth(mMessage) / 2, 
                           h - PADDING - 50 - 10 - m.getHeight());
        }
        
        private void drawProgress(Graphics2D g2d) {
            int w = getCanvas().getWidth();
            int h = getCanvas().getHeight();
            
            g2d.setColor(Color.red);
            g2d.fillRect(PADDING, 
                         h - PADDING - 50, 
                         (int) (w * mProgress) - PADDING * 2, 
                         50);
            
        }

        @Override
        protected void draw(Graphics2D g2d) {
            super.draw(g2d);
            
            Font f = new Font(getCanvas().getFont().getName(), Font.PLAIN, 30);
            FontMetrics m = g2d.getFontMetrics(f);
            int n = (int) (getDelta() / 500 % 3);
            String s = "Loading";
            
            while (n-- >= 0) {
                s += ".";
            }
            
            g2d.setFont(f);
            g2d.setColor(Color.red);
            g2d.drawString(s, PADDING, PADDING + m.getHeight());
            
            drawProgress(g2d);
            drawMessage(g2d);
        }
    }
    
    public static void main(String[] args) {
        Main.getInstance().launch(); 
    }
}
