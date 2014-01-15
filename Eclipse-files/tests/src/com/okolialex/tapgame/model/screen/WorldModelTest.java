package com.okolialex.tapgame.model.screen;

/**
 * This class tests the basic primary functions of the monster world. 
 * Tests that world can: 
 * - House monsters and they can move within world without
 *   crashing into obstacles (wall or other monsters)
 * - Attempt to kill monster by communicating user touch events 
 * - Score the game accurately
 * - Time the game accurately
 *  
 * @author Alexander Okoli
 */

import static com.okolialex.tapgame.common.Constants.*;


import java.util.concurrent.Semaphore;

import junit.framework.TestCase;

import com.okolialex.tapgame.common.Coordinate;
import com.okolialex.tapgame.common.WorldModelChangeListener;
import com.okolialex.tapgame.model.actor.ConcreteMonsterModel;
import com.okolialex.tapgame.model.actor.MonsterModel;

public class WorldModelTest extends TestCase {

	Coordinate potentialCoord1, potentialCoord2;
	MonsterModel[][] worldMonsters;
	Semaphore[][] worldCellAccess;

	protected MonsterModel monster1, monster2;
	/** Sets up 2 monsters for testing environment */
	protected void setMonsterModel(final MonsterModel monster1, final MonsterModel monster2) {
		this.monster1 = monster1;
		this.monster2 = monster2;
	}
	
	protected WorldModel world;
	/** Sets up world */
	protected void setWorldModel(final WorldModel world) {
		this.world = world;
	}

	/** 
	 * The view typically listens to the model for changes, but 
	 * a mock dependency object is used for testing purposes.
	 */
	private WorldModelChangeListener dependency = new WorldModelChangeListener() {
		
		@Override
		public void onWorldChange(WorldModel world) {
			// Mock object, so do nothing!
		}
	};
	
	protected void setUp() throws Exception {
		super.setUp();
		setMonsterModel(new ConcreteMonsterModel(), new ConcreteMonsterModel());
		// Sets the monster locations. Setting location is equivalent to monster 
		// "thinking" of it's travel route within world. So, in monster1's mind
		// it will travel to coordinate 3,4 within world.
		monster1.setMonsterLocation(3, 4);
		monster2.setMonsterLocation(1, 1);
		// Expose monsters to world so they can see
		monster1.setWorld(world);
		monster2.setWorld(world);
		
		setWorldModel(new ConcreteWorldModel());
		// Sets the world's width and height
		world.setDimensions(5, 6);
		// Initializes grid that will hold all monsters within world
		world.initAllMonstersGrid();
		// Initializes semaphore access to all cells within world
		world.enableAllCellAccess();
		// Set world change listener (use dependency)
		world.setWorldModelChangeListener(dependency);
		// Obtains the grid containing all monsters within world
		worldMonsters = world.getAllMonstersGrid();
		// Obtains the grid containing accessibility of cells within world
		worldCellAccess = world.getGameGridCellAccess();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		setMonsterModel(null, null);
		setWorldModel(null);
	}
	
	/** Verifies the world's width, height, and monsters' initial coordinates are accurate */
	public void testVerifyInitialMeasurements() {
		// Verifies the world's dimensions
		assertEquals(world.getWidth(), 5);
		assertEquals(world.getHeight(), 6);
		
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
		assertTrue(world.enterCell(monster1));
		// Verifies that monster1 entered the world coordinate 3,4
		assertSame(monster1, worldMonsters[3][4]);
		// Verifies that there is no room in cell 3,4 for any other monster to enter
		assertEquals(0, worldCellAccess[3][4].availablePermits());
		
		// MONSTER EXITING CELL WITHIN WORLD
		// Makes monster1 exit world cell 3,4
		assertTrue(world.exitCell(monster1));
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
		assertTrue(world.enterCell(monster1));
		assertEquals(monster1, worldMonsters[3][3]);
		
		// Verifies monster can travel South:
		potentialCoord1 = monster1.moveTo(SOUTH);
		// monster1 sets its coordinates Southbound (monster1 "made decision" on heading South)
		monster1.setMonsterLocation(potentialCoord1.getX(), potentialCoord1.getY());
		// monster1 attempts to enter its coordinates (3,4) within world
		// Confirms monster1 traveled South within world
		assertTrue(world.enterCell(monster1));
		assertEquals(monster1, worldMonsters[3][4]);
		
	}
	
	/** Checks to make sure monster isn't crashing into other monsters or wall */
	public void testCollision() {
		
		// MONSTER ENTRY TO WORLD
		// Places monster1 in the coordinate 3,4 within world
		assertTrue(world.enterCell(monster1));
		// Places monster2 in the coordinate 1,1, within world
		assertTrue(world.enterCell(monster2));
		// Verifies that monster1 entered the world coordinate 3,4
		assertEquals(monster1, worldMonsters[3][4]);
		// Verifies that monster2 entered the world coordinate 1,1
		assertEquals(monster2, worldMonsters[1][1]);
		
		// MONSTER COLLISION ATTEMPT
		// Sets monster1's location to match that of monster2
		monster1.setMonsterLocation(1, 1);
		// Attempts to move monster1 to monster2's location within world
		// Verifies that monster1 can't go into monster2's location, as it is occupied
		assertFalse(world.enterCell(monster1));
		
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
		assertFalse(world.enterCell(monster1));
	}
	
	/** 
	 * Verifies the monster can die when touched in vulnerable state, and 
	 * there would be no effect when touched in safe state 
	 */
	public void testKill() {
		
		// MONSTER ENTRY TO WORLD
		// Places monster1 in the coordinate 3,4 within world
		assertTrue(world.enterCell(monster1));
		// Verifies that monster1 entered the world coordinate 3,4
		assertEquals(monster1, worldMonsters[3][4]);
		
		// SIMULATION OF CELL TOUCH TO KILL MONSTER
		// Monster touched when in safe state:
		// Set monster state to safe.
		monster1.setMonsterState(SAFE_STATE);
		// Touch the monster cell
		world.touchCell(3, 4);
		// Verify monster didn't die
		assertFalse(monster1.isDead());
		
		// Monster touched when in vulnerable state:
		// Set monster state to vulnerable.
		monster1.setMonsterState(VULNERABLE_STATE);
		// Touch the monster cell
		world.touchCell(3, 4);
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
		assertTrue(world.enterCell(monster1));
		// Verifies that monster1 entered the world coordinate 3,4
		assertEquals(monster1, worldMonsters[3][4]);
		// Set max game time to 5 seconds 
		world.setTime(5);
		// Make clock tick down 1 second
		countDownTimeElapsed(1);
		// Verify game time is now 4 seconds
		assertEquals(world.getTime(), 4);
		// Set monster state to vulnerable.
		monster1.setMonsterState(VULNERABLE_STATE);
		// Touch the monster cell to kill monster
		world.touchCell(3, 4);
		// Verify monster died
		assertTrue(monster1.isDead());
		// Verify game score is rank A
		// (Killing monster within 100% - 80% of max game time = Rank A)
		assertEquals(world.getScore(), "A");
		
		// TESTING "RANK B":
		// Create new monster and expose it to world via constructor
		monster1 = new ConcreteMonsterModel(world);
		// Set monster coordinate to 3,4 
		monster1.setMonsterLocation(3, 4);
		// Places monster1 in the coordinate 3,4 within world
		assertTrue(world.enterCell(monster1));
		// Verifies that monster1 entered the world coordinate 3,4
		assertEquals(monster1, worldMonsters[3][4]);
		// Set max game time to 5 seconds 
		world.setTime(5);
		// Make clock tick down 2 seconds
		countDownTimeElapsed(2);
		// Verify game time is now 3 seconds
		assertEquals(world.getTime(), 3);
		// Set monster state to vulnerable.
		monster1.setMonsterState(VULNERABLE_STATE);
		// Touch the monster cell to kill monster
		world.touchCell(3, 4);
		// Verify monster died
		assertTrue(monster1.isDead());
		// Verify game score is rank B
		// (Killing monster within 79% - 60% of max game time = Rank B)
		assertEquals(world.getScore(), "B");
		
		// TESTING "RANK C":
		// Create new monster and expose it to world via constructor
		monster1 = new ConcreteMonsterModel(world);
		// Set monster coordinate to 3,4 
		monster1.setMonsterLocation(3, 4);
		// Places monster1 in the coordinate 3,4 within world
		assertTrue(world.enterCell(monster1));
		// Verifies that monster1 entered the world coordinate 3,4
		assertEquals(monster1, worldMonsters[3][4]);
		// Set max game time to 5 seconds 
		world.setTime(5);
		// Make clock tick down 3 seconds
		countDownTimeElapsed(3);
		// Verify game time is now 2 seconds
		assertEquals(world.getTime(), 2);
		// Set monster state to vulnerable.
		monster1.setMonsterState(VULNERABLE_STATE);
		// Touch the monster cell to kill monster
		world.touchCell(3, 4);
		// Verify monster died
		assertTrue(monster1.isDead());
		// Verify game score is rank C
		// (Killing monster within 59% - 40% of max game time = Rank C)
		assertEquals(world.getScore(), "C");
		
		// TESTING "RANK D":
		// Create new monster and expose it to world via constructor
		monster1 = new ConcreteMonsterModel(world);
		// Set monster coordinate to 3,4 
		monster1.setMonsterLocation(3, 4);
		// Places monster1 in the coordinate 3,4 within world
		assertTrue(world.enterCell(monster1));
		// Verifies that monster1 entered the world coordinate 3,4
		assertEquals(monster1, worldMonsters[3][4]);
		// Set max game time to 5 seconds 
		world.setTime(5);
		// Make clock tick down 4 seconds
		countDownTimeElapsed(4);
		// Verify game time is now 1 second
		assertEquals(world.getTime(), 1);
		// Set monster state to vulnerable.
		monster1.setMonsterState(VULNERABLE_STATE);
		// Touch the monster cell to kill monster
		world.touchCell(3, 4);
		// Verify monster died
		assertTrue(monster1.isDead());
		// Verify game score is rank D
		// (Killing monster within 39% - 20% of max game time = Rank D)
		assertEquals(world.getScore(), "D");
		
		// TESTING "RANK F":
		// Create new monster and expose it to world via constructor
		monster1 = new ConcreteMonsterModel(world);
		// Set monster coordinate to 3,4 
		monster1.setMonsterLocation(3, 4);
		// Places monster1 in the coordinate 3,4 within world
		assertTrue(world.enterCell(monster1));
		// Verifies that monster1 entered the world coordinate 3,4
		assertEquals(monster1, worldMonsters[3][4]);
		// Set max game time to 5 seconds 
		world.setTime(5);
		// Make clock tick down 5 seconds
		countDownTimeElapsed(5);
		// Verify game time is now 0 
		assertEquals(world.getTime(), 0);
		// Set monster state to vulnerable.
		monster1.setMonsterState(VULNERABLE_STATE);
		// Touch the monster cell to kill monster
		world.touchCell(3, 4);
		// Verify monster died
		assertTrue(monster1.isDead());
		// Verify game score is rank F
		// (Killing monster within 19% - 0% of max game time = Rank F)
		assertEquals(world.getScore(), "F");
	}
	
	public void testTimeCountDown() throws InterruptedException {
		/*int expectedTestDuration = 3;
		int actualTestDuration = 0;
		
		// Set max game time to 3 seconds 
		world.setTime(expectedTestDuration);
		// Start the clock countdown 
		world.startClock();
		Thread.sleep(1000); // <-- Initial 1 second wait, before continuous ticking.
		while (world.getTime() > 0) {
			actualTestDuration++;	
			Thread.sleep(1000); // <-- Continuous 1 second tick.
		}
		assertEquals(expectedTestDuration, actualTestDuration); 
		*/
	}
	
	/**
	 * Decrements the current game time in world
	 * 
	 * @param t is the number of seconds the time should be decremented by 
	 */
	protected void countDownTimeElapsed(int t) {
		for (int i = 0; i < t; i++){
			world.decrementTime();			
		}	
	}
}
