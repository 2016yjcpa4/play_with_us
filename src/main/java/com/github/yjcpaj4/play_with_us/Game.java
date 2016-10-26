package com.github.yjcpaj4.play_with_us;
 
import com.github.yjcpaj4.play_with_us.map.Map;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import com.github.yjcpaj4.play_with_us.resource.ImageResource;
import com.github.yjcpaj4.play_with_us.resource.ResourceManager;
import com.github.yjcpaj4.play_with_us.resource.SpriteImageResource;
import java.awt.image.BufferedImage;
 
/**
 * 메인 클래스는 비디오와 게임 캔버스를 관리합니다.
 * 
 * 외부에서 비디오를 재생시키면 게임스레드를 pause 하고
 * 비디오의 재생이 끝나면 게임스레드를 resume 시킵니다.
 * @author 차명도.
 */
public class Game extends GraphicLooper {
    
    private static Game INSTANCE = null;
    
    public static Game getInstance() {
        if (INSTANCE == null) {
            synchronized(Game.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Game();
                }
            }
        }
        
        return INSTANCE;
    }
    
    public static final boolean DEBUG = true;
    
    private Map mMap;
    private JFrame mWindow = new JFrame();
    private ResourceManager mRes = ResourceManager.getInstance();
    private InputManager mInput = InputManager.getInstance();
    
    private Game() {
        mMap = new Map();
        
        mWindow.setTitle("PLAY with us");
        mWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mWindow.setSize(1280, 800); 
        mWindow.add(getComponent(), BorderLayout.CENTER);
    }

    @Override
    protected void draw(Graphics2D g2d) {
        super.draw(g2d);
        
        mMap.update(this);
        mMap.draw(this, g2d);
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
        Game.getInstance().start();
    }
}
