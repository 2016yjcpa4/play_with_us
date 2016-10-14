package kr.ac.yeungin.cpa.java4.play_with_us.map;

public class TileNode {

    private TileNode parent; 
    
    private boolean canWalk = true; 
    
    private int x;
    private int y; 
    
    private int g;
    private int h; 
 
    public TileNode(int x, int y) {
        this.x = x;
        this.y = y;
    }
 
    public void setParent(TileNode n) {
        this.parent = n;
    }
 
    public TileNode getParent() {
        return parent;
    }
 
    public void setH(int h) {
        this.h = h;
    }
 
    public int getH() {
        return h;
    }
 
    public void setG(int g) {
        this.g = g;
    }
 
    public int getG() {
        return g;
    }
 
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    } 

    public int getF() {
        return g + h;
    } 
    
    public boolean canWalk() {
        return canWalk;
    }
    
    public void setWalkable() {
        canWalk = true;
    }
    
    public void setNotWalkable() {
        canWalk = false;
    }
}
