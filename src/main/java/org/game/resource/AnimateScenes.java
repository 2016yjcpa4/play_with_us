package org.game.resource;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class AnimateScenes {
    
    private int mX;
    private int mY;
    private int mWidth;
    private int mHeight;
    private String mAlias;
    private int mFramePerSecond;
    private BufferedImage mImage;
    private List<Frame> mFrames;

    public AnimateScenes(String alias, int x, int y, int w, int h, int steps, int fps, BufferedImage b) {
        mAlias = alias;
        mWidth = w;
        mHeight = h;
        mFramePerSecond = fps;
        mImage = b;
        
        List<Frame> l = new ArrayList<>();
        for(int n = 0; n < steps; ++n) {
            b.getSubimage(x * w, y, w, h);
        }
        mFrames = l;
    }
    
    public String getAlias() {
        return mAlias;
    }
    
    public int getFPS() {
        return mFramePerSecond;
    }
    
    public List<Frame> getFrames() {
        return mFrames;
    }
    
    public static class Frame {
        
        private int mX;
        private int mY;
        private int mStep;
        private BufferedImage mImage;
    }
}
