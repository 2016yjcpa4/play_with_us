package com.github.yjcpaj4.play_with_us;

import com.github.yjcpaj4.play_with_us.resource.SoundResource;
import com.github.yjcpaj4.play_with_us.resource.SpriteResource;
import com.github.yjcpaj4.play_with_us.resource.StageResource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.github.yjcpaj4.play_with_us.util.FileUtil;
import com.google.gson.JsonSyntaxException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * 리소스 객체.
 * 
 * @author 차명도.
 */
public class ResourceManager {
    
    private Map<String, Object> mResource = new HashMap<>(); 
    
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
        switch (FileUtil.getExtension(f)) {
            
            case "jpg":
            case "jpeg":
            case "bmp": 
            case "gif":
            case "png": 
            case "tiff":
            case "tif":
                mResource.put(k, ImageIO.read(f));
                break;
                
            case "mp3":
            case "ogg":
            case "wma":
            case "wav":
                mResource.put(k, new SoundResource(f));
                break;
                
            case "json": // map.json, sprite.json
                try {
                    mResource.put(k, SpriteResource.loadFromJSON(f)); 
                }
                catch(JsonSyntaxException e1) {
                    try {
                        mResource.put(k, StageResource.loadFromJSON(f));    
                    }
                    catch(JsonSyntaxException e2) {
                        throw new RuntimeException(e2);
                    }
                }
                break;
        }
    }
    
    public BufferedImage getImage(String k) {
        return (BufferedImage) mResource.get(k);
    }
    
    public SpriteResource getSprite(String k) {
        return (SpriteResource) mResource.get(k);
    }
    
    public SoundResource getSound(String k) {
        return (SoundResource) mResource.get(k);
    }
}
