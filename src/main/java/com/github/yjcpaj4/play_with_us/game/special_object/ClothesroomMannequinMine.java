package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Box2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;

public class ClothesroomMannequinMine extends GameObject {
    
    private static final int X = 252 - 75 + 8;
    private static final int Y = 469 - 100 + 66 + 10;
    private static final int WIDTH = 100;
    private static final int HEIGHT = 60;
    
    private Polygon mCollider = new Box2D(X, Y, WIDTH, HEIGHT).toPolygon();
    
    public ClothesroomMannequinMine() {
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        Polygon p1 = g.getPlayer().getCollider();
        Polygon p2 = mCollider;
        
        if (CollisionDetection.getCollision(p1, p2) != null) {
            for(GameObject o : g.getMap().getAllObject()) {
                if (o instanceof ClothesroomMannequin) {
                    ClothesroomMannequin m = (ClothesroomMannequin) o;
                    m.setSuprise();
                }
            }
        }
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        if (Application.DEBUG) {
            g2d.setColor(Color.red);
            g2d.drawPolygon(mCollider.toAWTPolygon());
        }
    }
}
