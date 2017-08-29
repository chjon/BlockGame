/****************************
 * This class runs the game *
 ****************************/

package com.hiddentester.blockGame.core;

import com.hiddentester.math.Vector2D;
import com.hiddentester.blockGame.entities.Player;

public class Main {
	public static void main (String[] args) {
		Game game = new Game();
		Player player = game.getPlayer();

		while (true) {
			try {
				Thread.sleep(10);

				if (game.getKeyboard().isDown(87) || game.getKeyboard().isDown(38)) {
					player.move(new Vector2D(0, Player.WALK_SPEED));
				}

				if (game.getKeyboard().isDown(83) || game.getKeyboard().isDown(40)) {
					player.move(new Vector2D(0, -Player.WALK_SPEED));
				}

				if (game.getKeyboard().isDown(68) || game.getKeyboard().isDown(39)) {
					player.move(new Vector2D(Player.WALK_SPEED, 0));
				}

				if (game.getKeyboard().isDown(65) || game.getKeyboard().isDown(37)) {
					player.move(new Vector2D(-Player.WALK_SPEED, 0));
				}
			} catch (Exception e) {

			}
		}
	}
}
