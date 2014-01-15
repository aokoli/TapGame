package com.okolialex.tapgame.common;

import java.util.Random;

public class AutoRandomEngine implements Runnable {
	private Runnable task;
	private Boolean stopped = false;
	private final int MODIFIED_MAX_TIME;
	private int minTime;
	private int variableTime;
	private Random random = new Random(); 
	
	public AutoRandomEngine(Runnable task, int minTime, int maxTime) {
		this.task = task;
		this.minTime = minTime;
		this.MODIFIED_MAX_TIME = maxTime - minTime; // Subtraction to accommodate minTime padding in timeChange()
	}
	
	@Override
	public void run() {
		
		while(!stopped) {
			timeChange();
			// System.out.println("TIME in ENGINE: " + variableTime); // TODO Remove. Debugging tool.
			task.run();
			try {
				Thread.sleep(variableTime);
			} catch (InterruptedException e) {
			//	System.out.println("Caught in AutoEngine.");
			}
		}
	}
	
	private void timeChange() {
		variableTime = minTime + (random.nextInt(MODIFIED_MAX_TIME));
	}

	public void cancel() {
		stopped = true;
	}
}
