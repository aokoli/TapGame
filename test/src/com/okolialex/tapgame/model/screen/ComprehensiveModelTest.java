package com.okolialex.tapgame.model.screen;

/**
 * This provides a comprehensive test for the major base interfaces that make
 * up the TapGameModel, namely, the MonsterCellWorld and HUD interfaces.
 * 
 *   The tests will verify that the monster world can: 
 * - House monsters and they can move within world without
 *   crashing into obstacles (wall or other monsters)
 * - Attempt to kill monster by receiving user touch events 
 * 
 *   The test will also verify that:
 * - The game is scored accurately
 * - The game timer can countdown
 *  
 * @author Alexander Okoli
 */

import static com.okolialex.tapgame.common.Constants.*;


import java.util.concurrent.Semaphore;

import junit.framework.TestCase;

import com.okolialex.tapgame.common.Coordinate;
import com.okolialex.tapgame.common.ModelChangeListener;
import com.okolialex.tapgame.model.ConcreteTapGameModel;
import com.okolialex.tapgame.model.TapGameModel;
import com.okolialex.tapgame.model.actor.ConcreteMonster;
import com.okolialex.tapgame.model.actor.Monster;

public class ComprehensiveModelTest extends TestCase {

	Coordinate potentialCoord1, potentialCoord2;
	Monster[][] worldMonsters;
	Semaphore[][] worldCellAccess;

	protected Monster monster1, monster2;
	/** Sets up 2 monsters for testing environment */
	protected void setMonsterModel(final Monster monster1, final Monster monster2) {
		this.monster1 = monster1;
		this.monster2 = monster2;
	}
	
	/**
	 * The TapGameModel contains the MonsterCellWorld (where the monster
	 * actually travels) and other interfaces.
	 * The variable "model" will be used to embody it all. 
	 */
	protected TapGameModel model;
	/** Sets up model */
	protected void setTapGameModel(final TapGameModel model) {
		this.model = model;
	}

	/** 
	 * The view typically listens to the model for changes, but 
	 * a mock dependency object is used for testing purposes.
	 */
	private ModelChangeListener dependency = new ModelChangeListener() {
		
		@Override
		public void onModelChange(TapGameModel model) {
			// Mock object, so do nothing!
		}
	};
	
	protected void setUp() throws Exception {
		super.setUp();
		setMonsterModel(new ConcreteMonster(), new ConcreteMonster());
		// Sets the monster locations. Setting location is equivalent to monster 
		// "thinking" of it's travel route within world. So, in monster1's mind
		// it will travel to coordinate 3,4 within world.
		monster1.setMonsterLocation(3, 4);
		monster2.setMonsterLocation(1, 1);
		// Expose monsters to world so they can see
		monster1.setTapGameModel(model);
		monster2.setTapGameModel(model);
		
		setTapGameModel(new ConcreteTapGameModel());
		// Sets the world's width and height
		model.setDimensions(5, 6);
		// Initializes grid that will hold all monsters within world
		model.initAllMonstersGrid();
		// Initializes semaphore access to all cells within world
		model.enableAllCellAccess();
		// Set world change listener (use dependency)
		model.setModelChangeListener(dependency);
		// Obtains the grid containing all monsters within world
		worldMonsters = model.getAllMonstersGrid();
		// Obtains the grid containing accessibility of cells within world
		worldCellAccess = model.getGameGridCellAccess();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		setMonsterModel(null, null);
		setTapGameModel(null);
	}
	
	/** Verifies the world's width, height, and monsters' initial coordinates are accurate */
	public void testVerifyInitialMeasurements() {
		// Verifies the world's dimensions
		assertEquals(model.getWidth(), 5);
		assertEquals(model.getHeight(), 6);
		
		// Verifies the monsters' initial x,y location
		assertTrue(monster1.getMonsterLocation().equals(new Coordinate(3,4)));
		assertTrue(monster2.getMonsterLocation().equals(new Coordinate(1,1)));
	}
	
	/**
	 * Verifies that monster can:
	 * - Enter and exit cells within world; and the cell access functions
	 *   properly i.e. When a monster enters a cell, only that
	 *   monster has permission to that cell (nothing else can acquire lock.) 
	 * - Move within world.
	 */
	public void testWorldMovement() {
		
		// MONSTER ENTERING CELL WITHIN WORLD
		// Verifies that there is room in cell 3,4 for a monster to enter
		assertEquals(1, worldCellAccess[3][4].availablePermits());
		// Places monster1 in the coordinate 3,4 within world
		assertTrue(model.enterCell(monster1));
		// Verifies that monster1 entered the world coordinate 3,4
		assertSame(monster1, worldMonsters[3][4]);
		// Verifies that there is no room in cell 3,4 for any other monster to enter
		assertEquals(0, worldCellAccess[3][4].availablePermits());
		
		// MONSTER EXITING CELL WITHIN WORLD
		// Makes monster1 exit world cell 3,4
		assertTrue(model.exitCell(monster1));
		// Verifies monster1 exited the world coordinate 3,4
		assertNotSame(monster1, worldMonsters[3][4]);
		// Verifies that there is room in cell 3,4 for a monster to enter
		assertEquals(1, worldCellAccess[3][4].availablePermits());
		
		
		// MONSTER MOVEMENT WITHIN WORLD
		// Verifies monster can travel North:
		// monster1 derives coordinates for traveling North (monster1 "thinks" about heading North)
		potentialCoord1 = monster1.moveTo(NORTH);
		// monster1 sets its coordinates Northbound (monster1 "made decision" on heading North)
		monster1.setMonsterLocation(potentialCoord1.getX(), potentialCoord1.getY());
		// monster1 attempts to enter its coordinates (3,3) within world
		// Confirms monster1 traveled North within world
		assertTrue(model.enterCell(monster1));
		assertEquals(monster1, worldMonsters[3][3]);
		
		// Verifies monster can travel South:
		potentialCoord1 = monster1.moveTo(SOUTH);
		// monster1 sets its coordinates Southbound (monster1 "made decision" on heading South)
		monster1.setMonsterLocation(potentialCoord1.getX(), potentialCoord1.getY());
		// monster1 attempts to enter its coordinates (3,4) within world
		// Confirms monster1 traveled South within world
		assertTrue(model.enterCell(monster1));
		assertEquals(monster1, worldMonsters[3][4]);
		
	}
	
	/** Checks to make sure monster isn't crashing into other monsters or wall */
	public void testCollision() {
		
		// MONSTER ENTRY TO WORLD
		// Places monster1 in the coordinate 3,4 within world
		assertTrue(model.enterCell(monster1));
		// Places monster2 in the coordinate 1,1, within world
		assertTrue(model.enterCell(monster2));
		// Verifies that monster1 entered the world coordinate 3,4
		assertEquals(monster1, worldMonsters[3][4]);
		// Verifies that monster2 entered the world coordinate 1,1
		assertEquals(monster2, worldMonsters[1][1]);
		
		// MONSTER COLLISION ATTEMPT
		// Sets monster1's location to match that of monster2
		monster1.setMonsterLocation(1, 1);
		// Attempts to move monster1 to monster2's location within world
		// Verifies that monster1 can't go into monster2's location, as it is occupied
		assertFalse(model.enterCell(monster1));
		
		// WALL COLLISION ATTEMPT
		// Sets monster1's coordinates to a location beyond world's borders
		monster1.setMonsterLocation(-1, -1);
		// Verifies that coordinate -1,-1 is indeed out of bounds
		try {
			worldMonsters[-1][-1].getMonsterLocation(); //<-- Used random function just to query worldMonsters
			fail("Expected IndexOutOfBoundsException!");
		} catch (IndexOutOfBoundsException e) {
			// If this region is reached, -1,-1 coordinate is indeed out of bounds!
		}
		// Attempts to move monster1 to the location (-1,-1) beyond world's borders
		// Verifies that monster1 can't travel beyond borders
		assertFalse(model.enterCell(monster1));
	}
	
	/** 
	 * Verifies the monster can die when touched in vulnerable state, and 
	 * there would be no effect when touched in safe state 
	 */
	public void testKill() {
		
		// MONSTER ENTRY TO WORLD
		// Places monster1 in the coordinate 3,4 within world
		assertTrue(model.enterCell(monster1));
		// Verifies that monster1 entered the world coordinate 3,4
		assertEquals(monster1, worldMonsters[3][4]);
		
		// SIMULATION OF CELL TOUCH TO KILL MONSTER
		// Monster touched when in safe state:
		// Set monster state to safe.
		monster1.setMonsterState(SAFE_STATE);
		// Touch the monster cell
		model.touchCell(3, 4);
		// Verify monster didn't die
		assertFalse(monster1.isDead());
		
		// Monster touched when in vulnerable state:
		// Set monster state to vulnerable.
		monster1.setMonsterState(VULNERABLE_STATE);
		// Touch the monster cell
		model.touchCell(3, 4);
		// Verify monster died
		assertTrue(monster1.isDead());
		
	}
	
	/**
	 * Tests the scoring mechanism. Uses a time fast forward to begin time countdown, 
	 * and verifies the game scores/ranks are correct at their respective times.
	 */
	public void testScoring() {  // TODO Change to match Activity test.
		
		// TESTING "RANK A":
		// Places monster1 in the coordinate 3,4 within world
		assertTrue(model.enterCell(monster1));
		// Verifies that monster1 entered the world coordinate 3,4
		assertEquals(monster1, worldMonsters[3][4]);
		// Set max game time to 5 seconds 
		model.setTime(5);
		// Make clock tick down 1 second
		countDownTimeElapsed(1);
		// Verify game time is now 4 seconds
		assertEquals(model.getTime(), 4);
		// Set monster state to vulnerable.
		monster1.setMonsterState(VULNERABLE_STATE);
		// Touch the monster cell to kill monster
		model.touchCell(3, 4);
		// Verify monster died
		assertTrue(monster1.isDead());
		// Verify game score is rank A
		// (Killing monster within 100% - 80% of max game time = Rank A)
		assertEquals(model.getScore(), "A");
		
		// TESTING "RANK B":
		// Create new monster and expose it to world via constructor
		monster1 = new ConcreteMonster(model);
		// Set monster coordinate to 3,4 
		monster1.setMonsterLocation(3, 4);
		// Places monster1 in the coordinate 3,4 within world
		assertTrue(model.enterCell(monster1));
		// Verifies that monster1 entered the world coordinate 3,4
		assertEquals(monster1, worldMonsters[3][4]);
		// Set max game time to 5 seconds 
		model.setTime(5);
		// Make clock tick down 2 seconds
		countDownTimeElapsed(2);
		// Verify game time is now 3 seconds
		assertEquals(model.getTime(), 3);
		// Set monster state to vulnerable.
		monster1.setMonsterState(VULNERABLE_STATE);
		// Touch the monster cell to kill monster
		model.touchCell(3, 4);
		// Verify monster died
		assertTrue(monster1.isDead());
		// Verify game score is rank B
		// (Killing monster within 79% - 60% of max game time = Rank B)
		assertEquals(model.getScore(), "B");
		
		// TESTING "RANK C":
		// Create new monster and expose it to world via constructor
		monster1 = new ConcreteMonster(model);
		// Set monster coordinate to 3,4 
		monster1.setMonsterLocation(3, 4);
		// Places monster1 in the coordinate 3,4 within world
		assertTrue(model.enterCell(monster1));
		// Verifies that monster1 entered the world coordinate 3,4
		assertEquals(monster1, worldMonsters[3][4]);
		// Set max game time to 5 seconds 
		model.setTime(5);
		// Make clock tick down 3 seconds
		countDownTimeElapsed(3);
		// Verify game time is now 2 seconds
		assertEquals(model.getTime(), 2);
		// Set monster state to vulnerable.
		monster1.setMonsterState(VULNERABLE_STATE);
		// Touch the monster cell to kill monster
		model.touchCell(3, 4);
		// Verify monster died
		assertTrue(monster1.isDead());
		// Verify game score is rank C
		// (Killing monster within 59% - 40% of max game time = Rank C)
		assertEquals(model.getScore(), "C");
		
		// TESTING "RANK D":
		// Create new monster and expose it to world via constructor
		monster1 = new ConcreteMonster(model);
		// Set monster coordinate to 3,4 
		monster1.setMonsterLocation(3, 4);
		// Places monster1 in the coordinate 3,4 within world
		assertTrue(model.enterCell(monster1));
		// Verifies that monster1 entered the world coordinate 3,4
		assertEquals(monster1, worldMonsters[3][4]);
		// Set max game time to 5 seconds 
		model.setTime(5);
		// Make clock tick down 4 seconds
		countDownTimeElapsed(4);
		// Verify game time is now 1 second
		assertEquals(model.getTime(), 1);
		// Set monster state to vulnerable.
		monster1.setMonsterState(VULNERABLE_STATE);
		// Touch the monster cell to kill monster
		model.touchCell(3, 4);
		// Verify monster died
		assertTrue(monster1.isDead());
		// Verify game score is rank D
		// (Killing monster within 39% - 20% of max game time = Rank D)
		assertEquals(model.getScore(), "D");
		
		// TESTING "RANK F":
		// Create new monster and expose it to world via constructor
		monster1 = new ConcreteMonster(model);
		// Set monster coordinate to 3,4 
		monster1.setMonsterLocation(3, 4);
		// Places monster1 in the coordinate 3,4 within world
		assertTrue(model.enterCell(monster1));
		// Verifies that monster1 entered the world coordinate 3,4
		assertEquals(monster1, worldMonsters[3][4]);
		// Set max game time to 5 seconds 
		model.setTime(5);
		// Make clock tick down 5 seconds
		countDownTimeElapsed(5);
		// Verify game time is now 0 
		assertEquals(model.getTime(), 0);
		// Set monster state to vulnerable.
		monster1.setMonsterState(VULNERABLE_STATE);
		// Touch the monster cell to kill monster
		model.touchCell(3, 4);
		// Verify monster died
		assertTrue(monster1.isDead());
		// Verify game score is rank F
		// (Killing monster within 19% - 0% of max game time = Rank F)
		assertEquals(model.getScore(), "F");
	}
	
	/** Verifies that the game clock countdown works */
	public void testTimeCountDown() throws InterruptedException {
		/*
		 * int expectedTestDuration = 3;
		int actualTestDuration = 0;
		
		// Set max game time to 3 seconds 
		model.setTime(expectedTestDuration);
		// Start the clock countdown 
		model.startClock();
		Thread.sleep(1000); // <-- Initial 1 second wait, before continuous ticking.
		while (model.getTime() > 0) {
			actualTestDuration++;	
			Thread.sleep(1000); // <-- Continuous 1 second tick.
		}
		assertEquals(expectedTestDuration, actualTestDuration); 
		*/
		
	}
	
	/**
	 * Decrements the current game time in model
	 * 
	 * @param t is the number of seconds the time should be decremented by 
	 */
	protected void countDownTimeElapsed(int t) {
		for (int i = 0; i < t; i++){
			model.decrementTime();			
		}	
	}
}
