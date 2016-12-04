package com.github.yjcpaj4.play_with_us.resource;

import java.io.File;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

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
    
    private Media mMedia;
    private MediaPlayer mMediaPlayer;
    
    private Runnable mOnEndOfMedia;
    
    public SoundResource(File f) {
        mMedia = new Media(f.toURI().toString());
    }
    
    public void setVolume(double v) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(v);
        }
    }
    
    public void play() {
        play(1);
    }
    
    public void setOnEndOfMedia(Runnable r) {
        mOnEndOfMedia = r;
    }
    
    public void play(int n) {
        new Thread() {
            
            @Override
            public void run() {
                mMediaPlayer = new MediaPlayer(mMedia);
                mMediaPlayer.setOnEndOfMedia(new Runnable() {
            
                    private int mCount = n;
                    
                    @Override
                    public void run() {
                        mCount--;
                        
                        if (mOnEndOfMedia != null) {
                            mOnEndOfMedia.run();
                        }
                        
                        if (mCount > 0 || n == -1) {
                            mMediaPlayer.seek(Duration.ZERO);
                        }
                    }
                });
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
