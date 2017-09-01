package com.hiddentester.blockGame.blocks;

public class Block_Dirt extends BlockCollidable {
	private static final String NAME = "Dirt";
	private static final int MINING_LEVEL = 1;

	//Constructor:

	public Block_Dirt () {
		super (NAME, MINING_LEVEL);
	}
}
