/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.tool;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

public class MarioList {

    private final Map<String, ImageIcon> imageMap;

    public MarioList() {
        imageMap = createImageMap();
        JList list = new JList(imageMap.keySet().toArray(new String[0]));
        list.setCellRenderer(new MarioListRenderer());

        JScrollPane scroll = new JScrollPane(list);
        scroll.setPreferredSize(new Dimension(300, 400));

        JFrame frame = new JFrame();
        frame.add(scroll);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public class MarioListRenderer extends JCheckBox implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            
            setComponentOrientation(list.getComponentOrientation());
            setFont(list.getFont());
            setBackground(list.getBackground());
            setForeground(list.getBackground());
            setSelected(isSelected);
            setEnabled(list.isEnabled());
            setIcon(imageMap.get(value));
            return this;
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MarioList();
            }
        });
    }
    
    private Map<String, ImageIcon> createImageMap() {
        Map<String, ImageIcon> map = new LinkedHashMap<>();
        
        
            try {
                
                BufferedImage bi = ImageIO.read(new File("./res/test.jpg"));//c.getSelectedFile());
                
                List<BufferedImage> b = new ArrayList<>();
                
                for(BufferedImage bi2 : getImagesAxisX(bi)) { // Y 축을 기준으로 이미지를 잘라버림
                    
                    for(BufferedImage bi3 : getImagesAxisY(bi2)) { // X 축을 기준으로 이미지를 잘라버림
                        
                        b.add(bi3);
                    }
                }
                
                int i = 0;
                
                for (BufferedImage _b : b) {
                    map.put(Integer.toString(i++), new ImageIcon(_b));
                }
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            
        return map;
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
}