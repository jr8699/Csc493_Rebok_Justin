package com.rebok.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

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
    
    public Vector2 velocity; //obj's velocity
    public Vector2 terminalVelocity; //obj's max velocity
    public Vector2 friction; //obj's friction
    public Vector2 acceleration; //obj's current acceleration
    public Rectangle bounds; //obj's collision bounds
    
    // Box2D Physics
 	public Body body;
 	
 	public float stateTime; //animations
 	public Animation animation;
    
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
     * Set the animation
     * @param animation
     */
    public void setAnimation (Animation animation) {
    	this.animation = animation;
    	stateTime = 0;
    }
    
    /**
     * Update method for objects, called by our world controller
     * @param deltaTime
     * @author Dr.Girard, Justin
     */
    public void update (float deltaTime) {
		stateTime += deltaTime;
		if (body == null)
		{
			updateMotionX(deltaTime);
			updateMotionY(deltaTime);

			position.x += velocity.x * deltaTime;
			position.y += velocity.y * deltaTime;
		}
		else
		{
			position.set(body.getPosition());
			rotation = body.getAngle() * MathUtils.radiansToDegrees;
		}
    }
    
    /**
     * Render method for objects, called by our world renderer
     * @param batch
     */
    public abstract void render (SpriteBatch batch);
    
    /**
     * Updates the physics attributes of the object on the x axis
     * @param deltaTime
     * @author Dr.Girard, Justin
     */
    protected void updateMotionX (float deltaTime) {
		if (velocity.x != 0)
		{
			if (velocity.x > 0)
			{
				velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
			}
			else
			{
				velocity.x = Math.min(velocity.x + friction.x*deltaTime, 0);
			}
		}
		
		velocity.x += acceleration.x*deltaTime;
		velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);
    }
    
    /**
     * Updates the physics attributes of the object on the y axis
     * @param deltaTime
     * @author Dr.Girard, Justin
     */
    protected void updateMotionY (float deltaTime) {
		if (velocity.y != 0)
		{
			//System.out.println("VELOCITY");
			if (velocity.y > 0)
			{
				velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
			}
			else
			{
				velocity.y = Math.min(velocity.y + friction.y*deltaTime, 0);
			}
		}
		
		velocity.y += acceleration.y*deltaTime;
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
}
}