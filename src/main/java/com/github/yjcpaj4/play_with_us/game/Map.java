package com.github.yjcpaj4.play_with_us.game;
 
import com.github.yjcpaj4.play_with_us.game.object.Lightless;
import com.github.yjcpaj4.play_with_us.game.object.NotWalkable;
import com.github.yjcpaj4.play_with_us.game.object.Light;
import com.github.yjcpaj4.play_with_us.game.special_object.Player;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import com.github.yjcpaj4.play_with_us.Application; 
import com.github.yjcpaj4.play_with_us.geom.BresenhamLine;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Line2D;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.ResourceManager;

public class Map {

    public static final boolean DEBUG = true;
    
    private static final int TILE_WIDTH = 7;
    private static final int TILE_HEIGHT = 7;
    
    private TileMap mTiles;
    private final BufferedImage mBackground;
    private List<GameObject> mObject = new ArrayList<>();
    
    public Map(BufferedImage b, List<GameObject> l) {
        mBackground = b;
        
        for (GameObject o : l) {
            addObject(o);
        }
        
        setTiles();
    }
    
    private void setTiles() {
        mTiles = new TileMap(getTileColumns(), getTileRows());
        
        for (Line2D l : getAllSideByNotWalkable()) {
            Point2D p1 = getTileIndex((int) l.getX1(), (int) l.getY1());
            Point2D p2 = getTileIndex((int) l.getX2(), (int) l.getY2());

            for (Point2D p : BresenhamLine.getBresenhamLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY())) {
                if (mTiles.isWithin((int)p.getX(), (int)p.getY())) {
                    mTiles.getNode((int)p.getX(), (int)p.getY()).setNotWalkable();
                }
            }
        }
    }
    
    public Player getPlayer() {
        for(GameObject o : getAllObject()) {
            if (o instanceof Player) {
                return (Player) o;
            }
        }
        
        return null;
    }
    
    public List<GameObject> getAllObject() {
        return mObject;
    }
    
    public void addObject(GameObject o) {
        o.setMap(this);
        
        mObject.add(o);
        
        if (o instanceof LightWithGameObject) { // 플레이어는 손전등의 빛오브젝트까지 추가함.
            addObject(((LightWithGameObject) o).getOwnedLight());
        }
    }
    
    public float getDarkness() {
        return  0.98f;
    }
    
    public int getWidth() {
        return mBackground.getWidth();
    }
    
    public int getHeight() {
        return mBackground.getHeight();
    }
    
    public int getTileColumns() {
        return (int) (getWidth() / (float) TILE_WIDTH);
    }
    
    public int getTileRows() {
        return (int) (getHeight() / (float) TILE_HEIGHT);
    }
    
    public Point2D getTileIndex(Point2D p) {
        return Map.this.getTileIndex((int)p.getX(), (int)p.getY());
    }
    
    public Point2D getTileIndex(int x, int y) {
        return new Point2D((int) (x / TILE_WIDTH)
                         , (int) (y / TILE_HEIGHT));
    }

    public List<Lightless> getAllLightless() {
        List<Lightless> l = new ArrayList<>();
        for(GameObject o : getAllObject()) {
            if (o instanceof Lightless) {
                l.add((Lightless) o);
            }
        }
        return l;
    }
    
    public List<NotWalkable> getAllNotWalkable() {
        List<NotWalkable> l = new ArrayList<>();
        for(GameObject o : getAllObject()) {
            if (o instanceof NotWalkable) {
                l.add((NotWalkable) o);
            }
        }
        return l;
    }
    
    public List<Line2D> getAllSideByNotWalkable() {
        List<Line2D> l = new ArrayList<>();
        for (NotWalkable o : getAllNotWalkable()) {
            l.addAll(o.getAllSide());
        }
        return l;
    }
    
    public List<Line2D> getAllSideByLightless() {
        List<Line2D> l = new ArrayList<>();
        for (Lightless o : getAllLightless()) {
            l.addAll(o.getAllSide());
        }
        return l;
    }
    
    public List<Light> getAllLight() {
        List<Light> l = new ArrayList<>();
        for(GameObject o : getAllObject()) {
            if (o instanceof Light) {
                l.add((Light) o);
            }
        }
        return l;
    }
    
    public List<LightWithGameObject> getAllLightObject() {
        List<LightWithGameObject> l = new ArrayList<>();
        for(GameObject o : getAllObject()) {
            if (o instanceof LightWithGameObject) {
                l.add((LightWithGameObject) o);
            }
        }
        return l;
    }
    
    public List<Point2D> getPath(Point2D p1, Point2D p2) {
        return getPath((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY());
    }
    
    public List<Point2D> getPath(int x1, int y1, int x2, int y2) {
        Point2D p1 = getTileIndex(x1, y1); // 출발지
        Point2D p2 = getTileIndex(x2, y2); // 목적지
        
        List<Point2D> l = new ArrayList<>();
        
        for (TileNode n : mTiles.getPath((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY())) { 
            l.add(new  Point2D(n.getX() * TILE_WIDTH  + (TILE_WIDTH / 2),
                               n.getY() * TILE_HEIGHT + (TILE_HEIGHT / 2)));
        }
        
        return l;
    }

    private List<GameObject> getAllObjectWithoutLightObject() {
        List<GameObject> l = new ArrayList<>();
        for(GameObject o : getAllObject()) {
            if (o instanceof Light || o instanceof LightWithGameObject) {
                continue;
            }
            
            l.add(o);
        }
        return l;
    }
    
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        g2d.drawImage(mBackground, 0, 0, null);
        
        debugTiles(g2d);

        for (GameObject o : getAllObjectWithoutLightObject()) {
            o.draw(g, delta, g2d);
        }
        
        BufferedImage b = new BufferedImage(mBackground.getWidth(), mBackground.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D t = b.createGraphics();
        t.setPaint(new Color(0, 0, 0, (int) (255 * getDarkness())));
        t.fillRect(0, 0, mBackground.getWidth(), mBackground.getHeight());
        t.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT));

        for (GameObject o : getAllLight()) {
            o.draw(g, delta, t);
        }

        t.dispose();
        
        g2d.drawImage(b, 0, 0, null);
        
        for (GameObject o : getAllLightObject()) {
            o.draw(g, delta, g2d); 
        }
    }
    
    private void debugTiles(Graphics2D g2d) {
        
        if ( ! (DEBUG && Application.DEBUG)) {
            return;
        }
        
        g2d.setColor(new Color(255, 0, 0, (int) (255 * 0.3)));

        int w = TILE_WIDTH;
        int h = TILE_HEIGHT;
        
        TileNode[][] n = mTiles.getNodes();
        for (int x = 0; x < n.length; ++x) {
            for (int y = 0; y < n[0].length; ++y) {    
                g2d.drawRect(x * w, y * h, w, h);
                
                if ( ! n[x][y].canWalk()) {
                    g2d.fillRect(x * w, y * h, w, h);
                }
            }
        }
    }

    public void update(GameLayer g, long delta) {
        for (GameObject o : getAllObject()) {
            o.update(g, delta);
        }
    }
}
