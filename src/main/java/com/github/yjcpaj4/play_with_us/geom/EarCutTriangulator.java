package com.github.yjcpaj4.play_with_us.geom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;

/**
 * A simple implementation of the ear cutting algorithm to triangulate simple
 * polygons without holes. For more information see
 * http://cgm.cs.mcgill.ca/~godfried/teaching/cg-projects/97/Ian/algorithm2.html
 *
 * @author badlogicgames@gmail.com
 *
 */
public final class EarCutTriangulator {

    public static List<Polygon> getTriangulate(Polygon p) {
        List<Point2D> l1 = new EarCutTriangulator().getTriangulate(p.getPoints());
        
        List<Polygon> r = new ArrayList<>();

        for (int n = 0; n < l1.size(); n += 3) {
            List<Point2D> l2 = new ArrayList();
            l2.add(new Point2D(l1.get(n).getX(), l1.get(n).getY()));
            l2.add(new Point2D(l1.get(n + 1).getX(), l1.get(n + 1).getY()));
            l2.add(new Point2D(l1.get(n + 2).getX(), l1.get(n + 2).getY()));
            r.add(new Polygon(l2));
        }

        return r;
    }
    
    private static final int CONVEX_POINT = 1;
    private static final int CONCAVE_POINT = -1;
    
    private int mConcaveCount = 0;
    
    private EarCutTriangulator() {
    }

    private List<Point2D> getTriangulate(List<Point2D> l) {
        List<Point2D> r = new ArrayList();

        if (l.size() == 3) {
            r.addAll(l);
            return r;
        }

        while (l.size() >= 3) {
            int p[] = getClassifyPoints(l);

            for (int n = 0; n < l.size(); ++n) {
                float x1 = l.get(n == 0 ? l.size() - 1 : n - 1).getX();
                float y1 = l.get(n == 0 ? l.size() - 1 : n - 1).getY();
                float x2 = l.get(n).getX();
                float y2 = l.get(n).getY();
                float x3 = l.get(n == l.size() - 1 ? 0 : n + 1).getX();
                float y3 = l.get(n == l.size() - 1 ? 0 : n + 1).getY();

                if (isEar(l, p, x1, y1, x2, y2, x3, y3)) {

                    setCutEar(l, r, n);
                    l.remove(n);
                    break;
                }
            }
        }
        
        return r;
    }

    public boolean isPolygonClockwise(List<Point2D> l) {
        float f = 0;
        
        for (int n = 0; n < l.size(); n++) {
            Point2D p1 = l.get(n);
            Point2D p2 = l.get(n == l.size() - 1 ? 0 : n + 1);
            f += p1.getX() * p2.getY() - p2.getX() * p1.getY();
        }

        if (f < 0) {
            return true;
        } else {
            return false;
        }
    }

    private int[] getClassifyPoints(List<Point2D> l) {
        int[] p = new int[l.size()];
        mConcaveCount = 0;

        if ( ! isPolygonClockwise(l)) {
            Collections.reverse(l);
        }

        for (int n = 0; n < l.size(); ++n) {
            if (n == 0) {
                if (isConvex(l.get(l.size() - 1), l.get(n), l.get(n + 1))) {
                    p[n] = CONVEX_POINT;
                } else {
                    p[n] = CONCAVE_POINT;
                    mConcaveCount++;
                }
            } else if (n == l.size() - 1) {
                if (isConvex(l.get(n - 1), l.get(n), l.get(0))) {
                    p[n] = CONVEX_POINT; 
                } else {
                    p[n] = CONCAVE_POINT;
                    mConcaveCount++;
                }
            } else if (isConvex(l.get(n - 1), l.get(n), l.get(n + 1))) {
                p[n] = CONVEX_POINT;
            } else {
                p[n] = CONCAVE_POINT;
                mConcaveCount++;
            }
        }

        return p;
    }

    private boolean isConvex(Point2D p1, Point2D p2, Point2D p3) {
        return isConvex(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
    }
    
    private boolean isConvex(float x1, float y1, float x2, float y2, float x3, float y3) {
        if (getArea(x1, y1, x2, y2, x3, y3) < 0) {
            return false;
        } else {
            return true;
        }
    }

    private float getArea(float x1, float y1, float x2, float y2, float x3, float y3) {
        return x1 * (y3 - y2) 
             + x2 * (y1 - y3)
             + x3 * (y2 - y1);
    }

    private boolean isTriangleContainsPoint(List<Point2D> l, int[] p, float x1, float y1, float x2, float y2, float x3, float y3) {
        int n = 0;
        boolean r = true;

        while (n < l.size() - 1 && r) {
            
            if (p[n] == -1 && (((l.get(n).getX() != x1) && (l.get(n).getY() != y1)) 
                            || ((l.get(n).getX() != x2) && (l.get(n).getY() != y2))
                            || ((l.get(n).getX() != x3) && (l.get(n).getY() != y3)))) {

                float n1 = getArea(x1, y1, x2, y2, l.get(n).getX(), l.get(n).getY());
                float n2 = getArea(x2, y2, x3, y3, l.get(n).getX(), l.get(n).getY());
                float n3 = getArea(x3, y3, x1, y1, l.get(n).getX(), l.get(n).getY());

                if (n1 > 0 && n2 > 0 && n3 > 0) {
                    r = false;
                }
                
                if (n1 <= 0 && n2 <= 0 && n3 <= 0) {
                    r = false;
                }
            }
            n++;
        }
        return !r;
    }

    private boolean isEar(List<Point2D> l, int[] p, float x1, float y1, float x2, float y2, float x3, float y3) {
        if (mConcaveCount != 0) {
            if (isTriangleContainsPoint(l, p, x1, y1, x2, y2, x3, y3)) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void setCutEar(List<Point2D> l1, List<Point2D> l2, int n) {
        if (n == 0) {
            l2.add(new Point2D(l1.get(l1.size() - 1)));
            l2.add(new Point2D(l1.get(n)));
            l2.add(new Point2D(l1.get(n + 1)));
        } 
        else if ((n > 0) && (n < l1.size() - 1)) {
            l2.add(new Point2D(l1.get(n - 1)));
            l2.add(new Point2D(l1.get(n)));
            l2.add(new Point2D(l1.get(n + 1)));
        } 
        else if (n == l1.size() - 1) {
            l2.add(new Point2D(l1.get(n - 1)));
            l2.add(new Point2D(l1.get(n)));
            l2.add(new Point2D(l1.get(0)));
        }
    } 

}
