package com.okolialex.tapgame.model;

import com.okolialex.tapgame.common.ModelChangeListenerSetter;
import com.okolialex.tapgame.model.engine.GamePlayEngine;
import com.okolialex.tapgame.model.screen.HUD;
import com.okolialex.tapgame.model.screen.MonsterCellWorld;


/**
 * TapGameModel is the main Model of the application, performing all game 
 * activities/computations (monster manipulation, time calculation etc.). It is 
 * manipulated by the Controller (e.g. a touch event to kill a monster), and it 
 * provides information on its changes (e.g. monster movement) for the View to display.
 * 
 * This interface serves as a "glue" for the 2 major model components:
 * Monster and MonsterCellWorld. The Monsters are actors which move around freely
 * within the MonsterCellWorld. The MonsterCellWorld is a grid of cells which houses 
 * these independent Monsters, and prohibits them from colliding with each other 
 * or with the MonsterCellWorld's borders. When the user touches the screen, events 
 * are sent to a cell within the MonsterCellWorld. If the cell is occupied by a Monster,
 * the Monster dies. If the cell is empty, there would be no effect. This interface
 * glues Monster and MonsterCellWorld, as it just appears to be one "Model" to its clients,
 * the View and the Controller. For instance, the Controller doesn't have to know the 
 * inner workings, of this Model. All is has to do is call generateMonsters(...) and 
 * within the Model, monsters will populate the monster world.   
 * 
 * This interface also serves as model for any visual component a user will ultimately 
 * see, such as the player score or time remaining ("HUD" displays). Depending on game 
 * mode, this model will alert the View on which HUD to display. 
 * 
 * Lastly, this interface facilitates gameplay (start game, stop game, difficulty setting
 * adjustment etc.) 
 * 
 * @author Alexander Okoli
 *
 */
		
public interface TapGameModel extends MonsterCellWorld, HUD, GamePlayEngine, ModelChangeListenerSetter{
	
	 /** Alerts the listener (View) that the model has changed */
    public void notifyListener();
    
    
    /** Generates the specified number of monsters within the MonsterCellWorld
     * This model is triggered when the View has gathered how many 
     * monsters can fit on the screen (device-dependent).
     */
    public void generateMonsters(int numOfMonsters);
    
    /** 
     * Returns the game grid with all the drawable cell icons mapped
     * using ints. The View uses the returned grid for graphical rendering 
     * of the model in the UI. 
     */
    public int[][] getModelScreen();
    
    /** Sets the desired image on the specified coordinate */
    public void setIcon(int icon, int x, int y);
   // public void setTile(int cellImage, int x, int y);
		
	    
	    
}
