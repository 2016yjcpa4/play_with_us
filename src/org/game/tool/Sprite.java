/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.tool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.game.GameCanvas;

public class Sprite {

    private final Map<String, BufferedImage> imageMap;

    private JList list;
    
    private LinkedList<BufferedImage> imgs = new LinkedList<>();
    
    public Sprite() {
        imageMap = createImageMap();
        list = new JList(imageMap.keySet().toArray(new String[0]));
        list.setCellRenderer(new CheckAndImageListRenderer());
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); 
        list.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(0);
        list.setPreferredSize(new Dimension(600, 300));
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
            
                imgs.clear();
                
                for(Object s :  list.getSelectedValuesList()) {
                    imgs.add(imageMap.get(s));
                }
            }
        });
        
        GameCanvas cv = new GameCanvas() {
            
            private int i = 0;
            
            @Override
            protected void draw(Graphics2D g2d) {
                super.draw(g2d); //To change body of generated methods, choose Tools | Templates.
            
                if (!imgs.isEmpty()) {
                    
                    i += delta;
                    
                    if (i > (1000 * 0.5)) {
                    
                        i = 0;
                        
                        imgs.offer(imgs.poll());
                    }
                    

                    g2d.drawImage(imgs.peek(), 0, 0, null);

                }
            }
        };
 
        JFrame frame = new JFrame();
        frame.add(new JScrollPane(list), BorderLayout.WEST);
        frame.add(cv.getCanvas(), BorderLayout.CENTER); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack(); 
        frame.setVisible(true);
    }
    
    public class CheckAndImageListRenderer extends JPanel implements ListCellRenderer {

        private final JLabel label = new JLabel();
        private final JCheckBox check = new JCheckBox();

        @Override
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            removeAll();

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            check.setComponentOrientation(list.getComponentOrientation());
            check.setFont(list.getFont());
            check.setForeground(getForeground());
            check.setBackground(getBackground());
            check.setSelected(isSelected);
            check.setEnabled(list.isEnabled());
            if (isSelected)
                check.setText(Integer.toString(list.getSelectedValuesList().indexOf(value) + 1));
            else 
                check.setText("");
            label.setIcon(new ImageIcon(imageMap.get(value)));

            add(check);
            add(label);

            return this;
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Sprite();
            }
        });
    }

    private Map<String, BufferedImage> createImageMap() {
        Map<String, BufferedImage> map = new LinkedHashMap<>();

        try {

            BufferedImage bi = ImageIO.read(new File("./res/test.jpg"));//c.getSelectedFile());

            List<BufferedImage> b = new ArrayList<>();

            for (BufferedImage bi2 : getImagesAxisX(bi)) { // Y 축을 기준으로 이미지를 잘라버림

                for (BufferedImage bi3 : getImagesAxisY(bi2)) { // X 축을 기준으로 이미지를 잘라버림

                    b.add(bi3);
                }
            }

            int i = 0;

            for (BufferedImage _b : b) {
                map.put(Integer.toString(i++), _b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    private static int[][] getPixels(BufferedImage bi) {

        int w = bi.getWidth();
        int h = bi.getHeight();

        int[][] r = new int[h][w];

        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < h; ++y) {

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

        for (int y = 0; y < h; ++y) {

            boolean isAlphaLine = true;

            for (int x = 0; x < w; ++x) {

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

        for (int x = 0; x < w; ++x) {

            boolean isAlphaLine = true;

            for (int y = 0; y < h; ++y) {

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
