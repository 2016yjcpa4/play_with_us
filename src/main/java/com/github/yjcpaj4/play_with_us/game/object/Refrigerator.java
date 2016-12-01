package com.github.yjcpaj4.play_with_us.game.object;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.layer.InterativeLayer;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Graphics2D;

public class Refrigerator extends GameObject {

    private Circle mCollider;
    
    public Refrigerator(float x, float y) {
        mCollider = new Circle(x, y, 50);
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        if (CollisionDetection.getCollision(mCollider, g.getPlayer().getCollider()) != null 
         && g.getInput().isKeyOnce(KeyEvent.VK_F)) {
            
            InterativeLayer l = new InterativeLayer(Application.getInstance()) {
                
                @Override
                protected void pause() {
                    super.pause();
                    
                    if (getCurrentAnswer().equals("살펴본다.")) {
                        g.showMessage("열쇠를 획득하였습니다.", 1000);
                    }
                }
            };
            l.setQuestion("살펴 보시겠습니까?");
            l.setAnswers(new String[] { "살펴본다.", "그만둔다." });
            l.setBackground(g.getResource().getImage("img.0"));
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
