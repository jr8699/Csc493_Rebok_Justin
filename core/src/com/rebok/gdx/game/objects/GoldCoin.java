package com.rebok.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rebok.gdx.game.Assets;

/**
 * A gold coin that the player can collect
 * @author Justin
 *
 */
public class GoldCoin extends AbstractGameObject {
	private TextureRegion regGoldCoin; //for the texture
	public boolean collected; //whether the coin was collected or not
	
	public GoldCoin () {
		init();
	}
	
	/**
	 * Constructor
	 */
	private void init () {
	    dimension.set(0.5f, 0.5f);
	    regGoldCoin = Assets.instance.goldCoin.goldCoin;
	    // Set bounding box for collision detection
	    bounds.set(0, 0, dimension.x, dimension.y);
	    collected = false;
	}
	
	/**
	 * Renders the gold coin
	 * @param batch
	 */
	public void render (SpriteBatch batch) {
	    if (collected) return;
	    TextureRegion reg = null;
	    reg = regGoldCoin;
	    batch.draw(reg.getTexture(), position.x, position.y,  
	    			origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,  
	    			rotation, reg.getRegionX(), reg.getRegionY(),  
	    			reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
	
	/**
	 * Controls the amount of points the player earns per coin
	 * @return
	 */
	public int getScore() {
	    return 100;
	}
}