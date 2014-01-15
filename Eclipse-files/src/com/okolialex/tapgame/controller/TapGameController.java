package com.okolialex.tapgame.controller;

// TODO Implement difficulty levels.
// TODO Think about Facade. Solely for controller? Separate Facades for diff clients?
// TODO Break world down into smaller interfaces - World (container), Cell, Engine? (start-stop) etc...
/*
 * TODO There is a strange error that occurs whenever user rapidly taps the screen (and touches some monsters
 * in the background) before just as the game start screen ("Tap screen to begin playing.") is showing up. 
 * The error seems to appear after user shuts down the app many times using the "back" button, and restarting it
 * quickly again and tapping the screen to start game. 
 * The logCat error reads (A detailed error log is at bottom on page):
    FATAL EXCEPTION: main
	java.lang.IllegalStateException: Cannot execute task: the task is already running.
	at android.os.AsyncTask.execute(AsyncTask.java:380)
	at com.okolialex.tapgame.model.screen.ConcreteWorldModel.startMonsters(ConcreteWorldModel.java:404)
	at com.okolialex.tapgame.model.screen.ConcreteWorldModel.startGame(ConcreteWorldModel.java:155)
	
	Whenever this error shows up once, trying to start the game following that, will yield the same error for the 
	first few tries, with the entire app freezing.
	
	The error seems to occur randomly after a couple of successful runs. Why does the error even occur? The logcat
	above seems to suggest that some monster AsyncTask object in the background is being executed more than once. How can
	the Async object be executed more than once, when the code has no provisions for this to possibly occur?
	Could it be that once the AsyncTask monsters are created, they are somewhere in memory, and by some system efficiency
	measure, some of those monsters are reused when a new game is started - hence, attempting to execute the monster
	again (Monster.execute()) will trigger the logCat error above?
	Is it something to do with "removeGlobalOnLayoutListener" (one error line jumps to that when clicked)?
	
	Potential cause? During startGame(), a loop, startMonsters(), triggers the Monster.execute() for 
	each monster. Once execute has been called on a monster, it immediately begins to move around the screen.
	In a split second, a monster that has already been executed might jump into the line of fire 
	of the Monster.execute() of the startMonsters() loop, and this will try to trigger ANOTHER execute() of the
	monster. Then we get the logCat error. 
	
	Potential solution: in doInBackground method for each monster, make sure monster receives word from World that it is 
	okay to move i.e. when all monsters in world have had their execute() methods called. <--- THIS SEEMS TO WORK SO FAR!
	
*
*/

import static com.okolialex.tapgame.common.Constants.*;

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
import com.okolialex.tapgame.model.actor.ConcreteMonsterModel;
import com.okolialex.tapgame.model.actor.MonsterModel;
import com.okolialex.tapgame.view.TapGameView;

public class TapGameController extends Activity {

	/** Sets the game difficulty level. */
	public static final String EXTRA_ASSIGN_DIFFICULTY =  
			"com.okolialex.tapgame.main.TapGameMain.EXTRA_ASSIGN_DIFFICULTY";
	
	/** View declaration (the monster world view.) */
	TapGameView mView;

	/** Model declaration (the monster world model.) */
	TapGameModelFacade mModel;

	boolean gameStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tapgame_layout);

		/** Initialize view */
		mView = (TapGameView) findViewById(R.id.gamegrid); 

		/** Initialize model */
		mModel = new ConcreteTapGameModelFacade();

		/** Make view listen to model (monster world) changes */
		mModel.setWorldModelChangeListener(mView);

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
		// Get the message from the intent
	    Intent intent = getIntent();
	    int gameDifficulty = intent.getIntExtra(TapGameController.EXTRA_ASSIGN_DIFFICULTY, NORMAL_DIFFICULTY);
		
		mModel.setGameDifficulty(gameDifficulty); // TODO Implement in test.
	}
	
	
	public void generateMonsters(){
		
		// The screen dimensions are used to set the number of monsters that will appear
		// on the screen. Below, the view is queried to return the number of monsters that 
		// will fit within 40% of the screen area.
		int numOfMonsters = mView.getNumOfFittableMonsters(0.40);
		// Alerts model on the number of monsters that should be generated on the screen
		//mModel.setNumOfMonsters(numOfMonsters);  //TODO Remove!
		
		// Adds monsters to model (40% of the screen area would contain monsters).  // TODO Replace with generateMonster!
		for (int i = 0; i < numOfMonsters; i++) {
			MonsterModel monster = new ConcreteMonsterModel();
			mModel.addMonster(monster);
		}
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
