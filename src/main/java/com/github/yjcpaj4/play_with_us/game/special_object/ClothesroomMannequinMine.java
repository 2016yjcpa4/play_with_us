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
import java.util.Timer;

public class ClothesroomMannequinMine extends GameObject {
    
    private static final int X = 252 - 75 + 8;
    private static final int Y = 469 - 100 + 66 + 20 - 10;
    private static final int WIDTH = 100;
    private static final int HEIGHT = 70;
    
    private Polygon mCollider = new Box2D(X, Y, WIDTH, HEIGHT).toPolygon();
    
    private boolean mOnMine = false;
    
    public ClothesroomMannequinMine() {
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        
        Player p = g.getPlayer();
        
        if (p.hasKitchenKey() && !mOnMine) {
            if (CollisionDetection.getCollision(p.getCollider(), mCollider) != null) {
                ClothesroomMannequin o = g.getMap().getFirstObjectByClass(ClothesroomMannequin.class);
                
                o.setSuprise();

                p.setInputDisable();
                p.setIdle();

                g.getResource().getSound("snd.obj.clothesroom.mannequin").play();

                mOnMine = true;
            }
        }
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        if (Application.DEBUG) {
            g2d.setColor(Color.RED);
            g2d.drawPolygon(mCollider.toAWTPolygon());
        }
    }
}
