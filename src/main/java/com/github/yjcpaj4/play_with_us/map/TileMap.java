package com.github.yjcpaj4.play_with_us.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;  
import com.github.yjcpaj4.play_with_us.math.Point2D;
 
public class TileMap { 
    
    private final TileNode[][] mNodes;
 
    public TileMap(int w, int h) {
        mNodes = new TileNode[w][h];
        
        for (int y = 0; y < mNodes[0].length; y++) {
            for (int x = 0; x < mNodes.length; x++) {
                mNodes[x][y] = new TileNode(x, y);
            }
        }
    }

    public int getColumns() {
        return mNodes.length;
    }
 
    public int getRows() {
        return mNodes[0].length;
    }

    public boolean isWithin(int x, int y) {
        return 0 <= x && x <= (getColumns() - 1)
            && 0 <= y && y <= (getRows() - 1);
    }  

    public List<TileNode> getPath(int sx, int sy, int dx, int dy) {
        TileNode s = getNode(sx, sy);
        TileNode d = getNode(dx, dy);
        
        return getPath(s, d);
    }

    public TileNode getNode(int x, int y) {
        return mNodes[x][y];
    }
 
    public TileNode[][] getNodes() {
        return mNodes;
    }
    
    /**
     * 
     * @param s 출발지 노드
     * @param d 목적지 노드
     * @return 출발지에서 목적지까지의 최단거리 노드를 List 형태로 반환
     */
    public List<TileNode> getPath(TileNode s, TileNode d) {
        
        if ( ! d.canWalk()) {
            return Collections.emptyList();
        }

        // 모든 노드에 H 값을 도착점과의 거리로 H 값을 기록합니다.
        for (int y = 0; y < getRows(); y++) {
            for (int x = 0; x < getColumns(); x++) {
                int dx = Math.abs(d.getX() - x);
                int dy = Math.abs(d.getY() - y);
                
                getNode(x, y).setHeuristic(dx + dy);
            }
        }

        TileNode n = s;
        List<TileNode> o = new ArrayList<>(); // 열린 노드
        List<TileNode> c = new ArrayList<>(); // 닫힌 노드

        boolean b = false;

        while ( ! b && ! c.contains(d)) {
            for (TileNode e : getNeighbors(n)) {
                if (e == d) {
                    d.setParent(n);
                    b = true;
                    c.add(d);
                    break;
                }

                if ( ! c.contains(e)) {
                    if (o.contains(e)) {
                        int g = n.getGoal() + 10;

                        if (g < e.getGoal()) {
                            e.setParent(n);
                            e.setGoal(g);
                        }
                    } 
                    else {
                        e.setParent(n);
                        e.setGoal(n.getGoal() + 10);
                        o.add(e);
                    }
                }
            }

            if ( ! b) {
                c.add(n);
                o.remove(n);

                if (o.isEmpty()) {
                    return Collections.emptyList();
                }

                n = o.stream().reduce((n1, n2) -> n2.getF() < n1.getF() ? n2 : n1).get(); // reduce ??
            }
        }
        
        List<TileNode> l = new ArrayList<>();
        
        do {
            l.add(d);
            d = d.getParent();
        } 
        while (d != s);

        Collections.reverse(l);
        return l;
    }

    /**
     * 플레이어 주변에 걸어갈 수 있는 노드를 가져오는 함수.
     * 
     * 플레이어를 기준으로 동,서,남,북 노드를 체크하고 걸어갈 수 있는 노드들만 리스트형태로 가져옵니다.
     * 
     * @param n 기준점이 되는 노드 입니다.
     * @return 이웃한 노드들을 List 형태로 반환
     */
    private List<TileNode> getNeighbors(TileNode n) {
        List<TileNode> l = new ArrayList<>();

        for (Point2D p : Arrays.asList(new Point2D(n.getX() - 1, n.getY() - 1)       // ↖
                                     , new Point2D(n.getX(),     n.getY() - 1)       // ↑
                                     , new Point2D(n.getX() + 1, n.getY() - 1)       // ↗
                
                                     , new Point2D(n.getX() - 1, n.getY())           // ←
                                     //, new Point2D(n.getX(),     n.getY())         // 기준점
                                     , new Point2D(n.getX() + 1, n.getY())           // →
                    
                                     , new Point2D(n.getX() - 1, n.getY() + 1)       // ↙
                                     , new Point2D(n.getX(),     n.getY() + 1)      // ↓    
                                     , new Point2D(n.getX() + 1, n.getY() + 1)       // ↘
        )) {
            int x = (int) p.getX();
            int y = (int) p.getY();
            
            if ( ! isWithin(x, y)) {
                continue;
            }
            
            if ( ! getNode(x, y).canWalk()) { // 해당 노드가 걸어갈수있는 노드 인지 체크
                continue;
            }
                
            l.add(getNode(x, y));
        }

        return l;
    }
}
