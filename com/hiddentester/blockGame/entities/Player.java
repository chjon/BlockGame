/*******************************
 * This class defines a player *
 *******************************/

package com.hiddentester.blockGame.entities;

import com.hiddentester.math.Vector2D;
import com.hiddentester.blockGame.core.Game;
import com.hiddentester.blockGame.core.Chunk;

public class Player extends Entity {
	private static final Vector2D DIMENSIONS = new Vector2D(0.6, 1.8);
	public static final double WALK_SPEED = 0.03;

	private Game game;

	//Constructor:

	public Player (Game game, Chunk containingChunk, Vector2D relPos, Vector2D vel) {
		super(containingChunk, relPos, vel, DIMENSIONS);
		this.game = game;
	}

	//Custom accessor
	@Override
	public void setAbsPos (Vector2D pos) {
		Chunk original = containingChunk;

		super.setAbsPos(pos);

		//Update loaded chunks if player has moved to a new chunk
		if (!containingChunk.getPos().equals(original.getPos())) {
			game.updateChunks();
		}
		//System.out.println("PLAYER");
	}
}
