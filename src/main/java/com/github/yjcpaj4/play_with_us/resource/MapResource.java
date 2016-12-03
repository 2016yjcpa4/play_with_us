package com.github.yjcpaj4.play_with_us.resource;

import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.game.object.Darkness;
import com.github.yjcpaj4.play_with_us.game.object.Wall;
import com.github.yjcpaj4.play_with_us.game.object.Player;
import com.github.yjcpaj4.play_with_us.game.Map;
import com.github.yjcpaj4.play_with_us.game.object.Portal;
import com.github.yjcpaj4.play_with_us.math.Box2D;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class MapResource {

    public static MapResource loadFromJSON(String s) {
        return loadFromJSON(new File(s));
    }

    public static MapResource loadFromJSON(File f) {
        MapResource r;
        BufferedImage b;

        try {
            r = new Gson().fromJson(FileUtil.getContents(f), MapResource.class);
            b = ImageIO.read(new File(f.getParentFile(), r.mImagePath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        r.mImage = b;

        return r;
    }

    @SerializedName("img")
    protected String mImagePath;

    @SerializedName("wall")
    protected List<List<Point2D>> mWall = new ArrayList();

    @SerializedName("darkness")
    protected List<List<Point2D>> mDarkness = new ArrayList();

    @SerializedName("portal")
    protected List<PortalResource> mPortal = new ArrayList();

    @SerializedName("spawn_pos")
    protected Point2D mSpawnPos;

    protected transient BufferedImage mImage;

    public MapResource(File f) {
        mImagePath = f.getName();

        try {
            mImage = ImageIO.read(f);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Point2D getSpwan() {
        return mSpawnPos;
    }

    public boolean hasSpawn() {
        return mSpawnPos != null;
    }
    
    public Map toMap() {
        Map o = new Map(mImage);
        
        // 벽을 만들고
        for (List<Point2D> l : mWall) {
            o.addObject(new Wall(l));
        }
        
        // 
        for (List<Point2D> l : mDarkness) {
            o.addObject(new Darkness(l));
        }
        
        for (PortalResource p : mPortal) {
            o.addObject(p.toGameObject());
        }
        
        return o;
    }

    public static class PortalResource {

        @SerializedName("dest_map")
        protected String mDestMap;

        @SerializedName("dest_pos")
        protected Point2D mDestPos;

        @SerializedName("x")
        protected float mX;
        
        @SerializedName("y")
        protected float mY;
        
        @SerializedName("width")
        protected float mWidth;
        
        @SerializedName("height")
        protected float mHeight;

        public GameObject toGameObject() {
            return new Portal(mDestMap, mDestPos, new Box2D(mX, mY, mWidth, mHeight).toPolygon());
        }
    }

}
