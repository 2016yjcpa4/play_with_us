package org.game.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

public class ImageUtil {

    /**
     * 픽셀단위로 배열을 가져옵니다.
     *
     * @param b
     * @return
     */
    public static int[][] getImagePixels(BufferedImage b) {

        int w = b.getWidth();
        int h = b.getHeight();

        int[][] r = new int[h][w];
        
        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < h; ++y) {

                int p[] = b.getRaster().getPixel(x, y, new int[4]);

                int n = 0;
                n += ((int) p[0] & 0xFF) << 24; // alpha
                n += ((int) p[1] & 0xFF);       // blue
                n += ((int) p[2] & 0xFF) << 8;  // green
                n += ((int) p[3] & 0xFF) << 16; // red
                
                r[y][x] = n;
            }
        }
        
        return r;
    }

    /**
     * 바둑판형태로 이미지되어있는것을 목록형태로 가져옵니다.
     *
     * @param b
     * @return
     */
    public static List<SpriteImage> getSlicedImagesByAlphaLines(File f) {
        List<SpriteImage> l = new ArrayList<>();

        BufferedImage b = null;

        try {
            b = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
            return l;
        }
        
        return getSlicedImagesByAlphaLines(b);
    }
    
    public static List<SpriteImage> getSlicedImagesByAlphaLines(BufferedImage b) {
        List<SpriteImage> l = new ArrayList<>();
 
        
        for (SpriteImage sy : getSlicedImagesByAlphaRows(b)) { // X 축을 기준으로 이미지를 잘라버림
            for (SpriteImage sx : getSlicedImagesByAlphaColumns(sy.getImage())) { // Y 축을 기준으로 이미지를 잘라버림
                sx.y = sy.y;
                l.add(sx);
            }
        }

        return l;
    }

    /**
     * 이미지를 Y축의 라인이 투명인것을 단위로 자릅니다.
     *
     * @param b
     * @return
     */
    public static List<SpriteImage> getSlicedImagesByAlphaRows(BufferedImage b) {
        final List<SpriteImage> r = new ArrayList<>();
        final int p[][] = getImagePixels(b);
        
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (int y = 0; y < p.length; ++y) {

            boolean isAlphaLine = true;

            for (int x = 0; x < p[0].length; ++x) {
                if ((p[y][x] >> 24) > 0) { // 투명도가 좀 찐하다 생각되면
                    isAlphaLine = false; // 투명한 라인이 아님...
                    
                    maxY = Math.max(y, maxY);
                    minY = Math.min(y, minY);
                }
            }

            if (minY != Integer.MAX_VALUE && isAlphaLine) {
                r.add(new SpriteImage(b.getSubimage(0, minY, p[0].length, maxY - minY), 0, minY, p[0].length, maxY - minY));

                maxY = minY;
                minY = Integer.MAX_VALUE;
            }
        }

        return r;
    }

    /**
     * 이미지를 X축의 라인이 투명인것을 단위로 자릅니다.
     *
     * @param b
     * @return
     */
    public static List<SpriteImage> getSlicedImagesByAlphaColumns(BufferedImage b) {
        List<SpriteImage> r = new ArrayList<>();
        int p[][] = getImagePixels(b);

        int g = 0;
        int l = p[0].length;

        for (int x = 0; x < p[0].length; ++x) {

            boolean isAlphaLine = true;

            for (int y = 0; y < p.length; ++y) {

                if (y == p.length - 1 || (p[y][x] >> 24) > (255 * 0.1)) {
                    isAlphaLine = false;

                    g = Math.max(x, g);
                    l = Math.min(x, l);
                }
            }

            if (l != p[0].length - 1 && isAlphaLine) {
                
                r.add(new SpriteImage(b.getSubimage(l, 0, g - l, p.length), l, 0, g - l, p.length));

                g = l;
                l = p[0].length;
            }
        }

        return r;
    }
    
    public static class SpriteImage {
        
        private BufferedImage img;
        private int x;
        private int y;
        private int width;
        private int height;
        
        public SpriteImage(BufferedImage img, int x, int y, int width, int height) {
            this.img = img;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public BufferedImage getImage() {
            return img;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        public int getWidth() {
            return img.getWidth();
        }
        
        public int getHeight() {
            return img.getHeight();
        }
    }
}
