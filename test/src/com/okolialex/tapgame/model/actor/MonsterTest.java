package com.okolialex.tapgame.model.actor;

/**
 * This class tests the basic primary functions of the monster. 
 * Tests that monster can: 
 * - Move in all 8 neighboring directions, and stay stationary
 * - Change state
 * - Be killed depending on its state (vulnerable vs safe)
 * 
 * @author Alexander Okoli
 */
import junit.framework.TestCase;

import static com.okolialex.tapgame.common.Constants.*;

import java.util.Random;

import com.okolialex.tapgame.common.AutoRandomEngine;
import com.okolialex.tapgame.common.Coordinate;


import com.okolialex.tapgame.model.ConcreteTapGameModel;
import com.okolialex.tapgame.model.TapGameModel;
import com.okolialex.tapgame.model.actor.ConcreteMonster;
import com.okolialex.tapgame.model.actor.Monster;

public class MonsterTest extends TestCase {

	protected Monster monster;
	protected void setMonsterModel(final Monster monster) {
		this.monster = monster;
	}
	
	/** TapGameModel contains the MonsterCellWorld, the "model"
	 * represents the world monster can travel in. 
	 */
	protected TapGameModel model;
	protected void setWorldModel(final TapGameModel model) {
		this.model = model;
	}

	protected void setUp() throws Exception {
		super.setUp();
		generateNewMonster();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		setMonsterModel(null);
		setWorldModel(null);
	}
	
	protected void generateNewMonster() {
		setMonsterModel(new ConcreteMonster());
		setWorldModel(new ConcreteTapGameModel());
		monster.setTapGameModel(model); // <-- Only needed for changeMonsterState(), in this test class.
	}

	
	/** 
	 * Tests to make sure monster can move in all 8 neighboring directions 
	 * as well as stay stationary. 
	 */
	public void testMonsterMove() {
		Coordinate potentialCoord;
		monster.setMonsterLocation(3, 4);
		// Verifies the monster's current x,y location
		assertTrue(monster.getMonsterLocation().equals(new Coordinate(3,4)));
		
		// Verifies the monster can travel North
		// Monster derives coordinates for traveling North
		potentialCoord = monster.moveTo(NORTH);
		// Monster sets its coordinates Northbound
		monster.setMonsterLocation(potentialCoord.getX(), potentialCoord.getY());
		// Confirms that monster is Northbound
		assertTrue(monster.getMonsterLocation().equals(new Coordinate(3,3)));
		
		// Verifies the monster can travel South
		potentialCoord = monster.moveTo(SOUTH);
		monster.setMonsterLocation(potentialCoord.getX(), potentialCoord.getY());
		assertTrue(monster.getMonsterLocation().equals(new Coordinate(3,4)));
		
		// Verifies the monster can travel East
		potentialCoord = monster.moveTo(EAST);
		monster.setMonsterLocation(potentialCoord.getX(), potentialCoord.getY());
		assertTrue(monster.getMonsterLocation().equals(new Coordinate(4,4)));
		
		// Verifies the monster can travel West
		potentialCoord = monster.moveTo(WEST);
		monster.setMonsterLocation(potentialCoord.getX(), potentialCoord.getY());
		assertTrue(monster.getMonsterLocation().equals(new Coordinate(3,4)));
		
		// Verifies the monster can travel Northeast
		potentialCoord = monster.moveTo(NORTHEAST);
		monster.setMonsterLocation(potentialCoord.getX(), potentialCoord.getY());
		assertTrue(monster.getMonsterLocation().equals(new Coordinate(4,3)));
		
		// Verifies the monster can travel Northwest
		potentialCoord = monster.moveTo(NORTHWEST);
		monster.setMonsterLocation(potentialCoord.getX(), potentialCoord.getY());
		assertTrue(monster.getMonsterLocation().equals(new Coordinate(3,2)));
		
		// Verifies the monster can travel Southeast
		potentialCoord = monster.moveTo(SOUTHEAST);
		monster.setMonsterLocation(potentialCoord.getX(), potentialCoord.getY());
		assertTrue(monster.getMonsterLocation().equals(new Coordinate(4,3)));
		
		// Verifies the monster can travel Southwest
		potentialCoord = monster.moveTo(SOUTHWEST);
		monster.setMonsterLocation(potentialCoord.getX(), potentialCoord.getY());
		assertTrue(monster.getMonsterLocation().equals(new Coordinate(3,4)));
		
		// Verifies the monster can stay stationary
		potentialCoord = monster.moveTo(NO_MOVE);
		monster.setMonsterLocation(potentialCoord.getX(), potentialCoord.getY());
		assertTrue(monster.getMonsterLocation().equals(new Coordinate(3,4)));
	}
	
	
	/** Verifies the monster can change state (vulnerable vs safe) */
	public void testMonsterStateChange() {
		// TODO Test change state after implementing difficulty level increase
		
		int monsterState;
		// Set the monster state to vulnerable
		monster.setMonsterState(VULNERABLE_STATE); 
		// Retrieve the monster state 
		monsterState = monster.getMonsterState();
		// Confirm monster state is vulnerable
		assertEquals(VULNERABLE_STATE, monsterState);
		// Confirm the monster state can change
		monster.changeMonsterState();
		// Retrieve the monster state 
		monsterState = monster.getMonsterState();
		// Confirm the state changed to safe
		assertEquals(SAFE_STATE, monsterState);
	}

	

	/** Verifies that monster can die. */
	public void testMonsterKill() {
		int monsterState;
		// Set the monster state to vulnerable
		monster.setMonsterState(VULNERABLE_STATE); 
		// Retrieve the monster state 
		monsterState = monster.getMonsterState();
		// Confirm monster state is vulnerable
		assertEquals(VULNERABLE_STATE, monsterState);
		// Attempt to kill the monster in vulnerable state
		monster.killMonster();
		// Confirm that the monster is dead
		assertTrue(monster.isCancelled());
		
		// Reset the monster (since previous is dead)
		generateNewMonster();
		// Set the monster state to safe
		monster.setMonsterState(SAFE_STATE); 
		// Retrieve the monster state 
		monsterState = monster.getMonsterState();
		// Confirm monster state is safe
		assertEquals(SAFE_STATE, monsterState);
		// Attempt to kill the monster in safe state
		monster.killMonster();
		// Confirm that monster isn't dead
		assertFalse(monster.isCancelled());
	}
	
	
}
