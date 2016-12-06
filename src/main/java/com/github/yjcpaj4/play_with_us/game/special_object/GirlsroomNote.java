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
import java.util.Timer;
import java.util.TimerTask;

public class GirlsroomNote extends GameObject {
    
    private static final int X = 37;
    private static final int Y = 124;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 62;
    
    private Polygon mCollider = new Box2D(X, Y, WIDTH, HEIGHT).toPolygon();
    
    private boolean mShowNote = false;
    
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        if (mShowNote) {
            return;
        }
        
        if (getMap().equals(g.getPlayer().getMap()) 
        && CollisionDetection.isCollide(mCollider, g.getPlayer().getCollider())
        && g.getInput().isKeyOnce(KeyEvent.VK_F)) {
                    
            mShowNote = true;

            g.getResource().getSound("snd.obj.paper").play();
            
            InterativeLayer l = new InterativeLayer(Application.getInstance()) {
                
                @Override
                protected void pause() {
                    super.pause(); //To change body of generated methods, choose Tools | Templates.
                    
                    g.getResource().getSound("snd.bgm.girlsroom.knock").play(-1);
                    
                    g.getPlayer().addItem("girlsroom.note");
                    
                    new Timer().schedule(new TimerTask() {
                        
                        @Override
                        public void run() {
                            
                            BrokenLight o = new BrokenLight();
                            o.setFinishEvent(new Runnable() {
                                @Override
                                public void run() {
                                    getMap().removeObject(o);
                                    g.getPlayer().setInputDisable();
                                    g.getPlayer().setTurnOffLight();
                                    g.getPlayer().setIdle();
                                    
                                    g.showMessage("손전등이 방전 되었습니다.");
                                    
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            
                                            g.getResource().getSound("snd.bgm.girlsroom.knock").stop();
                                            g.getResource().getSound("snd.obj.portal.open").play();
                                        }
                                    }, 1000);
                                }
                            });
                            getMap().addObject(o);
                        }
                    }, 2000);
                }
            };
            l.setBackground(g.getResource().getImage("img.bg.note"));
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
