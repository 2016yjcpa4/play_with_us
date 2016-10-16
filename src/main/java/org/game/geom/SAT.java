/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.geom;

import org.game.math.Point2D;
import org.game.math.Vector2D;

/**
 *
 * @author Administrator
 */
public class SAT {

    public static class CollisionInfo {

        public Shape shapeA;						// the first shape
        public Shape shapeB;						// the second shape
        public float distance;					// how much overlap there is
        public Vector2D vector = new Vector2D();			// the direction you need to move - unit vector
        public boolean shapeAContained = false;		// is object A contained in object B
        public boolean shapeBContained = false;		// is object B contained in object A
        public Vector2D separation;		// a vector that when subtracted to shape A will separate it from shape B

    }

    static private CollisionInfo calculateCollisionInfoSeparation(CollisionInfo obj, CollisionInfo obj2) {
        obj.separation = new Vector2D(obj.vector.x * obj.distance, obj.vector.y * obj.distance);
        if (obj2 != null) {
            obj.shapeAContained = (obj.shapeAContained && obj2.shapeAContained);	// hack to check for containment
        }
        if (obj2 != null) {
            obj.shapeBContained = (obj.shapeBContained && obj2.shapeBContained);
        }
        return obj;
    }
    
    
		/**
		 * Returns the normal of a polygons side.
		 * @param	polygon	Array of points
		 * @param	pointIndex
		 * @return
		 */
		static private function getAxisNormal(vertexArray:Array, pointIndex:uint):Point {
			// grab the points
			var pt1:Point = vertexArray[pointIndex];
			var pt2:Point = (pointIndex >= vertexArray.length - 1) ? vertexArray[0] : vertexArray[pointIndex + 1];
			//
			var p:Point = new Point( -(pt2.y - pt1.y), pt2.x - pt1.x);
			p.normalize(1);
			return p;
			
		}
		
		/**
		 * Returns the dor product of two vectors
		 * @param	pt1
		 * @param	pt2
		 * @return
		 */
		static private double vectorDotProduct(Point2D pt1, Point2D pt2) {
			return (pt1.x * pt2.x + pt1.y * pt2.y);
		}		
		
		
		// EVENT HANDLERS
		// ------------------------------------------------------------------------------------------
		
		
		// GETTERS & SETTERS
		// ------------------------------------------------------------------------------------------
}
