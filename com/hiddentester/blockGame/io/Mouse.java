/**
 * This class detects mouse events
 */

package com.hiddentester.blockGame.io;

import com.hiddentester.util.Vector2D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
	private Drawing context;

	public Mouse (Drawing context) {
		this.context = context;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println(context.getClickPos(new Vector2D(e.getX(), e.getY())));
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
