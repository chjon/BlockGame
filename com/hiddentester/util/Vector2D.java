/*******************************************************
 * This class describes a 2D vector and its operations *
 *******************************************************/

package com.hiddentester.util;

public class Vector2D {
	private double magX, magY;

	//Constructors:

	public Vector2D () {
		this.magX = 0;
		this.magY = 0;
	}

	public Vector2D (double magX, double magY) {
		this.magX = magX;
		this.magY = magY;
	}

	public Vector2D (Vector2D source) {
		this.magX = source.magX;
		this.magY = source.magY;
	}

	//Accessor/Mutators:

	public double getMagX () {
		return this.magX;
	}

	public void setMagX (double magX) {
		this.magX = magX;
	}

	public double getMagY () {
		return this.magY;
	}

	public void setMagY (double magY) {
		this.magY = magY;
	}

	//Convert vector data to string
	@Override
	public String toString () {
		return "[" + this.magX + "," + this.magY + "]";
	}

	//Calculate magnitude squared of vector based on components (Pythagorean)
	public double getMagSquared () {
		return Math.pow(magX, 2) + Math.pow(magY, 2);
	}

	//Calculate magnitude of vector based on components (Pythagorean)
	public double getMag () {
		return Math.sqrt(getMagSquared());
	}

	//Calculate magnitude of vector based on components (Manhattan)
	public double getMagManhattan () {
		return Math.abs(magX) + Math.abs(magY);
	}

	//Calculate the angle in radians of the vector CCW from the positive x-axis
	public double getAng () throws IllegalDirectionException {
		if (magY == 0) {
			if (magX == 0) {
				throw new IllegalDirectionException();
			}

			return 0;
		}

		return Math.atan(magX / magY);
	}

	//Compare two vectors
	public boolean equals (Vector2D comparand) {
		return (this.magX == comparand.magX) && (this.magY == comparand.magY);
	}

	//Scale a vector
	public static Vector2D scale (Vector2D a, double scaleFactor) {
		return new Vector2D(
				a.magX * scaleFactor,
				a.magY * scaleFactor
		);
	}

	//Set the magnitude of a vector without changing direction
	public static Vector2D setMag (Vector2D a, double mag) throws IllegalDirectionException {
		if (a.getMagManhattan() == 0) {
			throw new IllegalDirectionException();
		}

		return scale(a, mag / a.getMag());
	}

	//Set the angle in radians of a vector CCW from the positive x-axis without changing magnitude
	public static Vector2D setAng (Vector2D a, double ang) throws IllegalDirectionException {
		if (a.getMagManhattan() == 0) {
			throw new IllegalDirectionException();
		}

		return scale(new Vector2D(Math.cos(ang), Math.sin(ang)), a.getMag());
	}

	//Calculate the sum of two vectors
	public static Vector2D add (Vector2D a, Vector2D b) {
		return new Vector2D(
				a.magX + b.magX,
				a.magY + b.magY);
	}

	//Calculate the difference between two vectors
	public static Vector2D sub (Vector2D a, Vector2D b) {
		return new Vector2D(
				a.magX - b.magX,
				a.magY - b.magY);
	}

	//Calculate the dot product of two vectors
	public static double dot (Vector2D a, Vector2D b) {
		return (a.magX * b.magX) + (a.magY * b.magY);
	}
}