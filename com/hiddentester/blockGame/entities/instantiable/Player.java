/*******************************
 * This class defines a player *
 *******************************/

package com.hiddentester.blockGame.entities.instantiable;

import com.hiddentester.blockGame.core.ChunkLoader;
import com.hiddentester.blockGame.entities.EntityCollidable;
import com.hiddentester.util.IntVector;
import com.hiddentester.blockGame.core.GameClock;
import com.hiddentester.util.Vector2D;
import com.hiddentester.blockGame.core.Game;

public class Player extends EntityCollidable {
	public static final String CLASS_NAME = "Player";
	private static final Vector2D DIMENSIONS = new Vector2D(0.6f, 1.8f);

	//Jumping speed
	private static final float JUMP_SPEED = - 21 * GRAVITY;
	private static final float JUMP_COOLDOWN = 7;
	private float jumpCooldown = 0;

	//Maximum sneaking speed in blocks per second
	private static final float SNEAK_SPEED = 1;

	//Maximum walking speed in blocks per second
	private static final float WALK_SPEED = 4;

	//Maximum sprinting speed in blocks per second
	private static final float SPRINT_SPEED = 8;

	private Game game;

	//Constructor
	public Player (Game game, IntVector chunkPos, Vector2D blockPos, Vector2D vel, ChunkLoader chunkLoader) {
		super(chunkPos, blockPos, vel, DIMENSIONS, chunkLoader, false, false);
		this.game = game;
	}

	//Custom movement method - move the player if controls are pressed

	//-[Explanation of acceleration calculation]-\\
	// 											 \\
	// Velocity is changed using the formula:	 \\
	// v2 = f(v1 + a)							 \\
	// 											 \\
	// v2	:= new velocity						 \\
	// v1	:= original velocity				 \\
	// f	:= scaling due to friction			 \\
	// a	:= change in velocity				 \\
	// 											 \\
	// The desired maximum velocity occurs 		 \\
	// when v2 = v1. Letting v1 = v2 = v and	 \\
	// solving for a yields the equation:		 \\
	// a = v(1 - f) / f							 \\
	// 											 \\
	// This formula yields the acceleration in	 \\
	// blocks per second per second. Since this	 \\
	// calculation occurs every entity tick,	 \\
	// the unit must be converted to blocks per	 \\
	// entity tick per second. Therefore:		 \\
	// a = a = v(1 - f) / f	/ ETPS				 \\
	//											 \\
	// ETPS := Entity ticks per second			 \\
	//											 \\
	//-------------------------------------------\\

	@Override
	public void move () {
		//Update jump cooldown
		if (this.onGround && jumpCooldown > 0) {
			jumpCooldown--;
		} else if (!this.onGround) {
			jumpCooldown = JUMP_COOLDOWN;
		}

		//Select movement speed based on keys pressed
		float selectedSpeed = WALK_SPEED;

		if (game.getKeyboard().isDown(16)) {
			selectedSpeed = SNEAK_SPEED;
		} else if (game.getKeyboard().isDown(17)) {
			selectedSpeed = SPRINT_SPEED;
		}

		//Amount by which to change velocity per entity tick
		float acceleration = selectedSpeed * (1 - FRICTION) / FRICTION / GameClock.ENTITY_TICKS_PER_SECOND;

		//Jump
		if (game.getKeyboard().isDown(87) || game.getKeyboard().isDown(38)) {
			if (this.onGround && jumpCooldown == 0) {
				this.accelerate(new Vector2D(0, JUMP_SPEED - EntityCollidable.GRAVITY));
				jumpCooldown = JUMP_COOLDOWN;
			}
		}

		//Move down
		if (game.getKeyboard().isDown(83) || game.getKeyboard().isDown(40)) {
			//this.accelerate(new Vector2D(0, -acceleration));
		}

		if (Math.abs(vel.getMagX()) < selectedSpeed / GameClock.ENTITY_TICKS_PER_SECOND) {
			//Move right
			if (game.getKeyboard().isDown(68) || game.getKeyboard().isDown(39)) {
				this.accelerate(new Vector2D(acceleration, 0));
			}

			//Move left
			if (game.getKeyboard().isDown(65) || game.getKeyboard().isDown(37)) {
				this.accelerate(new Vector2D(-acceleration, 0));
			}
		}

		super.move();
	}
}
