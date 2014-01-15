package com.okolialex.tapgame.model.screen;

import java.util.concurrent.Semaphore;

import com.okolialex.tapgame.common.WorldChangeListenerSetter;
import com.okolialex.tapgame.model.actor.MonsterModel;

/**
 * This interface represents a model for a grid of cells the monster can travel in,
 * as well as any visual component a user will ultimately see, such as the score or 
 * time remaining. 
 * Each monster "sees" this cell world, and moves within it. The view will 
 * be a listener of this cell world, rendering changes (e.g. monster state and 
 * movement changes) in the world in the UI.
 * 
 * @author Alexander Okoli
 */
		
public interface WorldModel extends HUD, WorldChangeListenerSetter{
	  
	   /** Plant semaphore grid*/
	    public void enableAllCellAccess();
	    
	    /** Returns the game grid height (# of cells).*/
	    public int getHeight();
	    
	    /** Returns the game grid width (# of cells).*/
	    public int getWidth();
	    
	    public void setDimensions(int width, int height);
	    
	    /** 
	     * Initializes array grid that monitors the location of all monsters 
	     * within the game world grid. 
	     */
	    public void initAllMonstersGrid();
	    
	    /** Returns grid containing all monsters (mainly for debugging purposes.) */
	    public MonsterModel[][] getAllMonstersGrid();
	    
	    /** 
	     * Returns the monster world grid with all the drawable icons mapped
	     * using ints. The view uses the returned grid to render the UI. 
	     */
	    public int[][] getWorldScreen();
	    
	    public void notifyListener();
	    
	    public boolean enterCell(MonsterModel monster);
	    
	    /** Sets the desired image on the specified coordinate */
	    public void setIcon(int icon, int x, int y);
	   // public void setTile(int cellImage, int x, int y);
	    
	    public boolean exitCell(MonsterModel monster);
	    
	    /** Alternates the monster's state */
	    public void changeMonsterState(MonsterModel monster);
	    
	    /** When user touches a cell, this method is alerted with the coordinates of the touched cell */
	    public void touchCell(int touchedCellX, int touchedCellY);
	    
	    public boolean tryKillMonster(int touchedCellX, int touchedCellY);
	    
	    public boolean isBeyondBorder(int x, int y);
	    	        
	    public void startMonsters();

		public void setNumOfMonsters(int numOfMonsters);
		
		/** Begins the movement of the monsters within world */
		public void startGame();
		
		/** Adds a monster to the monster world*/
		public /*synchronized*/ void addMonster(MonsterModel monster);

		public void stopGame();

		public void stopMonsters();

		boolean isMonsterFreeToMove();

		/** 
		 * Provides the semaphore grid containing the accessibility of each cell in world
		 * (mainly used for debugging).
		 */
		public Semaphore[][] getGameGridCellAccess();

		int getGameDifficulty();

		void setGameDifficulty(int gameDifficulty);

	    
	    
	    
	    
}
