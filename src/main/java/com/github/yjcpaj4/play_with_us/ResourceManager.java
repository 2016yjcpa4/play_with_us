package com.github.yjcpaj4.play_with_us;

import com.github.yjcpaj4.play_with_us.resource.ImageResource;
import com.github.yjcpaj4.play_with_us.resource.Resource;
import com.github.yjcpaj4.play_with_us.resource.SoundResource;
import com.github.yjcpaj4.play_with_us.resource.SpriteResource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.github.yjcpaj4.play_with_us.util.FileUtil;

/**
 * 리소스 객체.
 * 
 * @author 차명도.
 * @param <T> 
 */
public class ResourceManager {
    
    private Map<String, Resource> mResource = new HashMap<>(); 
    
    protected ResourceManager() {
    }
    
    public void load(String f) throws IOException {
        load(new File(f));
    }
    
    public void load(File f) throws IOException {
        load(f, FileUtil.getNameWithoutExtension(f));
    }
    
    public void load(String f, String s) throws IOException {
        load(new File(f), s);
    } 
    
    public void load(File f, String k) throws IOException {
        Resource r = null; 

        switch (FileUtil.getExtension(f)) {
            
            case "jpg":
            case "jpeg":
            case "bmp": 
            case "gif":
            case "png": 
            case "tiff":
            case "tif":
                r = new ImageResource(f);
                break;
                
            case "mp3":
            case "ogg":
            case "wma":
            case "wav":
                r = new SoundResource(f);
                break;
                
            case "json":
                try {
                    r = SpriteResource.loadFromJSON(f);
                }
                catch(Exception e) {
                    throw new RuntimeException(e);
                } 
        }

        mResource.put(k, r);
    }
    
    public ImageResource getImage(String k) {
        return (ImageResource) mResource.get(k);
    }
    
    public SpriteResource getSprite(String k) {
        return (SpriteResource) mResource.get(k);
    }
    
    public SoundResource getSound(String k) {
        return (SoundResource) mResource.get(k);
    }
}
