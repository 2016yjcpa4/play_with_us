package com.github.yjcpaj4.play_with_us.game.object;

import com.github.yjcpaj4.play_with_us.game.special_object.KitchenRefrigerator;
import com.github.yjcpaj4.play_with_us.game.special_object.KitchenTV;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.game.Map;
import com.github.yjcpaj4.play_with_us.game.special_object.BathroomBloodstains;
import com.github.yjcpaj4.play_with_us.game.special_object.BathroomBrokenLight;
import com.github.yjcpaj4.play_with_us.game.special_object.BathroomGhost;
import com.github.yjcpaj4.play_with_us.game.special_object.BathroomTub;
import com.github.yjcpaj4.play_with_us.game.special_object.ClothesroomMannequin;
import com.github.yjcpaj4.play_with_us.game.special_object.ClothesroomMannequinMine;
import com.github.yjcpaj4.play_with_us.game.special_object.ClothesroomPicture;
import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.google.gson.annotations.SerializedName;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
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
    
    public Portal(String s, Point2D p, Polygon o) {
        mDestMap = s;
        mDestPos = p;
        mCollider = o;
    }
    
    public String getDestMap() {
        return mDestMap;
    }
    
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
    
    public void enterPortal(Player o) {
        GameLayer g = Application.getInstance().getLayer(GameLayer.class);
        g.getMap(mDestMap).addObject(o);
        o.setPosition(mDestPos);
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        // TODO 리팩토링
        Player o = g.getPlayer();
        if (g.getInput().isKeyOnce(KeyEvent.VK_F)) {
            if (o.isCollide(mCollider)) {

                switch(mDestMap) {
                    case "map.livingroom":
                    case "map.clothesroom":
                        break;
                    case "map.library":
                    case "map.bathroom":
                    case "map.girlsroom":
                    case "map.kitchen":
                        if ( ! o.hasItem(mDestMap)) {
                            g.showMessage("문이 잠겨있습니다.");
                            g.getResource().getSound("snd.obj.portal.locked").play();
                            return;
                        }
                        break;
                }

                g.getResource().getSound("snd.obj.portal.open").play();

                enterPortal(o);
            }
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