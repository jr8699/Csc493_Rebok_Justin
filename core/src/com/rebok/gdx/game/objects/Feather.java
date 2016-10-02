package com.rebok.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rebok.gdx.game.Assets;

/**
 * A feather that the player can collect
 * @author Justin
 *
 */
public class Feather extends AbstractGameObject {
	private TextureRegion regFeather; //for the texture
	public boolean collected; //whether the feather has been collected or not
	  
	public Feather () {
	    init();
	}
	
	/**
	 * Constructor
	 */
	private void init () {
	    dimension.set(0.5f, 0.5f);
	    regFeather = Assets.instance.feather.feather;
	    // Set bounding box for collision detection
	    bounds.set(0, 0, dimension.x, dimension.y);
	    collected = false;
	}
	
	/**
	 * Renders the feather
	 */
	public void render (SpriteBatch batch) {
	    if (collected) return;
	    TextureRegion reg = null;
	    reg = regFeather;
	    batch.draw(reg.getTexture(), position.x, position.y,  
	    			origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,  
	    			rotation, reg.getRegionX(), reg.getRegionY(),  
	    			reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
	
	/**
	 * Controls the amount of points that the player will receive when collected
	 * @return
	 */
	public int getScore() {
	    return 250;
	}
}