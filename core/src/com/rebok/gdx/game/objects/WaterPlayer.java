package com.rebok.gdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.rebok.gdx.game.Assets;
import com.rebok.gdx.game.util.AudioManager;
import com.rebok.gdx.game.util.Constants;
import com.rebok.gdx.game.util.GamePreferences;
import com.rebok.gdx.game.util.CharacterSkin;

public class WaterPlayer extends AbstractGameObject {
	public static final String TAG = WaterPlayer.class.getName(); //libgdx tag
	private final float JUMP_TIME_MAX = 0.6f; //max time of a jump
	private final float JUMP_TIME_MIN = 0.1f; //min time of a jump
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;
	public ParticleEffect dustParticles = new ParticleEffect(); //dust
	
	public enum VIEW_DIRECTION { LEFT, RIGHT }
	
	public enum JUMP_STATE { GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING }
	
	private TextureRegion regHead;
	
	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasLavaPowerup;
	public float timeLeftLavaPowerup;

	public WaterPlayer () {
	    init();
	}
	
	/**
	 * Constructor
	 */
	public void init () {
		dimension.set(1, 1);
		regHead = Assets.instance.waterPlayer.waterPlayer;
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		// Bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		// Set physics values
		terminalVelocity.set(3.0f, 4.0f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -25.0f);
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		// Jump state
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;
		// Power-ups
		hasLavaPowerup = false;
		timeLeftLavaPowerup = 0;
		// Particles
		dustParticles.load(Gdx.files.internal("../rebok-gdx-game-core/assets/particles/dust.pfx"),  
		Gdx.files.internal("../rebok-gdx-game-core/assets/particles"));
		
	}
	
	/**
	 * Control the current state of a character's jump
	 * @param jumpKeyPressed
	 */
	public void setJumping (boolean jumpKeyPressed) {
		switch (jumpState) {
		
		    case GROUNDED: // Character is standing on a platform
		    	if (jumpKeyPressed) {
		    		AudioManager.instance.play(Assets.instance.sounds.jump);
		    		// Start counting jump time from the beginning
		    		timeJumping = 0;
		    		jumpState = JUMP_STATE.JUMP_RISING;
		    	}
		    	break;
		    	
		    case JUMP_RISING: // Rising in the air
		    	if (!jumpKeyPressed)
		    		jumpState = JUMP_STATE.JUMP_FALLING;
		    	break;
		    	
		    case FALLING:// Falling down
		    	
		    case JUMP_FALLING: // Falling down after jump
		        if (jumpKeyPressed && hasLavaPowerup) {
		            AudioManager.instance.play(Assets.instance.sounds.jumpWithLava, 1,  
		            MathUtils.random(1.0f, 1.1f));
		            timeJumping = JUMP_TIME_OFFSET_FLYING;
		            jumpState = JUMP_STATE.JUMP_RISING;
		        }
		        break;
		}
	}
	
	/**
	 * For when a player picks up the feather powerup
	 * @param pickedUp
	 */
	public void setLavaPowerup (boolean pickedUp) {
		hasLavaPowerup = pickedUp;
		if (pickedUp) {
		    timeLeftLavaPowerup = Constants.ITEM_FEATHER_POWERUP_DURATION;
		}
	}
	
	/**
	 * Return whether the player has a feather or not
	 * @return
	 */
	public boolean hasLavaPowerup () {
		return hasLavaPowerup && timeLeftLavaPowerup > 0;
	}
	
	/**
	 * Reset speed when a lava block runs out
	 */
	private void resetSpeed(){
		terminalVelocity.x = 3;
		terminalVelocity.y = 4;
	}
	
	/**
	 * Enhance the players speed from a lava block
	 */
	private void enhanceSpeed(){
		terminalVelocity.x = 6;
		terminalVelocity.y = 8;
	}
	
	/**
	 * Update function, overridden to also change the view direction
	 * @author Dr.Girard, Justin
	 */
	@Override
	public void update (float deltaTime) {
		updateMotionX(deltaTime);
		updateMotionY(deltaTime);
		if (body != null)
		{
			body.setLinearVelocity(velocity);
			position.set(body.getPosition());
		}
		if (velocity.x != 0) {
			viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;
		}
		if (timeLeftLavaPowerup > 0) {
			enhanceSpeed(); //double the players speed
			timeLeftLavaPowerup -= deltaTime;
			if (timeLeftLavaPowerup < 0) {
				// disable power-up
				timeLeftLavaPowerup = 0;
				resetSpeed(); //take away the speed boost
				setLavaPowerup(false);
			}
		}
}
	
	/**
	 * Overridden to include jump state in our y axis calculations
	 */
	@Override
	protected void updateMotionY (float deltaTime) {
		switch (jumpState) {
	    	case GROUNDED:
	    		jumpState = JUMP_STATE.FALLING;
	    		if (velocity.x != 0) {
	    			dustParticles.setPosition(position.x + dimension.x / 2,  
	    			position.y);
	    			dustParticles.start();
	    		}
	    		break;
	    	case JUMP_RISING:
	    		// Keep track of jump time
	    		timeJumping += deltaTime;
	    		// Jump time left?
	    		if (timeJumping <= JUMP_TIME_MAX) {
	    			// Still jumping
	    			velocity.y = terminalVelocity.y;
	    		}
	    		break;
	    	case FALLING:
	    		break;
	    	case JUMP_FALLING:
	    		// Add delta times to track jump time
	    		timeJumping += deltaTime;
	    		// Jump to minimal height if jump key was pressed too short
	    		if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) {
	    			// Still jumping
	    			velocity.y = terminalVelocity.y;
	    		}
		}
		if (jumpState != JUMP_STATE.GROUNDED){
			super.updateMotionY(deltaTime);
			dustParticles.allowCompletion();
		}
	}
	
	/**
	 * Render our water player
	 */
	@Override
	public void render (SpriteBatch batch) {
		TextureRegion reg = null;
		// Set special color when game object has a feather power-up
		// Draw Particles
		dustParticles.draw(batch);
		// Apply Skin Color
		batch.setColor(CharacterSkin.values()[GamePreferences.instance.charSkin].getColor());
		
		if (hasLavaPowerup) {
			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
		}
		// Draw image
		reg = regHead;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x,  
					origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,  
					reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),  
					reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT, false);
		// Reset color to white
		batch.setColor(1, 1, 1, 1);
	}
}