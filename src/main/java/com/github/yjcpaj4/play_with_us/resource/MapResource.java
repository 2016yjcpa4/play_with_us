package com.github.yjcpaj4.play_with_us.resource;

import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.game.object.Darkness;
import com.github.yjcpaj4.play_with_us.game.object.Wall;
import com.github.yjcpaj4.play_with_us.game.object.Player;
import com.github.yjcpaj4.play_with_us.game.Map;
import com.github.yjcpaj4.play_with_us.game.object.Portal;
import com.github.yjcpaj4.play_with_us.game.special_object.BathroomBloodstains;
import com.github.yjcpaj4.play_with_us.game.special_object.BathroomBrokenLight;
import com.github.yjcpaj4.play_with_us.game.special_object.BathroomGhost;
import com.github.yjcpaj4.play_with_us.game.special_object.BathroomMirror;
import com.github.yjcpaj4.play_with_us.game.special_object.BathroomTowel;
import com.github.yjcpaj4.play_with_us.game.special_object.BathroomTub;
import com.github.yjcpaj4.play_with_us.game.special_object.BathroomWaterDrop;
import com.github.yjcpaj4.play_with_us.game.special_object.ClothesroomMannequin;
import com.github.yjcpaj4.play_with_us.game.special_object.ClothesroomMannequinMine;
import com.github.yjcpaj4.play_with_us.game.special_object.ClothesroomPicture;
import com.github.yjcpaj4.play_with_us.game.special_object.KitchenFirstCabinet;
import com.github.yjcpaj4.play_with_us.game.special_object.KitchenRefrigerator;
import com.github.yjcpaj4.play_with_us.game.special_object.KitchenSecondCabinet;
import com.github.yjcpaj4.play_with_us.game.special_object.KitchenTV;
import com.github.yjcpaj4.play_with_us.game.special_object.LibraryBookBlue;
import com.github.yjcpaj4.play_with_us.game.special_object.LibraryBookGreen;
import com.github.yjcpaj4.play_with_us.game.special_object.LibraryBookRed;
import com.github.yjcpaj4.play_with_us.game.special_object.LibraryBookWhite;
import com.github.yjcpaj4.play_with_us.game.special_object.LibraryBookYellow;
import com.github.yjcpaj4.play_with_us.game.special_object.LibraryClock;
import com.github.yjcpaj4.play_with_us.game.special_object.LibraryFirstPaper;
import com.github.yjcpaj4.play_with_us.game.special_object.LibraryPaino;
import com.github.yjcpaj4.play_with_us.game.special_object.LibraryFirstPicture;
import com.github.yjcpaj4.play_with_us.game.special_object.LibrarySecondPaper;
import com.github.yjcpaj4.play_with_us.game.special_object.LibrarySecondPicture;
import com.github.yjcpaj4.play_with_us.game.special_object.LivingroomShoerack;
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
        r.mAlias = FileUtil.getNameWithoutExtension(f);

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
    
    protected transient String mAlias;
 
    public Point2D getSpwan() {
        return mSpawnPos;
    }

    public boolean hasSpawn() {
        return mSpawnPos != null;
    }
    
    public Map newMap() {
        Map o = new Map(mImage, mSpawnPos);
        
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
        
        if (mAlias.equalsIgnoreCase("map.livingroom")) {
            o.addObject(new LivingroomShoerack());
        }
        
        if (mAlias.equalsIgnoreCase("map.kitchen")) {
            o.addObject(new KitchenTV());
            o.addObject(new KitchenRefrigerator());
            o.addObject(new KitchenFirstCabinet());
            o.addObject(new KitchenSecondCabinet());
        }

        if (mAlias.equalsIgnoreCase("map.clothesroom")) {
            o.addObject(new ClothesroomMannequin());
            o.addObject(new ClothesroomMannequinMine());
            o.addObject(new ClothesroomPicture());
        }
        
        if (mAlias.equalsIgnoreCase("map.library")) {
            o.addObject(new LibraryPaino());
            o.addObject(new LibraryFirstPicture());
            o.addObject(new LibrarySecondPicture());
            o.addObject(new LibraryClock());
            o.addObject(new LibraryBookGreen());
            o.addObject(new LibraryBookYellow());
            o.addObject(new LibraryBookRed());
            o.addObject(new LibraryBookBlue());
            o.addObject(new LibraryFirstPaper());
            o.addObject(new LibrarySecondPaper());
            o.addObject(new LibraryBookWhite());
        }

        if (mAlias.equalsIgnoreCase("map.bathroom")) {
            o.addObject(new BathroomBloodstains());
            o.addObject(new BathroomGhost());
            o.addObject(new BathroomTub());
            o.addObject(new BathroomBrokenLight());
            o.addObject(new BathroomTowel());
            o.addObject(new BathroomMirror());
            o.addObject(new BathroomWaterDrop());
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
