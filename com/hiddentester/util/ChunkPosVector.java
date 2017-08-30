/**************************************
 * This is a utility class for Chunks *
 **************************************/

package com.hiddentester.util;

public class ChunkPosVector {
	private int magX, magY;

	//Constructors:

	public ChunkPosVector (int magX, int magY) {
		this.magX = magX;
		this.magY = magY;
	}

	public ChunkPosVector (ChunkPosVector source) {
		this.magX = source.magX;
		this.magY = source.magY;
	}

	//Accessor/Mutators:

	public int getMagX () {
		return this.magX;
	}

	public void setMagX (int magX) {
		this.magX = magX;
	}

	public int getMagY () {
		return this.magY;
	}

	public void setMagY (int magY) {
		this.magY = magY;
	}

	//Convert vector data to string
	@Override
	public String toString () {
		return "[" + this.magX + "," + this.magY + "]";
	}

	//Calculate the sum of two vectors
	public static ChunkPosVector add (ChunkPosVector a, ChunkPosVector b) {
		return new ChunkPosVector(
				a.magX + b.magX,
				a.magY + b.magY);
	}

	//Calculate the difference between two vectors
	public static ChunkPosVector sub (ChunkPosVector a, ChunkPosVector b) {
		return new ChunkPosVector(
				a.magX - b.magX,
				a.magY - b.magY);
	}
}
