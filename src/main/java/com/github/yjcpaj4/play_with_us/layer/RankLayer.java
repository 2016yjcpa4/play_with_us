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

public class RankLayer extends Layer {
    
    private String mInput = "";
    
    private long mDuration;
    
    public RankLayer(Application c) {
        super(c);
    }

    @Override
    protected void resume() {
        super.resume();
        
        mInput = "";
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
        
        else if(m.isKeyOnce(KeyEvent.VK_A)) mInput += "A";
        else if(m.isKeyOnce(KeyEvent.VK_B)) mInput += "B";
        else if(m.isKeyOnce(KeyEvent.VK_C)) mInput += "C";
        else if(m.isKeyOnce(KeyEvent.VK_D)) mInput += "D";
        else if(m.isKeyOnce(KeyEvent.VK_E)) mInput += "E";
        else if(m.isKeyOnce(KeyEvent.VK_F)) mInput += "F";
        else if(m.isKeyOnce(KeyEvent.VK_G)) mInput += "G";
        else if(m.isKeyOnce(KeyEvent.VK_H)) mInput += "H";
        else if(m.isKeyOnce(KeyEvent.VK_I)) mInput += "I";
        else if(m.isKeyOnce(KeyEvent.VK_J)) mInput += "J";
        else if(m.isKeyOnce(KeyEvent.VK_K)) mInput += "K";
        else if(m.isKeyOnce(KeyEvent.VK_L)) mInput += "L";
        else if(m.isKeyOnce(KeyEvent.VK_M)) mInput += "M";
        else if(m.isKeyOnce(KeyEvent.VK_N)) mInput += "N";
        else if(m.isKeyOnce(KeyEvent.VK_O)) mInput += "O";
        else if(m.isKeyOnce(KeyEvent.VK_P)) mInput += "P";
        else if(m.isKeyOnce(KeyEvent.VK_Q)) mInput += "Q";
        else if(m.isKeyOnce(KeyEvent.VK_R)) mInput += "R";
        else if(m.isKeyOnce(KeyEvent.VK_S)) mInput += "S";
        else if(m.isKeyOnce(KeyEvent.VK_T)) mInput += "T";
        else if(m.isKeyOnce(KeyEvent.VK_U)) mInput += "U";
        else if(m.isKeyOnce(KeyEvent.VK_V)) mInput += "V";
        else if(m.isKeyOnce(KeyEvent.VK_W)) mInput += "W";
        else if(m.isKeyOnce(KeyEvent.VK_X)) mInput += "X";
        else if(m.isKeyOnce(KeyEvent.VK_Y)) mInput += "Y";
        else if(m.isKeyOnce(KeyEvent.VK_Z)) mInput += "Z";
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
        
        FontMetrics fm = g2d.getFontMetrics();
        
        String s = "플레이어 이름 : " + mInput;
        
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
