package com.github.yjcpaj4.play_with_us.activity;

import com.github.axet.play.VLC;
import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.map.Map;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.io.File;

public class VideoActivity extends Activity implements VLC.Listener {
    
    public static final String KEY_PATHNAME = "pathname";

    private Canvas mCanvas;
    private VLC mVLC = new VLC();
    
    private Application mApp = Application.getInstance();
    
    public VideoActivity(Canvas c) {
        mCanvas = c;
        mVLC.addListener(this);
    }
    
    @Override
    public void onStart(Param o) {
        mApp.pause(); // Video 부분은 외부라이브러리를 사용하다보니 GraphicLooper 를 잠시 일시정지 시킵니다.
        
        mVLC.setVideoCanvas(mCanvas);
        
        String s = o.getString(KEY_PATHNAME);
        
        mVLC.open(new File(s));
        mVLC.play();
    }

    @Override
    public void onDraw(long delta, Graphics2D g2d) {
    }

    @Override
    public void onPause() {
        mVLC.close();
        
        mApp.resume();
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void position(float n) {
    }
}
