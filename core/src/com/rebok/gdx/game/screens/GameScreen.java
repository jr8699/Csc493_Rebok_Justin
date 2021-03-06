package com.rebok.gdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.rebok.gdx.game.WorldController;
import com.rebok.gdx.game.WorldRenderer;
import com.rebok.gdx.game.util.GamePreferences;

/**
 * The game screen when playing
 * @author Justin
 *
 */
public class GameScreen extends AbstractGameScreen {
	private static final String TAG = GameScreen.class.getName();
	private WorldController worldController; //the world controller
	private WorldRenderer worldRenderer; //the world renderer
	private boolean paused; //if paused
	
	//Constructor
	public GameScreen (Game game) {
	    super(game);
	}
	
	/**
	 * Render the Game screen
	 */
	@Override
	public void render (float deltaTime) {
	    // Do not update game world when paused.
		if (!paused) {
			// Update game world by the time that has passed
			// since last rendered frame.
			worldController.update(deltaTime);
	    }
	    // Sets the clear screen color to: Cornflower Blue
	    Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed / 255.0f, 0xff / 255.0f);
	    // Clears the screen
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    // Render game world to screen
	    worldRenderer.render();
	}
	
	/**
	 * resize the window
	 */
	@Override
	public void resize (int width, int height) {
	    worldRenderer.resize(width, height);
	}
	
	/**
	 * Shows the screen
	 */
	@Override
	public void show () {
		GamePreferences.instance.load();
	    worldController = new WorldController(game);
	    worldRenderer = new WorldRenderer(worldController);
	    Gdx.input.setCatchBackKey(true);
	}
	
	/**
	 * Hide the screen
	 */
	@Override
	public void hide () {
		    worldRenderer.dispose();
		    worldController.dispose();
		    Gdx.input.setCatchBackKey(false);
	}
	
	//android pause
	@Override
	public void pause () {
		paused = true;
	}
	
	//android resume
	@Override
	public void resume () {
		super.resume();
		// Only called on Android!
		paused = false;
	}
}
