/******************************
 * This class defines a chunk *
 ******************************/

package com.hiddentester.blockGame.core;

import com.hiddentester.blockGame.blocks.Block;
import com.hiddentester.blockGame.blocks.instantiable.*;
import com.hiddentester.util.IntVector;
import com.hiddentester.util.Vector2D;

public class Chunk {
	public static final int SIZE = 16;
	private IntVector pos;
	Block[][] blocks;

	//Constructors:

	public Chunk (IntVector pos) {
		this.pos = new IntVector(pos);
		blocks = new Block[SIZE][SIZE];

		populate();
	}

	//Accessors:

	public IntVector getPos () {
		return this.pos;
	}

	public Block[][] getBlocks () {
		return blocks;
	}

	Block getBlock (Vector2D relPos) {
		return getBlock(
				new IntVector((int)(relPos.getMagX()), (int)(relPos.getMagY()))
		);
	}

	Block getBlock (IntVector relPos) {
		return blocks[relPos.getMagX()][relPos.getMagY()];
	}

	void setBlock (Vector2D relPos, Block block) {
		setBlock(
				new IntVector((int)(relPos.getMagX()), (int)(relPos.getMagY())),
				block
		);
	}

	void setBlock (IntVector relPos, Block block) {
		blocks[relPos.getMagX()][relPos.getMagY()] = block;
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
				if (Chunk.SIZE / 2 * (Math.sin((pos.getMagX() * Chunk.SIZE + i) / (double) Chunk.SIZE))
						< Chunk.SIZE * pos.getMagY() + j) {
					blocks[i][j] = new Block_Air();
				} else if (Chunk.SIZE / 2 * (Math.sin((pos.getMagX() * Chunk.SIZE + i) / (double) Chunk.SIZE))
						< Chunk.SIZE * pos.getMagY() + j + 1) {
					blocks[i][j] = new Block_Grass();
				} else if (Chunk.SIZE / 2 * (Math.sin((pos.getMagX() * Chunk.SIZE + i) / (double) Chunk.SIZE))
						< Chunk.SIZE * pos.getMagY() + j + 4) {
					blocks[i][j] = new Block_Dirt();
				} else {
					blocks[i][j] = new Block_Stone();
				}
			}
		}
	}
}
