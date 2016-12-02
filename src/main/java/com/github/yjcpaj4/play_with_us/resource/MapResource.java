package com.github.yjcpaj4.play_with_us.resource;

import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.game.object.Lightless;
import com.github.yjcpaj4.play_with_us.game.object.NotWalkable;
import com.github.yjcpaj4.play_with_us.game.object.Player;
import com.github.yjcpaj4.play_with_us.game.Map;
import com.github.yjcpaj4.play_with_us.game.object.Portal;
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

    @SerializedName("not_walkable")
    protected List<List<Point2D>> mNotWalkable = new ArrayList();

    @SerializedName("lightless")
    protected List<List<Point2D>> mLightless = new ArrayList();

    @SerializedName("portal")
    protected List<PortalResource> mPortal = new ArrayList();

    @SerializedName("player_spawn")
    protected Point2D mPlayerSpawn;

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

    public BufferedImage getImage() {
        return mImage;
    }

    public int getWidth() {
        return mImage.getWidth();
    }

    public int getHeight() {
        return mImage.getHeight();
    }

    public void addNotWalkable(List<Point2D> l) {
        mNotWalkable.add(l);
    }

    public void addLightless(List<Point2D> l) {
        mLightless.add(l);
    }

    public void addPortal(String s, Point2D p, List<Point2D> l) {
        mPortal.add(new PortalResource(s, p, l));
    }

    public List<PortalResource> getPortal() {
        return mPortal;
    }

    public List<NotWalkable> getNotWalkable() {
        List<NotWalkable> l = new ArrayList<>();
        for (List<Point2D> o : mNotWalkable) {
            l.add(new NotWalkable(o));
        }
        return l;
    }

    public void setPlayerSpawn(Point2D p) {
        mPlayerSpawn = p;
    }

    public Point2D getPlayerSpwan() {
        return mPlayerSpawn;
    }

    public boolean hasPlayerSpawn() {
        return mPlayerSpawn != null;
    }

    public List<Lightless> getLightless() {
        List<Lightless> l = new ArrayList<>();
        for (List<Point2D> o : mLightless) {
            l.add(new Lightless(o));
        }
        return l;
    }

    public Map toMap() {
        List<GameObject> l = new ArrayList();
        l.addAll(getNotWalkable());
        l.addAll(getLightless());
        for (PortalResource o : mPortal) {
            l.add(o.toPortal());
        }
        return new Map(mImage, l);
    }

    public static class PortalResource {

        @SerializedName("dest_map")
        protected String mDestMap;

        @SerializedName("spawn_pos")
        protected Point2D mSpawnPos;

        @SerializedName("portal_area")
        protected List<Point2D> mPortalArea = new ArrayList<>();

        public PortalResource(String s, Point2D p, List<Point2D> l) {
            mDestMap = s;
            mSpawnPos = p;
            mPortalArea = l;
        }
        
        public java.awt.Polygon toAWTPolygon() {
            return new java.awt.Polygon(Point2D.getXPoints(mPortalArea), Point2D.getYPoints(mPortalArea), mPortalArea.size());
        }

        public Portal toPortal() {
            return new Portal(mDestMap, mSpawnPos, mPortalArea);
        }
    }

}
