package com.github.yjcpaj4.play_with_us.geom;

import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import java.util.List;

/**
 * Separating Axis Theorem.
 *
 * 축 분리 정리 두개의 오브젝트를 교차(충돌)하였는지 검사하는 클래스.
 *
 * @see https://github.com/jriecken/sat-js/blob/master/SAT.js
 */
public class CollisionDetection {

    private CollisionDetection() {
    }
    
    public static class Response {

        private Shape mA;
        private Shape mB;
        private Vector2D mOverlapNorm;
        private Vector2D mOverlapVec;
        private float mOverlap;
        private boolean mAInB;
        private boolean mBInA;

        public Response() {
            mA = null;
            mB = null;
            mAInB = true;
            mBInA = true;
            mOverlap = Float.MAX_VALUE;
            mOverlapNorm = new Vector2D();
            mOverlapVec = new Vector2D();
        }
        
        public Shape getA() {
            return mA;
        }
        
        public Shape getB() {
            return mB;
        }
        
        public boolean isAInB() {
            return mAInB;
        }
        
        public boolean isBInA() {
            return mBInA;
        }
        
        public float getOverlap() {
            return mOverlap;
        }
        
        public Vector2D getOverlapVector() {
            return mOverlapVec;
        }
        
        public Vector2D getOverlapNorm() {
            return mOverlapNorm;
        }
    }
    
    private static final int LEFT_VORONOI_REGION = -1;
    private static final int MIDDLE_VORONOI_REGION = 0; 
    private static final int RIGHT_VORONOI_REGION = 1;
    
    private static int getVoronoiRegion(Vector2D v1, Vector2D v2) {
        double n1 = v1.lengthSq();
        double n2 = v2.dot(v1);
        
        if (n2 < 0) {
            return LEFT_VORONOI_REGION;
        }
        else if (n2 > n1) {
            return RIGHT_VORONOI_REGION;
        }
        else {
            return MIDDLE_VORONOI_REGION;
        }
    }
    
    public static boolean isCollides(Circle c, Polygon p) {
        return isCollides(c, p, null);
    }
    
    public static boolean isCollides(Circle c, Polygon p, Response r) {
        
        if (isCollides(p, c, r)) {
            
            if (r != null) {
                final Shape a = r.getA();
                final boolean b = r.isAInB();

                r.mA            = r.mB;
                r.mB            = a;
                r.mAInB         = r.isAInB();
                r.mBInA         = b;
                r.mOverlapVec   = r.mOverlapVec.neg();
                r.mOverlapNorm  = r.mOverlapNorm.neg();
            }
            
            return true;
        }
        
        return false;
    }
    
    public static boolean isCollides(Circle a, Circle b) {
        return isCollides(a, b, null);
    }
    
    public static boolean isCollides(Circle a, Circle b, Response r) {
        Vector2D v = new Vector2D(a.getPosition()).sub(b.getPosition());
        float f = a.getRadius() + b.getRadius();
        
        if (v.lengthSq() > (f * f)) {
            return false;
        }
        
        if (r != null) {
            double d = v.length();
            r.mA = a;
            r.mB = b;
            r.mOverlap = (float) (f - d);
            r.mOverlapNorm.set(v.norm());
            r.mOverlapVec.set(v.mult(r.mOverlap));
            r.mAInB = a.getRadius() <= b.getRadius() && d <= b.getRadius() - a.getRadius();
            r.mBInA = b.getRadius() <= a.getRadius() && d <= a.getRadius() - b.getRadius();
        }
        
        return true;
    }
    
    public static boolean isCollides(Polygon p, Circle c) {
        return isCollides(p, c, null);
    }
    
public static boolean isCollides(Polygon polygon, Circle circle, Response response) {
  /*
    // Get the position of the circle relative to the polygon.
    var circlePos = T_VECTORS.pop().copy(circle['pos']).sub(polygon['pos']);
    var radius = circle['r'];
    var radius2 = radius * radius;
    var points = polygon['calcPoints'];
    var len = points.length;
    var edge = T_VECTORS.pop();
    var point = T_VECTORS.pop();
    
    // For each edge in the polygon:
    for (var i = 0; i < len; i++) {
      var next = i === len - 1 ? 0 : i + 1;
      var prev = i === 0 ? len - 1 : i - 1;
      var overlap = 0;
      var overlapN = null;
      
      // Get the edge.
      edge.copy(polygon['edges'][i]);
      // Calculate the center of the circle relative to the starting point of the edge.
      point.copy(circlePos).sub(points[i]);
      
      // If the distance between the center of the circle and the point
      // is bigger than the radius, the polygon is definitely not fully in
      // the circle.
      if (response && point.len2() > radius2) {
        response['aInB'] = false;
      }
      
      // Calculate which Voronoi region the center of the circle is in.
      var region = voronoiRegion(edge, point);
      // If it's the left region:
      if (region === LEFT_VORONOI_REGION) {
        // We need to make sure we're in the RIGHT_VORONOI_REGION of the previous edge.
        edge.copy(polygon['edges'][prev]);
        // Calculate the center of the circle relative the starting point of the previous edge
        var point2 = T_VECTORS.pop().copy(circlePos).sub(points[prev]);
        region = voronoiRegion(edge, point2);
        if (region === RIGHT_VORONOI_REGION) {
          // It's in the region we want.  Check if the circle intersects the point.
          var dist = point.len();
          if (dist > radius) {
            // No intersection 
            return false;
          } else if (response) {
            // It intersects, calculate the overlap.
            response['bInA'] = false;
            overlapN = point.normalize();
            overlap = radius - dist;
          }
        }
        T_VECTORS.push(point2);
      // If it's the right region:
      } else if (region === RIGHT_VORONOI_REGION) {
        // We need to make sure we're in the left region on the next edge
        edge.copy(polygon['edges'][next]);
        // Calculate the center of the circle relative to the starting point of the next edge.
        point.copy(circlePos).sub(points[next]);
        region = voronoiRegion(edge, point);
        if (region === LEFT_VORONOI_REGION) {
          // It's in the region we want.  Check if the circle intersects the point.
          var dist = point.len();
          if (dist > radius) {
            // No intersection 
            return false;              
          } else if (response) {
            // It intersects, calculate the overlap.
            response['bInA'] = false;
            overlapN = point.normalize();
            overlap = radius - dist;
          }
        }
      // Otherwise, it's the middle region:
      } else {
        // Need to check if the circle is intersecting the edge,
        // Change the edge into its "edge normal".
        var normal = edge.perp().normalize();
        // Find the perpendicular distance between the center of the 
        // circle and the edge.
        var dist = point.dot(normal);
        var distAbs = Math.abs(dist);
        // If the circle is on the outside of the edge, there is no intersection.
        if (dist > 0 && distAbs > radius) {
          // No intersection 
          return false;
        } else if (response) {
          // It intersects, calculate the overlap.
          overlapN = normal;
          overlap = radius - dist;
          // If the center of the circle is on the outside of the edge, or part of the
          // circle is on the outside, the circle is not fully inside the polygon.
          if (dist >= 0 || overlap < 2 * radius) {
            response['bInA'] = false;
          }
        }
      }
      
      // If this is the smallest overlap we've seen, keep it. 
      // (overlapN may be null if the circle was in the wrong Voronoi region).
      if (overlapN && response && Math.abs(overlap) < Math.abs(response['overlap'])) {
        response['overlap'] = overlap;
        response['overlapN'].copy(overlapN);
      }
    }
    
    // Calculate the final overlap vector - based on the smallest overlap.
    if (response) {
      response['a'] = polygon;
      response['b'] = circle;
      response['overlapV'].copy(response['overlapN']).scale(response['overlap']);
    } 
    return true;
    */
    
     // Get the position of the circle relative to the polygon.
    Vector2D circlePos = new Vector2D(circle.getPosition());
    int radius = circle.getRadius();
    int radius2 = radius * radius;
    List<Point2D> points = polygon.getPoints();
    int len = points.size();
    Vector2D edge = new Vector2D();
    Vector2D point = new Vector2D();
    
    // For each edge in the polygon:
    for (int i = 0; i < len; i++) {
      int next = i == len - 1 ? 0 : i + 1;
      int prev = i == 0 ? len - 1 : i - 1;
      float overlap = 0;
      Vector2D overlapN = null;
      
      // Get the edge.
      edge = polygon.getEdge(i);
      // Calculate the center of the circle relative to the starting point of the edge.
      point = new Vector2D(circlePos).sub(points.get(i));
      
      // If the distance between the center of the circle and the point
      // is bigger than the radius, the polygon is definitely not fully in
      // the circle.
      if (response != null && point.lengthSq() > radius2) {
        response.mAInB = false;
      }
      
      // Calculate which Voronoi region the center of the circle is in.
      int region = getVoronoiRegion(edge, point);
      // If it's the left region:
      if (region == LEFT_VORONOI_REGION) {
        // We need to make sure we're in the RIGHT_VORONOI_REGION of the previous edge.
        edge = polygon.getEdge(prev);
        // Calculate the center of the circle relative the starting point of the previous edge
        Vector2D point2 = new Vector2D(circlePos).sub(points.get(prev));
        region = getVoronoiRegion(edge, point2);
        if (region == RIGHT_VORONOI_REGION) {
          // It's in the region we want.  Check if the circle intersects the point.
          double dist = point.length();
          if (dist > radius) {
            // No intersection 
            return false;
          } else if (response != null) {
            // It intersects, calculate the overlap.
            response.mBInA = false;
            overlapN = point.norm();
            overlap = (float) (radius - dist);
          }
        }
      // If it's the right region:
      } else if (region == RIGHT_VORONOI_REGION) {
        // We need to make sure we're in the left region on the next edge
        edge = polygon.getEdge(next);
        // Calculate the center of the circle relative to the starting point of the next edge.
        point = new Vector2D(circlePos).sub(points.get(next));
        region = getVoronoiRegion(edge, point);
        if (region == LEFT_VORONOI_REGION) {
          // It's in the region we want.  Check if the circle intersects the point.
          double dist = point.length();
          if (dist > radius) {
            // No intersection 
            return false;              
          } else if (response != null) {
            // It intersects, calculate the overlap.
            response.mBInA = false;
            overlapN = point.norm();
            overlap = (float) (radius - dist);
          }
        }
      // Otherwise, it's the middle region:
      } else {
        // Need to check if the circle is intersecting the edge,
        // Change the edge into its "edge normal".
        Vector2D normal = edge.perp().norm();
        // Find the perpendicular distance between the center of the 
        // circle and the edge.
        double dist = point.dot(normal);
        double distAbs = Math.abs(dist);
        // If the circle is on the outside of the edge, there is no intersection.
        if (dist > 0 && distAbs > radius) {
          // No intersection 
          return false;
        } else if (response != null) {
          // It intersects, calculate the overlap.
          overlapN = normal;
          overlap = (float) (radius - dist);
          // If the center of the circle is on the outside of the edge, or part of the
          // circle is on the outside, the circle is not fully inside the polygon.
          if (dist >= 0 || overlap < 2 * radius) {
            response.mBInA = false;
          }
        }
      }
      
      // If this is the smallest overlap we've seen, keep it. 
      // (overlapN may be null if the circle was in the wrong Voronoi region).
      if (overlapN != null && response != null && Math.abs(overlap) < Math.abs(response.mOverlap)) {
        response.mOverlap = overlap;
        response.mOverlapNorm = new Vector2D(overlapN);
      }
    }
    
    // Calculate the final overlap vector - based on the smallest overlap.
    if (response != null) {
      response.mA = polygon;
      response.mB = circle;
      response.mOverlapVec = new Vector2D(response.mOverlapNorm).mult(response.mOverlap);
    } 
    return true;
    
    
}

    public static boolean isCollides2(Polygon p, Circle c, Response r) {
        for (int l = 0; l < p.getPoints().size(); l++) {            
            float f = 0;
            Vector2D v1 = null;
            Vector2D v2 = p.getEdge(l);
            Vector2D v3 = new Vector2D(c.getPosition()).sub(p.getPoint(l));

            if (r != null && v3.lengthSq() > (c.getRadius() * c.getRadius())) {
                r.mAInB = false;
            }

            int n = getVoronoiRegion(v2, v3);
            if (n == LEFT_VORONOI_REGION) {
                v2 = p.getEdge(l - 1);
                Vector2D p2 = new Vector2D(c.getPosition()).sub(p.getPoint(l - 1));
                if (getVoronoiRegion(v2, p2) == RIGHT_VORONOI_REGION) {
                    double d = v3.length();
                    if (d > c.getRadius()) {
                        return false;
                    }
                    else if (r != null) {
                        r.mBInA = false;
                        v1 = v3.norm();
                        f = (float) (c.getRadius() - d);
                    }
                }
            } else if (n == RIGHT_VORONOI_REGION) {
                v2 = p.getEdge(l + 1);
                v3 = new Vector2D(c.getPosition()).sub(p.getPoint(l + 1));
                n = getVoronoiRegion(v2, v3);
                if (n == LEFT_VORONOI_REGION) {
                    double d = v3.length();
                    if (d > c.getRadius()) {
                        return false;
                    }
                    else if (r != null) {
                        r.mBInA = false;
                        v1 = v3.norm();
                        f = c.getRadius() - (float) d;
                    }
                }
            } 
            else {
                Vector2D v4 = v2.perp().norm();
                double d = v3.dot(v4);
                if (d > 0 && Math.abs(d) > c.getRadius()) {
                    return false;
                }
                else if (r != null) {
                    v1 = v4;
                    f = (float) (c.getRadius() - d);
                    
                    if (d >= 0 || f < 2 * c.getRadius()) {
                        r.mBInA = false;
                    }
                }
            }
 
            if (v1 != null && r != null && Math.abs(f) < Math.abs(r.mOverlap)) {
                r.mOverlap = f;
                r.mOverlapNorm.set(v1);
            }
        }
        
        if (r != null) {
            r.mA = p;
            r.mB = c;
            r.mOverlapVec.set(r.mOverlapNorm);
            r.mOverlapVec = r.mOverlapVec.mult(r.mOverlap);
        }
        
        return true;
    }
}
