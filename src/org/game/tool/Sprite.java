/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.tool;

import java.awt.BorderLayout;
import java.awt.Canvas;
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
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.game.GameCanvas;

public class Sprite {
    
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    private Map<String, BufferedImage> imageMap;

    private JList list;

    private LinkedList<BufferedImage> imgs = new LinkedList<>();

    public Sprite() {
        
        JPanel p1 = new JPanel();
        {
            p1.setBackground(Color.red);
            p1.setBounds(10, 10, 200, 280);
        }
        
        JPanel p2 = new JPanel();
        {
            p2.setBackground(Color.blue);
            p2.setBounds(220, 10, 790, 280);
            p2.setLayout(null);

            Canvas c = new Canvas();
            c.setBackground(Color.red);
            c.setBounds(630, 10, 150, 150);
            p2.add(c);

            JLabel l1 = new JLabel("FPS");
            l1.setBounds(630, 175, 150, 25);
            p2.add(l1);

            JTextField tf1 = new JTextField();
            tf1.setBounds(670, 175, 110, 25);
            p2.add(tf1);

            JLabel l2 = new JLabel("이름");
            l2.setBounds(630, 205, 150, 25);
            p2.add(l2);

            JTextField tf2 = new JTextField();
            tf2.setBounds(670, 205, 110, 25);
            p2.add(tf2);
        }
        
        JPanel p3 = new JPanel();
        p3.setBackground(Color.black);
        p3.setBounds(10, 300, 1000, 500);
        
        
        JFrame f = new JFrame();
        f.setLayout(null);
        f.add(p1);
        f.add(p2);
        f.add(p3);
        
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack(); 
        f.setSize(1025, 835);
        f.setResizable(false);
        f.setVisible(true);
        
        /*imageMap = createImageMap();
        list = new JList(imageMap.keySet().toArray(new String[0]));
        list.setCellRenderer(new CheckAndImageListRenderer());
        list.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(0);
        list.setBounds(0, 0, 300, 300);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                imgs.clear();

                for (Object s : list.getSelectedValuesList()) {
                    imgs.add(imageMap.get(s));
                }
            }
        });
        
        JPanel jp = new JPanel(); 
        jp.setBackground(Color.red);
        
        JPanel jp3 = new JPanel();
        jp3.setLayout(null);
        
        final Canvas c = new GameCanvas() {

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
                   
                   BufferedImage b = imgs.peek();

                   g2d.drawImage(b, canvas.getWidth() / 2 - b.getWidth() / 2,  canvas.getHeight()/ 2 - b.getHeight() / 2, null);

               }
           }
       }.getCanvas();
        c.setBounds(0, 0, 150, 150); 
        
        jp3.add(c);
        
        JList list2 = new JList(new String[] { "A", "B", "C"});
        list2.setBounds(0, 0, 400, 150);
        list2.setBackground(Color.black);
        list2.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list2.setEnabled(false);
        list2.setVisibleRowCount(1); 
        jp.setLayout(null);
        jp.add(list2, BorderLayout.CENTER);
        
        jp.add(jp3, BorderLayout.EAST);
        
        JScrollPane sp = new JScrollPane(list);
        sp.setBounds(0, 300, WINDOW_WIDTH, 500);
        
        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.add(jp);
        frame.add(sp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack(); 
        frame.setSize(1280, 800);
        frame.setVisible(true); */
    }

    public class CheckAndImageListRenderer extends JPanel implements ListCellRenderer {

        private final JLabel l = new JLabel();

        @Override
        public Component getListCellRendererComponent(JList v, Object o, int n, boolean s, boolean f) {

            removeAll();

            if (s) {
                setBackground(v.getSelectionBackground());
                setForeground(v.getSelectionForeground());
            } else {
                setBackground(v.getBackground());
                setForeground(v.getForeground());
            }

            l.setIcon(new ImageIcon(imageMap.get(o)));

            add(l);

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

                int n = 0;
                n += (((int) p[0] & 0xff) << 24); // alpha
                n += ((int) p[1] & 0xff); // blue
                n += (((int) p[2] & 0xff) << 8); // green
                n += (((int) p[3] & 0xff) << 16); // red
                r[y][x] = n;
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
