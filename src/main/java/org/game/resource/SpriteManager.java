package org.game.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class SpriteManager {

    private static SpriteManager INSTANCE;
    
    private SpriteManager() {
    }
    
    public static SpriteManager getInstance() {
        if (INSTANCE == null) {
            synchronized(SpriteManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SpriteManager();
                }
            }  
        }
        
        return INSTANCE;
    }
    
    private Map<String, BufferedImage> mSprites = new HashMap<>();

    public BufferedImage load(File f) {
        return load(f, f.getName().substring(0, f.getName().lastIndexOf(".")));
    }
    
    public BufferedImage load(File f, String s) {
        BufferedImage b;
        try {
            b = ImageIO.read(f);
        }
        catch(Exception e) {
            b = null;
        }
        
        mSprites.put(s, b);
        return b;
    }
    
    public BufferedImage get(String s) {
        if ( ! mSprites.containsKey(s)) {
            return null;
        }
        
        return mSprites.get(s);
    }
}
