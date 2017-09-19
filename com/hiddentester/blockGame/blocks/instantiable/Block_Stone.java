package com.hiddentester.blockGame.blocks.instantiable;

import com.hiddentester.blockGame.blocks.BlockCollidable;

public class Block_Stone extends BlockCollidable {
	public static final int BLOCK_ID = 1;
	public static final String CLASS_NAME = "Block_Stone";
	private static final int MINING_LEVEL = 1;

	//Constructor:

	public Block_Stone () {
		super (BLOCK_ID, CLASS_NAME, MINING_LEVEL);
	}

	public String getClassName () {
		return CLASS_NAME;
	}

	public int getBlockID () {
		return BLOCK_ID;
	}
}
