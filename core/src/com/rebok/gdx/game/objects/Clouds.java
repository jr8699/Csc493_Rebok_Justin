package com.rebok.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.rebok.gdx.game.Assets;

public class Clouds extends AbstractGameObject{
	  private float length; //Length of the clouds
	  private Array<TextureRegion> regClouds; //Cloud region for textures
	  private Array<Cloud> clouds; //Cloud region of clouds
	  
	  //A single cloud object
	  private class Cloud extends AbstractGameObject {
	    private TextureRegion regCloud; //region
	    
	    public Cloud () {}
	    
	    public void setRegion (TextureRegion region) {
	    	regCloud = region;
	    }
	    
	    @Override
	    public void render (SpriteBatch batch) {
	      TextureRegion reg = regCloud;
	      batch.draw(reg.getTexture(), position.x + origin.x,  
	    		  		position.y + origin.y, origin.x, origin.y, dimension.x,  
	    		  		dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),  
	    		  		reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),  
	    		  		false, false);
	    }
	  }
	  
	  //Constructor
	  public Clouds (float length) {
		  this.length = length;
		  init();
	  }
	  
	  //Constructor
	  private void init () {    
		  dimension.set(3.0f, 1.5f);
		  regClouds = new Array<TextureRegion>();
		  regClouds.add(Assets.instance.levelDecoration.cloud01);
		  regClouds.add(Assets.instance.levelDecoration.cloud02);
		  regClouds.add(Assets.instance.levelDecoration.cloud03);
		  int distFac = 5;
		  int numClouds = (int)(length / distFac);
		  clouds = new Array<Cloud>(2 * numClouds);
		  for (int i = 0; i < numClouds; i++) {
			  Cloud cloud = spawnCloud();
			  cloud.position.x = i * distFac;
			  clouds.add(cloud);
		  }
	  }
	  
	  /**
	   * This method is used to create a new cloud
	   * @return
	   */
	  private Cloud spawnCloud () {
		  Cloud cloud = new Cloud();
		  cloud.dimension.set(dimension);
		  // select random cloud image
		  cloud.setRegion(regClouds.random());
		  // position
		  Vector2 pos = new Vector2();
		  pos.x = length + 10; // position after end of level
		  pos.y += 1.75; // base position
		  pos.y += MathUtils.random(0.0f, 0.2f) * (MathUtils.randomBoolean() ? 1 : -1); // random additional position
	   	  cloud.position.set(pos);
	   	  return cloud;
	  }
	  
	  /**
	   * Renders our clouds
	   */
	  @Override
	  public void render (SpriteBatch batch) {
	    for (Cloud cloud : clouds)
	        cloud.render(batch);
	  }
}