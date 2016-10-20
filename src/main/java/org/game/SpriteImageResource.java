package org.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.game.util.FileUtil;

public class SpriteImageResource extends ImageResource {

    private static Map<String, SpriteImage> mSprites = new HashMap<>();
    
    static {
        mSprites.put("player.walk.south", new SpriteImage() {
            {
                mX = 0;
                mY = 82 * 0;
                mWidth = 36;
                mHeight = 82;
                mAlias = "player.walk.south";
                mFramePerSecond = 100;
                mSteps = 3;
            }
        });
        mSprites.put("player.walk.east", new SpriteImage() {
            {
                mX = 0;
                mY = 82 * 2;
                mWidth = 36;
                mHeight = 82;
                mAlias = "player.walk.south";
                mFramePerSecond = 100;
                mSteps = 3;
            }
        });
        mSprites.put("player.walk.west", new SpriteImage() {
            {
                mX = 0;
                mY = 82 * 1;
                mWidth = 36;
                mHeight = 82;
                mAlias = "player.walk.west";
                mFramePerSecond = 100;
                mSteps = 3;
            }
        });
        mSprites.put("player.walk.north", new SpriteImage() {
            {
                mX = 0;
                mY = 82 * 3;
                mWidth = 36;
                mHeight = 82;
                mAlias = "player.walk.north";
                mFramePerSecond = 100;
                mSteps = 3;
            }
        });
    }
    
    private SpriteImage mSprite;

    @Override
    public void load(File f, String k) throws IOException {
        super.load(f, k);
        
        mSprite = mSprites.get(k);
        
        List<SpriteImage.Frame> l = new ArrayList<>();
        for (int n = 0; n < mSprite.mSteps; ++n) {
            SpriteImage.Frame o = new SpriteImage.Frame();
            o.mX = mSprite.mX + (mSprite.mWidth * n);
            o.mY = mSprite.mY;
            o.mWidth = mSprite.mWidth;
            o.mHeight = mSprite.mHeight;
            o.mIndex = n;
            o.mImage = getImageData().getSubimage(o.mX, o.mY, o.mWidth, o.mHeight);
            l.add(o);
        }
        
        mSprite.mFrames = l;
    }
    
    public int getWidth() {
        return mSprite.mWidth;
    }
    
    public int getHeight() {
        return mSprite.mHeight;
    }
    
    public SpriteImage.Frame getFrame(int n) {
        return mSprite.getFrame(n);
    }
    
    public SpriteImage.Frame getCurrentFrame(long n) {
        return mSprite.getCurrentFrame(n);
    }
    
    public static boolean canLoad(File f, String k) {
        return ImageResource.canLoad(f) && mSprites.containsKey(k);
    }
    
    public static class SpriteImage {

        protected int mX;
        protected int mY;
        protected int mWidth;
        protected int mHeight;
        protected int mSteps;
        protected String mAlias;
        protected int mFramePerSecond;
        protected BufferedImage mImage;
        protected List<Frame> mFrames;

        public SpriteImage() {
        }
        
        public Frame getFrame(int n) {
            return mFrames.get(n);
        }
        
        public Frame getCurrentFrame(long d) {
            return getFrame((int) (d / mFramePerSecond % mSteps));
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

}
