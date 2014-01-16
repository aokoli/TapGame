package com.okolialex.tapgame.view;

import static com.okolialex.tapgame.common.Constants.*;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.TextView;

import com.okolialex.tapgame.android.R;
import com.okolialex.tapgame.common.ModelChangeListener;
import com.okolialex.tapgame.model.TapGameModel;

/**
 * This class serves as the View of the application. The View receives information 
 * from the Model, and graphically displays this on the screen. 
 * The View renders the playable area where the monster can travel, the monsters 
 * themselves, and HUD items. 
 * 
 * @author Alexander Okoli
 */													
public class TapGameView extends TileView implements ModelChangeListener {  
	
    /**  
     * Constructs a monster world based on inflation from XML
     * 
     * @param context
     * @param attrs
     */
    public TapGameView (Context context, AttributeSet attrs) {
        super(context, attrs);
        initIcons();
    }

    public TapGameView (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initIcons();
    }
    
    /**
     * Provides the number of monsters that can fit in the game grid, within
     * the specified screen % area. 
     * 
     * @param screenPercentage - The % area of the screen that monsters will occupy. E.g.
     * a 0.40 screenPercentage will populate 40% of the game grid with monsters.
     * @return The number of monsters that will be on the screen
     * 
     */
    public int getNumOfFittableMonsters(double screenPercentage) {
    	return (int) ((screenPercentage) * (mXTileCount * mYTileCount));
	}

    private void initIcons() {  

        setFocusable(true);

        Resources r = this.getContext().getResources();

        resetTiles(NUM_OF_ICONS); // TODO <-- Necessary to put NUM_OF_ICONS in Constants?
        loadTile(VULNERABLE_MONSTER, r.getDrawable(R.drawable.monster_nonblock));
        loadTile(SAFE_MONSTER, r.getDrawable(R.drawable.monster_block)); 
    }

    /** Holds a reference to the UI HUDs that need to be updated */
    TextView timeHUD;
    TextView scoreHUD;
    TextView promptHUD;
    
    /** Sets the HUDs (TextViews) that were built within when the Controller loaded up */
    public void setHUDViews(TextView timerHUD, TextView scoreHUD, TextView promptHUD) {
    	this.timeHUD = timerHUD;
    	this.scoreHUD = scoreHUD;
    	this.promptHUD = promptHUD;
    }
    
    
    private int currentGameTime;
    private int currentGameMode;
    private String currentScore;
    
    
	@Override
	public void onModelChange(final TapGameModel model) {
		// Gets everything (location of all icons) that is 
		// currently displayed in the monster world
    	mTileGrid = model.getModelScreen(); 
    	
    	// Gets the appropriate screen measurement of the monster world
    	// (incase it was manually altered e.g. during debugging).
    	mXTileCount = model.getWidth();
    	mYTileCount = model.getHeight();
    	
    	// Sets the current game time 
    	currentGameTime = model.getTime();
    	
    	// Sets the current game mode
    	currentGameMode = model.getMode();
    	
    	// Sets the current game score
    	currentScore = model.getScore();
    	
    	// Sets the current values derived from model in their respective
    	// HUD locations within the UI.
    	// Uses post(...) to set text in UI, as caller is background
    	// thread (worldModel). Not using post(...) yields error.
        post(new Runnable() { 
    	    public void run() {
    	        
    	        if (currentGameMode == RUNNING) {
        	    	promptHUD.setVisibility(INVISIBLE); 
        	    	timeHUD.setText("Time: " + currentGameTime);
        	    	scoreHUD.setText("Score: " + currentScore);
    	        }
    	        if (currentGameMode == PAUSED) {
    	        	promptHUD.setText("Tap screen to begin playing."); 
    	        	// setFocusable(false);
    	        	promptHUD.setVisibility(VISIBLE);  
    	        }
    	        if (currentGameMode == STOPPED) {
    	        	promptHUD.setText("Your rank is " + currentScore + ".\nGame Over!");
    	        	timeHUD.setVisibility(INVISIBLE);
    	        	scoreHUD.setVisibility(INVISIBLE);
    	        	promptHUD.setVisibility(VISIBLE);
    	        }
    	    } 
    	});
    	
    	// Draws all the monster world icons in the UI
    	postInvalidate(); 
	}

	

    
}
