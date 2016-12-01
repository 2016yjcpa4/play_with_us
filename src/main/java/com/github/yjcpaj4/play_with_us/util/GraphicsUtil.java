package com.github.yjcpaj4.play_with_us.util;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class GraphicsUtil {

    private GraphicsUtil() {
    }

    public static void drawStringMultiLine(Graphics2D g, String s, int w, int x, int y) {
        FontMetrics fm = g.getFontMetrics();
        if (fm.stringWidth(s) < w) {
            g.drawString(s, x, y);
        } 
        else {
            String[] str = s.split(" ");
            String cur = str[0];
            for (int n = 1; n < str.length; n++) {
                if (fm.stringWidth(cur + str[n]) < w) {
                    cur += " " + str[n];
                } else {
                    g.drawString(cur, x, y);
                    y += fm.getHeight();
                    cur = str[n];
                }
            }
            if (cur.trim().length() > 0) {
                g.drawString(cur, x, y);
            }
        }
    }
}
