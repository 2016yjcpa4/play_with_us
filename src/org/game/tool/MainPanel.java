/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.tool;

//-*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@ 
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public final class MainPanel extends JPanel {

    private MainPanel() {
        super(new BorderLayout());

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String t : Arrays.asList("aa", "bbbbbbbbbbbbb", "ccc", "dddddddddddddddd", "eeeeeee")) {
            model.addElement(t);
        }
        JList<String> list = new LinkCellList<>(model);
        add(new JScrollPane(list));
        setPreferredSize(new Dimension(320, 240));
    }

    public static void main(String... args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame("@title@");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class LinkCellList<E> extends JList<E> {

    private int prevIndex = -1;

    protected LinkCellList(ListModel<E> model) {
        super(model);
    }

    @Override
    public void updateUI() {
        setForeground(null);
        setBackground(null);
        setSelectionForeground(null);
        setSelectionBackground(null);
        super.updateUI();
        setFixedCellHeight(32);
        setCellRenderer(new LinkCellRenderer<E>());
        //TEST: putClientProperty("List.isFileList", Boolean.TRUE);
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        Point pt = e.getPoint();
        int i = locationToIndex(pt);
        E s = getModel().getElementAt(i);
        Component c = getCellRenderer().getListCellRendererComponent(this, s, i, false, false);
        Rectangle r = getCellBounds(i, i);
        c.setBounds(r);
        if (prevIndex != i) {
            c.doLayout();
        }
        prevIndex = i;
        pt.translate(-r.x, -r.y);
        setCursor(Optional.ofNullable(SwingUtilities.getDeepestComponentAt(c, pt.x, pt.y))
                .map(Component::getCursor)
                .orElse(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)));
    }
}

class LinkCellRenderer<E> implements ListCellRenderer<E> {

    private final JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JCheckBox check = new JCheckBox("check") {
        @Override
        public void updateUI() {
            super.updateUI();
            setOpaque(false);
        }
    };
    private final JButton button = new JButton("button") {
        @Override
        public void updateUI() {
            super.updateUI();
        }
    };
    private final JLabel label = new JLabel() {
        @Override
        public void updateUI() {
            super.updateUI();
        }
    };

    @Override
    public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
        p.removeAll();
        p.add(label);
        p.add(check);
        p.add(button);
        p.setOpaque(true);
        if (isSelected) {
            p.setBackground(list.getSelectionBackground());
            p.setForeground(list.getSelectionForeground());
        } else {
            p.setBackground(list.getBackground());
            p.setForeground(list.getForeground());
        }
        label.setText("<html><a href='#'>" + value);
        return p;
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
