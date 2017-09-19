/****************************************
 * This class runs the rendering thread *
 ****************************************/

package com.hiddentester.blockGame.io;

public class DrawingScheduler extends Thread {
	private Drawing draw;

	//Constructor
	public DrawingScheduler (Drawing draw) {
		this.draw = draw;
	}

	@Override
	public void run () {
		while (true) {
			try {
				Thread.sleep(10);
				draw.repaint();
			} catch (Exception e) {

			}
		}
	}
}
