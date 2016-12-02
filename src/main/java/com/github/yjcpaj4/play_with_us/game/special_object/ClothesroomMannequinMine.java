package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.game.object.Player;
import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Box2D;
import java.awt.Color;
import java.awt.Graphics2D;

public class ClothesroomMannequinMine extends GameObject {
    
    private static final int X = 185;
    private static final int Y = 445;
    private static final int WIDTH = 100;
    private static final int HEIGHT = 70;
    
    private Polygon mCollider = new Box2D(X, Y, WIDTH, HEIGHT).toPolygon();
    
    private boolean mRunned = false;
    
    public ClothesroomMannequinMine() {
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        
        Player p = g.getPlayer();
        
        if ( ! mRunned) {
            if (p.hasKitchenKey()) {
                if (CollisionDetection.getCollision(p.getCollider(), mCollider) != null) {
                    ClothesroomMannequin o = g.getMap().getFirstObjectByClass(ClothesroomMannequin.class);

                    o.setSuprise();

                    p.setInputDisable();
                    p.setIdle();

                    g.getResource().getSound("snd.obj.clothesroom.mannequin").play();

                    mRunned = true;
                }
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
