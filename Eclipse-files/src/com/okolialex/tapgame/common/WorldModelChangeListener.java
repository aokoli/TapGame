package com.okolialex.tapgame.common;

import com.okolialex.tapgame.model.screen.WorldModel;

/**
 * A listener for changes coming from the world (changes mainly stem from 
 * monster movement and state).
 * This is usually implemented by the view, which listens to the world's
 * changes and updates the UI accordingly.
 * 
 * @author Alexander Okoli
 *
 */
public interface WorldModelChangeListener {
	public void onWorldChange(WorldModel world);
}
