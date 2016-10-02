package com.rebok.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rebok.gdx.game.Assets;

/**
 * A Rock object
 * @author Justin
 *
 */
public class Rock extends AbstractGameObject{
    private TextureRegion regEdge; //edge of the rock region
    private TextureRegion regMiddle; //the middle of the rock region
    private int length;
    
    //constructor
    public Rock () {
    	init();
    }
    
    //constructor
    private void init () {
        dimension.set(1, 1.5f);
        regEdge = Assets.instance.rock.edge;
        regMiddle = Assets.instance.rock.middle;
        // Start length of this rock
        setLength(1);
    }
    
    //Sets the length of the region
    public void setLength (int length) {
        this.length = length;
        // Update bounding box for collision detection
        bounds.set(0, 0, dimension.x * length, dimension.y);
    }
    
    //Increases the length of the region
    public void increaseLength (int amount) {
        setLength(length + amount);
    }

    /**
     * Method to render our rock
     */
	@Override
	public void render(SpriteBatch batch) {
	    TextureRegion reg = null;
	    float relX = 0;
	    float relY = 0;
	    
	    // Draw left edge
	    reg = regEdge;
	    relX -= dimension.x / 4;
	    batch.draw(reg.getTexture(), position.x + relX, position.y +  
	    			relY, origin.x, origin.y, dimension.x / 4, dimension.y,  
	    			scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),  
	    			reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	    
	    // Draw middle
	    relX = 0;
	    reg = regMiddle;
	    for (int i = 0; i < length; i++) {
	        batch.draw(reg.getTexture(), position.x + relX, position.y  
	        			+ relY, origin.x, origin.y, dimension.x, dimension.y,  
	        			scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),  
	        			reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	        relX += dimension.x;
	    }
	    
	    //Draw right edge
	        reg = regEdge;
	        batch.draw(reg.getTexture(),position.x + relX, position.y +  
	        			relY, origin.x + dimension.x / 8, origin.y, dimension.x / 4,  
	        			dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),  
	        			reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),  
	        			true, false);
	}

}
