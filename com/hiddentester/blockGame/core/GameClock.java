/***********************************************
 * This class runs the main clock for the game *
 ***********************************************/

package com.hiddentester.blockGame.core;

public class GameClock extends Thread {
	public static final int MILLIS_IN_SECOND = 1000;
	public static final int CHUNK_TICKS_PER_SECOND = 10;
	public static final int ENTITY_TICKS_PER_SECOND = 100;
	public static final int CHUNK_TICK_LENGTH = MILLIS_IN_SECOND / CHUNK_TICKS_PER_SECOND;
	public static final int ENTITY_TICK_LENGTH = MILLIS_IN_SECOND / ENTITY_TICKS_PER_SECOND;

	private Game game;

	//Constructor
	GameClock (Game game) {
		this.game = game;
	}

	@Override
	public void run () {
		long nextBlockTick = System.currentTimeMillis();
		long nextEntityTick = System.currentTimeMillis();

		while (true) {
			//Update chunks
			if (System.currentTimeMillis() >= nextBlockTick) {
				game.updateChunks();
				nextBlockTick += CHUNK_TICK_LENGTH;
			}

			//Update entities
			if (System.currentTimeMillis() >= nextEntityTick) {
				game.updateEntities();
				nextEntityTick += ENTITY_TICK_LENGTH;
			}
		}
	}
}
