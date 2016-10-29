/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.yjcpaj4.play_with_us.tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color; // 배경색 불러오는 메소드
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class NewClass extends JFrame {

    public static void main(String[] args) {

        try {

            File f = new File("asdf.wav");
            AudioInputStream s = AudioSystem.getAudioInputStream(f);
            SourceDataLine l = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, s.getFormat()));
            
            l.open(s.getFormat());
            l.start();

            int n = 0;
            byte[] b = new byte[1024 * 4];
            while ((n = s.read(b, 0, b.length)) != -1) {
                l.write(b, 0, n);
            }

        } catch (Exception E) {
            System.out.println(E.getMessage());

        }

    }

    public void play() {
        new Thread() {
            public void run() {
                try {

                    File f = new File("asdf.wav");
                    AudioInputStream audio = AudioSystem.getAudioInputStream(f);
                    AudioFormat format;
                    format = audio.getFormat();
                    SourceDataLine auline;
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                    auline = (SourceDataLine) AudioSystem.getLine(info);
                    auline.open(format);
                    auline.start();

                    int nBytesRead = 0;
                    byte[] abData = new byte[524288];
                    while (nBytesRead != -1) {
                        nBytesRead = audio.read(abData, 0, abData.length);

                        if (nBytesRead >= 0) {
                            auline.write(abData, 0, nBytesRead);
                        }
                    }

                } catch (Exception E) {
                    System.out.println(E.getMessage());

                }
            }
        }.start();
    }

}
