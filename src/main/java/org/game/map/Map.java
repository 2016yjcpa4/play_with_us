package org.game.map;
 
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.game.GameLoop;
import org.game.Game; 
import org.game.geom.BresenhamLine;
import org.game.math.Line2D;
import org.game.math.Point2D;
import org.game.MapObject;

public class Map {

    public static final boolean DEBUG = false;
    public static final int MAP_WIDTH = 1280;
    public static final int MAP_HEIGHT = 800;
    
    public static final int TILE_COLUMNS = (int) (MAP_WIDTH / 11.0);
    public static final int TILE_ROWS = (int) (MAP_HEIGHT / 11.0);
    
    private TileMap mTiles = new TileMap(TILE_COLUMNS, TILE_ROWS);
    
    private List<MapObject> mObject = new ArrayList<>();
    
    private Image img;
    
    public Map() {
        addObject(new Wall(0, 0, MAP_WIDTH - 10, 10));// 북쪽
        addObject(new Wall(MAP_WIDTH - 30, 0, 10, MAP_HEIGHT));// 동쪽
        addObject(new Wall(0, MAP_HEIGHT - 10, MAP_WIDTH, 10));// 남쪽
        addObject(new Wall(20, 0, 10, MAP_HEIGHT - 10));// 서쪽
        
        // 장애물 1
        addObject(new Wall(10, 10, 480, 305));
        addObject(new Wall(750, 10, 500, 305));
        addObject(new Wall(30, 535, 550, 385));
        addObject(new Wall(660, 490, 600, 385));
        
        try {
            img = ImageIO.read(new File("./res/map.png"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        for(Line2D l : getAllLine()) {
            Point2D t1 = getTileIndexByPoint2D(l.getX1(), l.getY1());
            Point2D t2 = getTileIndexByPoint2D(l.getX2(), l.getY2());

            for (Point2D p : BresenhamLine.getBresenhamLine(t1.getX(), t1.getY(), t2.getX(), t2.getY())) {
                if (mTiles.isWithin(p.getX(), p.getY())) {
                    mTiles.getNode(p.getX(), p.getY()).setNotWalkable();
                }
            }
        }
    }
    
    public Player getPlayer() {
        for(MapObject o : getAllObject()) {
            if (o instanceof Player) {
                return (Player) o;
            }
        }
        
        return null;
    }
    
    public List<MapObject> getAllObject() {
        return mObject;
    }
    
    public void addObject(MapObject o) {
        o.setMap(this);
        
        mObject.add(o);
    }
    
    public float getDarkness() {
        return 0.75f;
    }
    
    public int getTileWidth() {
        return MAP_WIDTH / TILE_COLUMNS;
    }
    
    public int getTileHeight() {
        return MAP_HEIGHT / TILE_ROWS;
    }
    
    public Point2D getTileIndexByPoint2D(Point2D p) {
        return getTileIndexByPoint2D(p.getX(), p.getY());
    }
    
    public Point2D getTileIndexByPoint2D(int x, int y) {
        return new Point2D((int) (x / getTileWidth())
                         , (int) (y / getTileHeight()));
    }

    public List<Wall> getAllWall() {
        List<Wall> l = new ArrayList<>();
        
        for(MapObject o : getAllObject()) {
            if (o instanceof Wall) {
                l.add((Wall) o);
            }
        }
        
        return l;
    }
    
    public List<Line2D> getAllLine() {
        List<Line2D> l = new ArrayList<>();
        
        for(Wall w : getAllWall()) {
            
            for(int n = 0; n < w.getPoints().size(); ++n) {
                Point2D p1 = w.getPoint(n);
                Point2D p2 = w.getPoint(n + 1);

                l.add(new Line2D(p1.getX(), p1.getY(), p2.getX(), p2.getY()));
            }
        }
        
        return l;
    }
    
    public List<Point2D> getPath(Point2D p1, Point2D p2) {
        return getPath(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }
    
    public List<Point2D> getPath(int x1, int y1, int x2, int y2) {
        Point2D p1 = getTileIndexByPoint2D(x1, y1);
        Point2D p2 = getTileIndexByPoint2D(x2, y2);
        
        List<Point2D> l = new ArrayList<>();
        
        for(TileNode t : mTiles.getPath(p1.getX(), p1.getY(), p2.getX(), p2.getY())) {
            l.add(new  Point2D(t.getX() * getTileWidth() + (getTileWidth() / 2) , t.getY() * getTileHeight() + (getTileHeight() / 2)));
        }
        
        return l;
    }

    public void draw(GameLoop g, Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        
        g2d.drawImage(img, 0, 0, null);

        for(MapObject o : mObject) {
            if ( ! (o instanceof Light)) { // 빛 오브젝트는 아래에서 별도로 처리됩니다.
                o.draw(g, g2d);
            }
        }
    
        BufferedImage b = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D _g2d = b.createGraphics();

        _g2d.setPaint(new Color(0, 0, 0, (int) (255 * getDarkness())));
        _g2d.fillRect(0, 0, MAP_WIDTH, MAP_HEIGHT);
        
        {
            // 아직 이해를 못했는데 이작업을 하고...
            _g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT));

            for(MapObject o : mObject) {
                if (o instanceof Light) { // 빛 오브젝트는 아래에서 별도로 처리됩니다.
                    o.draw(g, _g2d);
                }
            }

            _g2d.dispose();
        }
        
        g2d.drawImage(b, 0, 0, null);
    }

    public void update(GameLoop g) {

        for(MapObject o : mObject) {
            o.update(g);
        }
    }

}
