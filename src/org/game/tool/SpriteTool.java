package org.game.tool;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.game.math.Line2D;
import org.game.math.Point2D;

public class SpriteTool implements ActionListener {

    private JFrame window = new JFrame();

    private JButton browse = new JButton("파일찾기");

    private Canvas canvas = new Canvas();

    public SpriteTool() {
        window.add(canvas, BorderLayout.CENTER);
 
        browse.addActionListener(this);
        window.add(browse, BorderLayout.EAST);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1024, 768); 
        window.setVisible(true);
    }

    public static void main(String args[]) {
        new SpriteTool();
    }
    
    private static class Temp {
        
        public int min;
        public int max;
        
        public Temp(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public String toString() {
            return "min = " + min + ", max = " + max;
        }
    }
    
    
    private static int[][] getPixels(BufferedImage bi) {
        
        int w = bi.getWidth();
        int h = bi.getHeight();

        int[][] r = new int[h][w];
 
        for(int x = 0; x < w; ++x) {
            for( int y = 0; y < h; ++y) {
                
                int p[] = bi.getRaster().getPixel(x, y, new int[4]);
                
                int argb = 0;
                argb += (((int) p[0] & 0xff) << 24); // alpha
                argb += ((int) p[1] & 0xff); // blue
                argb += (((int) p[2] & 0xff) << 8); // green
                argb += (((int) p[3] & 0xff) << 16); // red
                r[y][x] = argb;
            }
        }

        return r;
    }
    
    private static List<BufferedImage> getImagesAxisY(BufferedImage bi) {
        
        ArrayList<Temp> xl2 = new ArrayList<>();
        int r[][] = getPixels(bi);

        int w = bi.getWidth();
        int h = bi.getHeight(); 

        int max = -1;
        int min = h;

        for(int y = 0; y < h; ++y) {

            boolean isAlphaLine = true;

            for(int x = 0; x < w; ++x) {


                if ((r[y][x] >> 24) != 0x00) {
                    isAlphaLine = false;

                    max = Math.max(y, max);
                    min = Math.min(y, min);
                }
            }

            if (min != h && isAlphaLine) {
                xl2.add(new Temp(min, max));

                max = min;
                min = h;
            }
        }

        List<BufferedImage> bis = new ArrayList<>();

        for (int i = 0; i < xl2.size(); ++i) {

            Temp t = xl2.get(i);

            bis.add(bi.getSubimage(0, t.min, w, t.max - t.min));
        }

        return bis;
    }
    
    private static List<BufferedImage> getImagesAxisX(BufferedImage bi) {
        
        
        ArrayList<Temp> xl2 = new ArrayList<>();
        int r[][] = getPixels(bi);

        int w = bi.getWidth();
        int h = bi.getHeight(); 

        int max = -1;
        int min = w;


        for(int x = 0; x < w; ++x) {

            boolean isAlphaLine = true;

            for(int y = 0; y < h; ++y) {

                if ((r[y][x] >> 24) != 0x00) {
                    isAlphaLine = false;

                    max = Math.max(x, max);
                    min = Math.min(x, min);
                }
            }

            if (min != w && isAlphaLine) {
                xl2.add(new Temp(min, max));

                max = min;
                min = w;
            }
        }

        List<BufferedImage> bis = new ArrayList<>();

        for (int i = 0; i < xl2.size(); ++i) {

            Temp t = xl2.get(i);
            
            bis.add(bi.getSubimage(t.min, 0, t.max - t.min, h));
        }

        return bis;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

        JFileChooser c = new JFileChooser();
        c.setFileFilter(new FileNameExtensionFilter("이미지 파일", 
                                                    "png", 
                                                    "gif", 
                                                    "jpg", "jpeg",
                                                    "bmp"));
        
        int n = c.showOpenDialog(window);
        if (n == JFileChooser.APPROVE_OPTION) {
            Graphics g = canvas.getGraphics(); 
            try {
                
                BufferedImage bi = ImageIO.read(c.getSelectedFile());
                
                List<BufferedImage> b = new ArrayList<>();
                
                for(BufferedImage bi2 : getImagesAxisX(bi)) { // Y 축을 기준으로 이미지를 잘라버림
                    
                    for(BufferedImage bi3 : getImagesAxisY(bi2)) { // X 축을 기준으로 이미지를 잘라버림
                        
                        b.add(bi3);
                    }
                }
                
                for (BufferedImage _b : b) {
                    g.drawImage(_b, 0, 0, null);
                }
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
        if (n == JFileChooser.CANCEL_OPTION) {
            System.out.println();
        }
    }
}
