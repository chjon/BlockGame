/******************************
 * This class defines a block *
 ******************************/

package com.hiddentester.blockGame.blocks;

public abstract class Block {
	protected int blockID;
	protected String className;
	protected int miningLevel;

	//Constructor
	public Block (int blockID, String className, int miningLevel) {
		this.blockID = blockID;
		this.className = className;
		this.miningLevel = miningLevel;
	}

	//Accessors:

	public abstract String getClassName();
	public abstract int getBlockID();

	public int getMiningLevel() {
		return this.miningLevel;
	}

	//Converts block data to a string
	@Override
	public String toString () {
		return "(" + blockID + ", " + className + ")";
	}
}
