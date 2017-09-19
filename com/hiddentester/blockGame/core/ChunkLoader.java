/***************************************
 * This class loads and unloads chunks *
 ***************************************/

package com.hiddentester.blockGame.core;

import com.hiddentester.blockGame.blocks.Block;
import com.hiddentester.blockGame.blocks.instantiable.Block_Air;
import com.hiddentester.blockGame.blocks.instantiable.Block_Dirt;
import com.hiddentester.blockGame.blocks.instantiable.Block_Stone;
import com.hiddentester.util.IntVector;

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
		IntVector playerPos = game.getPlayer().getChunkPos();

		//Loop through chunks
		for (int i = 0; i < loadedChunks.length; i++) {
			for (int j = 0; j < loadedChunks[i].length; j++) {
				//Check if chunk needs to be loaded
				if (loadedChunks[i][j] == null) {
					loadedChunks[i][j] = load(new IntVector(
							playerPos.getMagX() - (i - LOADED_RADIUS + 1),
							playerPos.getMagY() - (j - LOADED_RADIUS + 1))
					);
				}
			}
		}
	}

	//Update array of chunks that are loaded
	void updateChunks() {
		IntVector playerPos = game.getPlayer().getChunkPos();
		IntVector centrePos = loadedChunks[LOADED_RADIUS - 1][LOADED_RADIUS - 1].getPos();

		IntVector shift = IntVector.sub(playerPos, centrePos);

		Chunk[][] temp = new Chunk[loadedChunks.length][loadedChunks[0].length];

		//Loop through loaded chunks and unload those that need to be unloaded
		for (int i = 0; i < loadedChunks.length; i++) {
			for (int j = 0; j < loadedChunks[i].length; j++) {
				IntVector destPos = IntVector.add(new IntVector(i, j), shift);

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
	private Chunk load (IntVector pos) {
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
					if (data[j].equals(Block_Air.BLOCK_ID + "")) {
						loadedChunk.blocks[i][j] = new Block_Air();
					} else if (data[j].equals(Block_Stone.BLOCK_ID + "")) {
						loadedChunk.blocks[i][j] = new Block_Stone();
					} else if (data[j].equals(Block_Dirt.BLOCK_ID + "")) {
						loadedChunk.blocks[i][j] = new Block_Dirt();
					}
				}
			}

			in.close();

			return loadedChunk;
		} catch (java.io.FileNotFoundException e) {
			return new Chunk(pos);
		} catch (java.io.IOException e) {
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
					out.write(blocks[i][j].getBlockID() + ",");
				}

				out.newLine();
			}

			out.close();
		} catch (java.io.IOException e) {
			System.out.println("Failed to save chunk at: " + chunk.getPos());
		}
	}

	//Get array of chunks contained within a specified region
	private Chunk[][] getChunks (IntVector originalChunkPos1, IntVector originalChunkPos2) {
		IntVector chunkPos1 = new IntVector(originalChunkPos1);
		IntVector chunkPos2 = new IntVector(originalChunkPos2);

		//Make sure chunkPos1 is on the left and chunkPos2 is on the right
		if (chunkPos1.getMagX() > chunkPos2.getMagX()) {
			int temp = chunkPos1.getMagX();
			chunkPos1.setMagX(chunkPos2.getMagX());
			chunkPos2.setMagX(temp);
		}

		//Make sure chunkPos1 is on the bottom and chunkPos2 is on top
		if (chunkPos1.getMagY() > chunkPos2.getMagY()) {
			int temp = chunkPos1.getMagY();
			chunkPos1.setMagY(chunkPos2.getMagY());
			chunkPos2.setMagY(temp);
		}

		Chunk[][] chunks = new Chunk
				[Math.abs(chunkPos1.getMagX() - chunkPos2.getMagX()) + 1]
				[Math.abs(chunkPos1.getMagY() - chunkPos2.getMagY()) + 1];

		//Get each chunk in the region
		for (int i = 0; i < chunks.length; i++) {
			for (int j = 0; j < chunks[i].length; j++) {
				chunks[i][j] = load(new IntVector(
						chunkPos1.getMagX() + i,
						chunkPos1.getMagY() + j)
				);
			}
		}

		return chunks;
	}

	//Get array of blocks contained within a specified region
	public Block[][] getBlocks (
			IntVector chunkPos1, IntVector blockPos1,
			IntVector chunkPos2, IntVector blockPos2
	) {
		//Array of chunks containing the region
		Chunk[][] chunks = getChunks(chunkPos1, chunkPos2);

		//Array of blocks containing the region
		Block[][] blocks;

		//Integers representing the size of the blocks array
		int blocksSizeX, blocksSizeY;

		//Calculate the size of the array along the x-axis
		blocksSizeX = Math.abs(
				blockPos2.getMagX() - blockPos1.getMagX() + Chunk.SIZE * Math.abs(chunkPos2.getMagX() - chunkPos1.getMagX())
		) + 1;

		//Calculate the size of the array along the y-axis
		blocksSizeY = Math.abs(
				blockPos2.getMagY() - blockPos1.getMagY() + Chunk.SIZE * Math.abs(chunkPos2.getMagY() - chunkPos1.getMagY())
		) + 1;

		//Create blocks array
		blocks = new Block[blocksSizeX][blocksSizeY];

		//Loop through each chunk
		for (Chunk[] chunkArray : chunks) {
			for (Chunk chunk : chunkArray) {
				int i = 0, max_i = Chunk.SIZE;
				int min_j = 0, max_j = Chunk.SIZE;

				//Set max/min indices within the chunk for edge behaviour

				if (chunk.getPos().getMagX() == chunkPos1.getMagX()) {
					i = blockPos1.getMagX();
				}

				if (chunk.getPos().getMagX() == chunkPos2.getMagX()) {
					max_i = blockPos2.getMagX() + 1;
				}

				if (chunk.getPos().getMagY() == chunkPos1.getMagY()) {
					min_j = blockPos1.getMagY();
				}

				if (chunk.getPos().getMagY() == chunkPos2.getMagY()) {
					max_j = blockPos2.getMagY() + 1;
				}

				//Loop through each block array
				while (i < max_i) {
					int j = min_j;

					//Loop through each block
					while (j < max_j) {
						blocks
								[Chunk.SIZE * (chunk.getPos().getMagX() - chunkPos1.getMagX()) + i - blockPos1.getMagX()]
								[Chunk.SIZE * (chunk.getPos().getMagY() - chunkPos1.getMagY()) + j - blockPos1.getMagY()]
						= chunk.getBlock(new IntVector(i, j));

						j++;
					}

					i++;
				}
			}
		}

		return blocks;
	}
}