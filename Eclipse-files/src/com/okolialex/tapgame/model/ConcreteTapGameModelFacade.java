package com.okolialex.tapgame.model;
// TODO - Re-think this interface. Can Facade only be customized for one client? (controller in this case).
import com.okolialex.tapgame.common.WorldModelChangeListener;
import com.okolialex.tapgame.model.actor.MonsterModel;
import com.okolialex.tapgame.model.screen.ConcreteWorldModel;
import com.okolialex.tapgame.model.screen.WorldModel;

public class ConcreteTapGameModelFacade implements TapGameModelFacade {
	
	WorldModel worldModel;
	
	public ConcreteTapGameModelFacade() {
		worldModel = new ConcreteWorldModel();
	}
	
	
	@Override
	public void setWorldModelChangeListener(WorldModelChangeListener worldModelChangeListener){
		worldModel.setWorldModelChangeListener(worldModelChangeListener);
	}

	@Override
	public void startGame() {
		worldModel.startGame();
	}

	@Override
	public void touchCell(int touchedCellX, int touchedCellY) {
		worldModel.touchCell(touchedCellX, touchedCellY);
	}

	@Override
	public void notifyListener() {
		worldModel.notifyListener();
	}

	@Override
	public void setDimensions(int width, int height) {
		worldModel.setDimensions(width, height);
	}

	@Override
	public void initMonsterEntryGrids() {
		worldModel.enableAllCellAccess();
		worldModel.initAllMonstersGrid();
	}

	@Override // TODO Remove.
	public void setNumOfMonsters(int numOfMonsters) {
		worldModel.setNumOfMonsters(numOfMonsters);
	}
	
	@Override
	public void addMonster(MonsterModel monster) {
		worldModel.addMonster(monster);
	}

	@Override
	public void stopGame() {
		worldModel.stopGame();
	}


	@Override
	public MonsterModel[][] getAllMonstersGrid() {
		return worldModel.getAllMonstersGrid();
	}


	@Override
	public int getTime() {
		return worldModel.getTime();
	}


	@Override
	public String getScore() {
		return worldModel.getScore();
	}
	
	@Override
	public void setGameDifficulty(int gameDifficulty) {
		worldModel.setGameDifficulty(gameDifficulty);
	}
	
	

}
