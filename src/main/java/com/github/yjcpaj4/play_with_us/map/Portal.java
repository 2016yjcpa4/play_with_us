package com.github.yjcpaj4.play_with_us.map;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.google.gson.annotations.SerializedName;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

/**
 * Portal.class.
 * 
 * 물리적인 오브젝트가 포탈오브젝트에 닿는순간 다른맵, 해당좌표로 이동이 됩니다.
 * 
 * @author 차명도.
 */
public class Portal extends GameObject {
    
    private Polygon mCollider;
    
    private String mDestMap;
    private Point2D mDestPos;
    
    private boolean mLocked = false;
    
    public Portal(String s, Point2D p, List<Point2D> l) {
        mDestMap = s;
        mDestPos = p;
        mCollider = new Polygon(l);
    }
    
    public void setLock() {
        mLocked = true;
    }
    
    public void setUnLock() {
        mLocked = false;
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        for (PhysicsObject o : getMap().getAllPhysicsObject()) {
            if (CollisionDetection.getCollision(mCollider, o.mCollider) != null) {
                Map m = g.getResource().getMap(mDestMap).toMap(); // 여기서는 맵을 새로 만드는게 있는데... 캐시로 저장해야합니다
                m.addObject(o);
                
                o.setPosition(mDestPos);
                break;
            }
        }
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        if (Application.DEBUG) {
            g2d.setColor(Color.BLUE);
            g2d.fillPolygon(mCollider.toAWTPolygon());
        }
    }
}