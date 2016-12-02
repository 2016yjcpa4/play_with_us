package com.github.yjcpaj4.play_with_us.game.object;

import com.github.yjcpaj4.play_with_us.game.special_object.KitchenRefrigerator;
import com.github.yjcpaj4.play_with_us.game.special_object.KitchenTV;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.game.Map;
import com.github.yjcpaj4.play_with_us.game.special_object.BathroomBloodstains;
import com.github.yjcpaj4.play_with_us.game.special_object.ClothesroomMannequin;
import com.github.yjcpaj4.play_with_us.game.special_object.ClothesroomMannequinMine;
import com.github.yjcpaj4.play_with_us.game.special_object.ClothesroomPicture;
import com.github.yjcpaj4.play_with_us.game.special_object.Player;
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
    
    public String getDestMap() {
        return mDestMap;
    }
    
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
    
    public void enterMap(Player p) {
        Map m = Application.getInstance().getResource().getMap(mDestMap).toMap(); // 여기서는 맵을 새로 만드는게 있는데... 캐시로 저장해야합니다
        m.addObject(p);
        p.setPosition(mDestPos);
    }
    
    public void setLock() {
        mLocked = true;
    }
    
    public void setUnLock() {
        mLocked = false;
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        // TODO 리팩토링
        if (CollisionDetection.getCollision(mCollider, g.getPlayer().getCollider()) != null) {
            Map m = g.getResource().getMap(mDestMap).toMap(); // 여기서는 맵을 새로 만드는게 있는데... 캐시로 저장해야합니다
            m.addObject(g.getPlayer());

            if (mDestMap.equalsIgnoreCase("map.kitchen")) {
                m.addObject(new KitchenTV());
                m.addObject(new KitchenRefrigerator());
            }
            
            if (mDestMap.equalsIgnoreCase("map.clothesroom")) {
                m.addObject(new ClothesroomMannequin());
                m.addObject(new ClothesroomMannequinMine());
                m.addObject(new ClothesroomPicture());
            }

            if (mDestMap.equalsIgnoreCase("map.bathroom")) {
                m.addObject(new BathroomBloodstains());
            }

            g.getPlayer().setPosition(mDestPos);
        }
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        if (Application.DEBUG) {
            g2d.setColor(new Color(0, 0, 255, (int) (255 * 0.5)));
            g2d.fillPolygon(mCollider.toAWTPolygon());
        }
    }
}