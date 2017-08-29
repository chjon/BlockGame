package com.hiddentester.blockGame.blocks;

public class Block_Air extends BlockUncollidable {
	private static final String NAME = "Air";
	private static final int MINING_LEVEL = -1;

	public Block_Air () {
		super (NAME, MINING_LEVEL);
	}
}
