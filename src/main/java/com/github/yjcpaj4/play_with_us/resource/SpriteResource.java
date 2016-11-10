package com.github.yjcpaj4.play_with_us.resource;

import com.google.gson.annotations.SerializedName;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 리팩토링이 필요합니다.
 * 
 * @author 차명도.
 */
public class SpriteResource implements Resource {
    
    @SerializedName("x")
    private int mX;
    
    @SerializedName("y")
    private int mY;
    
    @SerializedName("w")
    private int mWidth;
    
    @SerializedName("h")
    private int mHeight;
    
    @SerializedName("fps")
    private int mFPS;
    
    private transient BufferedImage mImage;
    private transient List<Frame> mFrames;
    
    private SpriteResource() {
    }
    
    public int getWidth() {
        return mWidth;
    }
    
    public int getHeight() {
        return mHeight;
    }
       
    public Frame getFrame(int n) {
        return mFrames.get(n);
    }

    public Frame getCurrentFrame(long delta) {
        int n[] = { 2, 3, 4, 3, 2, 1, 0, 1 };
        return getFrame(n[(int) (delta / mFPS % n.length)]);
    }

    public static class Frame {

        protected int mX;
        protected int mY;
        protected int mWidth;
        protected int mHeight;
        protected int mIndex;
        protected BufferedImage mImage;

        public BufferedImage getImage() {
            return mImage;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }
    }
}
