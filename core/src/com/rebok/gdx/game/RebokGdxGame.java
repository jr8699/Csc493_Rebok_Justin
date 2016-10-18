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
    private static final String TAG = RebokGdxGame.class.getName(); //libGDX tag
    
    private WorldController worldController; //World controller
    private WorldRenderer worldRenderer; //World Renderer
    
    private boolean paused; //Boolean to show game pausation 
    
    /**
     * Initialize everything needed to run the game
     */
    @Override public void create() {
        // Set Libgdx log level 
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        // Load assets
        Assets.instance.init(new AssetManager());
        // Start game at menu screen
        setScreen(new MenuScreen(this));
    }
    
    /**
     * Render the game
     */
    @Override public void render() { 
        // Do not update game world when paused.
    	
        if (!paused) {
        	// Update game world by the time that has passed
        	// since last rendered frame.
        	worldController.update(Gdx.graphics.getDeltaTime());
        }
        
        // Sets the clear screen color to: red
        Gdx.gl.glClearColor(123/255f, 43/255f, 50/255f, 1); //Set the background color to a red
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Render game world to screen
        worldRenderer.render();
    }
    
    /**
     * Resize the game
     */
    @Override public void resize(int width, int height) {
    	worldRenderer.resize(width, height);
    }
    
    /**
     * For mobile purposes. Pauses the app instance
     */
    @Override public void pause() {
    	paused = true;
    }
    
    /**
     * For mobile purposes. Resumes app instance
     */
    @Override public void resume() {
    	paused = false;
    }
    
    /**
     * Dispose of assets instance and the worldRenderer
     */
    @Override public void dispose() {
    	worldRenderer.dispose();
    	Assets.instance.dispose();
    }
}
