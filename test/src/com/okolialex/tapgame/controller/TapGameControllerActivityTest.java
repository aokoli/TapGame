package com.okolialex.tapgame.controller;


/**
 * A GUI-level test for the core TapGame functionalities. 
 * 
 * @author Alexander Okoli
 *
 */
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.view.MotionEvent;


import static com.okolialex.tapgame.common.Constants.*;

import com.okolialex.tapgame.controller.ModifiedTapGameController;
import com.okolialex.tapgame.controller.TapGameController;
import com.okolialex.tapgame.model.TapGameModelFacade;
import com.okolialex.tapgame.model.actor.ConcreteMonster;
import com.okolialex.tapgame.model.actor.Monster;
import com.okolialex.tapgame.view.TapGameView;

public class TapGameControllerActivityTest extends ActivityInstrumentationTestCase2<ModifiedTapGameController> {
	
	ModifiedTapGameController mActivity;
	TapGameModelFacade mModel;
	TapGameView mView;
	Monster[][] worldMonsters;
	public TapGameControllerActivityTest() {
	    super(ModifiedTapGameController.class); 
	}
	
	MockMonster monster1;
	/**
	 * Creates an instance of the {@link TapGameController} Activity prior to each unit test.
	 */
	@Override
	protected void setUp() throws Exception {
	    super.setUp();
	    mActivity = getActivity();
	    mModel = mActivity.getModel();
	    mView = mActivity.getView();
		// Obtains the grid containing all monsters within world
		worldMonsters = mModel.getAllMonstersGrid();
	}
	
	/** Verifies that the activity under test can be launched. */
	public void testActivityTestCaseSetUpProperly() {
		assertNotNull("activity should be launched successfully", mActivity);
	}
	
	/**  Verifies monster can:
	 * - Enter model (world model)
	 * - Travel in all 8 neighboring directions within world (effectively
	 *   testing monster's ability to enter and exit cells within world.)
	 */
	public void testWorldEntryAndMovement() throws InterruptedException {
		
	    while (!mActivity.isViewBuilt()) {
	    	// Wait till View is built!
	    }
	    
		// MONSTER ENTRY TO WORLD
	    // Create new monster
	    MockMonster monster = new MockMonster(1,1);
	    // Add monster to cell 1,1 within model (world model)
		mModel.addMonster(monster);
		// Verify that monster entered cell 1,1 in world
		assertSame(monster, mModel.getAllMonstersGrid()[1][1]);
		
		// Create screen tap MotionEvent
		MotionEvent screenTap = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 
								MotionEvent.ACTION_DOWN, 0, 0, 0);  
		// Touch screen to start game
		mView.dispatchTouchEvent(screenTap);  
		//Recycle the MotionEvent, to be re-used by a later caller. Don't use after this!
		screenTap.recycle();
		// Game engine has been started at this point. 
		// Screen pause here just for effect (to display "Tap Screen" prompt briefly.)
		Thread.sleep(500);
		
		
		// MONSTER 8-DIRECTION TRAVEL; AND CELL ENTRY AND EXIT WITHIN WORLD
		// Verifies monster can travel North:
		// Verify that monster is currently in cell 1,1 in world
		assertSame(monster, mModel.getAllMonstersGrid()[1][1]);
		// Make monster travel North
		monster.setDirection(NORTH);
		monster.moveMonster();
		// Verify that monster exited cell 1,1 in world
		assertNotSame(monster, mModel.getAllMonstersGrid()[1][1]);
		// Verify that monster entered cell 1,0 in world
		assertSame(monster, mModel.getAllMonstersGrid()[1][0]);
		Thread.sleep(500);
		
		// Verifies monster can travel South:
		// Make monster travel South
		monster.setDirection(SOUTH);
		monster.moveMonster();
		// Verify that monster exited cell 1,0 in world
		assertNotSame(monster, mModel.getAllMonstersGrid()[1][0]);  
		// Verify that monster entered cell 1,1 in world
		assertSame(monster, mModel.getAllMonstersGrid()[1][1]);
		Thread.sleep(500);
		
		// Verifies monster can travel East:
		// Make monster travel East
		monster.setDirection(EAST);
		monster.moveMonster();  
		// Verify that monster exited cell 1,1 in world
		assertNotSame(monster, mModel.getAllMonstersGrid()[1][1]);
		// Verify that monster entered cell 2,1 in world
		assertSame(monster, mModel.getAllMonstersGrid()[2][1]);
		Thread.sleep(500);
		
		// Verifies monster can travel West:
		// Make monster travel West
		monster.setDirection(WEST);
		monster.moveMonster(); 
		// Verify that monster exited cell 2,1 in world
		assertNotSame(monster, mModel.getAllMonstersGrid()[2][1]);
		// Verify that monster entered cell 1,1 in world
		assertSame(monster, mModel.getAllMonstersGrid()[1][1]); 
		Thread.sleep(500);
		
		// Verifies monster can travel NorthEast:
		// Make monster travel NorthEast
		monster.setDirection(NORTHEAST);
		monster.moveMonster();  
		// Verify that monster exited cell 1,1 in world
		assertNotSame(monster, mModel.getAllMonstersGrid()[1][1]);
		// Verify that monster entered cell 2,0 in world
		assertSame(monster, mModel.getAllMonstersGrid()[2][0]); 
		Thread.sleep(500);
		
		// Verifies monster can travel SouthWest:
		// Make monster travel SouthWest
		monster.setDirection(SOUTHWEST);
		monster.moveMonster();  
		// Verify that monster exited cell 2,0 in world
		assertNotSame(monster, mModel.getAllMonstersGrid()[2][0]);
		// Verify that monster entered cell 1,1 in world
		assertSame(monster, mModel.getAllMonstersGrid()[1][1]); 
		Thread.sleep(500);
		
		// Verifies monster can travel SouthEast:
		// Make monster travel SouthEast
		monster.setDirection(SOUTHEAST);
		monster.moveMonster();  
		// Verify that monster exited cell 1,1 in world
		assertNotSame(monster, mModel.getAllMonstersGrid()[1][1]);
		// Verify that monster entered cell 2,2 in world
		assertSame(monster, mModel.getAllMonstersGrid()[2][2]); 
		Thread.sleep(500);
		
		// Verifies monster can travel NorthWest:
		// Make monster travel NorthWest
		monster.setDirection(NORTHWEST);
		monster.moveMonster();  
		// Verify that monster exited cell 2,2 in world
		assertNotSame(monster, mModel.getAllMonstersGrid()[2][2]);
		// Verify that monster entered cell 1,1 in world	
		assertSame(monster, mModel.getAllMonstersGrid()[1][1]); 
		Thread.sleep(500);
			
	}
	
	
	public void testCollision() {
	
		while (!mActivity.isViewBuilt()) {
	    	// Wait till view is built!
	    }
	    
		// MONSTER ENTRY TO WORLD
	    // Create new monsters
	    MockMonster monster1 = new MockMonster(0,0);
	    MockMonster monster2 = new MockMonster(1,0);
	    // Add monsters to world 
		mModel.addMonster(monster1);
		mModel.addMonster(monster2);
		// Verify that monster1 entered cell 0,0 in world
		assertSame(monster1, mModel.getAllMonstersGrid()[0][0]);
		// Verify that monster2 entered cell 1,0 in world
		assertSame(monster2, mModel.getAllMonstersGrid()[1][0]);
		
		// Create screen tap MotionEvent
		MotionEvent screenTap = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 
								MotionEvent.ACTION_DOWN, 0, 0, 0);  
		// Touch screen to start game
		mView.dispatchTouchEvent(screenTap);  
		//Recycle the MotionEvent, to be re-used by a later caller. Don't use after this!
		screenTap.recycle();

		
		// MONSTER COLLISION ATTEMPT
		// monster2 is East of monster1, so we try to move monster1 
		// East in attempts to collide with monster2
		monster1.setDirection(EAST);
		monster1.moveMonster(); 
		// Verify that monster1 did NOT move. It should still be
		// at its original location, cell 0,0
		assertSame(monster1, mModel.getAllMonstersGrid()[0][0]);
		// Verify monster2 is still in its original location cell 1,1
		assertSame(monster2, mModel.getAllMonstersGrid()[1][0]);
		
		
		// WALL COLLISION ATTEMPT
		// Wall is West of monster1, so we try to move monster1
		// West in attempts to collide with wall. 
		monster1.setDirection(WEST);
		monster1.moveMonster();
		// Verify that monster1 did NOT move. It should still be
		// at its original location, cell 0,0
		assertSame(monster1, mModel.getAllMonstersGrid()[0][0]);
		
		// For effect...(Suspend display for a little bit)
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) { }
	
	}
	
	/** Verifies the monster can change is state from vulnerable to safe
	 	and the monster can be killed when touched (in vulnerable state).
	  */
	public void testStateChangeAndKill() throws InterruptedException {
	
		/* 
		 * Note: The use of Thread.sleep within this unit test is mainly to illustrate
		 * the effects of the test within the UI (so things don't disappear in a flash!)
		 */
	
		// MONSTER ENTRY TO WORLD
	    // Create new monster
	    MockMonster monster = new MockMonster(0,0);
	    // Add monster to cell 0,0 within model (world model)
		mModel.addMonster(monster);
		// Verify that monster entered cell 0,0 in world
		assertSame(monster, mModel.getAllMonstersGrid()[0][0]);
		
		// Create screen tap MotionEvent
		MotionEvent screenTap = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 
								MotionEvent.ACTION_DOWN, 0, 0, 0);  
		// Touch screen to start game
		mView.dispatchTouchEvent(screenTap);  
	
		// TESTING MONSTER STATE CHANGE AND KILLING
		// Verify current monster state is vulnerable	
		assertEquals(VULNERABLE_STATE, monster.getMonsterState());
		
		Thread.sleep(500);
		
		// Change monster state to safe
		monster.changeMonsterState();
		// Verify current monster state is safe	
		assertEquals(SAFE_STATE, monster.getMonsterState());
		// Attempt to kill monster by touching
	    mView.dispatchTouchEvent(screenTap);  
	    // Verify monster did not die
	    assertFalse(monster.isDead());
		assertSame(monster, mModel.getAllMonstersGrid()[0][0]);

		Thread.sleep(500);
	    
	    // Change  monster state to vulnerable
		monster.changeMonsterState();
		
		Thread.sleep(500);
		
		// Verify current monster state is vulnerable	
		assertEquals(VULNERABLE_STATE, monster.getMonsterState());
		// Attempt to kill monster by touching
	    mView.dispatchTouchEvent(screenTap);  // <-- Effectively is mModel.touchCell(0, 0);
		// Verify monster died
		assertTrue(monster.isDead());  // <-- Monster.isCancelled() doesn't appear instantaneous. So isDead() is used.
		assertNotSame(monster, mModel.getAllMonstersGrid()[0][0]);
		
		//Recycle the MotionEvent, to be re-used by a later caller. Don't use after this!
		screenTap.recycle();
		
	}
	
	/**
	 * Tests the scoring mechanism. 
	 * Verifies the game scores/ranks are correct at their respective times.
	 */
	public void testScoring() throws InterruptedException {
	
		// MONSTER ENTRY TO WORLD
	    // Create new monster
	    MockMonster monster = new MockMonster(0,0);
	    // Add monster to cell 0,0 within model (world model)
		mModel.addMonster(monster);
		// Verify that monster entered cell 0,0 in world
		assertSame(monster, mModel.getAllMonstersGrid()[0][0]);
		
		// Create screen tap MotionEvent
		MotionEvent screenTap = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 
								MotionEvent.ACTION_DOWN, 0, 0, 0);  
		// Touch screen to start game
		mView.dispatchTouchEvent(screenTap);  
		
		// Verify game starting time is 5 seconds
		assertEquals(5, mModel.getTime());
		
		// TESTING "RANK A":
		// (Killing monster within 100% - 80% of max game time = Rank A)
		// Wait for game clock to tick down to 4 seconds
		while (mModel.getTime() != 4) {	Thread.sleep(1); }
		// Verifies rank is A (Since 4 seconds is within 100% - 80% of max game time) 
		assertEquals("A", mModel.getScore());
		// TESTING "RANK B":
		// (Killing monster within 79% - 60% of max game time = Rank B)
		// Wait for game clock to tick down to 3 seconds
		while (mModel.getTime() != 3) {	Thread.sleep(1); }
		// Verifies rank is B (Since 3 seconds is within 79% - 60% of max game time) 
		assertEquals("B", mModel.getScore());
		// TESTING "RANK C":
		// (Killing monster within 59% - 40% of max game time = Rank C)
		// Wait for game clock to tick down to 2 seconds
		while (mModel.getTime() != 2) {	Thread.sleep(1); }
		// Verifies rank is C (Since 2 seconds is within 59% - 40% of max game time) 
		assertEquals("C", mModel.getScore());
		// TESTING "RANK D":
		// (Killing monster within 39% - 20% of max game time = Rank D)
		// Wait for game clock to tick down to 1 second
		while (mModel.getTime() != 1) {	Thread.sleep(1); }
		// Verifies rank is D (Since 1 second is within 39% - 20% of max game time) 
		assertEquals("D", mModel.getScore());
		// TESTING "RANK F":
		// (Killing monster within 19% - 0% of max game time = Rank F)
		// Wait for game clock to tick down to 0
		while (mModel.getTime() != 0) {	Thread.sleep(1); }
		// Verifies rank is F (Since 0 seconds is within 19% - 0% of max game time) 
		assertEquals("F", mModel.getScore());

	}
	

	/** 
	 * The real monster (ConcreteMonster) uses a random algorithm for
	 * movement and state change. For testing purposes, the mock monster will 
	 * provide a more controllable platform for monster movement and state.
	 */
	private class MockMonster extends ConcreteMonster {
		
		/** Initialize monster providing initial coordinates */
		private MockMonster(int x, int y){
			setMonsterLocation(x, y);
			// Set initial monster state
			mState = VULNERABLE_STATE;
		}
		
		/** Holds monster's intended direction of travel */
		private int mDirection;; 
		
		/** For testing, we will manually set the direction monster will travel*/
		private void setDirection(int direction) {
			mDirection = direction;
		}
		
		/** Gets monster's latest direction */
		private int getDirection() {
			return mDirection;
		}
	
		/** For testing, we will manually set the monster state */
		private void setState(int state) {
			mState = state;
		}
		
		@Override
		protected void moveMonster() {
			// For testing, we manually decide which direction monster should travel (North, South, East, No Move etc.)
			potentialCoord = moveTo(getDirection()); // <-- This is the major difference between the movement of this mock monster and the "real monster"
			if (mWorldModel.enterCell(this)){
				if (potentialCoord != mMonsterCoord){
					mWorldModel.exitCell(this);
				}
				mMonsterCoord = potentialCoord;				
			}
		}
			
			
		
		/** 
		 * The random algorithm that controls monster movemenet and state change,
		 * is disabled for testing purposes. 
		 */
		@Override
		protected void initMonsterMovementManager() {
			// Do nothing (for test purposes!)
		}
		
		@Override
		protected void initMonsterStateManager() {
			// Do nothing (for test purposes!)
		}
	}
	
	
}

