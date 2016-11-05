package com.github.yjcpaj4.play_with_us;

import java.awt.Canvas;
import java.awt.Graphics2D;

/**
 * Layer.
 * 
 * CanvasApplication 에서 화면단위를 Layer 라고 합니다.
 * Layer 에서는 resume, draw, pause, finish 라는 생명주기를 가지고있습니다.
 * 이 생명주기는 CanvasApplication 에서 관리되고 있습니다.
 * 
 * @see Application
 * @author 차명도.
 */
public abstract class Layer {
    
    private boolean mRunning = false;
    
    private final Application mContext;
    private final Canvas mCanvas;
    
    protected Layer(Application c) {
        mContext = c;
        mCanvas = c.getCanvas();
    }
    
    public Application getContext() {
        return mContext;
    }
    
    public Layer getApplicationLayer(Class<? extends Layer> cls) {
        return mContext.getLayer(cls);
    }
    
    public Canvas getApplicationCanvas() {
        return mCanvas;
    }
    
    public void finishLayer() {
        mContext.finishLayer(this);
    }
    
    public void showLayer(Class<? extends Layer> cls) {
        mContext.showLayer(cls);
    }
    
    public void showLayer(Layer l) {
        mContext.showLayer(l);
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
