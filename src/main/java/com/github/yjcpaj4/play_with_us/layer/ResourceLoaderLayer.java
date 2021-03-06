package com.github.yjcpaj4.play_with_us.layer;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.Layer;
import com.github.yjcpaj4.play_with_us.DropboxClient;
import com.github.yjcpaj4.play_with_us.ResourceManager;
import com.github.yjcpaj4.play_with_us.resource.MovieResource;
import com.github.yjcpaj4.play_with_us.util.FileUtil;
import com.google.gson.Gson;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ResourceLoaderLayer extends Layer {
    
    private final String DBX_CLIENT_ID = "play_with_us/v0.1";
    private final String DBX_ACCESS_TOKEN = "BG8Lirg0MOAAAAAAAAAAG2rc4quBs-xL2yoFBPxrUdyembJNsZyBrrZ6v8JlmSg5";
    private final String DBX_RESOURCE_DIR = "/resources";
        
    private static final int PADDING = 25;

    private final String RESOURCE_CHECKSUM_FILE = "checksum.json";
    private final File RESOURCE_DIR = new File("res");
    private DropboxClient mDropbox;
    private float mProgress;
    private String mMessage;
    
    private long mDuration;

    public ResourceLoaderLayer(Application c) {
        super(c);
        
        mDropbox = new DropboxClient(DBX_CLIENT_ID, DBX_ACCESS_TOKEN);
        
        if ( ! RESOURCE_DIR.exists()) {
            RESOURCE_DIR.mkdirs();
        }
    }
    
    private void loadResources() throws IOException {
        ResourceManager r = getResource();
        
        r.load("res/img.bg.clothesroom.picture.png");
        r.load("res/snd.obj.kitchen.tv.mp3");
        r.load("res/img.obj.kitchen.tv.png");
        r.load("res/img.bg.kitchen.refrigerator.png");
        r.load("res/img.obj.livingroom.shoerack.png");
        r.load("res/sprt.bathroom.bloodstains.json");
        r.load("res/img.bg.library.paper.0.png");
        r.load("res/img.bg.library.paper.1.png");
        r.load("res/img.bg.help.png");
        r.load("res/snd.bgm.girlsroom.scream.wav");
        r.load("res/snd.obj.clothesroom.mannequin.mp3");
        r.load("res/img.bg.bathroom.ghost.jpg");
        r.load("res/img.bg.bathroom.tub.png");
        r.load("res/img.bg.bathroom.towel.png");
        r.load("res/img.bg.bathroom.mirror.png");
        r.load("res/img.bg.clothesroom.mannequin.png");
        r.load("res/snd.bgm.girlsroom.feet.mp3");
        r.load("res/snd.player.breath.wav");
        r.load("res/map.girlsroom.json");
        r.load("res/snd.bgm.bathroom.ghost.wav");
        r.load("res/img.bg.kitchen.cabinet.0.png");
        r.load("res/img.bg.kitchen.cabinet.1.png");
        r.load("res/img.bg.kitchen.tv.png");
        r.load("res/snd.player.light.switch.mp3");
        r.load("res/snd.bgm.bathroom.water_drop.mp3");
        r.load("res/snd.bgm.bathroom.ghost.scream.wav");
        r.load("res/snd.obj.bathroom.bloodstains.foot.mp3");
        r.load("res/snd.obj.bathroom.bloodstains.glass.mp3");
        r.load("res/snd.bgm.ghost.begin.mp3");
        r.load("res/snd.obj.clothesroom.mannequin.feet.wav");
        r.load("res/snd.bgm.ghost.scream.mp3");
        r.load("res/snd.player.item.wav");
        r.load("res/snd.bgm.library.clock.mp3");
        r.load("res/snd.obj.portal.locked.mp3");
        r.load("res/snd.obj.portal.open.mp3");
        r.load("res/img.bg.library.piano.png");
        r.load("res/img.bg.library.picture.0.png");
        r.load("res/img.bg.library.picture.1.png");
        r.load("res/img.bg.library.picture.2.png");
        r.load("res/img.obj.library.piano.hint.png");
        r.load("res/snd.obj.library.piano.mp3");
        r.load("res/img.bg.library.clock.png");
        r.load("res/img.bg.library.book.red.png");
        r.load("res/img.bg.library.book.green.png");
        r.load("res/img.bg.library.book.blue.png");
        r.load("res/img.bg.library.book.yellow.png");
        r.load("res/img.bg.library.book.white.png");
        r.load("res/img.bg.library.locker.png");
        r.load("res/img.bg.girlsroom.note.png");
        r.load("res/snd.obj.paper.wav");
        r.load("res/snd.bgm.girlsroom.knock.wav");
        
        r.load("res/sprt.player.walk.n.json");
        r.load("res/sprt.player.walk.e.json");
        r.load("res/sprt.player.walk.s.json");
        r.load("res/sprt.player.walk.w.json");
        r.load("res/sprt.player.idle.n.json");
        r.load("res/sprt.player.idle.e.json");
        r.load("res/sprt.player.idle.s.json");
        r.load("res/sprt.player.idle.w.json");
        
        r.load("res/sprt.ghost.walk.n.json");
        r.load("res/sprt.ghost.walk.e.json");
        r.load("res/sprt.ghost.walk.s.json");
        r.load("res/sprt.ghost.walk.w.json");
        r.load("res/sprt.ghost.idle.n.json");
        r.load("res/sprt.ghost.idle.e.json");
        r.load("res/sprt.ghost.idle.s.json");
        r.load("res/sprt.ghost.idle.w.json");
        
        r.load("res/sprt.clothesroom.mannequin.walk.n.json");
        r.load("res/sprt.clothesroom.mannequin.walk.e.json");
        r.load("res/sprt.clothesroom.mannequin.walk.s.json");
        r.load("res/sprt.clothesroom.mannequin.walk.w.json");
        r.load("res/sprt.clothesroom.mannequin.idle.n.json");
        r.load("res/sprt.clothesroom.mannequin.idle.e.json");
        r.load("res/sprt.clothesroom.mannequin.idle.s.json");
        r.load("res/sprt.clothesroom.mannequin.idle.w.json");
        r.load("res/sprt.bathroom.ghost.walk.n.json");
        r.load("res/sprt.bathroom.ghost.walk.e.json");
        r.load("res/sprt.bathroom.ghost.walk.s.json");
        r.load("res/sprt.bathroom.ghost.walk.w.json");
        r.load("res/sprt.bathroom.ghost.idle.n.json");
        r.load("res/sprt.bathroom.ghost.idle.e.json");
        r.load("res/sprt.bathroom.ghost.idle.s.json");
        r.load("res/sprt.bathroom.ghost.idle.w.json");
        
        r.load("res/map.kitchen.json");
        r.load("res/map.library.json");
        r.load("res/map.clothesroom.json");
        r.load("res/map.bathroom.json");
        r.load("res/map.livingroom.json");
    }

    @Override
    protected void resume() {
        super.resume();
        
        mProgress = 0f;
        mMessage = "게임에 필요한 리소스를 검사합니다.";
        
        new Thread() {

            @Override
            public void run() {

                File f = new File(RESOURCE_DIR, RESOURCE_CHECKSUM_FILE);

                if (f.exists()) {
                    f.delete();
                }

                final Map<String, String> m;

                try { // 체크섬 파일 다운받기
                    mDropbox.download(DBX_RESOURCE_DIR + "/" + RESOURCE_CHECKSUM_FILE, f);

                    m = new Gson().fromJson(FileUtil.getContents(f), Map.class);
                }
                catch(Exception e) {
                    throw new RuntimeException(e);
                }

                float n = 0;
                for(String s : m.keySet()) { // 체크섬 목록을 토대로 리소스의 유효성검사
                    File res = new File(RESOURCE_DIR, s);


                    if (res.exists()) { // 리소스가 이미 있고   
                        if ( ! FileUtil.getChecksum(res).equals(m.get(s))) { // 체크섬 검사가 올바르지 않으면

                            if (res.delete()) { // 리소스 제거
                                res.deleteOnExit();
                            }
                        }
                    }

                    if ( ! res.exists()) { // 위에서 리소스가 제거되었거나 없는 리소스라면 드롭박스에서 다운받기
                        try {
                            mDropbox.download(DBX_RESOURCE_DIR + "/" + s, res);
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                            // TODO... 오류처리
                        }
                    } 

                    mProgress = ++n / m.size();
                    mMessage = String.format("게임에 필요한 리소스를 내려받고 있습니다. (%d%%)", (int) (mProgress * 100));
                }

                mProgress = 1.0f;
                mMessage = "게임에 필요한 리소스를 불러오는중 입니다.";

                try {
                    loadResources();
                }
                catch (Exception e) {
                    mProgress = 0;
                    mMessage = "불러오는 도중 알수없는 오류가 발생하였습니다.";
                    throw new RuntimeException(e);
                }
                
                VideoLayer l = getContext().getLayer(VideoLayer.class);
                l.load(MovieResource.MOV_INTRO);
                showLayer(l); 

                finishLayer();  
            }
        }.start();
    }

    private void drawMessage(Graphics2D g2d) {
        if (mMessage == null) {
            return;
        }

        Font f = new Font(getApplicationCanvas().getFont().getName(), Font.PLAIN, 30);
        FontMetrics m = g2d.getFontMetrics(f);

        int w = getApplicationCanvas().getWidth();
        int h = getApplicationCanvas().getHeight();

        g2d.setFont(f);
        g2d.drawString(mMessage, 
                       w / 2 - m.stringWidth(mMessage) / 2, 
                       h - PADDING - 50 - 10 - m.getHeight());
    }

    private void drawProgress(Graphics2D g2d) {
        int x = PADDING;
        int y = getApplicationCanvas().getHeight() - PADDING - 50;            

        g2d.setColor(Color.RED);
        g2d.drawRect(x, y, getApplicationCanvas().getWidth() - PADDING * 2, 50);
        g2d.fillRect(x, y, (int) (getApplicationCanvas().getWidth() * mProgress) - PADDING * 2, 50);
    }
    
    private void drawLoading(Graphics2D g2d) {
        Font f = new Font(getApplicationCanvas().getFont().getName(), Font.PLAIN, 50);
        FontMetrics m = g2d.getFontMetrics(f);
        int n = (int) (mDuration / 500 % 4);
        String s = "Loading";

        for (int l = 0; l < n; ++l) {
            s += ".";
        }

        g2d.setFont(f);
        g2d.setColor(Color.red);
        g2d.drawString(s, PADDING, PADDING + m.getHeight());
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        drawLoading(g2d);
        drawProgress(g2d);
        drawMessage(g2d);
        
        mDuration += delta;
    }
}