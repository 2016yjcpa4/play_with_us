package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.layer.InterativeLayer;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Graphics2D;

public class ClothesroomPicture extends GameObject {
    
    private static final int X = 94;
    private static final int Y = 55;
    private static final int RADIUS = 90;
    
    private static final String YES = "살펴본다.";
    private static final String NO = "그만둔다.";
    
    private Circle mCollider = new Circle(X, Y, RADIUS);
    
    public ClothesroomPicture() {
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        if (CollisionDetection.isCollide(mCollider, g.getPlayer().getCollider()) 
         && g.getInput().isKeyOnce(KeyEvent.VK_F)) {
            
            InterativeLayer l = new InterativeLayer(Application.getInstance()) {
                
                @Override
                protected void pause() {
                    super.pause();
                    
                    if (getCurrentAnswer().equals(YES) && !g.getPlayer().hasRoomKey("kitchen")) {
                        g.showMessage("열쇠를 획득하였습니다.", 1000);
                        g.getPlayer().addRoomKey("kitchen");
                    }
                }
            };
            l.setQuestion("살펴 보시겠습니까?");
            l.setAnswers(new String[] { YES, NO });
            l.setBackground(g.getResource().getImage("img.bg.clothesroom.picture"));
            g.showLayer(l);
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
