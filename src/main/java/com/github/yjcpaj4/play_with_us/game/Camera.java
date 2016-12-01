package com.github.yjcpaj4.play_with_us.game;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import java.awt.Graphics2D;

public class Camera {

    private Point2D mPos;
    private Application mContext;
    private float mZoom = 1.65f;

    public Camera(Application c) {
        mContext = c;
    }

    public int getWidth() {
        return (int) (mContext.getWidth() / mZoom);
    }

    public int getHeight() {
        return (int) (mContext.getHeight() / mZoom);
    }

    public float getZoom() {
        return mZoom;
    }

    public void update(Point2D p) {
        mPos = p;
    }

    public void draw(Graphics2D g2d) {
        float x = mPos.getX() * mZoom - mContext.getWidth() / 2;
        float y = mPos.getY() * mZoom - mContext.getHeight() / 2;

        g2d.translate(-x, -y);
        g2d.scale(mZoom, mZoom);
    }

    public Point2D getPosition() {
        float x = mPos.getX() - getWidth() / 2;
        float y = mPos.getY() - getHeight() / 2;

        return new Point2D(x, y);
    }
}
