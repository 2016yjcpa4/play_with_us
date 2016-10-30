package com.github.yjcpaj4.play_with_us.resource;

import com.github.yjcpaj4.play_with_us.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class SoundResource implements ResourceManager.Resource {

    public static final int PLAY_ONCE = 1;
    public static final int PLAY_INFINITY = -1;
    
    private AudioInputStream mStream;
    private SourceDataLine mDataLine;
    
    @Override
    public void load(File f, String k) throws IOException {
        try {
            mStream = AudioSystem.getAudioInputStream(f);
            mDataLine = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, mStream.getFormat()));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void play() {
        play(PLAY_ONCE);
    }
    
    public void play(int count) {
        new Thread() {
            
            @Override
            public void run() {
                try {
                    mDataLine.open(mStream.getFormat());
                    mDataLine.start();

                    int n = 0;
                    byte[] b = new byte[1024 * 4];
                    while ((n = mStream.read(b, 0, b.length)) != -1) {
                        mDataLine.write(b, 0, n);
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (count > 0 || count == PLAY_INFINITY) {
                        play(count - 1);
                    }
                }
            }
        }.start();
    }
    
    public void stop() {
        mDataLine.stop();
    }

    @Override
    public void release() {
    }
    
    public static boolean canLoad(File f) {
        return Arrays.asList("mp3", "ogg", "wma", "wav").contains(FileUtil.getExtension(f));
    }
}
