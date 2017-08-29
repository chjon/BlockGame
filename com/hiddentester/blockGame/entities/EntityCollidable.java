/*************************************************************
 * This class defines an entity that can collide with blocks *
 *************************************************************/

package com.hiddentester.blockGame.entities;

import com.hiddentester.math.Vector2D;

public abstract class EntityCollidable extends Entity {
	//Constructor
	public EntityCollidable (Vector2D chunkPos, Vector2D relPos, Vector2D vel, Vector2D dimensions) {
		super(chunkPos, relPos, vel, dimensions);
	}
}
