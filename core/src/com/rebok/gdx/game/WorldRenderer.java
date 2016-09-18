package com.rebok.gdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.rebok.gdx.game.util.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Renders our world
 * @author Justin
 *
 */
public class WorldRenderer implements Disposable{
	private OrthographicCamera camera; //The camera
	private SpriteBatch batch; //Sprites
	private WorldController worldController; //World controller instance
	  
	public WorldRenderer(WorldController worldController) {
		this.worldController = worldController;
		init();
	}
	
	/**
	 * Constructor code
	 */
	private void init() {
	    batch = new SpriteBatch();
	    camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
	    camera.position.set(0, 0, 0);
	    camera.update();
	}
	
	/**
	 * Render our testing objects
	 */
	public void render() {
		renderTestObjects();
	}
	
	/**
	 * Render our testing objects
	 */
	private void renderTestObjects() {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		for(Sprite sprite : worldController.testSprites) {
			sprite.draw(batch);
		}
		
		batch.end();
	}
	
	/**
	 * Resize the world
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height) {
	    camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) *  width;
	    camera.update();
	}
	
	/**
	 * Destroy objects
	 */
	@Override
	public void dispose() {
		batch.dispose();
	}
}
