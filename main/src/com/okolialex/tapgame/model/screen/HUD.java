package com.okolialex.tapgame.model.screen;

/** 
 * Head-up display (HUD) interface. This is responsible for all information,
 * not part of core gameplay (monster manipulation), that will be displayed 
 * on the screen. 
 * Information such as time remaining and player score are computed by
 * this interface, and will ultimately be relayed to the view for display.
 * 
 * @author Alexander Okoli
 */
public interface HUD {
	
	
	/**
	 * A rank scoring system was used for scoring (e.g. "Rank A", "Rank B" etc.)
	 * rather than a number scoring system (e.g. "400 points"), as screen sizes vary 
	 * on devices, and a number scoring system would yield varying results (as level time
	 * and number of monsters varies by screen size). A rank scoring system provides is
	 * based on percentages, so scoring is even across devices.
	 * 
	 * Killing all monsters within 
	 * 100% - 80% of max level time = Rank A
	 * 79% - 60% of max level time = Rank B
	 * 59% - 40% of max level time = Rank C
	 * 39% - 20% of max level time = Rank D
	 * 19% - 0% of max level time = Rank F
	 * 
	 */
	public String getScore();
	
	/** Returns the current game time */
	public int getTime();
	
	/** Sets the game clock */
	public void setTime(int maxLevelTime);
	
	/** Starts the game clock */
	public void startClock();
	
	/** Stops the game clock */
	public void stopClock();
	
	/** Gets the current game mode (running, paused, etc.) */
	public int getMode();
	
	/** Sets the game mode */
	public void setMode(int mode);
	
	/** Decrements clock time */
	void decrementTime();
	
}
