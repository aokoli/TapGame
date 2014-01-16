package com.okolialex.tapgame.common;

import com.okolialex.tapgame.model.TapGameModel;

/**
 * A listener for changes coming from the Model (such as 
 * monster movement and state change).
 * This is usually implemented by the View, which listens to the Model's
 * changes and updates the UI accordingly.
 * 
 * @author Alexander Okoli
 *
 */
public interface ModelChangeListener {
	public void onModelChange(TapGameModel model);
}
