package com.rebok.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rebok.gdx.game.Assets;

public class LavaOverlay extends AbstractGameObject{
	  private TextureRegion regLavaOverlay; //WaterOverlay region
	  private float length; //Length of the region
	  
	  //Constructor
	  public LavaOverlay (float length) {
	      this.length = length;
	      init();
	  }
	  
	  //Constructor
	  private void init () {
		  dimension.set(length * 10, 3);
		  regLavaOverlay = Assets.instance.levelDecoration.lavaOverlay;
		  origin.x = -dimension.x / 2;
	  }
	  
	  /**
	   * Renders our Water overlay
	   */
	  @Override
	  public void render (SpriteBatch batch) {
	    TextureRegion reg = null;
	    reg = regLavaOverlay;
	    batch.draw(reg.getTexture(), position.x + origin.x, position.y  
	    			+ origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,  
	    			scale.y, rotation, reg.getRegionX(), reg.getRegionY(),  
	    			reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	  }
}