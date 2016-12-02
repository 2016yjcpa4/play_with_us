package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.game.object.Light;
import com.github.yjcpaj4.play_with_us.game.object.Light;
import com.github.yjcpaj4.play_with_us.game.LightWithGameObject;
import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.resource.SoundResource;
import com.github.yjcpaj4.play_with_us.util.GraphicsUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Collection;

public class LightLeakingShoeCloset extends LightWithGameObject {
    
    private static final int RADIUS = 60;
            
    private static final double LIGHT_ANGLE = Math.toRadians(55);
    private static final double LIGHT_EXTENT = 40;
    private static final float LIGHT_LENGTH = 50;
    
    private BufferedImage mImage;
    private Circle mCollider;
    
    private Light mLight = new Light() {
        
        @Override
        public Point2D getPosition() {
            return LightLeakingShoeCloset.this.getPosition();
        }
        
        @Override
        public double getExtent() {
            return LIGHT_EXTENT;
        }

        @Override
        public float getLength() {
            return LIGHT_LENGTH;
        }

        @Override
        public double getAngle() {
            return LIGHT_ANGLE;
        }
    };
    
    public LightLeakingShoeCloset(Point2D p) {
        mCollider = new Circle(p.getX(), p.getY(), RADIUS);
        mImage = Application.getInstance().getResource().getImage("img.obj.shoecloset.door");
        
        mLight.setTurnOn();
    }
    
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        if (g.getPlayer().isOwnedLight()) {
            return;
        }
        
        Polygon p1 = g.getPlayer().getCollider();
        Polygon p2 = mCollider;
        
        if (CollisionDetection.getCollision(p1, p2) != null
         && g.getInput().isKeyOnce(KeyEvent.VK_F)) {
            g.getPlayer().setOwnedLight();
            
            mLight.setTurnOff();
            
            g.showMessage("손전등을 획득하였습니다.", 1000);
        }
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {        
        if (mLight.isTurnOff()) {
            return;
        }
        
        Point2D p = getPosition();
        int x = (int) p.getX() - mImage.getWidth() / 2;
        int y = (int) p.getY() - mImage.getHeight() / 2;
        
        /*
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("굴림", Font.PLAIN, 12));
        GraphicsUtil.drawStringMultiLine(g2d, "상호작용(F)키 를 눌러 서랍안에 있는 손전등을 습득할 수 있습니다.", 150, x, y - 50);
        */
        
        g2d.drawImage(mImage, x, y, null);
        
        if (Application.DEBUG) {
            g2d.setColor(Color.RED);
            g2d.drawPolygon(mCollider.toAWTPolygon());
        }
    }

    @Override
    public Light getOwnedLight() {
        return mLight;
    }
}
