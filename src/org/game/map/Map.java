package org.game.map;
 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.game.CanvasView;
import org.game.DrawableObject;
import org.game.Game;
import org.game.object.GhostObject;
import org.game.object.PlayerObject;
import org.game.math.Line2D;
import org.game.math.Point2D;
import org.game.map.TileMap;
import org.game.map.TileNode;
import org.game.util.IntersectionUtil;

public class Map implements DrawableObject {

    public static final boolean DEBUG = true;
    public static final int MAP_WIDTH = 800;
    public static final int MAP_HEIGHT = 600;
    
    public static final int TILE_COLUMNS = 50;
    public static final int TILE_ROWS = TILE_COLUMNS;
    
    private List<Line2D> wall = new ArrayList();
    private TileMap tiles = new TileMap(TILE_COLUMNS + 1, TILE_ROWS + 1);
    
    private PlayerObject player;
    private List<GhostObject> mobs = new ArrayList<>();
    
    public Map() { 
        // 기본적으로 맵의 테두리를 만들고...
        wall.add(new Line2D(0, 0, MAP_WIDTH, 0));
        wall.add(new Line2D(MAP_WIDTH, 0, MAP_WIDTH, MAP_HEIGHT));
        wall.add(new Line2D(MAP_WIDTH, MAP_HEIGHT, 0, MAP_HEIGHT));
        wall.add(new Line2D(0, MAP_HEIGHT, 0, 0));
        
        
        GhostObject m;
        
        m = new GhostObject(this);
        m.getPosition().setX(50);
        m.getPosition().setY(50);
        mobs.add(m);
        
        m = new GhostObject(this);
        m.getPosition().setX(760);
        m.getPosition().setY(560);
        mobs.add(m);
        
        m = new GhostObject(this);
        m.getPosition().setX(50);
        m.getPosition().setY(550);
        mobs.add(m);
        
        // 맵을 로드...
        wall.add(new Line2D(100, 150, 120, 50));
        wall.add(new Line2D(120, 50, 200, 80));
        wall.add(new Line2D(200, 80, 140, 210));
        wall.add(new Line2D(140, 210, 100, 150));

        wall.add(new Line2D(400, 400, 200, 400));
        wall.add(new Line2D(200, 500, 200, 400));

        wall.add(new Line2D(200, 260, 220, 150));
        wall.add(new Line2D(220, 150, 300, 200));
        wall.add(new Line2D(300, 200, 350, 320));
        wall.add(new Line2D(350, 320, 200, 260));

//        wall.add(new Line2D(340, 60, 360, 40));
//        wall.add(new Line2D(360, 40, 370, 70));
//        wall.add(new Line2D(370, 70, 340, 60));

//        wall.add(new Line2D(450, 190, 560, 170));
//        wall.add(new Line2D(560, 170, 540, 270));
//        wall.add(new Line2D(540, 270, 430, 290));
//        wall.add(new Line2D(430, 290, 450, 190));

        wall.add(new Line2D(700, 95, 580, 50));
        wall.add(new Line2D(580, 50, 480, 150));
        wall.add(new Line2D(480, 150, 700, 95));

//        wall.add(new Line2D(500, 400, 725, 520));
        wall.add(new Line2D(700, 400, 500, 520));
        
        for(Line2D l : wall) {
            Point2D p1 = getTileIndexByPoint2D(l.getX1(), l.getY1());
            Point2D p2 = getTileIndexByPoint2D(l.getX2(), l.getY2());
            
            for (Point2D p : IntersectionUtil.getBresenhamLines(p1.getX(), p1.getY(), p2.getX(), p2.getY())) {
                tiles.getNode(p.getX(), p.getY()).setNotWalkable();
            }
        }
    }
    
    public void setPlayer(PlayerObject p) {
        this.player = p;
    }
    
    public PlayerObject getPlayer() {
        return player;
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

    public List<Line2D> getWall() {
        return wall;
    }
    
    public List<Line2D> getObstacle() { 
        List<Line2D> l = new ArrayList<>(getWall());
        
        return l;
    }
    
    public List<Point2D> getPath(Point2D p1, Point2D p2) {
        return getPath(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }
    
    public List<Point2D> getPath(int x1, int y1, int x2, int y2) {
        Point2D p1 = getTileIndexByPoint2D(x1, y1);
        Point2D p2 = getTileIndexByPoint2D(x2, y2);
        
        List<Point2D> l = new ArrayList<>();
        
        for(TileNode n : tiles.getPath(p1.getX(), p1.getY(), p2.getX(), p2.getY())) {
            l.add(new  Point2D(n.getX() * getTileWidth() + (getTileWidth() / 2) , n.getY() * getTileHeight() + (getTileHeight() / 2)));
        }
        
        return l;
    }

    @Override
    public void draw(CanvasView c, Graphics2D g2d) {
        g2d.setColor(Color.WHITE);

        for (Line2D l : wall) {
            g2d.drawLine(l.getX1(), l.getY1(), l.getX2(), l.getY2());
        } 
        
        for(GhostObject m : mobs) {
            m.draw(c, g2d);
        }
         
        if (Game.DEBUG && Map.DEBUG) {
            g2d.setColor(new Color(255, 0, 0, (int) (255 * 0.20)));

            for (int y = 0; y < TILE_ROWS; ++y) {
                for (int x = 0; x < TILE_COLUMNS; ++x) {

                    TileNode n = tiles.getNode(x, y);

                    if (n.canWalk()) {
                        g2d.drawRect(getTileWidth() * x, getTileHeight() * y, getTileWidth(), getTileHeight());
                    }
                    else {
                        g2d.fillRect(getTileWidth() * x, getTileHeight() * y, getTileWidth(), getTileHeight());
                    }
                }
            }
        }
    }

}
