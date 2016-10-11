/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.geom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.game.math.Point2D;
import org.game.math.Vector2D;

/**
 * A simple implementation of the ear cutting algorithm to triangulate simple polygons without holes. For more information see
 * http://cgm.cs.mcgill.ca/~godfried/teaching/cg-projects/97/Ian/algorithm2.html
 * @author badlogicgames@gmail.com
 * 
 */
public final class EarCutTriangulator {
	/**
	 * Triangulates the list of points and returns an array of {@link Vector3} triples that each form a single triangle.
	 * 
	 * @param polygon The polygon to triangulate
	 * @return The list of triangle vertices.
	 */
    
    public  List<Polygon> triangulate (Polygon polygon) { 
        
        List<Vector2D> v = new ArrayList<>();
        
        for(int i = 0; i < polygon.getPoints().size(); ++i) {
            v.add(new Vector2D(polygon.getPoint(i)));
        }
        
        List<Vector2D> p = triangulate(v);
        List<Polygon> a = new ArrayList<Polygon>();
        
        for(int i = 0; i < p.size(); i += 3) {
            Polygon pp = new Polygon();
            pp.add(new Point2D((int) p.get(i).x, (int) p.get(i).y));
            pp.add(new Point2D((int) p.get(i + 1).x, (int) p.get(i + 1).y));
            pp.add(new Point2D((int) p.get(i + 2).x, (int) p.get(i + 2).y));
            
            a.add(pp);
        }
        
        return a;
    }
    
	public  List<Vector2D> triangulate (List<Vector2D> polygon) {
		List<Vector2D> triangles = new ArrayList<Vector2D>();
		List<Vector2D> tmp = new ArrayList<Vector2D>(polygon.size());
		tmp.addAll(polygon);
		polygon = tmp;

		if (polygon.size() == 3) {
			triangles.addAll(polygon);
			return triangles;
		}

		while (polygon.size() >= 3) {
			int ptType[] = classifyPoints(polygon);

			for (int i = 0; i < polygon.size(); i++) {
				float x1 = polygon.get(i == 0 ? polygon.size() - 1 : i - 1).x;
				float y1 = polygon.get(i == 0 ? polygon.size() - 1 : i - 1).y;
				float x2 = polygon.get(i).x;
				float y2 = polygon.get(i).y;
				float x3 = polygon.get(i == polygon.size() - 1 ? 0 : i + 1).x;
				float y3 = polygon.get(i == polygon.size() - 1 ? 0 : i + 1).y;

				if (ear(polygon, ptType, x1, y1, x2, y2, x3, y3)) {

					cutEar(polygon, triangles, i);
					updatePolygon(polygon, i);
					break;
				}
			}
		}

// if( polygon.size() == 3 )
// {
// triangles.add( polygon.get(0) );
// triangles.add( polygon.get(1) );
// triangles.add( polygon.get(2) );
// }

		return triangles;
	}

	/*
	 * polygonClockwise: Returns true if user inputted polygon in clockwise order, false if counterclockwise. The Law of Cosines is
	 * used to determine the angle.
	 */
	public  boolean polygonClockwise (List<Vector2D> polygon) {
		float area = 0;
		for (int i = 0; i < polygon.size(); i++) {
			Vector2D p1 = polygon.get(i);
			Vector2D p2 = polygon.get(i == polygon.size() - 1 ? 0 : i + 1);
			area += p1.x * p2.y - p2.x * p1.y;
		}

		if (area < 0)
			return true;
		else
			return false;
	}
 
	 int concaveCount = 0;

	 int[] classifyPoints (List<Vector2D> polygon) {
		int[] ptType = new int[polygon.size()];
		concaveCount = 0;

		/*
		 * Before cutting any ears, we must determine if the polygon was inputted in clockwise order or not, since the algorithm for
		 * cutting ears assumes that the polygon's points are in clockwise order. If the points are in counterclockwise order, they
		 * are simply reversed in the array.
		 */
		if (!polygonClockwise(polygon)) {
			Collections.reverse(polygon);
		}

		for (int i = 0; i < polygon.size(); i++) {
			if (i == 0) {
				if (convex(polygon.get(polygon.size() - 1).x, polygon.get(polygon.size() - 1).y, polygon.get(i).x, polygon.get(i).y,
					polygon.get(i + 1).x, polygon.get(i + 1).y)) {
					ptType[i] = 1; /* point is convex */
				} else {
					ptType[i] = -1; /* point is concave */
					concaveCount++;
				}
			} else if (i == polygon.size() - 1) {
				if (convex(polygon.get(i - 1).x, polygon.get(i - 1).y, polygon.get(i).x, polygon.get(i).y, polygon.get(0).x,
					polygon.get(0).y)) {
					ptType[i] = 1; /* point is convex */
				} else {
					ptType[i] = -1; /* point is concave */
					concaveCount++;
				}
			} else { /* i > 0 */
				if (convex(polygon.get(i - 1).x, polygon.get(i - 1).y, polygon.get(i).x, polygon.get(i).y, polygon.get(i + 1).x,
					polygon.get(i + 1).y)) {
					ptType[i] = 1; /* point is convex */
				} else {
					ptType[i] = -1; /* point is concave */
					concaveCount++;
				}
			}
		}

		return ptType;
	}

	/*
	 * convex: returns true if point (x2, y2) is convex
	 */
	 boolean convex (float x1, float y1, float x2, float y2, float x3, float y3) {
		if (area(x1, y1, x2, y2, x3, y3) < 0)
			return false;
		else
			return true;
	}

	/*
	 * area: determines area of triangle formed by three points
	 */
	 float area (float x1, float y1, float x2, float y2, float x3, float y3) {
		float areaSum = 0;

		areaSum += x1 * (y3 - y2);
		areaSum += x2 * (y1 - y3);
		areaSum += x3 * (y2 - y1);

		/*
		 * for actual area, we need to multiple areaSum * 0.5, but we are only interested in the sign of the area (+/-)
		 */

		return areaSum;
	}

	/*
	 * triangleContainsPoints: returns true if the triangle formed by three points contains another point
	 */
	 boolean triangleContainsPoint (List<Vector2D> polygon, int[] ptType, float x1, float y1, float x2, float y2, float x3, float y3) {
		int i = 0;
		float area1, area2, area3;
		boolean noPointInTriangle = true;

		while ((i < polygon.size() - 1) && (noPointInTriangle)) {
			if ((ptType[i] == -1) /* point is concave */
				&& (((polygon.get(i).x != x1) && (polygon.get(i).y != y1)) || ((polygon.get(i).x != x2) && (polygon.get(i).y != y2)) || ((polygon
					.get(i).x != x3) && (polygon.get(i).y != y3)))) {

				area1 = area(x1, y1, x2, y2, polygon.get(i).x, polygon.get(i).y);
				area2 = area(x2, y2, x3, y3, polygon.get(i).x, polygon.get(i).y);
				area3 = area(x3, y3, x1, y1, polygon.get(i).x, polygon.get(i).y);

				if (area1 > 0) if ((area2 > 0) && (area3 > 0)) noPointInTriangle = false;
				if (area1 <= 0) if ((area2 <= 0) && (area3 <= 0)) noPointInTriangle = false;
			}
			i++;
		}
		return !noPointInTriangle;
	}

	/*
	 * ear: returns true if the point (x2, y2) is an ear, false otherwise
	 */
	 boolean ear (List<Vector2D> polygon, int[] ptType, float x1, float y1, float x2, float y2, float x3, float y3) {
		if (concaveCount != 0)
			if (triangleContainsPoint(polygon, ptType, x1, y1, x2, y2, x3, y3))
				return false;
			else
				return true;
		else
			return true;
	}

	/*
	 * cutEar: creates triangle that represents ear for graphics purposes
	 */
	 void cutEar (List<Vector2D> polygon, List<Vector2D> triangles, int index) {
		if (index == 0) {
			triangles.add(new Vector2D(polygon.get(polygon.size() - 1)));
			triangles.add(new Vector2D(polygon.get(index)));
			triangles.add(new Vector2D(polygon.get(index + 1)));
		} else if ((index > 0) && (index < polygon.size() - 1)) {
			triangles.add(new Vector2D(polygon.get(index - 1)));
			triangles.add(new Vector2D(polygon.get(index)));
			triangles.add(new Vector2D(polygon.get(index + 1)));
		} else if (index == polygon.size() - 1) {
			triangles.add(new Vector2D(polygon.get(index - 1)));
			triangles.add(new Vector2D(polygon.get(index)));
			triangles.add(new Vector2D(polygon.get(0)));
		}
	}

	/*
	 * updatePolygon: creates new polygon without the ear that was cut
	 */
	 void updatePolygon (List<Vector2D> polygon, int index) {
		polygon.remove(index);
	}

}
