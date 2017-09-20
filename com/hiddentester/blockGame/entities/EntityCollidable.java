/*************************************************************
 * This class defines an entity that can collide with blocks *
 *************************************************************/

package com.hiddentester.blockGame.entities;

import com.hiddentester.blockGame.blocks.Block;
import com.hiddentester.blockGame.blocks.BlockCollidable;
import com.hiddentester.blockGame.core.Chunk;
import com.hiddentester.blockGame.core.ChunkLoader;
import com.hiddentester.util.IntVector;
import com.hiddentester.util.Vector2D;

public abstract class EntityCollidable extends Entity {
	//Gravity - downward acceleration
	protected static final float GRAVITY = -0.006f;

	//Friction - amount by which to slow down entities per tick
	protected static final float FRICTION = 0.90f;

	//Reference to game chunk loader to get blocks in world for collision calculation
	private ChunkLoader chunkLoader;

	//Data tags
	protected boolean noGravity;		//Whether the entity is not affected by gravity
	protected boolean onGround;		//Whether the entity is on the ground

	//Constructors:

	public EntityCollidable (
			IntVector chunkPos, Vector2D blockPos, Vector2D vel, Vector2D dimensions,
			ChunkLoader chunkLoader
	) {
		super(chunkPos, blockPos, vel, dimensions);

		this.chunkLoader = chunkLoader;
		this.noGravity = true;
		this.onGround = true;
	}

	public EntityCollidable (
			IntVector chunkPos, Vector2D blockPos, Vector2D vel, Vector2D dimensions,
			ChunkLoader chunkLoader, boolean noGravity, boolean onGround
	) {
		super(chunkPos, blockPos, vel, dimensions);

		this.chunkLoader = chunkLoader;
		this.noGravity = noGravity;
		this.onGround = onGround;
	}

	//Determine whether the entity will collide with a block

	//----[Summary of collision detection algorithm]----\\
	//													\\
	//	1.	Find a scale factor of the velocity that	\\
	//		will intersect the entity with the block.	\\
	//		If such a value exists between 0 and 1		\\
	// 		inclusive, there will be a collision.		\\
	//													\\
	//--------------------------------------------------\\

	private void setVelAfterCollision (BlockCollidable block, IntVector relBlockPos) {
		//Limits on the scale factor
		float	scaleFactor, scaleFactorX, scaleFactorY;

		//Determine the min/max horizontal scale factors based on the direction of the entity's velocity

		if (this.vel.getMagX() > 0) {			//Moving right
			scaleFactorX =
					(relBlockPos.getMagX() - this.blockPos.getMagX() - this.dimensions.getMagX() / 2) / this.vel.getMagX();

		} else if (this.vel.getMagX() < 0) {	//Moving left
			scaleFactorX =
					(relBlockPos.getMagX() - this.blockPos.getMagX() + this.dimensions.getMagX() / 2 + 1) / this.vel.getMagX();

		} else {								//Not moving horizontally
			scaleFactorX = 0;
		}

		//Determine the min/max vertical scale factors based on the direction of the entity's velocity

		if (this.vel.getMagY() > 0) {			//Moving up
			scaleFactorY =
					(relBlockPos.getMagY() - this.blockPos.getMagY() - this.dimensions.getMagY() / 2) / this.vel.getMagY();

		} else if (this.vel.getMagY() < 0) {	//Moving down
			scaleFactorY =
					(relBlockPos.getMagY() - this.blockPos.getMagY() + this.dimensions.getMagY() / 2 + 1) / this.vel.getMagY();

		} else {								//Not moving vertically
			scaleFactorY = 0;
		}

		//Determine the min/max scale factor
		scaleFactor = Math.max(scaleFactorX, scaleFactorY);

		//Determine whether a collision is possible
		if (0 < scaleFactor && scaleFactor <= 1) {
			//Update velocity
			if (scaleFactor == scaleFactorX) {
				this.vel.setMagX(0.1f * scaleFactor * this.vel.getMagX());
			} else {
				this.vel.setMagY(0.1f * scaleFactor * this.vel.getMagY());

				//Update data tag
				this.onGround = true;
			}
		}
	}

	//Find a set of vectors bounding the entire entity and its destination
	private void findBoundingPositions (
			IntVector chunkPos1, Vector2D blockPos1,
			IntVector chunkPos2, Vector2D blockPos2
	) {
		//Change the block positions to account for the entity's dimensions based on its velocity

		if (this.vel.getMagX() > 0) {
			blockPos1.setMagX(blockPos1.getMagX() - this.dimensions.getMagX() / 2);
			blockPos2.setMagX(blockPos2.getMagX() + this.dimensions.getMagX() / 2 + this.vel.getMagX());
		} else if (this.vel.getMagX() < 0) {
			blockPos1.setMagX(blockPos1.getMagX() + this.dimensions.getMagX() / 2);
			blockPos2.setMagX(blockPos2.getMagX() - this.dimensions.getMagX() / 2 + this.vel.getMagX());
		} else {
			blockPos1.setMagX(blockPos1.getMagX() + this.dimensions.getMagX() / 2);
			blockPos2.setMagX(blockPos2.getMagX() - this.dimensions.getMagX() / 2);
		}

		if (this.vel.getMagY() > 0) {
			blockPos1.setMagY(blockPos1.getMagY() - this.dimensions.getMagY() / 2);
			blockPos2.setMagY(blockPos2.getMagY() + this.dimensions.getMagY() / 2 + this.vel.getMagY());
		} else if (this.vel.getMagY() < 0) {
			blockPos1.setMagY(blockPos1.getMagY() + this.dimensions.getMagY() / 2);
			blockPos2.setMagY(blockPos2.getMagY() - this.dimensions.getMagY() / 2 + this.vel.getMagY());
		} else {
			blockPos1.setMagY(blockPos1.getMagY() + this.dimensions.getMagY() / 2);
			blockPos2.setMagY(blockPos2.getMagY() - this.dimensions.getMagY() / 2);
		}

		//Update chunk position if entity will pass between chunks
		Entity.updateChunkPos(chunkPos1, blockPos1);
		Entity.updateChunkPos(chunkPos2, blockPos2);

		//Make sure chunkPos1 is left of chunkPos2
		if (chunkPos1.getMagX() > chunkPos2.getMagX()) {
			//Swap chunk x-coordinates
			float temp = chunkPos1.getMagX();
			chunkPos1.setMagX(chunkPos2.getMagX());
			chunkPos2.setMagX((int) temp);

			//Swap block x-coordinates
			temp = blockPos1.getMagX();
			blockPos1.setMagX(blockPos2.getMagX());
			blockPos2.setMagX(temp);
			//Make sure blockPos1 is left of blockPos2
		} else if (chunkPos1.getMagX() == chunkPos2.getMagX() &&
				blockPos1.getMagX() > blockPos2.getMagX()) {

			//Swap block x-coordinates
			float temp = blockPos1.getMagX();
			blockPos1.setMagX(blockPos2.getMagX());
			blockPos2.setMagX(temp);
		}

		//Make sure chunkPos1 is below chunkPos2
		if (chunkPos1.getMagY() > chunkPos2.getMagY()) {
			//Swap chunk y-coordinates
			float temp = chunkPos1.getMagY();
			chunkPos1.setMagY(chunkPos2.getMagY());
			chunkPos2.setMagY((int) temp);

			//Swap block y-coordinates
			temp = blockPos1.getMagY();
			blockPos1.setMagY(blockPos2.getMagY());
			blockPos2.setMagY(temp);
			//Make sure blockPos1 is below blockPos2
		} else if (chunkPos1.getMagY() == chunkPos2.getMagY() &&
				blockPos1.getMagY() > blockPos2.getMagY()) {

			//Swap block y-coordinates
			float temp = blockPos1.getMagY();
			blockPos1.setMagY(blockPos2.getMagY());
			blockPos2.setMagY(temp);
		}
	}

	//Detect and account for collision in movement

	//--------[Summary of Movement Algorithm]-------\\
	//												\\
	//	1.	Get array of blocks containing the		\\
	//		entire entity and its destination.		\\
	//												\\
	//	2.	Searching the array outward from the	\\
	// 		entity, look for collidable blocks.		\\
	// 												\\
	//	3.	Determine whether the collidable		\\
	// 		surface is in the path of movement		\\
	//		and change the velocity such that a		\\
	// 		subsequent movement will not surpass	\\
	// 		the discovered surface.					\\
	// 												\\
	//	4.	Move the entity.						\\
	// 												\\
	//----------------------------------------------\\

	@Override
	public void move () {
		//Accelerate entity due to gravity
		if (!this.noGravity) {
			this.vel.setMagY(this.vel.getMagY() + GRAVITY);
		}

		//Update data tags
		if (this.vel.getMagY() != 0) {
			this.onGround = false;
		}

		//Consider movement only if velocity is not zero
		if (vel.getMagSquared() > 0) {
			//Step 1 of movement algorithm
			//Get array of blocks containing the entity and its destination
			IntVector chunkPos1 = new IntVector(this.chunkPos);
			Vector2D blockPos1 = new Vector2D(this.blockPos);
			IntVector chunkPos2 = new IntVector(this.chunkPos);
			Vector2D blockPos2 = new Vector2D(this.blockPos);

			findBoundingPositions(chunkPos1, blockPos1, chunkPos2, blockPos2);

			Block[][] blocks = chunkLoader.getBlocks(
					chunkPos1, new IntVector(blockPos1),
					chunkPos2, new IntVector(blockPos2)
			);

			int directionX, directionY;

			//Find direction of entity based on velocity
			if (this.vel.getMagX() >= 0) {
				directionX = 1;
			} else {
				directionX = -1;
			}

			if (this.vel.getMagY() >= 0) {
				directionY = 1;
			} else {
				directionY = -1;
			}

			//Loop through blocks starting from the entity
			for (int i = (directionX > 0) ? (0) : (blocks.length - 1);
					(directionX > 0) ? (i < blocks.length) : (i >= 0);
					i += (directionX > 0) ? (1) : (-1)) {
				for (int j = (directionY > 0) ? (0) : (blocks[i].length - 1);
						(directionY > 0) ? (j < blocks[i].length) : (j >= 0);
						j += (directionY > 0) ? (1) : (-1)) {

					//Step 2 of movement algorithm:
					//Check if block is collidable
					if (blocks[i][j] instanceof BlockCollidable) {
						//Calculate global chunk position
						IntVector chunkPos = new IntVector(
								(i + (int) Math.floor(blockPos1.getMagX())) / Chunk.SIZE + chunkPos1.getMagX(),
								(j + (int) Math.floor(blockPos1.getMagY())) / Chunk.SIZE + chunkPos1.getMagY()
						);

						//Calculate block position relative to chunk
						IntVector blockPos = new IntVector(
								((i + (int) Math.floor(blockPos1.getMagX())) % Chunk.SIZE + Chunk.SIZE) % Chunk.SIZE,
								((j + (int) Math.floor(blockPos1.getMagY())) % Chunk.SIZE + Chunk.SIZE) % Chunk.SIZE
						);

						//Calculate block position relative to entity
						IntVector relBlockPos = new IntVector(
								(Chunk.SIZE * (chunkPos.getMagX() - this.chunkPos.getMagX()) +
										(blockPos.getMagX())),
								(Chunk.SIZE * (chunkPos.getMagY() - this.chunkPos.getMagY()) +
										(blockPos.getMagY()))
						);

						//Step 3 of movement algorithm
						//Check if the entity collides with the block and restrict entity velocity

						this.setVelAfterCollision((BlockCollidable) blocks[i][j], relBlockPos);
					}
				}
			}

			//Step 4 of movement algorithm:
			//Move entity
			super.move();

			//Scale velocity according to friction
			this.vel.setMagX(this.vel.getMagX() * FRICTION);

			//Set velocity to zero if sufficiently small

			if (Math.abs(this.vel.getMagX()) < Math.pow(10, -4)) {
				this.vel.setMagX(0);
			}

			if (Math.abs(this.vel.getMagY()) < Math.pow(10, -4)) {
				this.vel.setMagY(0);
			}
		}
	}
}