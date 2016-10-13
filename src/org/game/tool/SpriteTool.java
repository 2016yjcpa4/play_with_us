package org.game.tool;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.game.GameLoop;

public class SpriteTool { 

    private JList imgs;
    private JFrame window;
    private AnimateCanvas canvas = new AnimateCanvas();

    public SpriteTool() {
        JPanel p1 = new JPanel();
        {
            p1.setBounds(10, 10, 200, 280);
        }

        JPanel p2 = new JPanel();
        JList l1;
        {
            p2.setBounds(220, 10, 790, 280);
            p2.setLayout(null);
            
            {
                l1 = new JList(new DefaultListModel());
                l1.setCellRenderer(new ImageSelectorListRenderer());
                l1.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
                l1.setVisibleRowCount(1);
                l1.addListSelectionListener(new ListSelectionListener() {
                
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if(e.getValueIsAdjusting()) {
                            DefaultListModel m = ((DefaultListModel) l1.getModel());
                            m.removeElement(l1.getSelectedValue());

                            canvas.setFrameImages(getImagesByJList(l1));
                        }
                    }
                });
                l1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                l1.setBounds(0, 0, 560, 220);
                l1.setFixedCellHeight(l1.getHeight());
                
                JScrollPane sp = new JScrollPane(l1); 
                sp.setBounds(10, 10, 560, 220);
                p2.add(sp);
                
                JLabel l2 = new JLabel("위 목록의 항목을 클릭할 경우 삭제가 됩니다.");
                l2.setBounds(10, 240, 560, 25);
                p2.add(l2);
                
                
                JButton b = new JButton("전체삭제");
                b.setBounds(450, 240, 120, 25);
                b.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DefaultListModel m = (DefaultListModel) l1.getModel();
                        m.removeAllElements();
                        
                        canvas.setFrameImages(getImagesByJList(l1));
                    }
                });
                p2.add(b);
            }

            {
                Canvas c = canvas.getCanvas();
                c.setBounds(580, 10, 200, 150);
                p2.add(c);
            }

            {
                JLabel l2 = new JLabel("FPS");
                l2.setBounds(580, 175, 150, 25);
                p2.add(l2);

                DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.getDefault());
                df.setGroupingUsed(false);
                JTextField tf1 = new JFormattedTextField(df); 
                tf1.setBounds(620, 175, 160, 25);
                tf1.addKeyListener(new KeyAdapter() {
                    
                    @Override
                    public void keyPressed(KeyEvent evt) { 
                        
                        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                           
                            try {
                                float n = Float.parseFloat(tf1.getText().toString());
                                
                                canvas.setFrameDelay(n);
                            }
                            catch(NumberFormatException e) {
                                tf1.setText("");
                            }
                        }
                    }
                });
                p2.add(tf1);

                JLabel l3 = new JLabel("이름");
                l3.setBounds(580, 205, 150, 25);
                p2.add(l3);

                JTextField tf2 = new JTextField();
                tf2.setBounds(620, 205, 160, 25);
                p2.add(tf2);
            }

            {
                JButton b1 = new JButton("추가");
                b1.setBounds(580, 240, 60, 25);
                p2.add(b1);

                JButton b2 = new JButton("수정");
                b2.setBounds(650, 240, 60, 25);
                p2.add(b2);

                JButton b3 = new JButton("삭제");
                b3.setBounds(720, 240, 60, 25);
                p2.add(b3);
            }
        }

        JPanel p3 = new JPanel();
        {
            p3.setBounds(10, 300, 1000, 500);
            p3.setLayout(new BorderLayout());

            imgs = new JList(new DefaultListModel());
            imgs.setCellRenderer(new ImageSelectorListRenderer());
            imgs.addListSelectionListener(new ListSelectionListener() {
                
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if(e.getValueIsAdjusting()) {
                        DefaultListModel m = ((DefaultListModel) l1.getModel());

                        for(Object o : imgs.getSelectedValuesList()) {
                            m.addElement(o);
                        }

                        canvas.setFrameImages(getImagesByJList(l1));
                    }
                }
            });
            imgs.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
            imgs.setVisibleRowCount(0);
            imgs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            p3.add(imgs, BorderLayout.CENTER);
        }

        window = new JFrame();
        window.setLayout(null);
        window.add(p1);
        window.add(p2);
        window.add(p3);
        
         

        JMenuBar mb = new JMenuBar(); 
        {
            JMenu m = new JMenu("파일");

            JMenuItem m1 = new JMenuItem("불러오기");
            m1.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    loadResource();
                }
            });

            JMenuItem m2 = new JMenuItem("리사이징");
            m2.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(window, "준비중");
                }
            });

            m.add(m1);
            m.add(m2);
            mb.add(m);
        }


        window.setJMenuBar(mb); 
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setSize(1025, 835);
        window.setResizable(false);
        window.setVisible(true);
        
        loadResource();
    }
    
    private void loadResource() {
        JFileChooser fc = new JFileChooser();
        int n = fc.showOpenDialog(window);

        if (n == JFileChooser.APPROVE_OPTION) {
            DefaultListModel m = ((DefaultListModel) imgs.getModel());
            m.removeAllElements();
            
            for(BufferedImage b : getSlicedImagesByAlphaLines(fc.getSelectedFile())) {
                m.addElement(b);
            }
        } else {
            // TODO ...
        }
    }
    
    private static List<BufferedImage> getImagesByJList(JList l) {

        DefaultListModel m = ((DefaultListModel) l.getModel());

        List<BufferedImage> r = new ArrayList();

        for(int n = 0; n < m.size(); ++n) {
            r.add((BufferedImage) m.get(n));
        }
        
        return r;
    }

    public class ImageSelectorListRenderer extends JPanel implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList v, Object o, int n, boolean s, boolean f) {
            removeAll();
            setLayout(new OverlayLayout(this));
            setBorder(new EmptyBorder(20, 20, 20, 20));
            
            BufferedImage b = (BufferedImage) o;
            
            if (s) {
                setBackground(v.getSelectionBackground());
                setForeground(v.getSelectionForeground());
            } else {
                setBackground(v.getBackground());
                setForeground(v.getForeground());
            }
            
            JLabel l = new JLabel();
            l.setIcon(new ImageIcon(b));

            add(l);

            return this;
        }
    }

    public static void main(String[] args) {
        new SpriteTool(); 
    }

    /**
     * 픽셀단위로 배열을 가져옵니다.
     * 
     * @param b
     * @return 
     */
    private static int[][] getImagePixels(BufferedImage b) {

        int w = b.getWidth();
        int h = b.getHeight();

        int[][] r = new int[h][w];

        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < h; ++y) {

                int p[] = b.getRaster().getPixel(x, y, new int[4]);

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

    /**
     * 바둑판형태로 이미지되어있는것을 목록형태로 가져옵니다.
     * 
     * @param b
     * @return 
     */
    private List<BufferedImage> getSlicedImagesByAlphaLines(File f) {
        List<BufferedImage> l = new ArrayList<>();
        
        BufferedImage b = null;

        try {
            b = ImageIO.read(f);
        } 
        catch (IOException e) {
            e.printStackTrace();
            return l;
        }

        for (BufferedImage x : getSlicedImagesByAlphaAxisX(b)) { // X 축을 기준으로 이미지를 잘라버림
            for (BufferedImage y : getSlicedImagesByAlphaAxisY(x)) { // Y 축을 기준으로 이미지를 잘라버림
                l.add(y);
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
    private static List<BufferedImage> getSlicedImagesByAlphaAxisY(BufferedImage b) {
        final List<BufferedImage> r = new ArrayList<>();
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
                r.add(b.getSubimage(0, g, p[0].length, l - g));

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
    private static List<BufferedImage> getSlicedImagesByAlphaAxisX(BufferedImage b) {
        List<BufferedImage> r = new ArrayList<>();
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
                r.add(b.getSubimage(l, 0, g - l, p.length));

                g = l;
                l = p[0].length;
            }
        } 

        return r;
    }
    
    private static class AnimateCanvas extends GameLoop {
        
        private long delta = 0;
        private float delay = 0;
        private LinkedList<BufferedImage> frame = new LinkedList<>();
        
        public void setFrameImages(List<BufferedImage> l) {
            frame.clear();
            frame.addAll(l);
        }
        
        public void setFrameDelay(float n) {
            delay = n;
        }

        @Override
        protected void draw(Graphics2D g2d) {
            super.draw(g2d);

            if ( ! frame.isEmpty()) {

                delta += getDelta();

                if (delta > (1000 * delay)) {
                    delta = 0;

                    frame.offer(frame.poll());
                }

                BufferedImage b = frame.peek();
                int x = canvas.getWidth() / 2 - b.getWidth() / 2;
                int y = canvas.getHeight() / 2 - b.getHeight() / 2;

                g2d.drawImage(b, x, y, null);
            }
        }
    }
}
