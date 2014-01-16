package com.okolialex.tapgame.model.actor;
// TODO sychronization to attempt to make monster killings more accurate?
import static com.okolialex.tapgame.common.Constants.*;

import java.util.Random;

import com.okolialex.tapgame.common.AutoRandomEngine;
import com.okolialex.tapgame.common.Coordinate;
import com.okolialex.tapgame.model.TapGameModel;
/**
 * This class represents the monster that travels randomly across
 * the playable game grid area. 
 * 
 * @author Alexander Okoli
 *
 */
public class ConcreteMonster extends Monster {
	
	@Override
	protected Void doInBackground(Void... nothing) {
		
		while (!mWorldModel.isMonsterFreeToMove()){
			// Wait till world tells monster it is okay to move.
		}
		
		// Begin engine that moves monster
		initMonsterMovementManager();
		// Begins the engine that alters monster's state
		initMonsterStateManager();
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		System.out.println("DONE!");
	}

	/** Holds the monster's current state */
	protected int mState; 
	
	/** A random number generator for all our random needs */
	protected static Random rand = new Random();  // TODO ThreadLocalRandom better? - http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ThreadLocalRandom.html 
	
	/** The coordinate that marks the monster's current location */
	protected Coordinate mMonsterCoord = new Coordinate(-1,-1);

	/** The entire game grid area the monster can travel on */
	protected TapGameModel mWorldModel;
	
	/** Injects the game grid which the monster depends on for travel,
	 *  and randomly decides the initial x,y coordinates of the monster.
	 *  
	 *  @param Gamegrid - the game grid the monster can travel on
	 */
	public ConcreteMonster(TapGameModel worldModel){
		this.mWorldModel = worldModel;
		// Set the monster's initial x,y coordinate.
		// setMonsterLocation(-1, -1);
		  setMonsterLocation(rand.nextInt(worldModel.getWidth()),   
						   rand.nextInt(worldModel.getHeight()));
		
		// Set the monster's initial x,y coordinate.
		
		//mMonsterCoord.setX(rand.nextInt(worldModel.getWidth()));  
		//mMonsterCoord.setY(rand.nextInt(worldModel.getHeight()));
		
		// Randomize the initial monster state
		mState = rand.nextInt(1 + VULNERABLE_STATE);
	}
	
	public ConcreteMonster() {
		// Randomize the initial monster state
		mState = rand.nextInt(1 + VULNERABLE_STATE);
	}
	
	@Override
	public void setTapGameModel (TapGameModel worldModel) {
		this.mWorldModel = worldModel;
	}
	
	/** 
	 * Injects the game grid which the monster depends on for travel,
	 * and specifies the initial x,y coordinates of the monster. 
	 *	
	 * @param worldModel - the game grid the monster can travel on
	 * @param initialX - the initial x coordinate for the monster
	 * @param initialY - the initial y coordinate for the monster
	 */
	public ConcreteMonster (TapGameModel worldModel, int initialX, int initialY) {
		this.mWorldModel = worldModel;
		// Set the monster's initial x,y coordinate to a specified location.
		setMonsterLocation(initialX, initialY);
		mState = rand.nextInt(1 + VULNERABLE_STATE);
	}
	
	/** 
	 * The safeStateCount counts how often changeMonsterState() has been called.
	 * This is used for computing how often to change the monster state (safe vs
	 * vulnerable) depending on difficulty level. 
	 * 
	 * (Initializing with 4 helps vary the beginning screen, when monster first launches;
	 * Otherwise, monster will always be in safe state when launched.) */
	private int safeStateCount = 4;
	
	
	@Override
	public void changeMonsterState() { 
		safeStateCount++;
		
		if (mWorldModel.getGameDifficulty() == NORMAL_DIFFICULTY) {
			mState = mState == SAFE_STATE ? VULNERABLE_STATE : SAFE_STATE;
		}
		
		else if (mWorldModel.getGameDifficulty() == HARD_DIFFICULTY) {
			// Makes monster stay in safe state 5 times longer than vulnerable state.
			if (safeStateCount % 5 == 0) {
				mState = mState == SAFE_STATE ? VULNERABLE_STATE : SAFE_STATE;
			} else {
				mState = SAFE_STATE;
			}
		}
		
		mWorldModel.changeMonsterState(this);
		
	}
	
	@Override
	public int getMonsterState() {
		return mState;
	}
	
	@Override
	public void setMonsterLocation(int x, int y) {
		mMonsterCoord.setX(x);
		mMonsterCoord.setY(y);
	}

	@Override
	public void setX(int x){
		mMonsterCoord.setX(x);
	}
	
	@Override
	public void setY(int y){
		mMonsterCoord.setY(y);
	}
	
	@Override
	public int getX(){
		return mMonsterCoord.getX();
	}
	
	@Override
	public int getY() {
		return mMonsterCoord.getY();
	}
	
	@Override
	public Coordinate moveTo(int direction) {
		
		switch (direction) {
	        case NORTH: {
	        	return new Coordinate(mMonsterCoord.getX(), mMonsterCoord.getY() - 1);
	        }
	        case SOUTH: {
	        	return new Coordinate(mMonsterCoord.getX(), mMonsterCoord.getY() + 1);
	        }
	        case EAST: {
	        	return new Coordinate(mMonsterCoord.getX() + 1, mMonsterCoord.getY());
	        }
	        case WEST: {
	        	return new Coordinate(mMonsterCoord.getX() - 1, mMonsterCoord.getY());
	        }
	        case NORTHEAST: {
	        	return new Coordinate(mMonsterCoord.getX() + 1, mMonsterCoord.getY() - 1);
	        }
	        case NORTHWEST: {
	        	return new Coordinate(mMonsterCoord.getX() - 1, mMonsterCoord.getY() - 1);
	        }
	        case SOUTHEAST: {
	        	return new Coordinate(mMonsterCoord.getX() + 1, mMonsterCoord.getY() + 1);
	        }
	        case SOUTHWEST: {
	        	return new Coordinate(mMonsterCoord.getX() - 1, mMonsterCoord.getY() + 1);
	        }
		}
		
		// If there is nowhere to move to, the original location is returned.
		return mMonsterCoord;
	}
		
	protected Coordinate potentialCoord = mMonsterCoord;
	
	@Override
	public int getPotentialX() {
		return potentialCoord.getX();
	}
	
	@Override
	public int getPotentialY() {
		return potentialCoord.getY();
	}
	
	/** 
	 *  Controls the monster's travel: It gets all possible directions monster
	 *  can travel, checking for obstacles (borders and other monsters) before moving
	 *  monster into a new cell. It communicates with the game grid to ensure a possible
	 *  move.
	 */
	protected void moveMonster() {
		
		boolean monsterStationary = true;
		
		while(monsterStationary){
			// Randomly decide which direction monster should travel (North, South, East, No Move etc.)
			potentialCoord = moveTo(rand.nextInt(9)); 
			// Attempt to make monster travel in the randomly selected direction in the game grid.
			if (mWorldModel.enterCell(this)){
				// At this point, monster has entered new cell. Now its time to leave old cell...
				
				// Checks to make sure monster isn't leaving a cell it already is in, as
				// doing so will leave the cell blank on the screen (even though monster is still in cell, user can't see it). 
				// So if monster is already in particular cell, it should stay there.
				if (potentialCoord != mMonsterCoord){
					// Monster 'leaves' (deleted from screen) the cell it currently occupies.
					mWorldModel.exitCell(this);
				}
				monsterStationary = false;
				// The monster is now located in the new location. Coordinate is set accordingly.
				mMonsterCoord = potentialCoord;				
			}
		}
	}
	
	// ExecutorService executorService = Executors.newFixedThreadPool(2);
	
	/** Alters the monster's state (safe vs vulnerable) periodically */ 
	AutoRandomEngine stateChangeEngine;
	protected void initMonsterStateManager() {
		Runnable stateChangeTask = new Runnable() { @Override public void run() {
			changeMonsterState(); 
		}};
		
		/* Monster changes state every 0.5 - 3 seconds */
		stateChangeEngine = new AutoRandomEngine(stateChangeTask, 500, 3000);
		new Thread(stateChangeEngine).start();
		//executorService.execute(stateTask);
	}
	
	
	AutoRandomEngine movementEngine;
	protected void initMonsterMovementManager() {
		Runnable moveTask = new Runnable() { @Override public void run() {
			moveMonster(); 
		}};
		
		/* Monster moves every 0.5 - 2 seconds */
		movementEngine = new AutoRandomEngine(moveTask, 500, 2000);
		new Thread(movementEngine).start();
		//executorService.execute(moveTask);
	}
	
	private boolean dead = false;
	
	@Override
	public void killMonster() {
		// Makes sure the monster is vulnerable before killing
		if (mState == VULNERABLE_STATE) {
			// The 'if' checks are just incase a user tries to kill monster displayed on the screen
			// but hasn't had MonsterModel.execute() called yet - and this might happen during
			// the start screen when monsters have already been instantiated, but execute() hasn't yet 
			// been called. Without execute(), the Engines below will be null.
			if (movementEngine != null) { movementEngine.cancel(); } //Ends movementEngine
			if (stateChangeEngine != null) { stateChangeEngine.cancel(); } // Ends stateChangeEngine
			this.cancel(true);  // Ends AsyncTask
			//executorService.shutdown();
			setDead(true);
		}
	}

	@Override
	public Coordinate getMonsterLocation() {
		return mMonsterCoord;
	}

	@Override
	public void setMonsterState(int monsterState) {
		mState = monsterState;
	}

	@Override
	public boolean isDead() {
		return dead;
	}

	@Override
	public void setDead(boolean dead) {
		this.dead = dead;
	}

	
}

