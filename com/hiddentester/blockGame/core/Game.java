/*******************************************
 * This class contains the core game logic *
 *******************************************/

package com.hiddentester.blockGame.core;

import com.hiddentester.blockGame.blocks.instantiable.*;
import com.hiddentester.blockGame.entities.Entity;
import com.hiddentester.util.IntVector;
import com.hiddentester.util.Vector2D;
import com.hiddentester.blockGame.entities.Player;
import com.hiddentester.blockGame.io.*;

import java.io.File;
import java.util.ArrayList;

public class Game {
	private static final String BLOCK_DIRECTORY =				//Directory of list of blocks
			"BlockGame/com/hiddentester/blockGame/blocks/instantiable";

	//References to other core objects
	private ChunkLoader chunkLoader;
	private Window window;
	private Keyboard keyboard;
	private Mouse mouse;

	private Player player;
	private ArrayList<Entity> entities;
	private String[] blockNames;

	//Constructor
	Game (String saveFile) {
		assignBlockIDs(BLOCK_DIRECTORY);

		GameClock gameClock = new GameClock(this);
		chunkLoader = new ChunkLoader(saveFile, this);
		entities = new ArrayList<Entity>();

		//Create player
		player = new Player(
				this,
				new IntVector(0, 0),
				new Vector2D(2, 2),
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

	public String[] getBlockNames () {
		return blockNames;
	}

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

	//Assign every block a numerical ID
	private void assignBlockIDs (String folderName) {
		//Get all files in directory

		File folder = new File( folderName + "/");
		File[] files = folder.listFiles();

		//Empty array if block files are not found
		if (files == null) {
			blockNames = new String[0];
		} else {
			//Create class list
			blockNames = new String[files.length];

			blockNames[0] = Block_Air	.CLASS_NAME;
			blockNames[1] = Block_Stone	.CLASS_NAME;
			blockNames[2] = Block_Dirt	.CLASS_NAME;
		}
	}
}
