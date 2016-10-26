package com.github.yjcpaj4.play_with_us.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 리팩토링이 필요합니다.
 * 
 * @author 차명도.
 */
public class SpriteImageResource extends ImageResource {

    private static Map<String, SpriteImage> mSprites = new HashMap<>();
    
    static {
        mSprites.put("player.walk.n", new SpriteImage() {
            {
                mX = 0;
                mY = 64 * 4;
                mWidth = 64;
                mHeight = 64;
                mAlias = "player.walk.n";
                mFramePerSecond = 100;
                mSteps = 5;
            }
        });
        
        mSprites.put("player.walk.ne", new SpriteImage() {
            {
                mX = 0;
                mY = 64 * 5;
                mWidth = 64;
                mHeight = 64;
                mAlias = "player.walk.ne";
                mFramePerSecond = 100;
                mSteps = 5;
            }
        });
        mSprites.put("player.walk.e", new SpriteImage() {
            {
                mX = 0;
                mY = 64 * 6;
                mWidth = 64;
                mHeight = 64;
                mAlias = "player.walk.e";
                mFramePerSecond = 100;
                mSteps = 5;
            }
        });
        mSprites.put("player.walk.se", new SpriteImage() {
            {
                mX = 0;
                mY = 64 * 7;
                mWidth = 64;
                mHeight = 64;
                mAlias = "player.walk.se";
                mFramePerSecond = 100;
                mSteps = 5;
            }
        });
        mSprites.put("player.walk.s", new SpriteImage() {
            {
                mX = 0;
                mY = 64 * 0;
                mWidth = 64;
                mHeight = 64;
                mAlias = "player.walk.s";
                mFramePerSecond = 100;
                mSteps = 5;
            }
        });
        mSprites.put("player.walk.sw", new SpriteImage() {
            {
                mX = 0;
                mY = 64 * 1;
                mWidth = 64;
                mHeight = 64;
                mAlias = "player.walk.sw";
                mFramePerSecond = 100;
                mSteps = 5;
            }
        });
        mSprites.put("player.walk.w", new SpriteImage() {
            {
                mX = 0;
                mY = 64 * 2;
                mWidth = 64;
                mHeight = 64;
                mAlias = "player.walk.w";
                mFramePerSecond = 100;
                mSteps = 5;
            }
        });
        mSprites.put("player.walk.nw", new SpriteImage() {
            {
                mX = 0;
                mY = 64 * 3;
                mWidth = 64;
                mHeight = 64;
                mAlias = "player.walk.nw";
                mFramePerSecond = 100;
                mSteps = 5;
            }
        });
        /*mSprites.put("player.walk.south", new SpriteImage() {
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
        });*/
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
            int n[] = { 2, 3, 4, 3, 2, 1, 0, 1 };
            return getFrame(n[(int) (d / mFramePerSecond % n.length)]);
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
