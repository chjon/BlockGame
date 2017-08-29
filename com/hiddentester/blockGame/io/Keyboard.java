/**************************************
 * This class detects keyboard events *
 **************************************/

package com.hiddentester.blockGame.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
	private boolean[] keys = new boolean[200];

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	//Check if a certain key is pressed
	public boolean isDown (int keyCode) {
		return keys[keyCode];
	}
}
