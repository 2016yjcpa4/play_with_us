package com.github.yjcpaj4.play_with_us.resource;

import java.io.File;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * 사운드 리소스를 관리하는 클래스입니다.
 * 
 * @author 차명도.
 */
public class SoundResource {

    static {
        /*
         * new JFXPanel() 생성자를 호출하게되면 내부적으로 처리하는 로직중
         * JavaFX 를 초기화작업이 진행됩니다.
         * 이는 사운드 처리에 필요한 MediaPlayer 를 사용할때 전처리로 필요합니다.
         */
        new JFXPanel();
    }
    
    private double mVolume = 1.0;
    private Media mMedia;
    private MediaPlayer mMediaPlayer;
    
    public SoundResource(File f) {
        mMedia = new Media(f.toURI().toString());
    }
    
    public void setVolume(double v) {
        mVolume = v;
    }
    
    public void play() {
        new Thread() {
            
            @Override
            public void run() {
                mMediaPlayer = new MediaPlayer(mMedia);
                mMediaPlayer.setVolume(mVolume);
                mMediaPlayer.play();
            }
        }.start();
    }
    
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }
}
