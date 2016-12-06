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
import com.github.yjcpaj4.play_with_us.resource.SoundResource;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class LibraryPaino extends GameObject {
    
    private static final int X = 854;
    private static final int Y = 721;
    private static final int RADIUS = 50;
    
    private static final String YES_1 = "살펴본다.";
    private static final String YES_2 = "악보를 놓는다.";
    private static final String NO = "그만둔다.";
    
    private Polygon mCollider = new Circle(X, Y, RADIUS);
    
    private BufferedImage mHintImage;
    
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        if (CollisionDetection.isCollide(mCollider, g.getPlayer().getCollider())
        && g.getInput().isKeyOnce(KeyEvent.VK_F)
        && g.getPlayer().isInputEnable()) {
            
            if (g.getPlayer().hasItem("beethoven")) {
                InterativeLayer l = new InterativeLayer(Application.getInstance()) {

                    @Override
                    protected void pause() {
                        super.pause();

                        if (getCurrentAnswer().equals(YES_2)) {
                            SoundResource r = g.getResource().getSound("snd.obj.library.piano");
                            r.setOnEndOfMedia(new Runnable() {
                            
                                @Override
                                public void run() {
                                    g.getPlayer().setTurnOffLight();
                                    g.getPlayer().setInputEnable();
                                    
                                    mHintImage = g.getResource().getImage("img.obj.library.piano.hint");
                                }
                            });
                            r.play();
                            
                            g.getPlayer().setInputDisable();
                            g.getPlayer().setIdle();
                        }
                    }
                };
                l.setQuestion("어떻게 하시겠습니까?");
                l.setAnswers(new String[] { YES_2, NO });
                l.setBackground(g.getResource().getImage("img.bg.library.piano"));
                g.showLayer(l);
            } else {
                InterativeLayer l = new InterativeLayer(Application.getInstance()) {

                    @Override
                    protected void pause() {
                        super.pause();

                        if (getCurrentAnswer().equals(YES_1)) {
                            g.showMessage("아무것도 발견하지 못하였습니다.", 1000);
                        }
                    }
                };
                l.setQuestion("살펴 보시겠습니까?");
                l.setAnswers(new String[] { YES_1, NO });
                l.setBackground(g.getResource().getImage("img.bg.library.piano"));
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
        
        if (mHintImage != null) {
            g2d.drawImage(mHintImage, 896, 625, null);
        }
    }
}
