/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.geom;

import org.game.math.Vector2D;

/**
 *
 * @author Administrator
 */
public class SAT {
    
    private static final int LEFT_VORNOI_REGION = -1;
    private static final int MIDDLE_VORNOI_REGION = 0;
    private static final int RIGHT_VORNOI_REGION = 1;

    public static class CollisionResponse {
    }
    
    private static int getVornoiRegion(Vector2D v1, Vector2D v2) { 
        double d = v2.scalar(v1);

        if (d < 0) {
            return LEFT_VORNOI_REGION;
        }
        
        if (d > v1.getLengthSquared()) {
            return RIGHT_VORNOI_REGION;
        }
        
        return MIDDLE_VORNOI_REGION;
    }
}
