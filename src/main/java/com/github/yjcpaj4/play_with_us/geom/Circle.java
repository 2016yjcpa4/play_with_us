/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.yjcpaj4.play_with_us.geom;

import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import java.util.ArrayList;
import java.util.List;

public class Circle extends Polygon {
    
    private static final int DEFAULT_SEGMENTS = 32;
    
    private float mRadius;
    
    public Circle(float x, float y, float r) {
        super(getCirclePoints(x, y, r, DEFAULT_SEGMENTS));
        
        mRadius = r;
    }
    
    public float getRadius() {
        return mRadius;
    }
    
    /**
     * 원을 다각형으로 만들때 사용하는 함수.
     * 
     * @param x
     * @param y
     * @param r
     * @return 
     */
    private static List<Point2D> getCirclePoints(float x, float y, float r, int s) {
        List<Point2D> l = new ArrayList();
        
        float f = 0.0f;
        
        for (int n = 0; n < s; ++n) {
            float dx = r * (float) Math.cos(f);
            float dy = r * (float) Math.sin(f);
            
            Vector2D v1 = new Vector2D(x, y);
            Vector2D v2 = new Vector2D(dx, dy);
            
            l.add(v1.add(v2).toPoint2D());
            
            f += 2.0 * Math.PI / s;
        }
        
        return l;
    }
}
