/**********************************************************************
 * This class defines the basic functions and behaviours of an entity *
 **********************************************************************/

package com.hiddentester.blockGame.entities;

import com.hiddentester.math.Vector2D;
import com.hiddentester.blockGame.core.Chunk;

public abstract class Entity {
	protected Chunk containingChunk;
	protected Vector2D relPos;				//Position of the entity relative to the chunk it is in
	protected Vector2D vel;
	protected Vector2D dimensions;

	//Constructor
	public Entity (Chunk containingChunk, Vector2D relPos, Vector2D vel, Vector2D dimensions) {
		this.containingChunk = containingChunk;
		setRelPos(relPos);
		this.vel = vel;
		this.dimensions = dimensions;
	}

	//Accessors/Mutators:

	public Chunk getContainingChunk () {
		return containingChunk;
	}

	public void setContainingChunk (Chunk containingChunk) {
		this.containingChunk = containingChunk;
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
		return Vector2D.add(Vector2D.scale(containingChunk.getPos(), Chunk.SIZE), relPos);
	}

	public void setAbsPos (Vector2D absPos) {
		Vector2D chunkPos = new Vector2D(
				Math.floor(absPos.getMagX() / Chunk.SIZE),
				Math.floor(absPos.getMagY() / Chunk.SIZE));
		setRelPos(absPos);

		if (!containingChunk.getPos().equals(chunkPos)) {
			containingChunk = Chunk.loadChunk(chunkPos);
		}
	}

	public Vector2D getVel () {
		return this.vel;
	}

	public void setVel (Vector2D vel) {
		this.vel = vel;
	}

	public Vector2D getDimensions () {
		return this.dimensions;
	}

	public void setDimensions (Vector2D dimensions) {
		this.dimensions = dimensions;
	}

	//Move entity
	public void move (Vector2D displacement) {
		setAbsPos(Vector2D.add(getAbsPos(), displacement));
	}

	//Converts entity data to a string
	@Override
	public String toString () {
		return "(" + this.containingChunk.getPos() + "," + this.relPos + "," + this.vel + "," + this.dimensions + ")";
	}
}
