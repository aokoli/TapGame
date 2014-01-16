package com.okolialex.tapgame.model;

/** Concrete implementation of the TapGameModelFacade */

import com.okolialex.tapgame.common.ModelChangeListener;
import com.okolialex.tapgame.model.actor.Monster;

public class ConcreteTapGameModelFacade implements TapGameModelFacade {
	
	TapGameModel model;
	
	public ConcreteTapGameModelFacade() {
		model = new ConcreteTapGameModel();
	}
	
	@Override
	public void setModelChangeListener(ModelChangeListener modelChangeListener){
		model.setModelChangeListener(modelChangeListener);
	}

	@Override
	public void startGame() {
		model.startGame();
	}

	@Override
	public void touchCell(int touchedCellX, int touchedCellY) {
		model.touchCell(touchedCellX, touchedCellY);
	}

	@Override
	public void notifyListener() {
		model.notifyListener();
	}

	@Override
	public void setDimensions(int width, int height) {
		model.setDimensions(width, height);
	}

	@Override
	public void initMonsterEntryGrids() {
		model.enableAllCellAccess();
		model.initAllMonstersGrid();
	}

	@Override
	public void addMonster(Monster monster) {
		model.addMonster(monster);
	}

	@Override
	public void stopGame() {
		model.stopGame();
	}


	@Override
	public Monster[][] getAllMonstersGrid() {
		return model.getAllMonstersGrid();
	}


	@Override
	public int getTime() {
		return model.getTime();
	}


	@Override
	public String getScore() {
		return model.getScore();
	}
	
	@Override
	public void setGameDifficulty(int gameDifficulty) {
		model.setGameDifficulty(gameDifficulty);
	}


	@Override
	public void generateMonsters(int numOfMonsters) {
		model.generateMonsters(numOfMonsters);
	}
	
	

}
