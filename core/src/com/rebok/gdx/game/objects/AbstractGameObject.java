package com.rebok.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Abstract class for our game objects
 * @author Justin
 *
 */
public abstract class AbstractGameObject {
    public Vector2 position; //Postition of the object
    public Vector2 dimension; //Dimension of the object
    public Vector2 origin; //Origin of the object
    public Vector2 scale; //Object's scale
    public float rotation; //Objects rotation
    
    public Vector2 velocity; //objects current speed
    public Vector2 terminalVelocity; //max neg and pos speed
    public Vector2 friction; //friction of the object
    public Vector2 acceleration; //object's current acceleration
    public Rectangle bounds; //for collision detection
    
    //Constructor
    public AbstractGameObject () {
        position = new Vector2();
        dimension = new Vector2(1, 1);
        origin = new Vector2();
        scale = new Vector2(1, 1);
        rotation = 0;
        velocity = new Vector2();
        terminalVelocity = new Vector2(1, 1);
        friction = new Vector2();
        acceleration = new Vector2();
        bounds = new Rectangle();
    }
    
    /**
     * Update method for objects, called by our world controller
     * @param deltaTime
     */
    public void update (float deltaTime) {
        updateMotionX(deltaTime);
        updateMotionY(deltaTime);
        // Move to new position
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    }
    
    /**
     * Render method for objects, called by our world renderer
     * @param batch
     */
    public abstract void render (SpriteBatch batch);
    
    /**
     * Updates the physics attributes of the object on the x axis
     * @param deltaTime
     */
    protected void updateMotionX (float deltaTime) {
    	if (velocity.x != 0) {
    		// Apply friction
    		if (velocity.x > 0) {
    			velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
    		} else {
    	    	velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
    		}
    	}
    	// Apply acceleration
    	velocity.x += acceleration.x * deltaTime;
    	// Make sure the object's velocity does not exceed the
    	// positive or negative terminal velocity
    	velocity.x = MathUtils.clamp(velocity.x,
    	    -terminalVelocity.x, terminalVelocity.x);
    	}
    
    /**
     * Updates the physics attributes of the object on the y axis
     * @param deltaTime
     */
    protected void updateMotionY (float deltaTime) {
    	  if (velocity.y != 0) {
    		  // Apply friction
    		  if (velocity.y > 0) {
    			  velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
    		  } else {
    			  velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);
    		  }
    	  }
    	  // Apply acceleration 
    	  velocity.y += acceleration.y * deltaTime;
    	  // Make sure the object's velocity does not exceed the
    	  // positive or negative terminal velocity
    	  velocity.y = MathUtils.clamp(velocity.y, - terminalVelocity.y, terminalVelocity.y);
    }
}
