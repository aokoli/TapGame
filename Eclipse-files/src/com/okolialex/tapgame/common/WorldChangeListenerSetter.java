package com.okolialex.tapgame.common;


/**
 * This injects a <code> WorldChangelistener </code> - a listener for world changes 
 * (monster movement and state changes).
 * This interface is usually implemented by the model, with the view as the listener.
 * 
 * @author Alexander Okoli
 *
 */
public interface WorldChangeListenerSetter {
	
	public void setWorldModelChangeListener(WorldModelChangeListener worldModelChangeListener);

}
