package com.github.yjcpaj4.play_with_us.resource;

import java.io.File;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class SoundResource {

    static {
        new JFXPanel();
    }
    
    private MediaPlayer mMediaPlayer;
    
    public SoundResource(File f) {
        mMediaPlayer = new MediaPlayer(new Media(f.toURI().toString()));
    }
    
    public void play() {
        new Thread() {
            
            @Override
            public void run() {
                mMediaPlayer.play();
            }
        }.start();
    }
    
    public void stop() {
        mMediaPlayer.stop();
    }
}
