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
public abstract class Layer {
    
    private final Application mContext;
    private final Canvas mCanvas;
    
    protected Layer(Application c) {
        mContext = c;
        mCanvas = c.getCanvas();
    }
    
    public Application getContext() {
        return mContext;
    }
    
    public Layer getApplicationLayer(Class<? extends Layer> c) {
        return mContext.getLayer(c);
    }
    
    public Canvas getApplicationCanvas() {
        return mCanvas;
    }
    
    public void finishStage() {
        mContext.finishLayer(this);
    }
    
    public void showStage(Class<? extends Layer> s) {
        mContext.showLayer(getApplicationLayer(s));
    }
    
    public void showStage(Layer s) {
        mContext.showLayer(s);
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
    
    protected void resume() {
        // TODO 초기화 진행후 GraphicLooper 에서 resume 을 호출한뒤 스테이지의 resume 도 호출
    }

    protected void finish() {
        // TODO
    }
    
}
