/**********************************************************************************
 * This class creates the window that displays graphics and registers mouse input *
 **********************************************************************************/

package com.hiddentester.blockGame.io;

import com.hiddentester.blockGame.core.Game;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Color;

public class Window {
	private static final String TITLE = "BlockGame";

	//Constructor:

	public Window (Game game, String saveFile, Drawing draw) {
		JFrame frame = new JFrame (TITLE + " - " + saveFile);

		//Initialize frame properties
		frame.add(draw);
		frame.setSize(800, 600);
		frame.setMinimumSize(new Dimension(400, 350));
		frame.setVisible(true);
		frame.setBackground(Color.WHITE);
		frame.setAlwaysOnTop(false);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				game.saveGame();
				System.exit(0);
			}
		});
	}
}
