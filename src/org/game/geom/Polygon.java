package org.game.geom;

import java.util.ArrayList;
import java.util.List;
import org.game.math.Matrix2D;
import org.game.math.Point2D;
import org.game.math.Vector2D;

public class Polygon {

    private List<Point2D> vtx = new ArrayList<>();
    private List<Point2D> edge = new ArrayList<>();

    public Polygon() {
    }
    
    public Polygon(List<Point2D> vtx) {
        addAll(vtx);
    }
    
    public void addAll(List<Point2D> l) {
        vtx.addAll(l);

        setEdges();
    }
    
    public int[] getXPoints() {
        return Point2D.getXPoints(vtx);
    }
    
    public int[] getYPoints() {
        return Point2D.getYPoints(vtx);
    }
    
    
    public List<Point2D> getVertex() {
        return vtx;
    }

    private void setEdges() {
        edge.clear();

        for (int n = 0; n < vtx.size(); n++) {
            Point2D p1 = vtx.get(n);
            Point2D p2;

            if (n + 1 >= vtx.size()) {
                p2 = vtx.get(0);
            } else {
                p2 = vtx.get(n + 1);
            }

            Vector2D v = new Vector2D(p2).sub(p1);

            edge.add(new Point2D((int) v.getX(), (int) v.getY()));
        }
    }
    
    public void transform(Matrix2D m) {
        transform(m, getPosition());
    }

    public void transform(Matrix2D m, Point2D about) { 
        m = Matrix2D.translate(about.getX(), about.getY()).concat(m).concat(Matrix2D.translate(-about.getX(), -about.getY()));
        
        for (int n = 0; n < vtx.size(); ++n) {
            Point2D p = vtx.get(n);
            double x = p.getX();
            double y = p.getY();
            
            p.setX((int) (x * m.getA() + y * m.getC() + m.getE()));
            p.setY((int) (x * m.getB() + y * m.getD()+ m.getF()));
        }
    }

    public Point2D getPosition() {

        float x = 0;
        float y = 0;
        for (int n = 0; n < vtx.size(); n++) {
            x += vtx.get(n).getX();
            y += vtx.get(n).getY();
        }

        return new Point2D((int) (x / (float) vtx.size()), (int) (y / (float) vtx.size()));
    }
    
    public class PolygonCollisionResult {
            public boolean willHit; // Are the polygons going to intersect forward in time?
            public boolean isHit; // Are the polygons currently intersecting
            public Vector2D minTranslVec; // The translation to apply to polygon A to push the polygons appart.
    }

    // min, max
    public void ProjectPolygon(Vector2D axis, Polygon polygon, double min, double max) {
        // To project a point on an axis use the dot product
        double d = axis.dotProduct(polygon.vtx.get(0));
        min = d;
        max = d;
        for (int n = 0; n < polygon.vtx.size(); n++) {
            d = new Vector2D(polygon.vtx.get(n)).dotProduct(axis);
            if (d < min) {
                min = d;
            } else if (d > max) {
                max = d;
            }
        }
    }

    public double IntervalDistance(double minA, double maxA, double minB, double maxB) {
        if (minA < minB) {
            return minB - maxA;
        } else {
            return minA - maxB;
        }
    }
    
    
		// Check if polygon A is going to collide with polygon B for the given velocity
		public PolygonCollisionResult PolygonCollision(Polygon polygonA, Polygon polygonB, Vector2D velocity) {
			PolygonCollisionResult result = new PolygonCollisionResult();
			result.isHit = true;
			result.willHit = true;

			int edgeCountA = polygonA.edge.size();
			int edgeCountB = polygonB.edge.size();
			double minIntervalDistance = Double.POSITIVE_INFINITY;
			Vector2D translationAxis = new Vector2D();
			Point2D edge;

			// Loop through all the edges of both polygons
			for (int edgeIndex = 0; edgeIndex < edgeCountA + edgeCountB; edgeIndex++) {
				if (edgeIndex < edgeCountA) {
					edge = polygonA.edge.get(edgeIndex);
				} else {
					edge = polygonB.edge.get(edgeIndex - edgeCountA);
				}

				// ===== 1. Find if the polygons are currently intersecting =====

				// Find the axis perpendicular to the current edge
				Vector2D axis = new Vector2D(-edge.getY(), edge.getX());
				axis.normalize();

				// Find the projection of the polygon on the current axis
				double minA = 0; double minB = 0; double maxA = 0; double maxB = 0;
				ProjectPolygonResult a = ProjectPolygon(axis, polygonA);
				ProjectPolygonResult b = ProjectPolygon(axis, polygonB);
                                
                                minA = a.min;
                                maxA = a.max;
                                minB = b.min;
                                maxB = b.max;

				// Check if the polygon projections are currentlty intersecting
				if (IntervalDistance(minA, maxA, minB, maxB) > 0) result.isHit = false;

				// ===== 2. Now find if the polygons *will* intersect =====

				// Project the velocity on the current axis
				double velocityProjection = axis.dotProduct(velocity);

				// Get the projection of polygon A during the movement
				if (velocityProjection < 0) {
					minA += velocityProjection;
				} else {
					maxA += velocityProjection;
				}

				// Do the same test as above for the new projection
				double intervalDistance = IntervalDistance(minA, maxA, minB, maxB);
				if (intervalDistance > 0) result.willHit = false;

				// If the polygons are not intersecting and won't intersect, exit the loop
				if (!result.isHit && !result.willHit) break;

				// Check if the current interval distance is the minimum one. If so store
				// the interval distance and the current distance.
				// This will be used to calculate the minimum translation vector
				intervalDistance = Math.abs(intervalDistance);
				if (intervalDistance < minIntervalDistance) {
					minIntervalDistance = intervalDistance;
					translationAxis = axis;

					Vector2D d = new Vector2D(polygonA.getPosition()).sub(polygonB.getPosition());
                                        
					if (d.dotProduct(translationAxis) < 0) {
                                            translationAxis = new Vector2D(-translationAxis.getX(), -translationAxis.getY());
                                        }
				}
			}

			// The minimum translation vector can be used to push the polygons appart.
			// First moves the polygons by their velocity
			// then move polygonA by MinimumTranslationVector.
			if (result.willHit) result.minTranslVec = translationAxis.mult(minIntervalDistance, minIntervalDistance);
			
			return result;
		}

                private static class ProjectPolygonResult {
                    
                    public double min;
                    public double max;
                }
                
		// Calculate the projection of a polygon on an axis and returns it as a [min, max] interval
		public ProjectPolygonResult ProjectPolygon(Vector2D axis, Polygon polygon) {
			// To project a point on an axis use the dot product
			double d = axis.dotProduct(polygon.vtx.get(0));
                        ProjectPolygonResult r = new ProjectPolygonResult();
			r.min = d;
			r.max = d;
			for (int i = 0; i < polygon.vtx.size(); i++) {
				d = new Vector2D(polygon.vtx.get(i)).dotProduct(axis);
				if (d < r.min) {
					r.min = d;
				} else {
					if (d > r.max) {
						r.max = d;
					}
				}
			}
                        return r;
		}
}
