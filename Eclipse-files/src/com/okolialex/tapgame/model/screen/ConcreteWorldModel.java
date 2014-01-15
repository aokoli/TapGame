package com.okolialex.tapgame.model.screen;
// TODO Draw class dependency
import static com.okolialex.tapgame.common.Constants.BLANK_CELL;
import static com.okolialex.tapgame.common.Constants.PAUSED;
import static com.okolialex.tapgame.common.Constants.RUNNING;
import static com.okolialex.tapgame.common.Constants.SAFE_MONSTER;
import static com.okolialex.tapgame.common.Constants.SAFE_STATE;
import static com.okolialex.tapgame.common.Constants.STOPPED;
import static com.okolialex.tapgame.common.Constants.VULNERABLE_MONSTER;
import static com.okolialex.tapgame.common.Constants.VULNERABLE_STATE;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import org.junit.experimental.theories.internal.AllMembersSupplier;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

import com.okolialex.tapgame.common.WorldModelChangeListener;
import com.okolialex.tapgame.model.actor.ConcreteMonsterModel;
import com.okolialex.tapgame.model.actor.MonsterModel;

/**
 * This implements <code> World </code> and configures all the game action;
 * the monsters moving around within a grid. 
 */
@SuppressLint("NewApi")
public class ConcreteWorldModel implements WorldModel  {
	
	private int gameDifficulty;

	
	
	private int numOfMonsters;
	
	// TODO Make constructor that allMonsters[][] can be added.
	// TODO create World.addMonster(monster);
	// TODO World.startMonsters();  <-- Execute each monster.

	/** Keys to determine if a cell is occupied by a monster or empty */
	private final int EMPTY = 1;
	private final int OCCUPIED = 0;

	
	/** Holds current mode the game is in */
	private int mMode = PAUSED;

	public int getMode() {
		return mMode;
	}
	
	public void setMode(int mode) {
		mMode = mode;
	}
	
	private String mScore;
	/** Computes the score and alerts the UI */
	@Override
	public String getScore() {
		if      (mCurrentTime > 0.79 * maxLevelTime) { return "A"; } 
		else if (mCurrentTime > 0.59 * maxLevelTime) { return "B"; }
		else if (mCurrentTime > 0.39 * maxLevelTime) { return "C"; }
		else if (mCurrentTime > 0.19 * maxLevelTime) { return "D"; }		
													   return "F";
	}
	
	/** Holds the game's current time */
	private int mCurrentTime;
	@Override
	public int getTime() {
		return mCurrentTime;
	}
	
	@Override
	public void decrementTime() {
		mCurrentTime--;
	}
	
	/** The time the countdown begins from */
	private int maxLevelTime;
	@Override
	public void setTime(int maxLevelTime) {
		this.mCurrentTime = maxLevelTime; 
		this.maxLevelTime = maxLevelTime;
	}
	
	
	/** Timer used for manipulating game clock/time, to ultimately update time HUD */
	Timer clock = new Timer();
	
	/**
	 *  Begins countdown of the game time, and alerts view
	 *  (time HUD) on time changes 
	 */
	@Override
	public void startClock() {
		setMode(RUNNING);
		clock.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if (mCurrentTime > 0) { 
					decrementTime();
					notifyListener();
				} // TODO else { stopGame(); }		
			}	
			
		}, 1000 /*Initial 1 sec delay*/, 1000 /*Continuous 1 sec tick*/);
	}
	
	@Override
	public void stopClock() {
		setMode(STOPPED);
		clock.cancel();
	}
	
	   int worldWidth; 
	   int worldHeight;
	   /** Maintains the accessibility of each cell in the game grid. */
	   public Semaphore[][] mGameGridCellAccess; 
	    
	   /** Makes each cell within world grid available for monster entry */
	   public void enableAllCellAccess() {
	    	mGameGridCellAccess = new Semaphore[worldWidth][worldHeight];  
	    	for (int x = 0; x < worldWidth; x += 1) {
	            for (int y = 0; y < worldHeight; y += 1) {
	            	mGameGridCellAccess[x][y] = new Semaphore(EMPTY);
	            }
	        }
	    }
	    
	   // TODO Might remove method.
	   /** Makes each cell within world grid unavailable for monster entry */
	   public void disableAllCellAccess() {  
	    	mGameGridCellAccess = new Semaphore[worldWidth][worldHeight];  
	    	for (int x = 0; x < worldWidth; x += 1) {
	            for (int y = 0; y < worldHeight; y += 1) {
	            	mGameGridCellAccess[x][y] = new Semaphore(OCCUPIED);
	            }
	        }
	    }
	   
	   private final int KILL_TIME_PER_MONSTER = 5;
	   @Override
	   public void startGame(){
		   // 5 seconds to try and kill each monster
		   setTime(numOfMonsters * KILL_TIME_PER_MONSTER);
		   startMonsters();	
		   startClock();
		   notifyListener();
	   }
	   
	   @Override
	   public void stopGame() {
		   stopMonsters();
		   stopClock();
		   notifyListener();
	   }
	   
	    
	    /** Returns the game grid height (# of cells).*/
	    public int height(){
	    	return worldHeight;
	    }
	    
	    /** Returns the game grid width (# of cells).*/
	    public int width(){
	    	return worldWidth;
	    }
	    
	    /** Monitors the location of all monsters within the game grid. */
	    MonsterModel[][] mAllMonsters;
	    
	    /** Initializes the grid containing all monsters. */
	    public void initAllMonstersGrid() {
	    	mAllMonsters = new MonsterModel[worldWidth][worldHeight];
	    }
	    
	    public boolean enterCell(MonsterModel monster){
	    	
	    	int x = monster.getPotentialX();
	    	int y = monster.getPotentialY();
	    	
	    	// Checks if monster is out of game grid bounds 
	    	if (isBeyondBorder(x, y)){
	    		//System.out.println("Beyond borders (ENTER_CELL): " + x + "," + y ); // TODO debugging
	    		return false;
	    	}
	    	
	    	boolean cellIsEmpty = mGameGridCellAccess[x][y].tryAcquire(); 
	    	
	    	if (cellIsEmpty){
	    		mAllMonsters[x][y] = monster;
	    		// TODO Set tile below depending on state!
	    		// Creates a better visual transition to a different cell, as it remains in its most recent state (rather than
	    		// starting in a new cell with a constant state.
	    		
	    		// Obtains monster's current state
	        	int mMonsterState = monster.getMonsterState();
	        	// Depending on state, the appropriate bitmap to be displayed is selected.
	        	mMonsterState = mMonsterState == SAFE_STATE ? SAFE_MONSTER : VULNERABLE_MONSTER; 
	        	// Bitmap displaying monster in current state, is set (Monster object will eventually invalidate UI canvas)
	        	setIcon(mMonsterState, x, y);
	        	// Alert the view on the world grid changes
	        	notifyListener(); // TODO Check
	    		// System.out.println("Successful ENTER_CELL: " + x + "," + y); // TODO debugging.
	    		return true;
	    	}
	    	
	    	return false;
	    	
	    }
	    
	    public boolean exitCell(MonsterModel monster) {
	    	
	    	int x = monster.getX();
	    	int y = monster.getY();
	    	
	    	// This border check ensures monster isn't exiting a non-existing cell!
	    	// E.g. if game starts, monster can be assigned to a random cell OUTSIDE of borders.
	    	// Exiting that cell will throw an out of bounds exception, so code below catches
	    	// that.
	    	if (isBeyondBorder(x, y)){
	    	//	System.out.println("Beyond borders (EXIT_CELL): " + x + "," + y );  // TODO debugging
	    		return false;
	    	}
	 
	    	mAllMonsters[x][y] = null;
	    	mGameGridCellAccess[x][y].release();
	    	setIcon(BLANK_CELL, x, y); // TODO place background in 0.
	    	notifyListener(); // TODO Check // invalidate(); TODO Let monster control this
		//	 System.out.println("Successful EXIT_CELL: " + x + "," + y);  // TODO debugging.
	    	return true;
		}
	    
	    /** Alternates the monster's state */
	    public void changeMonsterState(MonsterModel monster){
	    	
	    	int mMonsterState = monster.getMonsterState();
	    	int x = monster.getX();
	    	int y = monster.getY();
	    	
	    	mMonsterState = mMonsterState == SAFE_STATE ? SAFE_MONSTER : VULNERABLE_MONSTER;  // TODO Make '0' vulnerable field. 
	    	
	    	// Border check to make sure the x,y coords aren't out of bounds. 
	    	if (isBeyondBorder(x, y)){ // TODO Change back!
	    		// System.out.println("Beyond borders (SET_STATE): " + x + "," + y ); // TODO Debugging
	    		return;
	    	}    
	    	
	    	// Extra care to make sure right monster/cell is changed. If check isn't in place, there
	    	// are certain instances where a monster might be leaving a cell, just as state is changing,
	    	// leaving a printed bitmap on the exited cell, while the monster is actively in a different cell.
	    	if (monster == mAllMonsters[x][y]) {
	    		setIcon(mMonsterState, x, y);
	    		notifyListener(); // TODO Check
	    	}
	    }
	    
	    
	    @Override
	    public void touchCell(int touchedCellX, int touchedCellY){
	    	tryKillMonster(touchedCellX, touchedCellY);
	    }
	    
	    public boolean tryKillMonster(int touchedCellX, int touchedCellY) {
	    	// Retrieve the current monster in the touched cell location (if any)
	    	MonsterModel monster = mAllMonsters[touchedCellX][touchedCellY];  // TODO <-- Controller to get from view. Controller to update model.
	    	// Checks to see if monster exists in touched cell
	    	if (monster != null){
	    		// Check the state of monster
	    		if (monster.getMonsterState() == VULNERABLE_STATE) {
	        		// Kills monster currently in touched cell
	        		monster.killMonster(); 
	        		// Removes the dead monster from cell
	        		exitCell(monster);
	        		
	        		// Checks if game is over (i.e. no monsters left within world). 
	        		if (isGameOver()) { 
	        			// Stop time from ticking (and updating time HUD)
	        			stopClock(); 
	        		}
	        		notifyListener();
	        		return true;
	    		}

	    	}
	    	notifyListener();
	    	return false;
		}
	   
	    /** 
	     * Determines if game is over. Does a count of all monster's within world
	     * grid, and if monster # < 0, then the game is over. 
	     */
	    private boolean isGameOver() {
	    	for (int x = 0; x < worldWidth; x += 1) {
	            for (int y = 0; y < worldHeight; y += 1) {
	            	if (mAllMonsters[x][y] != null) {
	            		return false;
	            	}
	            }
	        }
	    	
	    	return true;
		}
	    
	    public boolean isBeyondBorder(int x, int y) {
	    	if (x > (worldWidth - 1) || y > (worldHeight - 1) || x < 0 || y < 0){ // TODO Change back!
	    		return true;
	    	}    
	    	
	    	try {
				mGameGridCellAccess[x][y].availablePermits();  // TODO Better access
			} catch (Exception e) {
			//	System.out.println("Caught in BORDER_EXCEEDED: " + e + " Coord: " + x + "," + y ); // TODO Debugging.
				return true;
			}
	    	
	    	return false;    	
	    }

	@Override
	public int getHeight() {
		return worldHeight;
	}

	@Override
	public int getWidth() {
		return worldWidth;
	}
 

	@Override
	public int[][] getWorldScreen() {
		return mWorldScreen;
	}

	int[][] mWorldScreen;
	@Override
	public void setDimensions(int width, int height) {
		this.worldWidth = width;
		this.worldHeight = height;
		
		mWorldScreen = new int[width][height];
	}

	@Override
	public void notifyListener() {
		worldChangeListener.onWorldChange(this);
	}

	private WorldModelChangeListener worldChangeListener;
	@Override
	public void setWorldModelChangeListener(WorldModelChangeListener worldChangeListener) {
		this.worldChangeListener = worldChangeListener;
		
	}

	@Override
	public void setIcon(int icon, int x, int y) {
		mWorldScreen[x][y] = icon;
	}
	
	// Synchronized, to prevent a call of monster.execute before this finishes
	public /*synchronized*/ void addMonster(MonsterModel monster){
		numOfMonsters++;
		// Exposes monster to world
		monster.setWorld(this);
		Random rand = new Random();
		boolean entered = false;
		
		/*
		 * There are 2 ways a monster can enter world. 
		 * 1. Monster can enter a location it was pre-assigned. 
		 * 2. Monster can enter a location randomly selected by world.
		 * Below we check for a pre-assignment. If there isn't, the 
		 * world will randomly assign a cell for monster to enter. 
		 * 
		 * Pre-assigning cells are mainly useful for debugging - 
		 * controlling where monster will be when game starts. 
		 */ 
		
		// If monster has a pre-assigned location, 
		if (monster.getX() != -1 && monster.getY() != -1){
			// Attempt to add monster to pre-assigned location within world
			entered = enterCell(monster) ? true : false;
		} 
		
		// If monster has no pre-assigned location,
		while (!entered) {
			// Randomly assign x,y coordinates based on screen size
			int x = rand.nextInt(worldWidth);
			int y = rand.nextInt(worldHeight);
			// Monster attempts to enter random cell
			monster.setMonsterLocation(x, y);	// TODO 1 + ...?
			
			// Check to see if lock can be acquired for cell
			//entered = mGameGridCellAccess[x][y].tryAcquire() ? true : false;
			// Check if monster has entered empty cell
			entered = enterCell(monster) ? true : false;	
		}
		
	}

	@Override
	public void stopMonsters() {
		
		for (int x = 0; x < worldWidth; x += 1) {
            for (int y = 0; y < worldHeight; y += 1) {
            	if (mAllMonsters[x][y] != null) {
            		mAllMonsters[x][y].killMonster();  
            	}
            }
        }
		
	}
	
	boolean monstersFreeToMove = false;
	@Override
	public boolean isMonsterFreeToMove() {
		return monstersFreeToMove;
	}
	
	@Override
	public void startMonsters() {
		
		for (int x = 0; x < worldWidth; x += 1) {
            for (int y = 0; y < worldHeight; y += 1) {
            	if (mAllMonsters[x][y] != null) {
            		MonsterModel monster = mAllMonsters[x][y];
            		
            		// Since executeOnExecutor(...) is API 11, check to make
        			// sure devices with APIs below 11 aren't trying to use this. 
        			// Otherwise, we get an error "java.lang.NoSuchFieldError..."
            		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        				monster.executeOnExecutor(
        						AsyncTask.THREAD_POOL_EXECUTOR,
        						(Void[]) null);
        			} else {
        				monster.execute();
        			}
            	}
            }
        }
        
		monstersFreeToMove = true;
	}

	@Override
	public void setNumOfMonsters(int numOfMonsters) {
		this.numOfMonsters = numOfMonsters;
		// 5 seconds to try and kill each monster
		setTime(numOfMonsters * 5);
	}

	@Override
	public MonsterModel[][] getAllMonstersGrid() {
		return mAllMonsters;
	}

	@Override
	public Semaphore[][] getGameGridCellAccess() {
		return mGameGridCellAccess;
	}

	@Override
	public int getGameDifficulty() {
		return gameDifficulty;
	}

	@Override
	public void setGameDifficulty(int gameDifficulty) {
		this.gameDifficulty = gameDifficulty;
	}

}
