/***************************************
 * This class loads and unloads chunks *
 ***************************************/

package com.hiddentester.blockGame.core;

import com.hiddentester.blockGame.blocks.Block;
import com.hiddentester.blockGame.blocks.Block_Air;
import com.hiddentester.blockGame.blocks.Block_Stone;
import com.hiddentester.util.ChunkPosVector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class ChunkLoader {
	private static final String SAVE_DIRECTORY = "BlockGame/saves";
	private static final String CHUNK_DIRECTORY = "chunks";
	static final int LOADED_RADIUS = 2;				//"Radius" of square of chunks to load around the player
	private String saveFile;
	private Chunk[][] loadedChunks;
	private Game game;

	//Constructor
	ChunkLoader (String saveFile, Game game) {
		this.saveFile = saveFile;
		this.game = game;
		loadedChunks = new Chunk[2 * LOADED_RADIUS - 1][2 * LOADED_RADIUS - 1];
	}

	//Accessor
	public Chunk[][] getLoadedChunks() {
		return loadedChunks;
	}

	//Load chunks that need to be loaded
	void loadChunks () {
		ChunkPosVector playerPos = game.getPlayer().getChunkPos();

		//Loop through chunks
		for (int i = 0; i < loadedChunks.length; i++) {
			for (int j = 0; j < loadedChunks[i].length; j++) {
				//Check if chunk needs to be loaded
				if (loadedChunks[i][j] == null) {
					loadedChunks[i][j] = load(new ChunkPosVector(
							playerPos.getMagX() - (i - LOADED_RADIUS + 1),
							playerPos.getMagY() - (j - LOADED_RADIUS + 1))
					);
				}
			}
		}
	}

	//Update array of chunks that are loaded
	void updateChunks() {
		ChunkPosVector playerPos = game.getPlayer().getChunkPos();
		ChunkPosVector centrePos = loadedChunks[LOADED_RADIUS - 1][LOADED_RADIUS - 1].getPos();

		ChunkPosVector shift = ChunkPosVector.sub(playerPos, centrePos);

		Chunk[][] temp = new Chunk[loadedChunks.length][loadedChunks[0].length];

		//Loop through loaded chunks and unload those that need to be unloaded
		for (int i = 0; i < loadedChunks.length; i++) {
			for (int j = 0; j < loadedChunks[i].length; j++) {
				ChunkPosVector destPos = ChunkPosVector.add(new ChunkPosVector(i, j), shift);

				//Shift chunk
				if (destPos.getMagX() >= 0 && destPos.getMagX() < loadedChunks.length &&
						destPos.getMagY() >= 0 && destPos.getMagY() < loadedChunks.length) {
					temp[destPos.getMagX()][destPos.getMagY()] = loadedChunks[i][j];
				//Unload chunk
				} else {
					save(loadedChunks[i][j]);
				}
			}
		}

		loadedChunks = temp;

		//Load chunks that need to be loaded
		loadChunks();
	}

	//Load chunk
	private Chunk load (ChunkPosVector pos) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					SAVE_DIRECTORY + "/" + saveFile + "/" + CHUNK_DIRECTORY + "/" +
							pos.getMagX() + " " + pos.getMagY() + ".txt"
					)
			);

			Chunk loadedChunk = new Chunk(pos);

			for (int i = 0; i < loadedChunk.blocks.length; i++) {
				String[] data = in.readLine().split(",");

				for (int j = 0; j < loadedChunk.blocks[i].length; j++) {
					if (data[j].equals("Block_Air")) {
						loadedChunk.blocks[i][j] = new Block_Air();
					} else if (data[j].equals("Block_Stone")) {
						loadedChunk.blocks[i][j] = new Block_Stone();
					}
				}
			}

			in.close();

			System.out.println("Successfully loaded chunk at: " + pos);

			return loadedChunk;
		} catch (java.io.FileNotFoundException e) {
			System.out.println("Created chunk at: " + pos);

			return new Chunk(pos);
		} catch (java.io.IOException e) {
			e.printStackTrace();
			System.out.println("Created chunk at: " + pos);

			return new Chunk(pos);
		}
	}

	//Save chunk to file
	private void save (Chunk chunk) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					SAVE_DIRECTORY + "/" + saveFile + "/" + CHUNK_DIRECTORY + "/" +
							chunk.getPos().getMagX() + " " + chunk.getPos().getMagY() + ".txt",
					false)
			);

			Block[][] blocks = chunk.getBlocks();

			for (int i = 0; i < blocks.length; i++) {
				for (int j = 0; j < blocks[i].length; j++) {
					out.write("Block_" + blocks[i][j].getName() + ",");
				}

				out.newLine();
			}

			out.close();

			System.out.println("Successfully saved chunk at: " + chunk.getPos());
		} catch (java.io.IOException e) {
			System.out.println("Failed to save chunk at: " + chunk.getPos());
		}
	}
}
