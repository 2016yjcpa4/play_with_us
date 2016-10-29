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
        play(0);
    }
    
    public void play(int count) {
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
