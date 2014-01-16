package com.okolialex.tapgame.model.engine;

/**
 * This interface is responsible for controlling the game model,
 * facilitating events such as game start, game end etc.  
 * 
 * @author Alexander Okoli
 *
 */

public interface GamePlayEngine {
	
	/** Begins the game */
	public void startGame();
	
	/** Stops game in progress */
	public void stopGame();
	
	/** Determines if game is over.*/
	public boolean isGameOver();
	
	/** Gets game difficult (normal vs hard) */
	int getGameDifficulty();
	
	/** Sets game difficult (normal vs hard) */
	void setGameDifficulty(int gameDifficulty);

}
