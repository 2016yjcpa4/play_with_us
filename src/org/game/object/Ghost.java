package org.game.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List; 

import org.game.map.Map;
import org.game.CanvasView;
import org.game.DrawableObject;
import org.game.Game;
import org.game.geom.Polygon;
import org.game.math.Point2D;
import org.game.math.Vector2D;
import org.game.util.IntersectionUtil;

public class Ghost implements DrawableObject {

    private Map map;
    
    private Point2D pos = new Point2D(50, 50); 
    private Vector2D vel = new Vector2D(0, 0);
    
    public Ghost(Map m) {
        this.map = m; 
    }
    
    public Point2D getPosition() {
        return pos;
    }
    
    public double getDistanceToPlayer() {
        return new Vector2D(pos).sub(map.getPlayer().getPosition()).length();
    }
    
    @Override
    public void draw(CanvasView c, Graphics2D g2d) {
        Game g = (Game) c;
        double distanceToPlayer = getDistanceToPlayer();
        boolean isNearPlayer = distanceToPlayer < 200;
        boolean isClosePlayer = distanceToPlayer < 100;
        boolean isLightProjected = IntersectionUtil.hasPoint(pos, map.getPlayer().projectLight());
        
        if (distanceToPlayer < 30) {
            g.setGameOver();
            return;
        }
        
        vel.set(0, 0);
        
        if (isNearPlayer) { // 플레이어가 근처에 있는경우
            vel.set(2, 2);
        }
        
        if (isLightProjected  // 플레이어 손전등에 비춰진상태
            || (isNearPlayer && map.getPlayer().isTurnOnFlash())) {// 플레이어가 근처에있으면서 플래시가 켜진 경우
            vel.set(4, 4);
        }
        
        if (vel.getX() != 0 && vel.getY() != 0) {
            
            List<Point2D> l = map.getPath(pos, map.getPlayer().getPosition());
            
            if ( ! l.isEmpty()) {
                
                double angle = new Vector2D(l.get(0)).sub(pos).angle();
                int x = (int) (pos.getX() + vel.getX() * Math.cos(angle));
                int y = (int) (pos.getY() + vel.getY() * Math.sin(angle));
                
                pos.setX(x);
                pos.setY(y); 
                
                if (Game.DEBUG) {
 
                    for (Point2D p : l) {
                        g2d.setColor(Color.RED);
                        g2d.fillOval(p.getX() - 2, p.getY() - 2, 4, 4);
                    } 
                }
            }
        }
        
        if (isLightProjected || isClosePlayer) {
            g2d.setColor(Color.CYAN);
            g2d.fillRect(pos.getX() - 5, pos.getY() - 5, 10, 10);
        } else if (Game.DEBUG) {
            g2d.setColor(Color.GRAY);
            g2d.fillRect(pos.getX() - 5, pos.getY() - 5, 10, 10);
        }
    }

}
