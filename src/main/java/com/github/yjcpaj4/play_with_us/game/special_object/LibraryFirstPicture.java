package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.layer.InterativeLayer;
import com.github.yjcpaj4.play_with_us.layer.LockerLayer;
import com.github.yjcpaj4.play_with_us.math.Box2D;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Graphics2D;

public class LibraryFirstPicture extends GameObject {
    
    private static final int X = 297;
    private static final int Y = 74;
    private static final int WIDTH = 58;
    private static final int HEIGHT = 73;
    
    private static final String YES = "살펴본다.";
    private static final String NO = "그만둔다.";
    
    private static final String PASSWORD = "Y48";
    
    private Polygon mCollider = new Box2D(X, Y, WIDTH, HEIGHT).toPolygon();
    
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        if (CollisionDetection.isCollide(mCollider, g.getPlayer().getCollider())
        && g.getInput().isKeyOnce(KeyEvent.VK_F)) {
            
            InterativeLayer l = new InterativeLayer(Application.getInstance()) {
                
                @Override
                protected void pause() {
                    super.pause();
                    
                    if (getCurrentAnswer().equals(YES)) {
                        new Thread() {

                            public void run() {
                                LockerLayer l = new LockerLayer(Application.getInstance()) {
                                    @Override
                                    protected void pause() {
                                        super.pause(); //To change body of generated methods, choose Tools | Templates.

                                        if (getInputValue().equals(PASSWORD)) {
                                            
                                            if (g.getPlayer().hasItem("map.girlsroom")) {
                                                g.showMessage("아무것도 발견되지 않았습니다.");
                                            } else {
                                                g.showMessage("열쇠를 획득하였습니다.", 1000);
                                                g.getPlayer().addItem("map.girlsroom");
                                                g.getResource().getSound("snd.player.item").play();
                                            }
                                        } else {
                                            g.showMessage("비밀번호가 맞지 않습니다.", 1000);
                                        }
                                    }
                                };
                                showLayer(l);
                            }
                        }.start();
                    }
                }
            };
            l.setQuestion("살펴 보시겠습니까?");
            l.setAnswers(new String[] { YES, NO });
            l.setBackground(g.getResource().getImage("img.bg.library.picture.0"));
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
