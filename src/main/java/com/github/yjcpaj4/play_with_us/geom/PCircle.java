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

/**
 *
 * @author Administrator
 */
public class PCircle extends Polygon {
    
    private static final int EDGES = 32;
    
    private int mRadius;
    
    public int getRadius() {
        return mRadius;
    }
    
    public PCircle(int x, int y, int r) {
        
        mRadius = r;
         
        List<Point2D> vertex = new ArrayList();
        float theta = 0.0f;
        
        vertex.add(new Point2D(x + r, y));
        for (int next = 0; next < EDGES; ++next) {
            
            Vector2D v = new Vector2D(x, y).add(new Vector2D(r * (float) Math.cos(theta), r * (float) Math.sin(theta)));
            
            vertex.add(v.toPoint2D());
            theta += 2.0 * Math.PI / EDGES;
        }
        vertex.add(new Point2D(x + r, y));
        
        this.mPoints.addAll(vertex);
        
        init();
    }
}
