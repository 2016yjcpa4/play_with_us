package org.game.map;

import java.awt.geom.Area;
import org.game.math.Point2D;

public class Light {

    private Point2D mPosition; // 위치
    private double mAngle; // 각도
    private double mLength; // 빛의 길이
    private double mExtent; // 빛의 범위
    
    public Point2D getPosition() {
        return mPosition;
    }
    
    public double getAngle() {
        return mAngle;
    }
    
    public double getLength() {
        return mLength;
    }
    
    public double getExtent() {
        return mExtent;
    }
}
