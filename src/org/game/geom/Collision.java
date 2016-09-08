package org.game.geom;

public class Collision {
    
    // Find the minimum separating axis for the given poly and plane list.
//    function _getMSA(polygon, planes, count) {
//        var result = {
//            distance: -999999,
//            offset: -1
//        }
//        for (var next = 0; next < count; next++) {
//            var distance = polygon.getDistanceOnPlane(planes[next].normal, planes[next].distance);
//            if (distance > 0) { // no collision
//                return {distance: 0, offset: -1};
//            } else if (distance > result.distance) {
//                result.distance = distance;
//                result.offset = next;
//            }
//        }
//        return result;
//    }

//    function _getVerticesFallback(polygon1, polygon2, normal) {
//        var count = 0;
//        for (var next = 0; next < polygon1.vertices.length; next++) {
//            var vertex = polygon1.vertices[next];
//            if (polygon2.contains(vertex, normal)) {
//                count++;
//            }
//        }
//        for (var next = 0; next < polygon2.vertices.length; next++) {
//            var vertex = polygon2.vertices[next];
//            if (polygon1.contains(vertex, normal)) {
//                count++;
//            }
//        }
//        return count;
//    }
//
//    // Find the overlapped vertices.
//    function _getVertices(polygon1, polygon2, normal) {
//        var count = 0;
//        for (var next = 0; next < polygon1.vertices.length; next++) {
//            var vertex = polygon1.vertices[next];
//            if (polygon2.contains(vertex)) {
//                count++;
//            }
//        }
//        for (var next = 0; next < polygon2.vertices.length; next++) {
//            var vertex = polygon2.vertices[next];
//            if (polygon1.contains(vertex)) {
//                count++;
//            }
//        }
//        return count > 0 ? count : _getVerticesFallback(polygon1, polygon2, normal);
//    }

}
