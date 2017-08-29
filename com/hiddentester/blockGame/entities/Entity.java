/**********************************************************************
 * This class defines the basic functions and behaviours of an entity *
 **********************************************************************/

package com.hiddentester.blockGame.entities;

import com.hiddentester.math.Vector2D;
import com.hiddentester.blockGame.core.Chunk;

public abstract class Entity {
	static final double FRICTION = 0.90;
	private Vector2D chunkPos;					//Position of the chunk containing the entity
	private Vector2D relPos;					//Position of the entity relative to the chunk it is in
	private Vector2D vel;						//Velocity of entity in blocks per tick.
	private Vector2D dimensions;

	//Constructor
	public Entity (Vector2D chunkPos, Vector2D relPos, Vector2D vel, Vector2D dimensions) {
		this.chunkPos = chunkPos;
		setRelPos(relPos);
		this.vel = vel;
		this.dimensions = dimensions;
	}

	//Accessors/Mutators:

	public Vector2D getChunkPos() {
		return chunkPos;
	}

	public void setChunkPos(Vector2D chunkPos) {
		this.chunkPos = chunkPos;
	}

	public Vector2D getRelPos () {
		return this.relPos;
	}

	public void setRelPos (Vector2D relPos) {
		relPos.setMagX((relPos.getMagX() % Chunk.SIZE + Chunk.SIZE) % Chunk.SIZE);
		relPos.setMagY((relPos.getMagY() % Chunk.SIZE + Chunk.SIZE) % Chunk.SIZE);
		this.relPos = relPos;
	}

	public Vector2D getAbsPos () {
		return Vector2D.add(Vector2D.scale(chunkPos, Chunk.SIZE), relPos);
	}

	public void setAbsPos (Vector2D absPos) {
		chunkPos = new Vector2D(
				Math.floor(absPos.getMagX() / Chunk.SIZE),
				Math.floor(absPos.getMagY() / Chunk.SIZE)
		);

		setRelPos(absPos);
	}

	public Vector2D getVel () {
		return this.vel;
	}

	public void accelerate (Vector2D vel) {
		this.vel = Vector2D.add(this.vel, vel);
	}

	public Vector2D getDimensions () {
		return this.dimensions;
	}

	public void setDimensions (Vector2D dimensions) {
		this.dimensions = dimensions;
	}

	//Move entity
	public void move () {
		//Change position according to velocity
		Vector2D newPos = Vector2D.add(getRelPos(), vel);

		//Update chunkPos if player has passed between chunks

		if (newPos.getMagX() < 0 || newPos.getMagX() >= Chunk.SIZE) {
			chunkPos.setMagX(chunkPos.getMagX() + Math.floor(newPos.getMagX() / Chunk.SIZE));
		}

		if (newPos.getMagY() < 0 || newPos.getMagY() >= Chunk.SIZE) {
			chunkPos.setMagY(chunkPos.getMagY() + Math.floor(newPos.getMagY() / Chunk.SIZE));
		}

		setRelPos(newPos);

		//Scale velocity according to friction
		this.vel = Vector2D.scale(this.vel, FRICTION);
	}

	//Converts entity data to a string
	@Override
	public String toString () {
		return "(" + this.chunkPos + "," + this.relPos + "," + this.vel + "," + this.dimensions + ")";
	}
}