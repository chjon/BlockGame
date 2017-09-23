/**
 * This class detects mouse events
 */

package com.hiddentester.blockGame.io;

import com.hiddentester.blockGame.blocks.instantiable.Block_Air;
import com.hiddentester.blockGame.core.Game;
import com.hiddentester.util.IntVector;
import com.hiddentester.util.Vector2D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
	private Game game;
	private Drawing context;

	public Mouse (Game game, Drawing context) {
		this.game = game;
		this.context = context;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		Vector2D clickPos = new Vector2D(e.getX(), e.getY());
		IntVector chunkPos = context.getChunkPosFromMouse(clickPos);
		Vector2D relPos = context.getRelPosFromMouse(clickPos);

		game.getChunkLoader().setBlock(chunkPos, relPos, new Block_Air());
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
