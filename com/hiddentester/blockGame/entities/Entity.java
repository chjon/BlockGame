/**********************************************************************
 * This class defines the basic functions and behaviours of an entity *
 **********************************************************************/

package com.hiddentester.blockGame.entities;

import com.hiddentester.util.IntVector;
import com.hiddentester.util.Vector2D;
import com.hiddentester.blockGame.core.Chunk;

public abstract class Entity {
	protected IntVector chunkPos;				//Position of the chunk containing the centre of the entity
	protected Vector2D blockPos;				//Position of the centre of the entity relative to the chunk it is in
	protected Vector2D vel;						//Velocity of the entity in blocks per tick.
	protected Vector2D dimensions;

	//Constructor
	public Entity (IntVector chunkPos, Vector2D blockPos, Vector2D vel, Vector2D dimensions) {
		this.chunkPos = chunkPos;
		setBlockPos(blockPos);
		this.vel = vel;
		this.dimensions = dimensions;
	}

	//Accessors/Mutators:

	public IntVector getChunkPos() {
		return chunkPos;
	}

	public void setChunkPos(IntVector chunkPos) {
		this.chunkPos = chunkPos;
	}

	public Vector2D getBlockPos () {
		return this.blockPos;
	}

	private void setBlockPos (Vector2D blockPos) {
		this.blockPos = blockPos;
	}

	public Vector2D getAbsPos () {
		return Vector2D.add(new Vector2D(
				chunkPos.getMagX() * Chunk.SIZE,
				chunkPos.getMagY() * Chunk.SIZE),
				blockPos
		);
	}

	public void setAbsPos (Vector2D absPos) {
		chunkPos = new IntVector(
				(int) Math.floor(absPos.getMagX() / Chunk.SIZE),
				(int) Math.floor(absPos.getMagY() / Chunk.SIZE)
		);

		setBlockPos(absPos);
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

		//Find target position
		setBlockPos(Vector2D.add(getBlockPos(), this.vel));

		//Update position if entity has passed between chunks
		updateChunkPos(this.chunkPos, this.blockPos);
	}

	//Update chunk coordinates if an entity has passed between chunks
	static void updateChunkPos (IntVector chunkPos, Vector2D blockPos) {
		//Update x-coordinate
		if (blockPos.getMagX() < 0 || blockPos.getMagX() >= Chunk.SIZE) {
			chunkPos.setMagX(chunkPos.getMagX() + (int) Math.floor(blockPos.getMagX() / Chunk.SIZE));
			blockPos.setMagX((blockPos.getMagX() % Chunk.SIZE + Chunk.SIZE) % Chunk.SIZE);
		}

		//Update y-coordinate
		if (blockPos.getMagY() < 0 || blockPos.getMagY() >= Chunk.SIZE) {
			chunkPos.setMagY(chunkPos.getMagY() + (int) Math.floor(blockPos.getMagY() / Chunk.SIZE));
			blockPos.setMagY((blockPos.getMagY() % Chunk.SIZE + Chunk.SIZE) % Chunk.SIZE);
		}
	}

	//Converts entity data to a string
	@Override
	public String toString () {
		return "(" + this.chunkPos + "," + this.blockPos + "," + this.vel + "," + this.dimensions + ")";
	}
}