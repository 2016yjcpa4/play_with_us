/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kr.ac.yeungin.cpa.java4.play_with_us.util;

public class GameUtil {
    
    private GameUtil() {
    }
    
    public static char getDirectionByDegree(double d) {
        d = MathUtil.getNormalDegrees(d);
        
        if (45 <= d && d < 135) {
            return 's'; // south
        }
        
        if (135 <= d && d < 225) {
            return 'w'; // west
        }
        
        if (225 <= d && d < 315) {
            return 'n'; // north
        }
        
        return 'e'; // east
    }
}
