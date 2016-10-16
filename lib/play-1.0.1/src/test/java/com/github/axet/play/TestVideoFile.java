package com.github.axet.play;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class TestVideoFile extends JFrame {
    VLC vlc;
    Canvas c;
    JProgressBar progressBar;

    static String ms2time(long ms) {
        long second = (ms / 1000) % 60;
        long minute = (ms / (1000 * 60)) % 60;
        long hour = (ms / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public TestVideoFile() {
        progressBar = new JProgressBar();
        getContentPane().add(progressBar, BorderLayout.SOUTH);

        vlc = new VLC();

        vlc.addListener(new VLC.Listener() {
            @Override
            public void position(final float pos) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setValue((int) (100.0 * pos));
                    }
                });
            }

            @Override
            public void stop() {
                System.out.println("actual streaming stop");

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        vlc.close();
                        dispose();
                    }
                });
            }

            @Override
            public void start() {
                System.out.println("actual streaming start");
            }
        });

        c = new Canvas();

        getContentPane().add(c, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        vlc.setVideoCanvas(c);
    }

    public void run(File f) {
        vlc.open(f);
        System.out.println("run play");
        vlc.play();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String name = args.length == 0 ? "test.mp3" : args[0];

        File f = new File(name);
        TestVideoFile t = new TestVideoFile();
        t.run(f);
    }
}
