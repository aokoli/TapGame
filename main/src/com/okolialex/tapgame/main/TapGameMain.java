package com.okolialex.tapgame.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.okolialex.tapgame.android.R;
import com.okolialex.tapgame.android.R.id;
import com.okolialex.tapgame.android.R.layout;
import com.okolialex.tapgame.android.R.menu;
import com.okolialex.tapgame.controller.TapGameController;

import static com.okolialex.tapgame.common.Constants.*;

/**
 * The game begins from this class. Presents "Welcome" screen
 * and provides an option for players to pick either normal or 
 * hard game setting.
 * 
 * @author Alexander Okoli
 *
 */

public class TapGameMain extends Activity {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.intro_screen);
			
			findViewById(R.id.button_normal_game).setOnClickListener(
	                new OnClickListener() {
	            public void onClick(View v) {
	                startGame(true);
	            }
	        });
	
	        findViewById(R.id.button_hard_game).setOnClickListener(
	                new OnClickListener() {
	            public void onClick(View v) {
	                startGame(false);
	            }
	        });
	    }

	private void startGame(boolean startNormalDifficultyGame) {
	    Intent i = new Intent(this, TapGameController.class);
	    i.putExtra(TapGameController.EXTRA_ASSIGN_DIFFICULTY,
	            startNormalDifficultyGame ? NORMAL_DIFFICULTY : HARD_DIFFICULTY);
	    startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intro_screen, menu);
		return true;
	}

}
