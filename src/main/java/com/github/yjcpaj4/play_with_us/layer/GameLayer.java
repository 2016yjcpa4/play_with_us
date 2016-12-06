package com.github.yjcpaj4.play_with_us.layer;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.Layer;
import com.github.yjcpaj4.play_with_us.game.Camera;
import com.github.yjcpaj4.play_with_us.game.Map;
import com.github.yjcpaj4.play_with_us.game.special_object.LivingroomShoerack;
import com.github.yjcpaj4.play_with_us.game.object.Player;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.resource.MapResource;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class GameLayer extends Layer {

    private static final String MAIN_MAP = "map.girlsroom";

    private Timer mMessageTimer = new Timer();
    private Queue<String> mMessages = new ArrayDeque<>();

    private Player mPlayer;

    private Camera mCamera;

    private boolean mShowHelper = false;

    private long mStartTime;
    private int mDieCount;

    private java.util.Map<String, Map> mCachedMap = new HashMap<>();

    public GameLayer(Application c) {
        super(c);

        /*
         * 게임은 플레이어의 중심으로 돌아가기때문에
         * 맵이아닌 플레이어와 맵을 생성하고 draw 시 플레이어가 속한 맵을 draw 합니다.
         */
        init();
        
        mPlayer.setOwnedLight();

        mCamera = new Camera(c);
    }

    public void restart() {
        mDieCount++;
        init();
    }

    public void init() {
        mCachedMap.clear();

        mStartTime = System.currentTimeMillis();

        Map o = getMap(MAIN_MAP);

        if (o.hasSpawnPosition()) {
            mPlayer = new Player(o.getSpwanPosition());
            o.addObject(mPlayer);
        }
    }

    public Map getMap(String s) {
        if (mCachedMap.containsKey(s)) {
            return mCachedMap.get(s);
        }

        MapResource r = getResource().getMap(s);
        Map o = r.newMap();

        mCachedMap.put(s, o);

        return o;
    }

    public void showMessage(String s) {
        showMessage(s, 2000);
    }

    public void showMessage(String s, int n) {
        final TimerTask t = new TimerTask() {

            @Override
            public void run() {
                mMessages.poll();
            }
        };

        mMessages.add(s);
        mMessageTimer.schedule(t, n);
    }

    public Camera getCamera() {
        return mCamera;
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public Map getMap() {
        return mPlayer.getMap();
    }

    private void drawFPS(Graphics2D g2d, long delta) {
        if (!Application.DEBUG) {
            return;
        }

        Point2D p = mCamera.getPosition();
        int w = mCamera.getWidth();
        int h = mCamera.getHeight();
        int x = (int) p.getX();
        int y = (int) p.getY();

        Font f = new Font("굴림", Font.BOLD, 15);
        FontMetrics fm = g2d.getFontMetrics(f);

        String s = "FPS : " + delta;

        g2d.setColor(Color.RED);
        g2d.setFont(f);
        g2d.drawString(s,
                x + w - fm.stringWidth(s) - 10,
                y + fm.getHeight() + 10);
    }

    private void drawHelp(Graphics2D g2d) {
        Point2D p = mCamera.getPosition();
        int w = mCamera.getWidth();
        int h = mCamera.getHeight();
        int x = (int) p.getX();
        int y = (int) p.getY();

        Font f = new Font("굴림", Font.BOLD, 13);
        FontMetrics fm = g2d.getFontMetrics(f);

        if (mShowHelper) {
            BufferedImage b = getResource().getImage("img.bg.help");
            g2d.drawImage(b,
                    x,
                    y,
                    (int) (b.getWidth() / mCamera.getZoom()),
                    (int) (b.getHeight() / mCamera.getZoom()),
                    null);
        } else {
            g2d.setColor(Color.WHITE);
            g2d.drawString("도움말 F1", x + 10, y + fm.getHeight() + 10);
        }

    }

    private void drawStatus(Graphics2D g2d) {
        Point2D p = mCamera.getPosition();
        int w = mCamera.getWidth();
        int h = mCamera.getHeight();
        int x = (int) p.getX();
        int y = (int) p.getY();

        Font f = new Font("굴림", Font.BOLD, 13);
        FontMetrics fm = g2d.getFontMetrics(f);

        g2d.setFont(f);
        g2d.setColor(Color.WHITE);

        String s1 = "죽은횟수 : " + mDieCount + "번";

        y = y + h - 10 - fm.getHeight();

        g2d.drawString(s1, x + 10, y);

        /*
        long ms = System.currentTimeMillis() - mStartTime;
        long secs = ms / 1000;
        long minutes = secs / 60;
        long hours = minutes / 60;
        
        secs = secs % 60;
        minutes = minutes % 60;
        
        
        String s2 = "플레이타임 : " + String.format("%02d:%02d:%02d", hours, minutes, secs);
        
        g2d.drawString(s2, x + 10, y - 10 - fm.getHeight());*/
    }

    private void drawMessages(Graphics2D g2d) {
        if (mMessages.isEmpty()) {
            return;
        }

        Point2D p = mCamera.getPosition();
        int w = mCamera.getWidth();
        int h = mCamera.getHeight();
        int x = (int) p.getX();
        int y = (int) p.getY();

        Font f = new Font("굴림", Font.BOLD, 13);
        FontMetrics fm = g2d.getFontMetrics(f);

        g2d.setFont(f);
        g2d.setColor(Color.WHITE);

        for (String s : mMessages) {
            g2d.drawString(s,
                    x - fm.stringWidth(s) + w - 10,
                    y + fm.getHeight() + 10);

            y += (fm.getHeight() + 3);
        }
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);

        mCamera.update(mPlayer.getPosition());
        mCamera.draw(g2d);

        Map m = mPlayer.getMap();
        m.update(this, delta);
        m.draw(this, delta, g2d);

        if (getInput().isKeyOnce(KeyEvent.VK_F1)) {
            mShowHelper = !mShowHelper;
        } else if (mShowHelper && getInput().isKeyOnce(KeyEvent.VK_ESCAPE)) {
            mShowHelper = false;
        }

        drawHelp(g2d);
        drawMessages(g2d);
        drawFPS(g2d, delta);
        drawStatus(g2d);
    }
}
