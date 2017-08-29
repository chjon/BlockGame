package com.hiddentester.blockGame.blocks;

public class Stone extends BlockCollidable {
	private static final String NAME = "Dirt";
	private static final int MINING_LEVEL = 1;

	//Constructor:

	public Stone () {
		super (NAME, MINING_LEVEL);
	}
}
