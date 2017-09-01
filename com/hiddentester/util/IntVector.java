/***************************************************************
 * This class describes a vector using only integer components *
 ***************************************************************/

package com.hiddentester.util;

public class IntVector {
	private int magX, magY;

	//Constructors:

	public IntVector (int magX, int magY) {
		this.magX = magX;
		this.magY = magY;
	}

	public IntVector (IntVector source) {
		this.magX = source.magX;
		this.magY = source.magY;
	}

	public IntVector (Vector2D source) {
		this.magX = (int) Math.floor(source.getMagX());
		this.magY = (int) Math.floor(source.getMagY());
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
	public static IntVector add (IntVector a, IntVector b) {
		return new IntVector(
				a.magX + b.magX,
				a.magY + b.magY);
	}

	//Calculate the difference between two vectors
	public static IntVector sub (IntVector a, IntVector b) {
		return new IntVector(
				a.magX - b.magX,
				a.magY - b.magY);
	}
}
