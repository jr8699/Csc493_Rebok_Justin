package com.rebok.gdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.assets.AssetManager;
import com.rebok.gdx.game.Assets;
import com.rebok.gdx.game.screens.MenuScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RebokGdxGame extends Game {
	  @Override
	  public void create () {
	    // Set Libgdx log level 
	    Gdx.app.setLogLevel(Application.LOG_DEBUG);
	    // Load assets
	    Assets.instance.init(new AssetManager());
	    // Start game at menu screen
	    setScreen(new MenuScreen(this));
	  }
}
