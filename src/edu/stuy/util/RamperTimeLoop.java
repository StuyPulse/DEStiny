package edu.stuy.util;

import java.util.LinkedList;

/** RamperTimeLoop.java
 * 
 * This goes through a loop that's at a regular time interval
 * It stores an expandable list of rampers
 *
 */

public class RamperTimeLoop {
	private boolean running = false;
	Thread thread;

	private LinkedList<Ramper> list = new LinkedList<Ramper>();

	public void start() {
		running = true;
		thread = new Thread() {
			public void run() {
				loop();
			}
		};
		thread.start();
	}

	public void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			thread.interrupt();
			e.printStackTrace();
		}
	}

	private void loop() {
		final double FPS = 48.0;
		//  1 / (24 / 0.5)

		final double UPDATE_TIME = 1000000000 / FPS;
		double lastTime = System.nanoTime();
		
		while (running) {
			double now = System.nanoTime();
			
			// Plays "catch up".
			// If it skips too many frames, it 
			// compensates by updating.
			while(now - lastTime > UPDATE_TIME) {
				update();
				lastTime += UPDATE_TIME;
			}
			
			// If in the process of playing "catch up"
			if (now - lastTime > UPDATE_TIME) {
				lastTime = now - UPDATE_TIME;
			}
		}
	}

	private void update() {
		for(int i = 0; i < list.size(); i++) {
			list.get(i).update();
		}
	}

	public void addRamper(Ramper ramper) {
		list.add(ramper);
	}

	public void removeRamper(Ramper ramper) {
		list.remove(ramper);
	}
}