package org.game.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List; 

import org.game.map.Map;
import org.game.CanvasView;
import org.game.DrawableObject;
import org.game.Game;
import org.game.geom.Circle;
import org.game.geom.Polygon; 
import org.game.math.Point2D;
import org.game.math.Vector2D;
import org.game.util.IntersectionUtil;

public class Ghost extends Circle implements DrawableObject {

    private Map map;
    
    private Vector2D vel = new Vector2D(0, 0);
    private int speed = 3;
    
    public Ghost(int x, int y, Map m) {
        super(x, y, 13);
        this.map = m; 
    }
    
    public Point2D getPosition() {
        return pos;
    }
    
    public double getDistanceToPlayer() {
        return new Vector2D(pos).sub(map.getPlayer().getPosition()).getLength();
    }
    
    @Override
    public void draw(CanvasView c, Graphics2D g2d) {
        Game g = (Game) c;
        int rad = getRadius();
        double distanceToPlayer = getDistanceToPlayer();
        boolean isNearPlayer = distanceToPlayer < 200;
        boolean isClosePlayer = distanceToPlayer < 100;
        boolean isLightProjected = false;
        
        // 일반적인 상태는 가만히 있음
        vel.setX(0);
        vel.setY(0);
        
        // 걸어오는 경우
        if (isNearPlayer) { // 플레이어가 근처에 있는경우
            vel.setX(speed);
            vel.setY(speed); 
        }
        
        // 달려오는 경우
        if (isLightProjected  // 플레이어 손전등에 비춰진상태
            || (isNearPlayer && map.getPlayer().isTurnOnFlash())) {// 플레이어가 근처에있으면서 플래시가 켜진 경우
            vel.setX(speed * 2);
            vel.setY(speed * 2); 
        }
        
        if (vel.getX() != 0 && vel.getY() != 0) {
            
            List<Point2D> l = map.getPath(pos, map.getPlayer().getPosition());
            
            if ( ! l.isEmpty()) {
                
                double angle = new Vector2D(l.get(0)).sub(pos).getAngle();
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
            g2d.fillOval(pos.getX() - rad, pos.getY() - rad, rad * 2, rad * 2);
        } else if (Game.DEBUG) {
            g2d.setColor(Color.GRAY);
            g2d.fillOval(pos.getX() - rad, pos.getY() - rad, rad * 2, rad * 2);
        }
    }

}
