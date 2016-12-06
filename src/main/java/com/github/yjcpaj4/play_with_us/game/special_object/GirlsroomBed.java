package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.layer.InterativeLayer;
import com.github.yjcpaj4.play_with_us.math.Box2D;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Graphics2D;

public class GirlsroomBed extends GameObject {
    
    private static final int X = 67;
    private static final int Y = 125;
    private static final int WIDTH = 84;
    private static final int HEIGHT = 97;
    
    private static final String YES_1 = "살펴본다.";
    private static final String YES_2 = "숨는다.";
    private static final String NO = "그만둔다.";
    
    private Polygon mCollider = new Box2D(X, Y, WIDTH, HEIGHT).toPolygon();
    
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        if (CollisionDetection.isCollide(mCollider, g.getPlayer().getCollider())
        && g.getInput().isKeyOnce(KeyEvent.VK_F)) {
            
            if (g.getPlayer().hasItem("girlsroom.note")) {
                InterativeLayer l = new InterativeLayer(Application.getInstance()) {

                    @Override
                    protected void pause() {
                        super.pause();

                        if (getCurrentAnswer().equals(YES_2)) {
                            // 엔딩...
                        }
                    }
                };
                l.setQuestion("살펴 보시겠습니까?");
                l.setAnswers(new String[] { YES_2, NO });
                l.setBackground(g.getResource().getImage("img.bg.girlsroom.bed"));
                g.showLayer(l);
            } else {
                InterativeLayer l = new InterativeLayer(Application.getInstance()) {

                    @Override
                    protected void pause() {
                        super.pause();

                        if (getCurrentAnswer().equals(YES_1)) {
                            g.showMessage("아무것도 발견되지 않았습니다.", 1000);
                        }
                    }
                };
                l.setQuestion("살펴 보시겠습니까?");
                l.setAnswers(new String[] { YES_1, NO });
                l.setBackground(g.getResource().getImage("img.bg.girlsroom.bed"));
                g.showLayer(l);
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
