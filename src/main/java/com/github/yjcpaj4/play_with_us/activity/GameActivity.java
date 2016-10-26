package com.github.yjcpaj4.play_with_us.activity;

import com.github.yjcpaj4.play_with_us.map.Map;
import java.awt.Graphics2D;

public class GameActivity extends Activity {

    private Map mMap;
    
    public GameActivity() {
        mMap = new Map();
    }
    
    @Override
    public void onStart(Param o) {
    }

    @Override
    public void onDraw(long delta, Graphics2D g2d) {
        mMap.update(delta);
        mMap.draw(delta, g2d);
    }

    @Override
    public void onPause() {
    }
}
