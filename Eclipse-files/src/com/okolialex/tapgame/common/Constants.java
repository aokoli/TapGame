package com.okolialex.tapgame.common;

public class Constants {
	

	/** 8-way direction for monster to travel */
	public static final int NORTH = 1;
	public static final int SOUTH = 2;
	public static final int EAST = 3;
	public static final int WEST = 4;
	public static final int NORTHEAST = 5;
	public static final int NORTHWEST = 6;
	public static final int SOUTHEAST = 7;
	public static final int SOUTHWEST = 8;
	public static final int NO_MOVE = 0;
	
	/** The states the monster can be in at any time: Vulnerable or Safe*/
	public static final int SAFE_STATE = 0;
	public static final int VULNERABLE_STATE = 1;
	
	/** The number of icons used within the game */
    public static final int NUM_OF_ICONS = 3;
	
	/** Labels for the icons that will be loaded into the TileView class */
    public static final int BLANK_CELL = 0;
    public static final int SAFE_MONSTER = 1;
    public static final int VULNERABLE_MONSTER = 2;
    
    /** The game modes (e.g. RUNNING, PAUSED etc.) */
    public static final int STOPPED = 0;
	public static final int PAUSED = 1;
	public static final int RUNNING = 2;
	
	/** The game difficulty levels */
	public static final int NORMAL_DIFFICULTY = 0;
	public static final int HARD_DIFFICULTY = 1;
	
    
    
	
	
	

}
