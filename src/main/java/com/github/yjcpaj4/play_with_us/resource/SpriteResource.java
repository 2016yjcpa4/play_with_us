package com.github.yjcpaj4.play_with_us.resource;

import com.github.yjcpaj4.play_with_us.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

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
    
    @SerializedName("src")
    private String mImage;
    private transient List<Frame> mFrames;
    
    private SpriteResource() {
    }
    
    public static SpriteResource loadFromJSON(File f) throws Exception {
        SpriteResource r = new Gson().fromJson(FileUtil.getContents(f), SpriteResource.class);
        BufferedImage b = ImageIO.read(new File(r.mImage));
        
        for(Frame e : r.mFrames) {
            e.mImage = b.getSubimage(e.mX, e.mY, e.mWidth, e.mHeight);
        }
        
        return r;
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

        @SerializedName("x")
        protected int mX;
        
        @SerializedName("y")
        protected int mY;
        
        @SerializedName("w")
        protected int mWidth;
        
        @SerializedName("h")
        protected int mHeight;
        
        protected transient BufferedImage mImage;

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
