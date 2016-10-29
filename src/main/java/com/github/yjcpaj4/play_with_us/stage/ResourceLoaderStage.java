package com.github.yjcpaj4.play_with_us.stage;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.resource.DropboxDownloader;
import com.github.yjcpaj4.play_with_us.resource.ResourceManager;
import com.github.yjcpaj4.play_with_us.resource.VideoResource;
import com.github.yjcpaj4.play_with_us.util.FileUtil;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ResourceLoaderStage extends LoaderStage {
    
    private final String DBX_CLIENT_ID = "play_with_us/v0.1";
    private final String DBX_ACCESS_TOKEN = "BG8Lirg0MOAAAAAAAAAAG2rc4quBs-xL2yoFBPxrUdyembJNsZyBrrZ6v8JlmSg5";
    private final String DBX_RESOURCE_DIR = "/resources";
    
    private final String RESOURCE_CHECKSUM_FILE = "checksum.json";
    private final File RESOURCE_DIR = new File("res");
    private DropboxDownloader mDropbox;

    private ResourceManager mRes = ResourceManager.getInstance();
    
    public ResourceLoaderStage(Application c) {
        super(c);
        
        mDropbox = new DropboxDownloader(DBX_CLIENT_ID, DBX_ACCESS_TOKEN);
    }
    
    private void loadResources() throws IOException {
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

    @Override
    protected void init() {
        if ( ! RESOURCE_DIR.exists()) {
            RESOURCE_DIR.mkdirs();
        }
        
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
                    e.printStackTrace();
                    return;
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
                    return;
                }

                VideoStage s = Application.getStage(VideoStage.class);
                s.load(VideoResource.MOV_INTRO);
                showStage(s);
            }
        }.start();
    }
}