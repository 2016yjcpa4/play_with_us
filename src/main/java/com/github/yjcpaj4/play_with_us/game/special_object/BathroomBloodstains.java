package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.ResourceManager;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Box2D;
import com.github.yjcpaj4.play_with_us.resource.SpriteResource;
import com.github.yjcpaj4.play_with_us.util.MathUtil;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BathroomBloodstains extends GameObject {
    
    private static final int WIDTH = 64;
    private static final int HEIGHT = 173;
    
    private static final int MARGIN = 50;
    
    private Polygon mCollider;
    
    public BathroomBloodstains(int x, int y) {
        mCollider = new Box2D(x, y, WIDTH, HEIGHT).toPolygon();
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        
        ResourceManager m = g.getResource();
        SpriteResource r = m.getSprite("sprt.bathroom.bloodstains");
        BufferedImage b = r.getCurrentFrame(delta).getImage();
        
        g2d.setColor(Color.red);
        g2d.drawPolygon(mCollider.toAWTPolygon());
        
        g2d.drawImage(b, 0, 0, null);
    }
}
