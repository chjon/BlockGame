/*******************************
 * This class renders the game *
 *******************************/

package com.hiddentester.blockGame.io;

import javax.swing.*;
import java.awt.*;

import com.hiddentester.util.IntVector;
import com.hiddentester.util.Vector2D;
import com.hiddentester.blockGame.blocks.Block;
import com.hiddentester.blockGame.core.Game;
import com.hiddentester.blockGame.entities.instantiable.Player;
import com.hiddentester.blockGame.core.Chunk;

public class Drawing extends JComponent {
	private Game game;

	private static final int FIELD_OF_VIEW = 32;			//This defines how many blocks fit across the window
	private int blockSize;									//This is the size of a block in pixels

	//Texture variables
	private static final String TEXTURE_DIRECTORY =			//This is the relative path to the textures directory
			"BlockGame/res/textures";
	private static final String BLOCK_DIRECTORY =			//This is the name of the block textures subdirectory
			"blocks";
	private static final String ENTITY_DIRECTORY =			//This is the name of the entity texture subdirectory
			"entities";
	private ImageIcon[] blockTextures;						//This is an array of block textures to use
	private ImageIcon[] entityTextures;						//This is an array of entity textures to use

	//Debug variables:

	//This determines whether chunk outlines should be drawn
	private static final boolean DEBUG_DRAW_CHUNK_OUTLINES = false;

	//Constructor
	public Drawing (Game game) {
		super();
		this.game = game;
		updateScale();
		blockTextures = loadBlockTextures(BLOCK_DIRECTORY);
		entityTextures = loadEntityTextures(ENTITY_DIRECTORY);
	}

	//Load block textures
	private ImageIcon[] loadBlockTextures (String folderName) {
		String[] blockNames = game.getBlockNames();
		ImageIcon[] textures = new ImageIcon[blockNames.length];

		for (int i = 0; i < textures.length; i++) {
			textures[i] = new ImageIcon(
					TEXTURE_DIRECTORY + "/" + folderName + "/" + blockNames[i] + ".png"
			);
		}

		return textures;
	}

	//Load block textures
	private ImageIcon[] loadEntityTextures (String folderName) {
		String[] blockNames = game.getEntityNames();
		ImageIcon[] textures = new ImageIcon[blockNames.length];

		for (int i = 0; i < textures.length; i++) {
			textures[i] = new ImageIcon(
					TEXTURE_DIRECTORY + "/" + folderName + "/" + blockNames[i] + ".png"
			);
		}

		return textures;
	}

	//Converts mouse click coordinates to block coordinates
	Vector2D getClickPos (Vector2D clickPos) {
		Vector2D centre = new Vector2D(getWidth() / 2, getHeight() / 2);
		Vector2D offset = Vector2D.sub(clickPos, centre);
		offset = Vector2D.scale(offset, 1.0f / blockSize);
		offset.setMagY(-offset.getMagY());

		IntVector chunkPos = new IntVector(game.getPlayer().getChunkPos());
		Vector2D relPos = Vector2D.add(offset, game.getPlayer().getBlockPos());

		//Update chunkPos if the mouse is in a different chunk than the player

		if (relPos.getMagX() < 0 || relPos.getMagX() >= Chunk.SIZE) {
			chunkPos.setMagX(chunkPos.getMagX() + (int) Math.floor(relPos.getMagX() / Chunk.SIZE));
		}

		if (relPos.getMagY() < 0 || relPos.getMagY() >= Chunk.SIZE) {
			chunkPos.setMagY(chunkPos.getMagY() + (int) Math.floor(relPos.getMagY() / Chunk.SIZE));
		}

		//Take the modulo of each component of the vector
		relPos.setMagX((relPos.getMagX() % Chunk.SIZE + Chunk.SIZE) % Chunk.SIZE);
		relPos.setMagY((relPos.getMagY() % Chunk.SIZE + Chunk.SIZE) % Chunk.SIZE);

		return new Vector2D(
				chunkPos.getMagX() * Chunk.SIZE + relPos.getMagX(),
				chunkPos.getMagY() * Chunk.SIZE + relPos.getMagY()
		);
	}

	//Calculate scaling of the block
	private void updateScale () {
		blockSize = this.getWidth() / FIELD_OF_VIEW;
	}

	//Paint method
	@Override
	public void paint (Graphics g) {
		updateScale();

		//Fill background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		//Offset graphics
		g.translate(this.getWidth() / 2, this.getHeight() / 2);

		Player player = game.getPlayer();
		Chunk[][] loadedChunks = game.getChunkLoader().getLoadedChunks();

		//Draw player
		Vector2D relDim = Vector2D.scale(player.getDimensions(), blockSize);

		g.drawImage(
				entityTextures[0].getImage(),
				(int) (-0.5 * relDim.getMagX()),
				(int) (0.5 * relDim.getMagY()) - (int) (relDim.getMagY()),
				(int) (relDim.getMagX()),
				(int) (relDim.getMagY()),
				this
		);

		//Loop through each chunk
		for (int chunk_i = 0; chunk_i < loadedChunks.length; chunk_i++) {
			for (int chunk_j = 0; chunk_j < loadedChunks[chunk_i].length; chunk_j++) {
				//Do not draw while chunk is being loaded
				try {
					Chunk curChunk = loadedChunks[chunk_i][chunk_j];

					Block[][] blocks = curChunk.getBlocks();

					//Loop through each block
					for (int i = 0; i < blocks.length; i++) {
						for (int j = 0; j < blocks[i].length; j++) {

							//Get and scale position of block relative to player
							Vector2D relPos = Vector2D.sub(new Vector2D(
									Chunk.SIZE * (curChunk.getPos().getMagX() - player.getChunkPos().getMagX()),
									Chunk.SIZE * (curChunk.getPos().getMagY() - player.getChunkPos().getMagY())),
									player.getBlockPos()
							);

							relPos.setMagX(relPos.getMagX() + i);
							relPos.setMagY(relPos.getMagY() + j);
							relPos = Vector2D.scale(relPos, blockSize);

							//Select texture

							int blockID = blocks[i][j].getBlockID();

							if (blockID == -1) {
								g.setColor(Color.ORANGE);

								//Draw block
								g.fillRect(
										(int) (relPos.getMagX()),
										-(int) (relPos.getMagY()),
										blockSize,
										-blockSize
								);
							} else {
								g.drawImage(
										blockTextures[blockID].getImage(),
										(int) (relPos.getMagX()),
										-(int) (relPos.getMagY()) - blockSize,
										blockSize,
										blockSize,
										this
								);
							}
						}
					}
				} catch (NullPointerException e) {

				}
			}
		}

		//Mark chunk
		if (DEBUG_DRAW_CHUNK_OUTLINES) {
			g.setColor(Color.RED);
			g.translate(
					-(int) (player.getBlockPos().getMagX() * blockSize),
					(int) (player.getBlockPos().getMagY() * blockSize)
			);

			for (int i = 0; i <= Chunk.SIZE; i += 2) {
				g.drawLine(0, -i * blockSize, Chunk.SIZE * blockSize, -i * blockSize);
				g.drawLine(i * blockSize, 0, i * blockSize, -Chunk.SIZE * blockSize);
			}

			//Reset graphics translation
			g.translate(
					(int) (player.getBlockPos().getMagX() * blockSize),
					-(int) (player.getBlockPos().getMagY() * blockSize)
			);
			g.translate(-this.getWidth() / 2, -this.getHeight() / 2);

			//Mark centre of screen
			g.setColor(Color.GREEN);
			g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
			g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
		}
	}
}