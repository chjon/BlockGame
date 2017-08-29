package com.hiddentester.blockGame.blocks;

public class Block_Stone extends BlockCollidable {
	private static final String NAME = "Stone";
	private static final int MINING_LEVEL = 1;

	//Constructor:

	public Block_Stone () {
		super (NAME, MINING_LEVEL);
	}
}
