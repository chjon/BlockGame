/******************************
 * This class defines a chunk *
 ******************************/

package com.hiddentester.blockGame.core;

import com.hiddentester.math.Vector2D;
import com.hiddentester.blockGame.blocks.Block;
import com.hiddentester.blockGame.blocks.Stone;
import com.hiddentester.blockGame.blocks.Air;

public class Chunk {
	public static final int SIZE = 16;
	private Vector2D pos;
	private Block[][] blocks;

	//Constructor
	public Chunk (Vector2D pos) {
		this.pos = new Vector2D(pos);
		blocks = new Block[SIZE][SIZE];

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (i == 0 || j == 0 || i == j) {
					blocks[i][j] = new Stone();
				} else {
					blocks[i][j] = new Air();
				}
			}
		}
	}

	//Accessors:

	public Vector2D getPos () {
		return this.pos;
	}

	public Block[][] getBlocks () {
		return blocks;
	}

	//Unload chunk
	public void unload () {

	}

	//Load chunk
	public static Chunk loadChunk (Vector2D pos) {
		return new Chunk(pos);
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
}
