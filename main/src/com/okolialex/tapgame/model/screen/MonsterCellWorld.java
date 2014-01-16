package com.okolialex.tapgame.model.screen;

import java.util.concurrent.Semaphore;

import com.okolialex.tapgame.model.actor.Monster;

/**
 * This interface represents a model for a grid of cells the monster can travel in.
 * It is a rectangular grid of cells (or a single cell), that serve as the game board 
 * and the monster's movements are confined to the dimensions of this cell grid. 
 *  
 * Each monster "sees" this cell world, and moves within it. Though the monster changes
 * are independent (i.e. the monster acts on its own free will), this interface is 
 * responsible for ensuring the monster doesn't go out of the defined cell boundaries
 * nor collide with other monsters. 
 * 
 * The View will ultimately be a listener of this interface, rendering its changes 
 * (e.g. monster state and movement changes) in the UI.
 * 
 * @author Alexander Okoli
 */


public interface MonsterCellWorld {
	
	// CELL-RELATED METHODS
	//_____________________________________
	
    /** Returns the game grid height (# of cells).*/
    public int getHeight();
    
    /** Returns the game grid width (# of cells).*/
    public int getWidth();
    
    /** Attempts to make monster enter a cell */
    public boolean enterCell(Monster monster);
    
    
    /** Attempts to make monster exit a cell */
    public boolean exitCell(Monster monster);
    
    /** When user touches a cell, this method is alerted with the coordinates of the touched cell */
    public void touchCell(int touchedCellX, int touchedCellY);

    /** 
	 * Provides the semaphore grid containing the accessibility of each cell in world
	 * (mainly used for debugging).
	 */
	public Semaphore[][] getGameGridCellAccess();
	
	/** 
	 * Verifies whether the coordinate parameters are beyond the 
	 * cell grid's borders 
	 */
    public boolean isBeyondBorder(int x, int y);
    
    /** Sets the height and width (measured in # cells) of cell grid */
    public void setDimensions(int width, int height);
    
    /** 
     *  Makes each cell within world grid available for monster entry
     *  (by using Semaphores).
     */
    public void enableAllCellAccess();
    
	/** Adds a monster to the monster world*/
	public void addMonster(Monster monster);	
	
	
	// OTHER METHODS THAT ENABLE MONSTERWORLD INTERACT WITH MONSTER
	//_____________________________________________________________
	
    /** 
     * Initializes array grid that monitors the location of all monsters 
     * within the game world grid. 
     */
    public void initAllMonstersGrid();
    
    /** Returns grid containing all monsters within world (mainly for debugging purposes.) */
    public Monster[][] getAllMonstersGrid();
    
    
    /** 
     * Alternates the monster's state. The call to this is triggered
     * by the monster itself. 
     */
    public void changeMonsterState(Monster monster);
    
    /** Attempts to kill a monster in a touched cell. */   
    public boolean tryKillMonster(int touchedCellX, int touchedCellY);
    
    	        
    /** Begins the movement of monsters within world */
    public void startMonsters();


    /** Stops the movement of monsters within world */
	public void stopMonsters();

	/** Verifies whether monsters can start moving within world. This verification is crucial because:
	 * 
	During startGame(), a loop, startMonsters(), triggers the Monster.execute() for 
	each AsyncTask monster. Once execute has been called on a monster, it immediately begins to move around the screen.
	In a split second, a monster that has already been executed might jump into the line of fire 
	of the Monster.execute() of the startMonsters() loop, and this will try to trigger ANOTHER execute() of the
	monster. An error occurs ("it has already been executed..."). 
	
	As a solution, the monster has to wait for the monster world's authorization before it can begin moving.
	The monster world will give an "ok" to move when all monsters in the world have had their execute() 
	methods called. This function lets monsters know if they have the permission to move or not. 
	*/
	boolean isMonsterFreeToMove();

}
