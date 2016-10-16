package com.github.axet.play;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestSoundStream extends JFrame {
    VLC p = new VLC();

    public TestSoundStream() {
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        p.addListener(new VLC.Listener() {
            @Override
            public void position(final float pos) {
            }

            @Override
            public void stop() {
                System.out.println("actual streaming stop");
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        p.close();
                        dispose();
                    }
                });
            }

            @Override
            public void start() {
                System.out.println("actual streaming start");
            }
        });
    }

    public void open(InputStream is) {
        p.open(is);
        p.play();
    }

    public static void main(String[] args) {
        File f = new File(args[0]);
        InputStream is = null;

        try {
            is = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        TestSoundStream t = new TestSoundStream();
        t.open(is);
    }
}
