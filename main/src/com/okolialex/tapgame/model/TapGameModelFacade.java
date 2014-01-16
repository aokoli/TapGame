package com.okolialex.tapgame.model;

import com.okolialex.tapgame.common.ModelChangeListener;
import com.okolialex.tapgame.model.actor.Monster;

/**
 * A model facade. Following the Facade pattern,
 * this isolates the complexity of the model from its client 
 * (the Controller in the TapeGame implementation).
 *
 * @author Alexander Okoli
 */

public interface TapGameModelFacade {
	void setModelChangeListener(ModelChangeListener modelChangeListener);
	/** This initializes the grids responsible for the monsters'
	 * entry into the monster cell world. */
	void initMonsterEntryGrids(); 	
	void startGame();
	void touchCell(int touchedCellX, int touchedCellY);
	void notifyListener();
	void setDimensions(int width, int height);
	void addMonster(Monster monster); 
	void generateMonsters(int numOfMonsters);
	void stopGame();
	Monster[][] getAllMonstersGrid();
	int getTime();
	String getScore();
	void setGameDifficulty(int gameDifficulty);
}
