package com.rebok.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rebok.gdx.game.Assets;

/**
 * A Lava Block that the player can collect, provides score and speed boost
 * @author Justin
 *
 */
public class LavaBlock extends AbstractGameObject {
	private TextureRegion regLavaBlock; //for the texture
	public boolean collected; //whether the block has been collected or not
	  
	public LavaBlock() {
	    init();
	}
	
	/**
	 * Constructor
	 */
	private void init () {
	    dimension.set(0.5f, 0.5f);
	    regLavaBlock = Assets.instance.blocks.lavaBlock;
	    // Set bounding box for collision detection
	    bounds.set(0, 0, dimension.x, dimension.y);
	    collected = false;
	}
	
	/**
	 * Renders the lava block
	 */
	public void render (SpriteBatch batch) {
	    if (collected) return;
	    TextureRegion reg = null;
	    reg = regLavaBlock;
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
