/*******************************
 * This class defines a player *
 *******************************/

package com.hiddentester.blockGame.entities;

import com.hiddentester.blockGame.core.GameClock;
import com.hiddentester.math.Vector2D;
import com.hiddentester.blockGame.core.Game;
import com.hiddentester.blockGame.core.Chunk;

public class Player extends EntityCollidable {
	private static final Vector2D DIMENSIONS = new Vector2D(0.6, 1.8);

	//Maximum sneaking speed in blocks per second
	private static final double SNEAK_SPEED = 1;

	//Maximum walking speed in blocks per second
	private static final double WALK_SPEED = 3;

	//Maximum sprinting speed in blocks per second
	private static final double SPRINT_SPEED = 9;

	private Game game;

	//Constructor
	public Player (Game game, Vector2D chunkPos, Vector2D relPos, Vector2D vel) {
		super(chunkPos, relPos, vel, DIMENSIONS);
		this.game = game;
	}

	//Custom movement method - move the player if controls are pressed
	/**[Explanation of acceleration calculation]**
	 * 											 *
	 * Velocity is changed using the formula:	 *
	 * v2 = f(v1 + a)							 *
	 * 											 *
	 * v2	:= new velocity						 *
	 * v1	:= original velocity				 *
	 * f	:= scaling due to friction			 *
	 * a	:= change in velocity				 *
	 * 											 *
	 * The desired maximum velocity occurs 		 *
	 * when v2 = v1. Letting v1 = v2 = v and	 *
	 * solving for a yields the equation:		 *
	 * a = v(1 - f) / f							 *
	 * 											 *
	 * This formula yields the acceleration in	 *
	 * blocks per second per second. Since this	 *
	 * calculation occurs every entity tick,	 *
	 * the unit must be converted to blocks per	 *
	 * entity tick per second. Therefore:		 *
	 * a = a = v(1 - f) / f	/ ETPS				 *
	 *											 *
	 * ETPS := Entity ticks per second			 *
	 *********************************************/
	@Override
	public void move () {
		double selectedSpeed = WALK_SPEED;

		if (game.getKeyboard().isDown(16)) {
			selectedSpeed = SNEAK_SPEED;
		} else if (game.getKeyboard().isDown(17)) {
			selectedSpeed = SPRINT_SPEED;
		}

		//Amount by which to change velocity per entity tick
		double acceleration = selectedSpeed * (1 - FRICTION) / FRICTION / GameClock.ENTITY_TICKS_PER_SECOND;

		if (game.getKeyboard().isDown(87) || game.getKeyboard().isDown(38)) {
			game.getPlayer().accelerate(new Vector2D(0, acceleration));
		}

		if (game.getKeyboard().isDown(83) || game.getKeyboard().isDown(40)) {
			game.getPlayer().accelerate(new Vector2D(0, -acceleration));
		}

		if (game.getKeyboard().isDown(68) || game.getKeyboard().isDown(39)) {
			game.getPlayer().accelerate(new Vector2D(acceleration, 0));
		}

		if (game.getKeyboard().isDown(65) || game.getKeyboard().isDown(37)) {
			game.getPlayer().accelerate(new Vector2D(-acceleration, 0));
		}

		super.move();
	}
}
