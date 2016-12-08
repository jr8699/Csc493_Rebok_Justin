package com.rebok.gdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.rebok.gdx.game.WorldController;
import com.rebok.gdx.game.WorldRenderer;
import com.rebok.gdx.game.util.GamePreferences;
import com.rebok.gdx.game.util.Highscores;

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
	public Highscores scores = Highscores.instance;
	public String currentLevel;

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
	    Gdx.gl.glClearColor(223 / 255.0f, 31 / 255.0f,41 / 255.0f, 0 / 255.0f);
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
	    worldController = new WorldController(game,currentLevel);
	    worldRenderer = new WorldRenderer(worldController);
	    Gdx.input.setCatchBackKey(true);
	}

	/**
	 * Hide the screen
	 */
	@Override
	public void hide () {
		    worldRenderer.dispose();
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