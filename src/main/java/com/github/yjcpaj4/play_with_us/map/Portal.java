package com.github.yjcpaj4.play_with_us.map;

import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.google.gson.annotations.SerializedName;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Portal.class.
 * 
 * 물리적인 오브젝트가 포탈오브젝트에 닿는순간 다른맵, 해당좌표로 이동이 됩니다.
 * 
 * @author 차명도.
 */
public class Portal extends PhysicsObject {
    
    protected Point2D mSpawnPos;
    protected final String mDestMapID;
    
    protected boolean mLocked = false;
    
    public Portal(String s, Point2D p) {
        mDestMapID = s;
        mSpawnPos = p;
        mCollider = new Circle(p.getX(), p.getY(), 20);
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
            if (isCollide(o)) {
                Map s = g.getResource().getMap(mDestMapID).toMap();
                s.addObject(o);
            }
        }
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.fillPolygon(mCollider.toAWTPolygon());
    }
}