package org.game;
 
import com.github.axet.play.VLC;
import com.google.gson.Gson;
import org.game.resource.VideoResource;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.OverlayLayout;
import org.game.resource.DropboxDownloader;
import org.game.resource.ResourceManager;
import org.game.util.FileUtil;
 
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
        mLayer.add(new ResourceDownload().getComponent(), 0); // 리소스 다운로드는 초반에 리소스 체크만하고 바로 사라질 컴포넌트이므로 멤버변수로 등록하지 않음
        mLayer.add(mGame.getComponent(), -1);
        mLayer.add(mVideoCanvas, -1);
        mLayer.setLayout(new OverlayLayout(mLayer));
        mLayer.setVisible(true);
        
        mWindow.setTitle("PLAY with us");
        mWindow.add(mLayer, BorderLayout.CENTER);
        mWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mWindow.setSize(1280, 800);
    }
    
    public void playVideo(String f) {
        playVideo(new File(f));
    }
    
    private void stopVideo() {
        mGame.resume();

        mLayer.moveToFront(mGame.getComponent());
        mLayer.moveToBack(mVideoCanvas);
        mVLC.close();
    }
    
    /**
     * 비디오를 재생함수.
     * 
     * 비디오 재생시 게임은 일시정지(pause)상태로 돌아가고 비디오를 재생시킵니다.
     * 리스너를 등록하여 비디오가 끝나게될시 게임으로 되돌아오고(resume) VLC 를 닫습니다.
     * 
     * @param f 비디오 파일객체
     */
    public void playVideo(File f) {
        mGame.pause();
        mLayer.moveToFront(mVideoCanvas);
        mLayer.moveToBack(mGame.getComponent());

        mVLC.open(f);
        mVLC.play();
        
        final KeyListener l = new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) { 
            }

            @Override
            public void keyPressed(KeyEvent e) { 
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    stopVideo();
                    
                    mWindow.removeKeyListener(this);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) { 
            }
        };
        
        mVLC.addListener(new VLC.Listener() {

            @Override
            public void stop() {
                stopVideo();
            }
            
            @Override
            public void start() {
                mWindow.addKeyListener(l);
            }

            @Override
            public void position(float n) {
                // 구현하지 않음
            }
        });
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
    
    public void launch() {
        mWindow.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        mWindow.setUndecorated(true);
        mWindow.setLocationRelativeTo(null);
        mWindow.setVisible(true);
        
        mVLC.setVideoCanvas(mVideoCanvas);
    }
    
    private class ResourceDownload extends GraphicLooper {
        
        private final String DBX_CLIENT_ID = "play-with-us/v0.1";
        private final String DBX_ACCESS_TOKEN = "O6KQjX3-8tAAAAAAAAAAFX5otGZsbsLIHma4OwgDnc1TOqUtSfssR7azAy8O9Jfb";
        private final String DBX_RESOURCE_DIR = "/resources";

        private static final int PADDING = 25;
            
        private float mProgress;
        private String mMessage;
        
        private final String RESOURCE_CHECKSUM_FILE = "checksum.json";
        private final File RESOURCE_DIR = new File("res");
        private DropboxDownloader mDropbox;
    
        private ResourceDownload() {
            super();

            mDropbox = new DropboxDownloader(DBX_CLIENT_ID, DBX_ACCESS_TOKEN);

            if ( ! RESOURCE_DIR.exists()) {
                RESOURCE_DIR.mkdirs();
            }
        }
        
        private boolean isValid() {
            try {
                File chksum = new File(RESOURCE_DIR, RESOURCE_CHECKSUM_FILE);

                if (chksum.exists()) {
                    chksum.delete();
                }

                mDropbox.download(DBX_RESOURCE_DIR + "/" + RESOURCE_CHECKSUM_FILE, chksum);

                Map<String, String> m = new Gson().fromJson(FileUtil.getContents(chksum), Map.class);

                boolean isValid = true;

                for(String s : m.keySet()) {
                    File res = new File(RESOURCE_DIR, s);

                    if (res.exists()) { // 리소스가 이미 있고
                        if (FileUtil.getChecksum(res).equals(m.get(s))) { // 체크섬 검사가 올바르면
                            continue; // 통과
                        }

                        if (res.delete()) { // 파일이 있지만 존재한다면 삭제작업
                            res.deleteOnExit();
                        }
                    }

                    isValid = false;
                }

                return isValid;
            }
            catch(Exception e) {
                return false;
            }
        }
        
        @Override
        public void start() {
            super.start();
            
            // 완료작업
            Runnable r = new Runnable() {
                
                @Override
                public void run() {
                    mProgress = 1.0f;
                    mMessage = "게임에 필요한 리소스를 불러오는중 입니다.";

                    try {
                        loadResources();
                    }
                    catch (Exception e) {
                        mProgress = 0;
                        mMessage = "불러오는 도중 알수없는 오류가 발생하였습니다.";
                        return;
                    }

                    stop();
                    
                    mLayer.remove(getComponent());

                    playVideo(VideoResource.MOV_INTRO);
                }
            };
        
            mMessage = "게임에 필요한 리소스를 검사합니다.";
            
            // 다운로드 하는 작업을 별도의 스레드로 뺍니다.
            new Thread() {
                
                @Override
                public void run() {
                    
                    // 그냥 아래에 다 포함시켜야할듯...
                    if (isValid()) {
                        r.run();
                        return;
                    }

                    Map<String, String> m;

                    try {
                        m = new Gson().fromJson(FileUtil.getContents(new File(RESOURCE_DIR, RESOURCE_CHECKSUM_FILE)), Map.class);
                    }
                    catch(IOException e) {
                        return;
                    }
                    
                    float n = 0;
                    for(String s : m.keySet()) {
                        File res = new File(RESOURCE_DIR, s);

                        if ( ! res.exists()) { // 리소스가 이미 있고
                            try {
                                mDropbox.download(DBX_RESOURCE_DIR + "/" + s, res);
                                mProgress = ++n / m.size();
                            }
                            catch(Exception e) {
                                // TODO... 오류
                            }
                        } 
                        else {
                            mProgress = ++n / m.size();
                        }
  
                        mMessage = String.format("게임에 필요한 리소스를 내려받고 있습니다. (%.1f%%)", n * 100);
                    }

                    r.run();
                }
            }.start();
        }
        
        private void drawMessage(Graphics2D g2d) {
            if (mMessage == null) {
                return;
            }
            
            Font f = new Font(getComponent().getFont().getName(), Font.PLAIN, 30);
            FontMetrics m = g2d.getFontMetrics(f);
            
            int w = getComponent().getWidth();
            int h = getComponent().getHeight();
            
            g2d.setFont(f);
            g2d.drawString(mMessage, 
                           w / 2 - m.stringWidth(mMessage) / 2, 
                           h - PADDING - 50 - 10 - m.getHeight());
        }
        
        private void drawProgress(Graphics2D g2d) {
            int x = PADDING;
            int y = getComponent().getHeight() - PADDING - 50;            
            
            g2d.setColor(Color.RED);
            g2d.drawRect(x, y, getComponent().getWidth() - PADDING * 2, 50);
            g2d.fillRect(x, y, (int) (getComponent().getWidth() * mProgress) - PADDING * 2, 50);
        }

        @Override
        protected void draw(Graphics2D g2d) {
            super.draw(g2d);
            
            Font f = new Font(getComponent().getFont().getName(), Font.PLAIN, 50);
            FontMetrics m = g2d.getFontMetrics(f);
            int n = (int) (getDelta() / 500 % 4);
            String s = "Loading";
            
            for (int l = 0; l < n; ++l) {
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
