package org.game.map;

public class TileNode {

    private TileNode mParent; 
    
    private boolean mCanWalk = true; 
    
    private int mX;
    private int mY; 
    
    private int mGoal;
    private int mHeuristic; 
 
    public TileNode(int x, int y) {
        mX = x;
        mY = y;
    }
 
    public void setParent(TileNode n) {
        mParent = n;
    }
 
    public TileNode getParent() {
        return mParent;
    }
 
    public void setH(int h) {
        mHeuristic = h;
    }
 
    public int getH() {
        return mHeuristic;
    }
 
    public void setG(int g) {
        mGoal = g;
    }
 
    public int getG() {
        return mGoal;
    }
 
    public int getX() {
        return mX;
    }
    
    public int getY() {
        return mY;
    } 

    public int getF() {
        return mGoal + mHeuristic;
    } 
    
    public boolean canWalk() {
        return mCanWalk;
    }
    
    public void setWalkable() {
        mCanWalk = true;
    }
    
    public void setNotWalkable() {
        mCanWalk = false;
    }
}
