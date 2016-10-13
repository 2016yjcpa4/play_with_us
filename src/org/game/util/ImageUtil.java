package org.game.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
                n += ((int) p[0] & 0xff) << 24; // alpha
                n += ((int) p[1] & 0xff);       // blue
                n += ((int) p[2] & 0xff) << 8;  // green
                n += ((int) p[3] & 0xff) << 16; // red
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

        for (SpriteImage sx : getSlicedImagesByAlphaAxisX(b)) { // X 축을 기준으로 이미지를 잘라버림
            for (SpriteImage sy : getSlicedImagesByAlphaAxisY(sx.getImage())) { // Y 축을 기준으로 이미지를 잘라버림
                sy.x = sx.x;
                l.add(sy);
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
    public static List<SpriteImage> getSlicedImagesByAlphaAxisY(BufferedImage b) {
        final List<SpriteImage> r = new ArrayList<>();
        final int p[][] = getImagePixels(b);

        int g = p.length;
        int l = -1;

        for (int y = 0; y < p.length; ++y) {

            boolean isAlphaLine = true;

            for (int x = 0; x < p[0].length; ++x) {

                if (x == p[0].length || (p[y][x] >> 24) != 0x00) {
                    isAlphaLine = false;

                    l = Math.max(y, l);
                    g = Math.min(y, g);
                }
            }

            if (g != p.length && isAlphaLine) {
                r.add(new SpriteImage(b.getSubimage(0, g, p[0].length, l - g), 0, g, p[0].length, l - g));

                l = g;
                g = p.length;
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
    public static List<SpriteImage> getSlicedImagesByAlphaAxisX(BufferedImage b) {
        List<SpriteImage> r = new ArrayList<>();
        int p[][] = getImagePixels(b);

        int g = -1;
        int l = p[0].length;

        for (int x = 0; x < p[0].length; ++x) {

            boolean isAlphaLine = true;

            for (int y = 0; y < p.length; ++y) {

                if (y == p.length || (p[y][x] >> 24) != 0x00) {
                    isAlphaLine = false;

                    g = Math.max(x, g);
                    l = Math.min(x, l);
                }
            }

            if (l != p[0].length && isAlphaLine) {
                
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
    }
}
