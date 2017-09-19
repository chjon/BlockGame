package com.hiddentester.blockGame.blocks.instantiable;

import com.hiddentester.blockGame.blocks.BlockUncollidable;

public class Block_Air extends BlockUncollidable {
	public static final int BLOCK_ID = 0;
	public static final String CLASS_NAME = "Block_Air";
	private static final int MINING_LEVEL = -1;

	public Block_Air () {
		super (BLOCK_ID, CLASS_NAME, MINING_LEVEL);
	}

	public String getClassName () {
		return CLASS_NAME;
	}

	public int getBlockID () {
		return BLOCK_ID;
	}
}
