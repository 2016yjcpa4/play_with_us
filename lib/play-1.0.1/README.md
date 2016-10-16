# play

Play - is a Java library to play Video / Music.

It would be based on VLC or multi based on VLC / QTMovie / gstreams.

Right now I'm testing VLC on all platroms, but if it fails. I will support platform dependent librarys (Windows - VLC,
Mac - QTMovie, Linux - gstreams).

Why another Java Video Library?

- VLCJ - windows library, never been tested on Mac and Linux platform. It is worth mentioning author do not care about community, unless you have paid subscription. Also I'm sure VLCJ will never be released under LGPL licence.

YouTube: https://www.youtube.com/watch?v=h0rdCroOO3c

# Features
  - native maven support (take it from central repo!)
  - native librarys in place (no longer need to search for vlc.dll or libvlc.so!)
  - support for all platforms (win, linux, mac)
  - native stream support (InputStream to the VLC!)

# This library just works!

  - Windows (x32)
  - Windows (x64)
  - Linux (x32)
  - Linux (x64)
  - Mac OS X (x64)

## Example Sound Player

    package com.github.axet.play;
    
    import java.awt.BorderLayout;
    import java.io.File;
    
    import javax.swing.JFrame;
    import javax.swing.JProgressBar;
    import javax.swing.SwingUtilities;
    
    public class TestSoundFile extends JFrame {
        JProgressBar progressBar;
    
        public TestSoundFile() {
            progressBar = new JProgressBar();
            progressBar.setMinimum(0);
            progressBar.setMaximum(100);
    
            getContentPane().add(progressBar, BorderLayout.CENTER);
    
            setSize(300, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    
        VLC p = new VLC();
    
        public void run(File f) {
            p.addListener(new VLC.Listener() {
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
    
            p.open(f);
            System.out.println("run play");
            p.play();
            p.setPosition(0.99f);
        }
    
        public static void main(String[] args) {
            TestSoundFile t = new TestSoundFile();
            File f = new File(args[0]);
            t.run(f);
        }
    }

## Example Sound InputStream

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

## Example Video Player

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

## Example Video InputStream

    package com.github.axet.play;
    
    import java.awt.BorderLayout;
    import java.awt.Canvas;
    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileNotFoundException;
    import java.io.InputStream;
    
    import javax.swing.JFrame;
    import javax.swing.SwingUtilities;
    
    public class TestVideoSteam extends JFrame {
        VLC c;
        Canvas cc;
    
        public TestVideoSteam() {
            c = new VLC();
    
            c.addListener(new VLC.Listener() {
                @Override
                public void position(final float pos) {
                    System.out.println("no position event for inputstream possible");
                }
    
                @Override
                public void stop() {
                    System.out.println("actual streaming stop");
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            c.close();
                            dispose();
                        }
                    });
                }
    
                @Override
                public void start() {
                    System.out.println("actual streaming start");
                }
            });
    
            cc = new Canvas();
            c.setVideoCanvas(cc);
    
            getContentPane().add(cc, BorderLayout.CENTER);
    
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
            setSize(500, 500);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    
        public void open(InputStream is) {
            c.open(is);
            c.play();
        }
    
        public static void main(String[] args) {
            String name = args.length == 0 ? "test.mp3" : args[0];
    
            File f = new File(name);
    
            InputStream is = null;
            try {
                is = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    
            TestVideoSteam t = new TestVideoSteam();
            t.open(is);
        }
    }

## Central Maven Repo

    <dependency>
      <groupId>com.github.axet</groupId>
      <artifactId>play</artifactId>
      <version>1.0.1</version>
    </dependency>
    
# Licence

LGPL 3.0

Next releases will be compatible with VLC project licence (I will change licence if required)
