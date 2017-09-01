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
		//Check side surfaces if the entity's velocity has a horizontal component
		if (this.vel.getMagX() != 0) {
			float blockSurfaceX = blockPos.getMagX();
			float entitySurfaceX = this.blockPos.getMagX();
			float slope1, slope2, velSlope;

			//Select surfaces to check depending on the direction of the entity's movement
			if (this.vel.getMagX() < 0) {
				entitySurfaceX -= this.dimensions.getMagX() / 2;
				blockSurfaceX++;

				if (blockSurfaceX > entitySurfaceX) {
					return false;
				}
			} else {
				entitySurfaceX += this.dimensions.getMagX() / 2;

				if (blockSurfaceX < entitySurfaceX) {
					return false;
				}
			}

			slope1 = ((blockPos.getMagY()) - (this.blockPos.getMagY() + this.dimensions.getMagY() / 2)) / (blockSurfaceX - entitySurfaceX);
			slope2 = ((blockPos.getMagY() + 1) - (this.blockPos.getMagY() - this.dimensions.getMagY() / 2)) / (blockSurfaceX - entitySurfaceX);
			velSlope = this.vel.getMagY() / this.vel.getMagX();

			//Detect collision
			if (!((slope1 > velSlope && slope2 > velSlope) || (slope1 < velSlope && slope2 < velSlope))) {
				System.out.println(blockPos);
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

				if (blockSurfaceY > entitySurfaceY) {
					return false;
				}
			} else {
				entitySurfaceY += this.dimensions.getMagY() / 2;

				if (blockSurfaceY < entitySurfaceY) {
					return false;
				}
			}

			slope1 = ((blockPos.getMagX()) - (this.blockPos.getMagX() + this.dimensions.getMagX() / 2)) / (blockSurfaceY - entitySurfaceY);
			slope2 = ((blockPos.getMagX() + 1) - (this.blockPos.getMagX() - this.dimensions.getMagX() / 2)) / (blockSurfaceY - entitySurfaceY);
			velSlope = this.vel.getMagX() / this.vel.getMagY();

			//Detect collision
			if ((slope1 > velSlope && slope2 < velSlope) || (slope1 < velSlope && slope2 > velSlope)) {
				System.out.println(blockPos);
				return true;
			}
		}

		return false;
	}

	//Find a set of vectors bounding the entire entity and its destination
	private void findBoundingPositions (
			IntVector chunkPos1, Vector2D blockPos1,
			IntVector chunkPos2, Vector2D blockPos2
	) {
		//Change the block positions to account for the entity's dimensions based on its velocity

		if (this.vel.getMagX() > 0) {
			blockPos1.setMagX(blockPos1.getMagX() + this.dimensions.getMagX() / 2);
			blockPos2.setMagX(blockPos2.getMagX() + this.dimensions.getMagX() / 2 + this.vel.getMagX());
		} else if (this.vel.getMagX() < 0) {
			blockPos1.setMagX(blockPos1.getMagX() - this.dimensions.getMagX() / 2);
			blockPos2.setMagX(blockPos2.getMagX() - this.dimensions.getMagX() / 2 + this.vel.getMagX());
		} else {
			blockPos1.setMagX(blockPos1.getMagX() + this.dimensions.getMagX() / 2);
			blockPos2.setMagX(blockPos2.getMagX() - this.dimensions.getMagX() / 2);
		}

		if (this.vel.getMagY() > 0) {
			blockPos1.setMagY(blockPos1.getMagY() + this.dimensions.getMagY() / 2);
			blockPos2.setMagY(blockPos2.getMagY() + this.dimensions.getMagY() / 2 + this.vel.getMagY());
		} else if (this.vel.getMagY() < 0) {
			blockPos1.setMagY(blockPos1.getMagY() - this.dimensions.getMagY() / 2);
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
	// 		surface is in the path of movement.		\\
	//												\\
	//	4.	Change the velocity such that a			\\
	// 		subsequent movement will not surpass	\\
	// 		the discovered surface.					\\
	// 												\\
	//	5.	Move the entity.						\\
	// 												\\
	//----------------------------------------------\\

	@Override
	public void move () {
		//Consider movement only if velocity is not zero
		if (vel.getMagSquared() > 0) {
			Vector2D newVel = new Vector2D(vel);

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

			//Loop through blocks
			for (int i = 0; i < blocks.length; i++) {
				for (int j = 0; j < blocks[i].length; j++) {
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
								(int) (Chunk.SIZE * (chunkPos.getMagX() - this.chunkPos.getMagX()) +
										(blockPos.getMagX() - this.blockPos.getMagX())),
								(int) (Chunk.SIZE * (chunkPos.getMagY() - this.chunkPos.getMagY()) +
										(blockPos.getMagY() - this.blockPos.getMagY()))
						);

						//Step 3 of movement algorithm
						//Check if the entity collides with the block
						if (this.collides((BlockCollidable) blocks[i][j], relBlockPos)) {
							//Step 4 of movement algorithm
							//Restrict entity velocity
							newVel = new Vector2D(0,0);
						}
					}
				}
			}

			vel = newVel;

			//Step 5 of movement algorithm:
			super.move();
		}
	}
}