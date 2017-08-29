/*******************************************
 * This class contains the core game logic *
 *******************************************/

package com.hiddentester.blockGame.core;

import com.hiddentester.math.Vector2D;
import com.hiddentester.blockGame.entities.Player;
import com.hiddentester.blockGame.io.*;

public class Game {
	private final int LOADED_RADIUS = 2;	//"Radius" of square of chunks to load around the player
	private Chunk[][] loadedChunks;
	private Player player;
	private Window window;
	private Keyboard keyboard;
	private Mouse mouse;

	//Constructor
	public Game () {
		loadedChunks = new Chunk[2 * LOADED_RADIUS - 1][2 * LOADED_RADIUS - 1];

		//Create player
		player = new Player(
				this,
				Chunk.loadChunk(new Vector2D(0, 0)),
				new Vector2D(0, 0),
				new Vector2D()
		);

		loadChunks();

		//Set up input/output:
		Drawing draw = new Drawing(this);
		draw.setFocusable(true);
		keyboard = new Keyboard();
		mouse = new Mouse(draw);
		draw.addKeyListener(keyboard);
		draw.addMouseListener(mouse);

		DrawingScheduler drawScheduler = new DrawingScheduler(draw);
		drawScheduler.start();

		window = new Window(draw);
	}

	//Accessors:

	public Chunk[][] getLoadedChunks() {
		return loadedChunks;
	}

	public Player getPlayer() {
		return player;
	}

	public Window getWindow() {
		return window;
	}

	public Keyboard getKeyboard() {
		return keyboard;
	}

	public Mouse getMouse() {
		return mouse;
	}

	//Load chunks that need to be loaded
	public void loadChunks () {
		Vector2D playerPos = player.getContainingChunk().getPos();

		//Loop through chunks
		for (int i = 0; i < loadedChunks.length; i++) {
			for (int j = 0; j < loadedChunks[i].length; j++) {
				//Check if chunk needs to be loaded
				if (loadedChunks[i][j] == null) {
					loadedChunks[i][j] = Chunk.loadChunk(Vector2D.sub(playerPos, new Vector2D(
							i - LOADED_RADIUS + 1,
							j - LOADED_RADIUS + 1))
					);
				}
			}
		}
	}

	//Update array of chunks that are loaded
	public void updateChunks() {
		Vector2D playerPos = player.getContainingChunk().getPos();
		Vector2D shift = Vector2D.sub(playerPos, loadedChunks[LOADED_RADIUS - 1][LOADED_RADIUS - 1].getPos());
		Chunk[][] temp = new Chunk[loadedChunks.length][loadedChunks[0].length];

		//Loop through loaded chunks and unload those that need to be unloaded
		for (int i = 0; i < loadedChunks.length; i++) {
			for (int j = 0; j < loadedChunks[i].length; j++) {
				Vector2D destPos = Vector2D.add(new Vector2D(i, j), shift);

				//Shift chunk
				if (destPos.getMagX() >= 0 && destPos.getMagX() < loadedChunks.length &&
						destPos.getMagY() >= 0 && destPos.getMagY() < loadedChunks.length) {
					temp[(int)destPos.getMagX()][(int)destPos.getMagY()] = loadedChunks[i][j];
				//Unload chunk
				} else {
					loadedChunks[i][j].unload();
				}
			}
		}

		loadedChunks = temp;

		//Load chunks that need to be loaded
		loadChunks();
	}
}
