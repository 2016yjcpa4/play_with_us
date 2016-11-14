package com.github.yjcpaj4.play_with_us.layer;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.Layer;
import com.github.yjcpaj4.play_with_us.map.GameMap;
import com.github.yjcpaj4.play_with_us.map.Player;
import com.github.yjcpaj4.play_with_us.ResourceManager;
import com.github.yjcpaj4.play_with_us.map.Lightless;
import com.github.yjcpaj4.play_with_us.map.NotWalkable;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.util.FileUtil;
import com.google.gson.Gson;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class GameLayer extends Layer {
    
    private Player mPlayer;
    
    public GameLayer(Application c) { 
        super(c);

        /*
         * 게임은 플레이어의 중심으로 돌아가기때문에
         * 맵이아닌 플레이어와 맵을 생성하고 draw 시 플레이어가 속한 맵을 draw 합니다.
         */
        mPlayer = new Player();
        
        BufferedImage b = null;
        Map<String, Object> o = null;
        
        try {
            File f = new File("res/map.json");
            
            o = new Gson().fromJson(FileUtil.getContents(f), Map.class);
            
            b = ImageIO.read(new File(f.getParentFile(), (String) o.get("img")));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        GameMap m = new GameMap(b);
        
        for(List not_walkable : (List<List>) o.get("not_walkable")) {
            List<Point2D> l = new ArrayList<Point2D>();
            for(List e : (List<List>) not_walkable) {
                l.add(new Point2D((int) Double.parseDouble(e.get(0).toString()), (int) Double.parseDouble(e.get(1).toString())));
            }
            m.addObject(new NotWalkable(l));
        }
            
        
        for(List not_walkable : (List<List>) o.get("lightless")) {
            List<Point2D> l = new ArrayList<Point2D>();
            for(List e : (List<List>) not_walkable) {
                l.add(new Point2D((int) Double.parseDouble(e.get(0).toString()), (int) Double.parseDouble(e.get(1).toString())));
            }
            m.addObject(new Lightless(l));
        }
            
        m.addObject(mPlayer);
    }
    
    public Player getPlayer() {
        return mPlayer;
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        GameMap m = mPlayer.getMap();
        m.update(this, delta);
        m.draw(this, delta, g2d);
    }
}
