package com.okolialex.tapgame.model.actor;

import android.os.AsyncTask;

import com.okolialex.tapgame.common.Coordinate;
import com.okolialex.tapgame.model.TapGameModel;

public abstract class Monster extends AsyncTask<Void, Void, Void> {

	/** Gets the monster's current state */
	public abstract int getMonsterState();
	
	/** Sets the monster's state. Alternates between Vulnerable and Safe */
	public abstract void changeMonsterState();
	
	/** Sets monster's x,y coordinates */
	public abstract void setMonsterLocation(int x, int y);
	
	/** Get the monster's x,y coordinate (saves lines during debugging). */
	public abstract Coordinate getMonsterLocation();

	/** Set monster's x location */
	public abstract void setX(int x);

	/** Set monster's y location */
	public abstract void setY(int y);

	/** Get monster's x location */
	public abstract int getX();

	/** Get monster's y location */
	public abstract int getY();
	
	/* Set the monster's x,y coordinate (saves lines during debugging). */
	//public abstract void setCoordinate(Coordinate coordinate); TODO Dangerous to use in some locations e.g. startGame() loop

	/** Get x location monster is attempting to move to */
	public abstract int getPotentialX();

	/** Get y location monster is attempting to move to */
	public abstract int getPotentialY();

	/** Kills monster */
	public abstract void killMonster();
	
	/** Alters the monster's state (safe vs vulnerable) periodically */ 
	protected abstract void initMonsterStateManager();
	
	/** Manages the timing of the monster's movement  */
	protected abstract void initMonsterMovementManager();
	
	
	/** 
	 *  Setting TapGameModel within Monster has 2 purposes. It,
	 * 
	 * - Provides the monster with a view of the monster world (so monster 
	 *   can 'see' where its going) .
	 * - Provides the monster with difficulty setting information.
	 * 
	 *  TapGameModel contains MonsterCellWorld, and this is where the monster
	 *  will actually be traveling in. TapGameModel also contains difficulty
	 *  setting information (normal vs hard); hence, it is set here.
	 *  */
	public abstract void setTapGameModel (TapGameModel model);
    

	/**
	 * Sets the new x,y location for a potential move.
	 * 
	 * @param direction - monster's direction of travel.
	 * @return the new coordinate as a result of travel. If there
	 * is no new location to travel to, the old location is returned.
	 */
	public abstract Coordinate moveTo(int direction);

	/** 
	 * Manually sets the monster's state, mainly used for debugging.
	 * Necessary to set state manually as monster is initialized with random state,
	 * so this gives control over state during debugging. 
	 */
	public abstract void setMonsterState(int monsterState);

	/** Confirms whether monster is alive or dead (mainly for debugging). */
	public abstract boolean isDead();

	/** Sets a monster's alive or dead state (mainly for debugging). */
	public abstract void setDead(boolean dead);
}