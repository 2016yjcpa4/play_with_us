package com.github.yjcpaj4.play_with_us.map;
 
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

public class Stage {

    public static final boolean DEBUG = false;
    
    public static final int MAP_WIDTH = 1280;
    public static final int MAP_HEIGHT = 800;
    
    public static final int TILE_COLUMNS = (int) (MAP_WIDTH / 11.0);
    public static final int TILE_ROWS = (int) (MAP_HEIGHT / 11.0);
    
    private TileMap mTiles = new TileMap(TILE_COLUMNS, TILE_ROWS);
    
    private final BufferedImage mBackground;
    private List<GameObject> mObject = new ArrayList<>();
    
    public Stage(BufferedImage b, List<GameObject> l) {
        mBackground = b;
        
        for (GameObject o : l) {
            addObject(o);
        }
        
        setTiles();
    }
    
    private void setTiles() {
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
        o.setStage(this);
        
        mObject.add(o);
        
        if (o instanceof Player) { // 플레이어는 손전등의 빛오브젝트까지 추가함.
            addObject(((Player) o).getOwnedLight());
        }
    }
    
    public float getDarkness() {
        return 0.96f;
    }
    
    public int getTileWidth() {
        return MAP_WIDTH / TILE_COLUMNS;
    }
    
    public int getTileHeight() {
        return MAP_HEIGHT / TILE_ROWS;
    }
    
    public Point2D getTileIndex(Point2D p) {
        return Stage.this.getTileIndex((int)p.getX(), (int)p.getY());
    }
    
    public Point2D getTileIndex(int x, int y) {
        return new Point2D((int) (x / getTileWidth())
                         , (int) (y / getTileHeight()));
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
    
    public List<PhysicsObject> getAllPhysicsObject() {
        List<PhysicsObject> l = new ArrayList<>();
        for(GameObject o : getAllObject()) {
            if (o instanceof PhysicsObject) {
                l.add((PhysicsObject) o);
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
    
    public List<Point2D> getPath(Point2D p1, Point2D p2) {
        return getPath((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY());
    }
    
    public List<Point2D> getPath(int x1, int y1, int x2, int y2) {
        Point2D p1 = getTileIndex(x1, y1); // 출발지
        Point2D p2 = getTileIndex(x2, y2); // 목적지
        
        List<Point2D> l = new ArrayList<>();
        
        for (TileNode n : mTiles.getPath((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY())) { 
            l.add(new  Point2D(n.getX() * getTileWidth()  + (getTileWidth() / 2),
                               n.getY() * getTileHeight() + (getTileHeight() / 2)));
        }
        
        return l;
    }

    private List<GameObject> getAllObjectWithoutLightAndPlayer() {
        List<GameObject> l = new ArrayList<>();
        for(GameObject o : getAllObject()) {
            if (o instanceof Light || o instanceof Player) {
                continue;
            }
            
            l.add(o);
        }
        return l;
    }
    
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        g2d.drawImage(mBackground, 0, 0, null);

        for (GameObject o : getAllObjectWithoutLightAndPlayer()) {
            o.draw(g, delta, g2d);
        }
        
        BufferedImage b = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D t = b.createGraphics();
        t.setPaint(new Color(0, 0, 0, (int) (255 * getDarkness())));
        t.fillRect(0, 0, MAP_WIDTH, MAP_HEIGHT);
        t.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT));

        for (GameObject o : getAllLight()) {
            o.draw(g, delta, t);
        }

        t.dispose();
        
        g2d.drawImage(b, 0, 0, null);
        
        getPlayer().draw(g, delta, g2d);
    }

    public void update(GameLayer g, long delta) {
        for (GameObject o : getAllObject()) {
            o.update(g, delta);
        }
    }
}
