/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.util.ArrayList;
import java.util.List;
import org.game.DrawableObject;
import org.game.GameLoop;
import org.game.geom.Polygon;
import org.game.geom.Raycast;
import static org.game.map.Map.MAP_HEIGHT;
import static org.game.map.Map.MAP_WIDTH;
import org.game.math.Point2D;

public class Light implements DrawableObject {

    private Map map;
    private Point2D pos = new Point2D();
    
    public Light(Map map) {
        pos.set(1000, 400);
        this.map = map;
    }
    
    @Override
    public void update(GameLoop g) {
        
    }
    
    private Polygon projectLight() { 
        
        List<Point2D> l = new ArrayList<>();
        l.add(pos);
        
        for (Point2D e : Raycast.getRaycast(pos, Math.toRadians(50), map.getWall2())) {  
            l.add(e);    
        }
        
        return new Polygon(l); 
    }
    
    private java.awt.Polygon pr() {
        Polygon p = projectLight();
        
        return new java.awt.Polygon(p.getXPoints(), p.getYPoints(), p.getPoints().size());
    }

    @Override
    public void draw(GameLoop g, Graphics2D g2d) {
        //원본 맵을 그리고
        map.draw(g, g2d);
            
        float dark = 0.90f;
        
        // 검은 마스크를 씌움...
        g2d.setColor(new Color(0, 0, 0, (int)(255 * dark)));
        g2d.fillRect(0, 0, MAP_WIDTH, MAP_HEIGHT);
        
        
        RadialGradientPaint paint = new RadialGradientPaint(pos.getX(), pos.getY(), 100f,
                                                            new float[] { 0.7f, 1f },
                                                            new Color[] {
                                                                new Color(0, 0, 0, (int) (255 * 0)),
                                                                new Color(0, 0, 0, (int) (255 * dark))
                                                            });
        
        java.awt.Polygon arc = pr(); 

        g2d.setClip(arc);
        map.draw(g, g2d); // 밝은부분만 그려짐
        g2d.setPaint(paint);
        g2d.fill(arc);
        g2d.setClip(null);

    }
    
}
