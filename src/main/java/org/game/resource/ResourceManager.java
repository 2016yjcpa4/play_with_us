package org.game.resource;

import com.dropbox.core.DbxException;
import com.google.gson.Gson;
import org.game.resource.ImageResource;
import org.game.resource.SpriteImageResource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.game.util.FileUtil;

/**
 * 리소스 객체.
 * 
 * @author 차명도.
 * @param <T> 
 */
public class ResourceManager {
    
    private static ResourceManager INSTANCE;
    
    public static ResourceManager getInstance() {
        if (INSTANCE == null) {
            synchronized(ResourceManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ResourceManager();
                }
            }  
        }
        
        return INSTANCE;
    }
    
    private static final String DBX_CLIENT_ID = "play-with-us/v0.1";
    private static final String DBX_ACCESS_TOKEN = "BG8Lirg0MOAAAAAAAAAAC0-b5uDsvabj82WaQjgu4sTvV6LOwKrt1l2_QCixyK2D";
    private static final String DBX_RESOURCE_DIR = "/resources";
    
    private static final String RESOURCE_CHECKSUM_FILE = "checksum.json";
    private static final File RESOURCE_DIR = new File("res");
    
    private Map<String, Resource> mResource = new HashMap<>();
    private DropboxDownloader mDropbox;
    
    private ResourceManager() {
        mDropbox = new DropboxDownloader(DBX_CLIENT_ID, DBX_ACCESS_TOKEN);
        
        if ( ! RESOURCE_DIR.exists()) {
            RESOURCE_DIR.mkdirs();
        }
    }
    
    public boolean isValid() {
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
    
    public void setup(SetupListener l) {
        new Thread() {
            
            @Override
            public void run() {
                if (l != null) {
                    l.onReady();
                }
                
                if (isValid()) {
                    if (l != null) {
                        l.onComplete();
                    }
                    return;
                }

                Map<String, String> m;

                try {
                    m = new Gson().fromJson(FileUtil.getContents(new File(RESOURCE_DIR, RESOURCE_CHECKSUM_FILE)), Map.class);
                }
                catch(IOException e) {
                    return;
                }

                int success = 0;
                int error = 0;

                for(String s : m.keySet()) {
                    File res = new File(RESOURCE_DIR, s);

                    if ( ! res.exists()) { // 리소스가 이미 있고
                        try {
                            mDropbox.download(DBX_RESOURCE_DIR + "/" + s, res);
                            success++;
                        }
                        catch(Exception e) {
                            error++;
                        }
                    } 
                    else {
                        success++;
                    }

                    if (l != null) {
                        l.onProgress(success, error, m.size());
                    }
                }

                if (l != null) {
                    l.onComplete();
                }
            }
        }.start();
    }
    
    public void load(File f) throws IOException {
        load(f, FileUtil.getNameWithoutExtension(f));
    }
    
    public void load(String f, String s) throws IOException {
        load(new File(f), s);
    }
    
    public void load(File f, String k) throws IOException {
        Resource r = null;
        
        if (SpriteImageResource.canLoad(f, k)) {
            r = new SpriteImageResource();
        }
        else if (ImageResource.canLoad(f)) {
            r = new ImageResource();
        }
        
        r.load(f, k);

        mResource.put(k, r);
    }
    
    public ImageResource getImage(String k) {
        return (ImageResource) mResource.get(k);
    }
    
    public SpriteImageResource getSprite(String k) {
        return (SpriteImageResource) mResource.get(k);
    }
    
    public void release(String k) {
        mResource.get(k).release();
    }
    
    public interface Resource {
        
        void load(File f, String s) throws IOException;

        void release();
    }
    
    public interface SetupListener {
        
        void onReady();
        
        void onProgress(int success, int error, int total);
        
        void onComplete();
    }
}
