package com.okolialex.tapgame.model;

import com.okolialex.tapgame.common.WorldModelChangeListener;
import com.okolialex.tapgame.model.actor.MonsterModel;

/**
 * A model facade. Following the Facade pattern,
 * this isolates the complexity of the WorldModel from its client 
 * (the controller in the TapeGame implementation).
 *
 * @author Alexander Okoli
 */

public interface TapGameModelFacade {
	void setWorldModelChangeListener(WorldModelChangeListener worldModelChangeListener);
	void initMonsterEntryGrids(); 	// cell access & monster
	void startGame();
	void touchCell(int touchedCellX, int touchedCellY);
	void notifyListener();
	void setDimensions(int width, int height);
	void setNumOfMonsters(int numOfMonsters);
	void addMonster(MonsterModel monster);  // TODO Replace with generateMons...
	void stopGame();
	MonsterModel[][] getAllMonstersGrid();
	int getTime();
	String getScore();
	void setGameDifficulty(int gameDifficulty);
}
