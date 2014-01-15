package com.okolialex.tapgame.controller;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/** 
 * This class is the same as {@code TapGameController} except it has been modified 
 * slightly for testing purposes. The base {@code TapGameController} automatically
 * generates monsters and adds them to the model. For testing purposes, the addition
 * of monsters has to be manual; hence, the need for this class which excludes the
 * automatic generation of monsters. 
 *
 * @author Alexander Okoli
 */
public class ModifiedTapGameController extends TapGameController {

	@Override
	public void setViewTouchListener() {
		
		mView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (!gameStarted) {
					
					/*
					 * In comparison to the base class, no monsters are added at this point
					 * of the code.
					 * This is the only major change from the base class.
					 */
					
					mModel.startGame();
					gameStarted = true;
					
				} else {
					int action = event.getAction();
					if (action == MotionEvent.ACTION_DOWN) {
						int intX = (int) event.getX();
						int intY = (int) event.getY();
						mView.setTouchedCellCoords(intX, intY);
						mModel.touchCell(mView.getTouchedCellX(), mView.getTouchedCellY());
						return true;
					}
				}
				return false;
			}

		});
	}
	
	
	
}


