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
	//Reference to game chunk loader to get blocks in world for collision calculation
	private ChunkLoader chunkLoader;

	//Constructor
	public EntityCollidable (
			IntVector chunkPos, Vector2D blockPos, Vector2D vel, Vector2D dimensions,
			ChunkLoader chunkLoader
	) {
		super(chunkPos, blockPos, vel, dimensions);

		this.chunkLoader = chunkLoader;
	}

	//Determine whether the entity will collide with a block

	//------[Summary of Collision Detection Algorithm]------\\
	//														\\
	//	1.	Determine which faces may be involved in a		\\
	//		collision based on the entity's velocity.		\\
	//														\\
	//	2.	Compare the slopes of the lines connecting		\\
	//		opposite ends of the collidable surfaces with	\\
	//		the slope of the entity's velocity.	There is 	\\
	//		no collision if the slopes are both higher or	\\
	//		lower than the slope of the velocity.			\\
	//														\\
	//------------------------------------------------------\\

	private boolean collides (BlockCollidable block, IntVector blockPos) {
		//An entity will not collide with a block it is already in.
		if (Math.abs(blockPos.getMagX() + 0.5 - this.blockPos.getMagX()) < this.dimensions.getMagX() / 2 + 0.5 &&
				Math.abs(blockPos.getMagY() + 0.5 - this.blockPos.getMagY()) < this.dimensions.getMagY() / 2 + 0.5) {
			return false;
		}

		//Check side surfaces if the entity's velocity has a horizontal component
		if (this.vel.getMagX() != 0) {
			float blockSurfaceX = blockPos.getMagX();
			float entitySurfaceX = this.blockPos.getMagX();
			float slope1, slope2, velSlope;

			//Select surfaces to check depending on the direction of the entity's movement
			if (this.vel.getMagX() < 0) {
				entitySurfaceX -= this.dimensions.getMagX() / 2;
				blockSurfaceX++;
			} else {
				entitySurfaceX += this.dimensions.getMagX() / 2;
			}

			slope1 = ((blockPos.getMagY()) - (this.blockPos.getMagY() + this.dimensions.getMagY() / 2)) / (blockSurfaceX - entitySurfaceX);
			slope2 = ((blockPos.getMagY() + 1) - (this.blockPos.getMagY() - this.dimensions.getMagY() / 2)) / (blockSurfaceX - entitySurfaceX);
			velSlope = this.vel.getSlope();

			//Detect collision
			if (!((slope1 > velSlope && slope2 > velSlope) || (slope1 < velSlope && slope2 < velSlope))) {
				return true;
			}
		}

		//Check top/bottom surfaces if the entity's velocity has a vertical component
		if (this.vel.getMagY() != 0) {
			float blockSurfaceY = blockPos.getMagY();
			float entitySurfaceY = this.blockPos.getMagY();
			float slope1, slope2, velSlope;

			//Select surfaces to check depending on the direction of the entity's movement
			if (this.vel.getMagY() < 0) {
				entitySurfaceY -= this.dimensions.getMagY() / 2;
				blockSurfaceY++;
			} else {
				entitySurfaceY += this.dimensions.getMagX() / 2;
			}

			slope1 = (blockSurfaceY - entitySurfaceY) / ((blockPos.getMagX()) - (this.blockPos.getMagX() + this.dimensions.getMagX() / 2));
			slope2 = (blockSurfaceY - entitySurfaceY) / ((blockPos.getMagX() + 1) - (this.blockPos.getMagX() - this.dimensions.getMagY() / 2));
			velSlope = this.vel.getSlope();

			//Detect collision
			if ((slope1 > velSlope && slope2 < velSlope) || (slope1 < velSlope && slope2 > velSlope)) {
				return true;
			}
		}

		return false;
	}

	//Calculate the position at which an entity will collide with a block

	//-----------[Summary of Collision Algorithm]-----------\\
	//														\\
	//	1.	Check vertical surfaces (on the left and right)	\\
	//		if the entity is moving horizontally. Check		\\
	//		horizontal surfaces (on the top and bottom) if	\\
	//		the entity is moving vertically.				\\
	//														\\
	//	2.	Find the multiple of the velocity needed in		\\
	//		order to move the outer edge of the entity to	\\
	// 		the x or y coordinate of the collidable			\\
	// 		surface.										\\
	//														\\
	//	3.	Use this multiple to scale the velocity to find	\\
	//		the position of the entity if the x or y		\\
	// 		coordinate of its outer edge is	moved to the x	\\
	//		or y coordinate of the collidable surface.		\\
	//														\\
	//	4.	If the edges of the entity and the collidable	\\
	//		surface are intersecting, the entity has		\\
	//		collided with the surface.						\\
	//														\\
	//------------------------------------------------------\\

	private Vector2D velAfterCollision (BlockCollidable block, IntVector blockPos) {
		//Check whether the entity collides with the block
		if (!collides(block, blockPos)) {
			return this.vel;
		}

		Vector2D newVel = new Vector2D(this.vel);

		//Check side surfaces if the entity's velocity has a horizontal component
		if (this.vel.getMagX() != 0) {
			float surfaceX = blockPos.getMagX();

			//Check right surface if the entity is moving to the left
			if (this.vel.getMagX() < 0) {
				surfaceX++;
			}

			//Find scale factor needed to move the x/y coordinate of the entity to the x/y coordinate of the surface
			float scaleFactor =
					(Math.abs(surfaceX - this.blockPos.getMagX()) -
					this.dimensions.getMagX() / 2) / this.vel.getMagX();

			// If the velocity needed in order to move the entity
			// is greater than the current velocity or is in the
			// other direction, the entity does not collide.

			if (scaleFactor >= 0 && scaleFactor <= 1) {
				newVel = Vector2D.scale(this.vel, scaleFactor);

				if (Math.abs(blockPos.getMagY() + 0.5 - Vector2D.add(this.blockPos, newVel).getMagY()) < this.dimensions.getMagY() / 2 + 0.5) {
					newVel.setMagY(this.vel.getMagY());
				}
			}
		}

		//Check top/bottom surfaces if the entity's velocity has a vertical component
		if (this.vel.getMagY() != 0) {
			float surfaceY = blockPos.getMagY();

			//Check top surface if the entity is moving down
			if (this.vel.getMagY() < 0) {
				surfaceY++;
			}

			float scaleFactor =
					(Math.abs(surfaceY - this.blockPos.getMagY()) -
					this.dimensions.getMagY() / 2) / vel.getMagY();

			// If the velocity needed in order to move the entity
			// is greater than the current velocity or is in the
			// other direction, the entity does not collide.

			if (scaleFactor >= 0 && scaleFactor <= 1) {
				newVel = Vector2D.scale(this.vel, scaleFactor);

				if (Math.abs(blockPos.getMagX() + 0.5 - Vector2D.add(this.blockPos, newVel).getMagX()) < this.dimensions.getMagX() / 2 + 0.5) {
					newVel.setMagX(this.vel.getMagX());
				}
			}
		}

		//Return original velocity if entity does not collide with block
		return newVel;
	}

	//Get an array of all the blocks that are possibly in between the entity and its destination

	private Block[][] getBlocksInPath () {
		//Find a pair of vectors bounding the entire entity and its destination
		IntVector chunkPos1 = new IntVector(this.chunkPos);
		IntVector chunkPos2 = new IntVector(this.chunkPos);
		Vector2D blockPos1 = new Vector2D(this.blockPos);
		Vector2D blockPos2 = new Vector2D(this.blockPos);

		//Change the block positions to account for the entity's dimensions based on its velocity

		if (this.vel.getMagX() > 0) {
			blockPos1.setMagX(blockPos1.getMagX() - this.dimensions.getMagX() / 2);
			blockPos2.setMagX(blockPos2.getMagX() + this.dimensions.getMagX() / 2);
		} else {
			blockPos1.setMagX(blockPos1.getMagX() + this.dimensions.getMagX() / 2);
			blockPos2.setMagX(blockPos2.getMagX() - this.dimensions.getMagX() / 2);
		}

		if (this.vel.getMagY() > 0) {
			blockPos1.setMagY(blockPos1.getMagY() - this.dimensions.getMagY() / 2);
			blockPos2.setMagY(blockPos2.getMagY() + this.dimensions.getMagY() / 2);
		} else {
			blockPos1.setMagY(blockPos1.getMagY() + this.dimensions.getMagY() / 2);
			blockPos2.setMagY(blockPos2.getMagY() - this.dimensions.getMagY() / 2);
		}

		//Update chunk position if entity will pass between chunks
		Entity.updateChunkPos(chunkPos1, blockPos1);
		Entity.updateChunkPos(chunkPos2, blockPos2);

		//Get array of blocks bounded by the two positions
		return chunkLoader.getBlocks(
				chunkPos1, new IntVector(blockPos1), chunkPos2, new IntVector(blockPos2)
		);
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
		Vector2D newVel = new Vector2D(vel);

		//Step 1 of movement algorithm
		//Get array of blocks containing the entity and its destination
		Block[][] blocks = getBlocksInPath();

		//Loop through blocks
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				//Step 2 of movement algorithm:
				//Check if block is collidable
				if (blocks[i][j] instanceof BlockCollidable) {

					//Calculate block position
					IntVector blockPos = new IntVector(
							(int) Math.floor(this.blockPos.getMagX()),
							(int) Math.floor(this.blockPos.getMagY())
					);

					//The following calculation is based on the behaviour of ChunkLoader.getBlocks().
					//Add to relative player position if the entity is moving to the right
					//Subtract from relative player position if the entity is moving to the left
					//Add to relative player position if the entity is moving up
					//Subtract from relative player position if the entity is moving down

					if (vel.getMagX() > 0) {
						blockPos.setMagX(blockPos.getMagX() + i);
					} else if (vel.getMagX() < 0) {
						blockPos.setMagX(blockPos.getMagX() - i);
					}

					if (vel.getMagY() > 0) {
						blockPos.setMagY(blockPos.getMagY() + j);
					} else if (vel.getMagY() < 0) {
						blockPos.setMagY(blockPos.getMagY() - j);
					}

					if (this.collides((BlockCollidable) blocks[i][j], blockPos)) {
						vel = new Vector2D(0,0);
					}
				}
			}
		}

		//Step 4 of collision algorithm:
		super.move();
	}
}
