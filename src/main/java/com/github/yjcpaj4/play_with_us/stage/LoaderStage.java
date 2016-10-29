package com.github.yjcpaj4.play_with_us.stage;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.Stage;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public abstract class LoaderStage extends Stage {
        
    protected static final int PADDING = 25;

    protected float mProgress;
    protected String mMessage;

    public LoaderStage(Application c) {
        super(c);
    }

    private void drawMessage(Graphics2D g2d) {
        if (mMessage == null) {
            return;
        }

        Font f = new Font(getCanvas().getFont().getName(), Font.PLAIN, 30);
        FontMetrics m = g2d.getFontMetrics(f);

        int w = getCanvas().getWidth();
        int h = getCanvas().getHeight();

        g2d.setFont(f);
        g2d.drawString(mMessage, 
                       w / 2 - m.stringWidth(mMessage) / 2, 
                       h - PADDING - 50 - 10 - m.getHeight());
    }

    private void drawProgress(Graphics2D g2d) {
        int x = PADDING;
        int y = getCanvas().getHeight() - PADDING - 50;            

        g2d.setColor(Color.RED);
        g2d.drawRect(x, y, getCanvas().getWidth() - PADDING * 2, 50);
        g2d.fillRect(x, y, (int) (getCanvas().getWidth() * mProgress) - PADDING * 2, 50);
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        Font f = new Font(getCanvas().getFont().getName(), Font.PLAIN, 50);
        FontMetrics m = g2d.getFontMetrics(f);
        int n = (int) (delta / 500 % 4);
        String s = "Loading";

        for (int l = 0; l < n; ++l) {
            s += ".";
        }

        g2d.setFont(f);
        g2d.setColor(Color.red);
        g2d.drawString(s, PADDING, PADDING + m.getHeight());

        drawProgress(g2d);
        drawMessage(g2d);
    }

    @Override
    protected void stop() {
    }
}