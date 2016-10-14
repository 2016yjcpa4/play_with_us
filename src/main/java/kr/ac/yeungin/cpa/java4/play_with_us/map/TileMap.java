package kr.ac.yeungin.cpa.java4.play_with_us.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kr.ac.yeungin.cpa.java4.play_with_us.math.Point2D;
 
public class TileMap { 
    
    private final TileNode[][] nodes;
 
    public TileMap(int w, int h) {
        this.nodes = new TileNode[w][h];
        
        for (int y = 0; y < nodes[0].length; y++) {
            for (int x = 0; x < nodes.length; x++) {
                this.nodes[x][y] = new TileNode(x, y);
            }
        }
    }

    public int getColumns() {
        return nodes.length;
    }
 
    public int getRows() {
        return nodes[0].length;
    }

    public boolean isWithin(int x, int y) {
        return 0 <= x && x <= (getColumns() - 1)
            && 0 <= y && y <= (getRows() - 1);
    }  

    public List<TileNode> getPath(int srcX, int srcY, int dstX, int dstY) {
        TileNode src = getNode(srcX, srcY);
        TileNode dst = getNode(dstX, dstY);
        
        return getPath(src, dst);
    }

    public TileNode getNode(int x, int y) {
        return nodes[x][y];
    }
 
    public TileNode[][] getNodes() {
        return nodes;
    }
    
    public List<TileNode> getPath(TileNode src, TileNode dst) {
        
        if ( ! dst.canWalk()) {
            return Collections.emptyList();
        }

        // 모든 노드에 H 값을 도착점과의 거리로 H 값을 기록합니다.
        for (int y = 0; y < getRows(); y++) {
            for (int x = 0; x < getColumns(); x++) {
                int dx = Math.abs(dst.getX() - x);
                int dy = Math.abs(dst.getY() - y);
                
                getNode(x, y).setH(dx + dy);
            }
        }

        TileNode n = src;
        List<TileNode> o = new ArrayList<>(); // 열린 노드
        List<TileNode> c = new ArrayList<>(); // 닫힌 노드

        boolean isPathFound = false;

        while ( ! isPathFound && ! c.contains(dst)) {
            for (TileNode e : getNeighbors(n)) {
                if (e == dst) {
                    dst.setParent(n);
                    isPathFound = true;
                    c.add(dst);
                    break;
                }

                if ( ! c.contains(e)) {
                    if (o.contains(e)) {
                        int g = n.getG() + 10;

                        if (g < e.getG()) {
                            e.setParent(n);
                            e.setG(g);
                        }
                    } 
                    else {
                        e.setParent(n);
                        e.setG(n.getG() + 10);
                        o.add(e);
                    }
                }
            }

            if ( ! isPathFound) {
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
            l.add(dst);
            dst = dst.getParent();
        } 
        while (dst != src);

        Collections.reverse(l);
        return l;
    }

    /**
     * 플레이어 주변에 걸어갈 수 있는 노드를 가져오는 함수.
     * 
     * 플레이어를 기준으로 동,서,남,북 노드를 체크하고 걸어갈 수 있는 노드들만 리스트형태로 가져옵니다.
     * 
     * @param n 기준점이 되는 노드 입니다.
     * @return 
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
