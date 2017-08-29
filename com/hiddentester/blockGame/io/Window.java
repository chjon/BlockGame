/**********************************************************************************
 * This class creates the window that displays graphics and registers mouse input *
 **********************************************************************************/

package com.hiddentester.blockGame.io;

import javax.swing.*;
import java.awt.*;

public class Window {
	private static final String TITLE = "BlockGame";

	//Constructor:

	public Window (String saveFile, Drawing draw) {
		JFrame frame = new JFrame (TITLE + " - " + saveFile);

		//Initialize frame properties
		frame.add(draw);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setMinimumSize(new Dimension(400, 350));
		frame.setVisible(true);
		frame.setBackground(Color.WHITE);
		frame.setAlwaysOnTop(false);
	}
}
