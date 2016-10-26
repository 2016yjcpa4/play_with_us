package com.github.yjcpaj4.play_with_us.geom;

import java.util.ArrayList;
import com.github.yjcpaj4.play_with_us.math.Point2D;
public class Rect extends Polygon {

    public Rect(int x, int y, int w, int h) {
        super(new ArrayList<Point2D>() {{
            add(new Point2D(x, y));
            add(new Point2D(w + x, y));
            add(new Point2D(w + x, h + y));
            add(new Point2D(x, h + y));
        }});
    }
}
