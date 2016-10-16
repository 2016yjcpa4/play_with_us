package org.game.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List; 

import org.game.map.Map;
import org.game.GameLoop;
import org.game.DrawableObject;
import org.game.Game;
import org.game.SpriteManager;
import org.game.geom.Polygon; 
import org.game.geom.BresenhamLine;
import org.game.math.Point2D;
import org.game.math.Vector2D;

public class Ghost implements DrawableObject {

    private Map map;
    
    private Point2D pos = new Point2D(50, 50); 
    private Vector2D vel = new Vector2D(0, 0);
    private int speed = 3;
    
    public Ghost(Map m) {
        this.map = m; 
    }
    
    public Point2D getPosition() {
        return pos;
    }
    
    public double getDistanceToPlayer() {
        return new Vector2D(pos).sub(map.getPlayer().getPosition()).getLength();
    }
    
    @Override
    public void draw(GameLoop c, Graphics2D g2d) {
        
        
        g2d.drawImage(sp.getSprite((int) (i % 3), getGridIndex()), (int) pos.getX() - 16, (int) pos.getY() - 16, null);
        
    }
    
    private double angle = 0;
    private SpriteManager sp = new SpriteManager();

    private double i = 0;

    public static double normalAbsoluteAngleDegrees(double angle) {
        return (angle %= 360) >= 0 ? angle : (angle + 360);
    }
    
    private int getGridIndex() {
        double ang = normalAbsoluteAngleDegrees(Math.toDegrees(angle));
        
        if (45 <= ang && ang < 135) {
            return 0;
        }
        
        if (135 <= ang && ang < 225) {
            return 1;
        }
        
        if (225 <= ang && ang < 315) {
            return 3;
        }
        
        return 2;
    }

    @Override
    public void update(GameLoop c) {
        Game g = (Game) c;
        double distanceToPlayer = getDistanceToPlayer();
        boolean isNearPlayer = distanceToPlayer < 200;
        boolean isClosePlayer = distanceToPlayer < 100;
        boolean isLightProjected = false;//IntersectionUtil.hasPoint(pos, map.getPlayer().projectLight());
        
        if (distanceToPlayer < 30) {
            g.setGameOver();
            return;
        }
        
        vel.setX(0);
        vel.setY(0);
        
        if (isNearPlayer) { // 플레이어가 근처에 있는경우
            vel.setX(speed);
            vel.setY(speed); 
        }
        
        if (isLightProjected  // 플레이어 손전등에 비춰진상태
            || (isNearPlayer && map.getPlayer().isTurnOnFlash())) {// 플레이어가 근처에있으면서 플래시가 켜진 경우
            vel.setX(speed * 2);
            vel.setY(speed * 2); 
        }
        
        if (vel.getX() != 0 || vel.getY() != 0) {
            i += 0.5;
        }
        
        if (vel.getX() != 0 && vel.getY() != 0) {
            
            List<Point2D> l = map.getPath(pos, map.getPlayer().getPosition());
            
            if ( ! l.isEmpty()) {
                
                angle = new Vector2D(l.get(0)).sub(pos).getAngle();
                int x = (int) (pos.getX() + vel.getX() * Math.cos(angle));
                int y = (int) (pos.getY() + vel.getY() * Math.sin(angle));
                
                pos.setX(x);
                pos.setY(y); 
                
                if (Game.DEBUG) {
 
                    for (Point2D p : l) {
                        //g2d.setColor(Color.RED);
                        //g2d.fillOval(p.getX() - 2, p.getY() - 2, 4, 4);
                    } 
                }
            }
        }
        if (isLightProjected || isClosePlayer) {
            //g2d.setColor(Color.CYAN);
            //g2d.fillRect(pos.getX() - 5, pos.getY() - 5, 10, 10);
        } else if (Game.DEBUG) {
            //g2d.setColor(Color.GRAY);
            //g2d.fillRect(pos.getX() - 5, pos.getY() - 5, 10, 10);
        }
    }
}
