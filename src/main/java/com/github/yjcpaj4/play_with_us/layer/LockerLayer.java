package com.github.yjcpaj4.play_with_us.layer;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.InputManager;
import com.github.yjcpaj4.play_with_us.Layer;
import com.github.yjcpaj4.play_with_us.game.object.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;

public class LockerLayer extends Layer {
    
    private BufferedImage mBackground;
    
    private String mInput = "";
    
    private long mDuration;
    
    public LockerLayer(Application c) {
        super(c);
    }

    @Override
    protected void resume() {
        super.resume();
        
        mInput = "";
        mBackground = getResource().getImage("img.bg.library.locker");
    }
    
    public String getInputValue() {
        return mInput;
    }
    
    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        InputManager m = getInput();
        
        if (!mInput.isEmpty() && m.isKeyOnce(KeyEvent.VK_BACK_SPACE)) {
            mInput = mInput.substring(0, mInput.length() - 1);
        }
        
        else if (m.isKeyOnce(KeyEvent.VK_ENTER)) {
            finishLayer();
        }
        
        else if(m.isKeyOnce(KeyEvent.VK_R)) mInput += "R";
        else if(m.isKeyOnce(KeyEvent.VK_G)) mInput += "G";
        else if(m.isKeyOnce(KeyEvent.VK_B)) mInput += "B";
        else if(m.isKeyOnce(KeyEvent.VK_Y)) mInput += "Y";
        else if(m.isKeyOnce(KeyEvent.VK_W)) mInput += "W";
        
        else if(m.isKeyOnce(KeyEvent.VK_0)) mInput += "0";
        else if(m.isKeyOnce(KeyEvent.VK_1)) mInput += "1";
        else if(m.isKeyOnce(KeyEvent.VK_2)) mInput += "2";
        else if(m.isKeyOnce(KeyEvent.VK_3)) mInput += "3";
        else if(m.isKeyOnce(KeyEvent.VK_4)) mInput += "4";
        else if(m.isKeyOnce(KeyEvent.VK_5)) mInput += "5";
        else if(m.isKeyOnce(KeyEvent.VK_6)) mInput += "6";
        else if(m.isKeyOnce(KeyEvent.VK_7)) mInput += "7";
        else if(m.isKeyOnce(KeyEvent.VK_8)) mInput += "8";
        else if(m.isKeyOnce(KeyEvent.VK_9)) mInput += "9";
        
        int x = 0;
        int y = 0;
        int w = getContext().getWidth();
        int h = getContext().getHeight();
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("굴림", Font.PLAIN, 40));
        g2d.drawImage(mBackground, 0, 0, w, h, null);
        
        FontMetrics fm = g2d.getFontMetrics();
        
        String s = "암호입력 : " + mInput;
        
        if (mDuration < 700) {
            s += "_";
        } else {
            mDuration = 0;
        }

        g2d.drawString(s, 
                       x + 50, 
                       y + 50 + fm.getHeight());
        
        mDuration += delta;
    }
}
