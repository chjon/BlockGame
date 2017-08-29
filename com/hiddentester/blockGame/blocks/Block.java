/******************************
 * This class defines a block *
 ******************************/

package com.hiddentester.blockGame.blocks;

public abstract class Block {
	protected String name;
	protected int miningLevel;

	//Constructor
	public Block (String name, int miningLevel) {
		this.name = name;
		this.miningLevel = miningLevel;
	}

	//Accessors:

	public String getName() {
		return this.name;
	}

	public int getMiningLevel() {
		return this.miningLevel;
	}

	//Converts block data to a string
	@Override
	public String toString () {
		return "(" + name + ")";
	};
}
