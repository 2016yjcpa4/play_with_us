package org.game.tool;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.game.GameLoop;
import org.game.util.ImageUtil;

public class SpriteTool {

    private JList sprites;
    private JList frames; // 작업중인 이미지를 프레임단위로 목록을 보여줌.
    private JFrame window; // 창
    private AnimateCanvas canvas = new AnimateCanvas(); // 작업중인 이미지를 애니메이션 형태로 보여주는 캔버스

    public SpriteTool() {
        JPanel p1 = new JPanel();
        {
            p1.setBounds(10, 10, 200, 280);
            p1.setLayout(new BorderLayout());
            
            sprites = new JList(new DefaultListModel());
            sprites.setBounds(0, 0, 200, 280);
            
            p1.add(new JScrollPane(sprites), BorderLayout.CENTER);
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
                l1.setBounds(0, 0, 570, 240);
                l1.setFixedCellHeight(l1.getHeight());
                
                JScrollPane sp = new JScrollPane(l1); 
                sp.setBounds(0, 0, 570, 240);
                p2.add(sp);
                
                JLabel l2 = new JLabel("위 목록의 항목을 클릭할 경우 삭제가 됩니다.");
                l2.setBounds(10, 250, 560, 25);
                p2.add(l2);
                
                
                JButton b = new JButton("전체삭제");
                b.setBounds(450, 250, 120, 25);
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
                c.setBounds(580, 0, 200, 175);
                p2.add(c);
            }

            JTextField tf1;
            JTextField tf2;
            {
                JLabel l2 = new JLabel("FPS");
                l2.setBounds(580, 185, 150, 25);
                p2.add(l2);

                tf1 = new JTextField(); 
                tf1.setBounds(620, 185, 160, 25);
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

                JLabel l3 = new JLabel("ID");
                l3.setBounds(580, 215, 150, 25);
                p2.add(l3);

                tf2 = new JTextField();
                tf2.setBounds(620, 215, 160, 25);
                p2.add(tf2);
            }

            {
                JButton b1 = new JButton("추가");
                b1.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DefaultListModel m1 = ((DefaultListModel) l1.getModel());

                        List<ImageUtil.SpriteImage> imgs = new ArrayList();
                        int fps = (int) (1000 * Float.parseFloat(tf1.getText()));
                        String id = tf2.getText();

                        for(int n = 0; n < m1.size(); ++n) {
                            imgs.add((ImageUtil.SpriteImage) m1.get(n));
                        }
                        
                        DefaultListModel m2 = (DefaultListModel) sprites.getModel();
                        m2.addElement(new SpriteSheet(imgs, fps, id));
                        
                        // TODO...
                    }
                });
                b1.setBounds(580, 250, 60, 25);
                p2.add(b1);

                JButton b2 = new JButton("수정");
                b2.setBounds(650, 250, 60, 25);
                p2.add(b2);

                JButton b3 = new JButton("삭제");
                b3.setBounds(720, 250, 60, 25);
                p2.add(b3);
            }
        }

        JPanel p3 = new JPanel();
        {
            p3.setBounds(10, 300, 1000, 470);
            p3.setLayout(new BorderLayout());

            frames = new JList(new DefaultListModel());
            frames.setCellRenderer(new ImageSelectorListRenderer());
            frames.addListSelectionListener(new ListSelectionListener() {
                
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if(e.getValueIsAdjusting()) {
                        DefaultListModel m = ((DefaultListModel) l1.getModel());

                        for(Object o : frames.getSelectedValuesList()) {
                            m.addElement(o);
                        }

                        canvas.setFrameImages(getImagesByJList(l1));
                    }
                }
            });
            frames.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
            frames.setVisibleRowCount(0);
            frames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            p3.add(new JScrollPane(frames), BorderLayout.CENTER);
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

            JMenuItem m2 = new JMenuItem("크기조절");
            m2.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(window, "준비중");
                }
            });
            
            JMenuItem m3 = new JMenuItem("시트제작");
            m3.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(window, "준비중");
                }
            });

            m.add(m1);
            m.add(m2);
            m.add(m3);
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
    
    private static class SpriteSheet {
        
        private List<ImageUtil.SpriteImage> imgs;
        private int width;
        private int height;
        private int fps;
        private String id;
        
        public SpriteSheet(List<ImageUtil.SpriteImage> imgs, int fps, String id) {
            this.imgs = imgs;
            this.fps = fps;
            this.id = id;
        }
        
        public int getWidth() {
            return width;
        }
        
        public int getHeight() {
            return height;
        }
        
        public String getId() {
            return id;
        }
        
        public List<ImageUtil.SpriteImage> getSpriteImages() {
            return imgs;
        }
        
        public int getFPS() {
            return fps;
        }
    }
    
    private void loadResource() {
        JFileChooser fc = new JFileChooser();
        int n = fc.showOpenDialog(window);

        if (n == JFileChooser.APPROVE_OPTION) {
            DefaultListModel m = ((DefaultListModel) frames.getModel());
            m.removeAllElements();
            
            for(ImageUtil.SpriteImage s : ImageUtil.getSlicedImagesByAlphaLines(fc.getSelectedFile())) {
                m.addElement(s);
            }
        } else {
            // TODO exception
        }
    }
    
    private static List<BufferedImage> getImagesByJList(JList l) {

        DefaultListModel m = ((DefaultListModel) l.getModel());

        List<BufferedImage> r = new ArrayList();

        for(int n = 0; n < m.size(); ++n) {
            r.add(((ImageUtil.SpriteImage) m.get(n)).getImage());
        }
        
        return r;
    }

    public class ImageSelectorListRenderer extends JPanel implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList v, Object o, int n, boolean s, boolean f) {
            removeAll();
            setLayout(new OverlayLayout(this));
            setBorder(new EmptyBorder(20, 20, 20, 20));
            
            BufferedImage b = ((ImageUtil.SpriteImage) o).getImage();
            
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
