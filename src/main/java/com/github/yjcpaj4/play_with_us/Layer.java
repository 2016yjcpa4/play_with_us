package com.github.yjcpaj4.play_with_us;

import java.awt.Canvas;
import java.awt.Graphics2D;

/**
 * Stage.
 * 
 * 프로그램의 화면단위를 Stage 라고 합니다.
 * Stage 에서는 init, draw, finish 라는 생명주기를 가지고있습니다.
 */
public abstract class Layer {
    
    private boolean mRunning = false;
    
    private final CanvasApplication mContext;
    private final Canvas mCanvas;
    
    protected Layer(CanvasApplication c) {
        mContext = c;
        mCanvas = c.getCanvas();
    }
    
    public CanvasApplication getContext() {
        return mContext;
    }
    
    public Layer getApplicationLayer(Class<? extends Layer> cls) {
        return mContext.getLayer(cls);
    }
    
    public Canvas getApplicationCanvas() {
        return mCanvas;
    }
    
    public void finishStage() {
        mContext.finishLayer(this);
    }
    
    public void showStage(Class<? extends Layer> cls) {
        mContext.showLayer(getApplicationLayer(cls));
    }
    
    public void showStage(Layer s) {
        mContext.showLayer(s);
    }
    
    protected void draw(long delta, Graphics2D g2d) {
        // TODO
    }
    
    protected boolean isRunning() {
        return mRunning;
    }
    
    protected void pause() {
        mRunning = false;
        // TODO 끝난건 아니지만 다른화면이 위에 올라온경우...
    }
    
    protected void resume() {
        mRunning = true;
        // TODO 초기화 진행후 GraphicLooper 에서 resume 을 호출한뒤 스테이지의 resume 도 호출
    }

    protected void finish() {
        // TODO
    }
    
}
