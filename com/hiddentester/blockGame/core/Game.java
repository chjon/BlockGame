/*******************************************
 * This class contains the core game logic *
 *******************************************/

package com.hiddentester.blockGame.core;

import com.hiddentester.blockGame.entities.Entity;
import com.hiddentester.util.IntVector;
import com.hiddentester.util.Vector2D;
import com.hiddentester.blockGame.entities.Player;
import com.hiddentester.blockGame.io.*;

import java.util.ArrayList;

public class Game {
	private ChunkLoader chunkLoader;
	private ArrayList<Entity> entities;
	private Player player;
	private Window window;
	private Keyboard keyboard;
	private Mouse mouse;

	//Constructor
	Game (String saveFile) {
		GameClock gameClock = new GameClock(this);
		chunkLoader = new ChunkLoader(saveFile, this);
		entities = new ArrayList<Entity>();

		//Create player
		player = new Player(
				this,
				new IntVector(0, 0),
				new Vector2D(2, 1),
				new Vector2D(),
				chunkLoader
		);

		entities.add(player);

		chunkLoader.loadChunks();

		//Set up input/output:
		Drawing draw = new Drawing(this);
		draw.setFocusable(true);
		keyboard = new Keyboard();
		mouse = new Mouse(draw);
		draw.addKeyListener(keyboard);
		draw.addMouseListener(mouse);

		DrawingScheduler drawScheduler = new DrawingScheduler(draw);
		drawScheduler.start();

		window = new Window(saveFile, draw);

		//Start game
		gameClock.start();
	}

	//Accessors:

	public ChunkLoader getChunkLoader() {
		return chunkLoader;
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

	//Update loaded chunks if player has moved to a new chunk
	void updateChunks () {
		if (!player.getChunkPos().equals(
				chunkLoader.getLoadedChunks()[ChunkLoader.LOADED_RADIUS - 1][ChunkLoader.LOADED_RADIUS - 1].getPos())) {
			chunkLoader.updateChunks();
		}
	}

	//Update states of entities
	void updateEntities () {
		for (Entity e: entities) {
			e.move();
		}
	}
}
