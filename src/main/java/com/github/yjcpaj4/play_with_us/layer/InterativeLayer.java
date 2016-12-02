package com.github.yjcpaj4.play_with_us.layer;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.Layer;
import com.github.yjcpaj4.play_with_us.game.special_object.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;

public class InterativeLayer extends Layer {
    
    private static final int PADDING = 20;
    
    private static final int ANSWERS_LINE_HEIGHT = 40;
    
    private String mQuestion;
    private String[] mAnswers;
    private BufferedImage mBackground;
    
    private int mCurrentAnswerIndex = 0;
    
    public InterativeLayer(Application c) {
        super(c);
    }
    
    public void setQuestion(String s) {
        mQuestion = s;
    }
    
    public void setAnswers(String[] s) {
        mAnswers = s;
    }
    
    public void setBackground(BufferedImage b) {
        mBackground = b;
    }
    
    public String getCurrentAnswer() {
        return mAnswers[mCurrentAnswerIndex];
    }

    @Override
    protected void resume() {
        super.resume(); //To change body of generated methods, choose Tools | Templates.
        
        mCurrentAnswerIndex = 0;
    }
    
    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        if (getInput().isKeyOnce(KeyEvent.VK_UP)) {
            mCurrentAnswerIndex = Math.max(0, mCurrentAnswerIndex - 1);
        }
        else if (getInput().isKeyOnce(KeyEvent.VK_DOWN)) {
            mCurrentAnswerIndex = Math.min(mAnswers.length - 1, mCurrentAnswerIndex + 1);
        }
        else if (getInput().isKeyOnce(KeyEvent.VK_ENTER)) {
            finishLayer();
        }
        
        int x = 0;
        int y = 0;
        int w = getContext().getWidth();
        int h = getContext().getHeight();
        
        g2d.drawImage(mBackground, 0, 0, w, h, null);
        
        w -= PADDING * 2;
        h -= PADDING * 2;
        
        g2d.setFont(new Font("굴림", Font.BOLD, 25));
        g2d.setColor(Color.WHITE);
        
        FontMetrics fm = g2d.getFontMetrics();
        
        for(int n = 0; n < mAnswers.length; ++n) {
            String s = mAnswers[n];
            
            if (mCurrentAnswerIndex == n) {
                s = "▶ " + s;
            }
            
            y = h - (mAnswers.length - n) * ANSWERS_LINE_HEIGHT;
            
            g2d.drawString(s, w - fm.stringWidth(s), y);
        }
        
        g2d.setFont(new Font("굴림", Font.BOLD, 30));
        fm = g2d.getFontMetrics();
        
        g2d.drawString(mQuestion, w - fm.stringWidth(mQuestion), y - fm.getHeight() - 50);
    }
}
