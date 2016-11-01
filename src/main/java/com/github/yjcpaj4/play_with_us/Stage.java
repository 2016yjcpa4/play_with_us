package com.github.yjcpaj4.play_with_us;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Stage.
 * 
 * 프로그램의 화면단위를 Stage 라고 합니다.
 * Stage 에서는 init, draw, finish 라는 생명주기를 가지고있습니다.
 */
public abstract class Stage {
    
    private final Application mContext;
    private final Canvas mCanvas;
    
    protected Stage(Application c) {
        mContext = c;
        mCanvas = c.getCanvas();
    }
    
    public Application getContext() {
        return mContext;
    }
    
    public Canvas getCanvas() {
        return mCanvas;
    }
    
    public void finishStage() {
        mContext.finishStage(this);
    }
    
    public void showStage(Class<? extends Stage> s) {
        mContext.showStage(s);
    }
    
    public void showStage(Stage s) {
        mContext.showStage(s);
    }
    
    protected void init() {
        // TODO
    }
    
    protected void draw(long delta, Graphics2D g2d) {
        // TODO
    }
    
    protected void pause() {
        // TODO 끝난건 아니지만 다른화면이 위에 올라온경우...
    }

    protected void finish() {
        // TODO
    }
    
}
