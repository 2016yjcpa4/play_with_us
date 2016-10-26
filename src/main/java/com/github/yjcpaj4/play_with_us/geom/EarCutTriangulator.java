/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
        EarCutTriangulator t = new EarCutTriangulator();
        List<Vector2D> l = t.getTriangulate(p.getVectors());
        
        List<Polygon> r = new ArrayList<>();

        for (int n = 0; n < l.size(); n += 3) {
            Polygon o = new Polygon();
            o.addPoint(new Point2D((int) l.get(n).getX(), (int) l.get(n).getY()));
            o.addPoint(new Point2D((int) l.get(n + 1).getX(), (int) l.get(n + 1).getY()));
            o.addPoint(new Point2D((int) l.get(n + 2).getX(), (int) l.get(n + 2).getY()));
            r.add(o);
        }

        return r;
    }
    
    private static final int CONVEX_POINT = 1;
    private static final int CONCAVE_POINT = -1;
    
    private int mConcaveCount = 0;
    
    private EarCutTriangulator() {
    }

    private List<Vector2D> getTriangulate(List<Vector2D> l) {
        List<Vector2D> r = new ArrayList<Vector2D>();

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

    public boolean isPolygonClockwise(List<Vector2D> v) {
        float n = 0;
        
        for (int l = 0; l < v.size(); l++) {
            Vector2D p1 = v.get(l);
            Vector2D p2 = v.get(l == v.size() - 1 ? 0 : l + 1);
            n += p1.getX() * p2.getY() - p2.getX() * p1.getY();
        }

        if (n < 0) {
            return true;
        } else {
            return false;
        }
    }

    private int[] getClassifyPoints(List<Vector2D> l) {
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

    private boolean isConvex(Vector2D v1, Vector2D v2, Vector2D v3) {
        return isConvex(v1.getX(), v1.getY(), v2.getX(), v2.getY(), v3.getX(), v3.getY());
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

    private boolean isTriangleContainsPoint(List<Vector2D> l, int[] p, float x1, float y1, float x2, float y2, float x3, float y3) {
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

    private boolean isEar(List<Vector2D> l, int[] p, float x1, float y1, float x2, float y2, float x3, float y3) {
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

    private void setCutEar(List<Vector2D> v, List<Vector2D> l, int n) {
        if (n == 0) {
            l.add(new Vector2D(v.get(v.size() - 1)));
            l.add(new Vector2D(v.get(n)));
            l.add(new Vector2D(v.get(n + 1)));
        } 
        else if ((n > 0) && (n < v.size() - 1)) {
            l.add(new Vector2D(v.get(n - 1)));
            l.add(new Vector2D(v.get(n)));
            l.add(new Vector2D(v.get(n + 1)));
        } 
        else if (n == v.size() - 1) {
            l.add(new Vector2D(v.get(n - 1)));
            l.add(new Vector2D(v.get(n)));
            l.add(new Vector2D(v.get(0)));
        }
    } 

}
