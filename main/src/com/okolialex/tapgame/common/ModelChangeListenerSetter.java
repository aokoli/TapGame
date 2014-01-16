package com.okolialex.tapgame.common;


/**
 * This injects a <code> ModelChangelistener </code> - a listener for model changes 
 * (such as monster movement and state changes).
 * This interface is usually implemented by the model, with the view as the listener.
 * 
 * @author Alexander Okoli
 *
 */
public interface ModelChangeListenerSetter {
	
	public void setModelChangeListener(ModelChangeListener modelChangeListener);

}
