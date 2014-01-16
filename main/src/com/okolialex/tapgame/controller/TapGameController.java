package com.okolialex.tapgame.controller;

import static com.okolialex.tapgame.common.Constants.NORMAL_DIFFICULTY;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

import com.okolialex.tapgame.android.R;
import com.okolialex.tapgame.model.ConcreteTapGameModelFacade;
import com.okolialex.tapgame.model.TapGameModelFacade;
import com.okolialex.tapgame.view.TapGameView;

/**
 * This class serves the Controller for the game Model. It alerts the 
 * Model on user touch events stemming from the View. The Model is adjusted
 * accordingly, with changes ultimately reflected back in the View. 
 * 
 * @author Alexander Okoli
 *
 */

public class TapGameController extends Activity {

	/** Sets the game difficulty level. */
	public static final String EXTRA_ASSIGN_DIFFICULTY =  
			"com.okolialex.tapgame.main.TapGameMain.EXTRA_ASSIGN_DIFFICULTY";
	
	/** View declaration (the monster world view.) */
	TapGameView mView;

	/** Model declaration (the monster world model.) */
	TapGameModelFacade mModel;
//	TapGameModel mModel;

	boolean gameStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tapgame_layout);

		/** Initialize view */
		mView = (TapGameView) findViewById(R.id.gamegrid); 

		/** Initialize model */
		mModel = new ConcreteTapGameModelFacade();
	//	mModel = new ConcreteTapGameModel();

		/** Make view listen to model changes */
		mModel.setModelChangeListener(mView);

		/** Loads/sets up view */
		prepareView();
		
		/** Sets the touch listener */
		setViewTouchListener();
	}
	
	
	/**
	 * A view listener is attached to the view, to be alerted on when the view has been completely 
	 * built/rendered. Excluding listener, there would be a blank screen when application
	 * is loaded. This is because the WorldView isn't built instantaneously in
	 * onCreate(), onStart(), or onResume(), so invoking a WorldView method
	 * such as invalidate() in any of these call backs won't display
	 * anything (because getHeight(), getWidth() and other view measurements
	 * are still 0). When the WorldView is built, the listener below
	 * becomes aware of it, and invokes enclosing methods (which then work
	 * because WorldView measurements for drawing tiles are now in place).
	 */
	public void prepareView() {
		
		ViewTreeObserver vto = mView.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			/** Method invoked when the system has built the view */
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {

				// At this point, view is completely built so we have measurements like
				// device height, width etc. available. 
				
				// The model depends on certain view elements (such as screen width and 
				// height) to function. Since view has been built at this point, it passes
				// the elements to the model via this initModelDimensions() function.
				initModelDimensions();
				
			    TextView promptHUD = (TextView) findViewById(R.id.user_prompt_HUD);
			    TextView scoreHUD = (TextView) findViewById(R.id.score_HUD);
				TextView timeHUD = (TextView) findViewById(R.id.time_HUD);
				
				// Since HUDs (TextViews) are built at this point, it is best to set them within the WorldView here.
				// Without this injection, trying to manually create/derive the HUD TextViews from within the 
				// WorldView will require another ViewTreeObserver to build a view within WorldView. Without this
				// new View listener, there will be the same NullPointerException within WorldView as the view hasn't been 
				// completely built, and user is trying to manipulate the HUD TextViews e.g. by setText
			    mView.setHUDViews(timeHUD, scoreHUD, promptHUD);
			    
			    // To display the initial built view
			    mModel.notifyListener();
			    
			    //generateMonsters();
			    
			    // Tells debugger (or any third party) that view has been built
			    setViewBuilt(true); 
		
				ViewTreeObserver obs = mView.getViewTreeObserver();
				// Remove the global view listener, so it does nothing further
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					obs.removeOnGlobalLayoutListener(this);
				} else {
					obs.removeGlobalOnLayoutListener(this);
				}
			}

		});

		
	}
	
	private boolean viewBuilt = false;
	
	public boolean isViewBuilt() {
		return viewBuilt;
	}
	
	public void setViewBuilt(boolean viewBuilt) {
		this.viewBuilt = viewBuilt;
	}
	
	public void setGameDifficulty() {
		// Get intent
		Intent intent = getIntent();
	    // Get difficulty setting from intent. If there is nothing, normal difficulty is selected by default.
	    int gameDifficulty = intent.getIntExtra(TapGameController.EXTRA_ASSIGN_DIFFICULTY, NORMAL_DIFFICULTY);
		
		mModel.setGameDifficulty(gameDifficulty); 
	}
	
	
	public void generateMonsters(){
		
		// The screen dimensions are used to set the number of monsters that will appear
		// on the screen. Below, the view is queried to return the number of monsters that 
		// will fit within 40% of the screen area.
		int numOfMonsters = mView.getNumOfFittableMonsters(0.40);
		
		// Generates monsters within model (40% of the screen area would contain monsters).  
		mModel.generateMonsters(numOfMonsters);
		
	}
	
	/** Sets the touch listener for WorldView */
	public void setViewTouchListener() {
		
    mView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (!gameStarted) {
					
				   generateMonsters();
				   setGameDifficulty();
				   mModel.startGame();
	
				   gameStarted = true;
					
				} else {
	
					int action = event.getAction();
	
					if (action == MotionEvent.ACTION_DOWN) {
						int intX = (int) event.getX();
						int intY = (int) event.getY();
	
						// Converts the Android pixel coordinates into game grid
						// cell coordinates
						mView.setTouchedCellCoords(intX, intY);
						// Game world grid is alerted of the touched cell (world 
						// grid attempts to kill monster located in touched cell)
						mModel.touchCell(mView.getTouchedCellX(), mView.getTouchedCellY());
	
						return true;
					}
				}
				return false;
			}
	
		});
		
	}
	
	/**
	 * This method initializes dimensions (such as width and height)
	 * within the model (monster world).
	 * The model depends on a couple of view components (such as screen 
	 * width and height) in order to function. As soon as the view is 
	 * built, the model will be alerted via this method about the built 
	 * view and it will receive the components it needs to function.
	 */
	public void initModelDimensions() {
		// Sets initial width and height within the game model (WorldModel)
		// based on the game view's (WorldView) measurements of the device screen.
		mModel.setDimensions(mView.getXTileCount(), mView.getYTileCount());
		// Sets the grids that monitor monsters and their movements within the world.
		mModel.initMonsterEntryGrids();
		
	}
	
	/** We need to manipulate view during debugging */
	public TapGameView getView() {
		return mView;
	}
	
	public TapGameModelFacade getModel() {
		return mModel;
	}
	
	public void setModel(TapGameModelFacade model) {
		this.mModel = model;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mModel.stopGame();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mModel.stopGame();
	}
	
}
