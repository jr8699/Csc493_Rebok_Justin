package com.rebok.gdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.assets.AssetManager;
import com.rebok.gdx.game.Assets;
import com.rebok.gdx.game.screens.MenuScreen;
import com.rebok.gdx.game.util.AudioManager;
import com.rebok.gdx.game.util.GamePreferences;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main function of our game
 * @author Justin
 *
 */
public class RebokGdxGame extends Game {
	  @Override
	  /**
	   * Initialize everything that we need
	   */
	  public void create () {
	    // Set Libgdx log level 
	    Gdx.app.setLogLevel(Application.LOG_DEBUG);
	    // Load assets
	    Assets.instance.init(new AssetManager());
	    // Load preferences for audio settings and start playing music
	    GamePreferences.instance.load();
	    AudioManager.instance.play(Assets.instance.music.song01);
	    // Start game at menu screen
	    setScreen(new MenuScreen(this));
	  }
}
