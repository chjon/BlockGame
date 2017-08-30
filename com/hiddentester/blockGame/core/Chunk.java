/******************************
 * This class defines a chunk *
 ******************************/

package com.hiddentester.blockGame.core;

import com.hiddentester.blockGame.blocks.Block;
import com.hiddentester.blockGame.blocks.Block_Air;
import com.hiddentester.blockGame.blocks.Block_Stone;
import com.hiddentester.util.ChunkPosVector;

public class Chunk {
	public static final int SIZE = 16;
	private ChunkPosVector pos;
	Block[][] blocks;

	//Constructors:

	public Chunk (ChunkPosVector pos) {
		this.pos = new ChunkPosVector(pos);
		blocks = new Block[SIZE][SIZE];

		populate();
	}

	//Accessors:

	public ChunkPosVector getPos () {
		return this.pos;
	}

	public Block[][] getBlocks () {
		return blocks;
	}

	//Converts chunk data to a string
	@Override
	public String toString() {
		String output = "{";

		for (int i = 0; i < SIZE; i++) {
			output += "{";

			for (int j = 0; j < SIZE; j++) {
				output += blocks[i][j].toString() + ",";
			}

			output = output.substring(0, output.length() - 1);
			output += "},";
		}

		output = output.substring(0, output.length() - 1);
		output += "}";

		return output;
	}

	//Fill chunk
	private void populate () {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (Chunk.SIZE / 2 * (Math.sin((pos.getMagX() * Chunk.SIZE + i) / (double) Chunk.SIZE)) < Chunk.SIZE * pos.getMagY() + j) {
					blocks[i][j] = new Block_Air();
				} else {
					blocks[i][j] = new Block_Stone();
				}
			}
		}
	}
}
