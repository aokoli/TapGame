package com.okolialex.tapgame.common;

/**
 * A class containing x,y coordinate values and a comparison function.  
 */
public class Coordinate {
    private int x;
    private int y;

    public Coordinate() {
    	this(0,0);
    }
    
    public Coordinate(int newX, int newY) {
        x = newX;
        y = newY;
    }
    
    public int getX() {
    	return x;
    }
    
    public int getY() {
    	return y;
    }
    
    public void setX(int x) {
    	this.x = x;
    }
    
    public void setY(int y) {
    	this.y = y;
    }

    public boolean equals(Coordinate other) {
        if (x == other.x && y == other.y) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Coordinate: [" + x + "," + y + "]";
    }
}