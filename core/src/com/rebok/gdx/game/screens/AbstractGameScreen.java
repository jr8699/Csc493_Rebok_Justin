package com.rebok.gdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.rebok.gdx.game.Assets;

/**
 * An abstract screen
 * @author Justin
 *
 */
public abstract class AbstractGameScreen implements Screen {
	protected Game game; //the game
	
	//Constructor
	public AbstractGameScreen (Game game) {
	    this.game = game;
	}
	
	//implimented in children
	public abstract void render (float deltaTime);
	public abstract void resize (int width, int height);
	public abstract void show ();
	public abstract void hide ();
	public abstract void pause ();
	
	//android resume
	public void resume () {
	    Assets.instance.init(new AssetManager());
	}
	
	/**
	 * Destroy the object
	 */
	public void dispose () {
	    Assets.instance.dispose();
	}
}
