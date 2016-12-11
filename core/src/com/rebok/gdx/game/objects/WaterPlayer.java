package com.rebok.gdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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

	private TextureRegion regHead;
	
	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public boolean hasLavaPowerup;
	public float timeLeftLavaPowerup;
	public float timeLeftIcePowerup;
	public boolean hasIcePowerup;
	
	public boolean jump;
	public boolean canJump;
	public boolean right; //movement
	public boolean left;
	
	public WaterPlayer () {
	    init();
	}
	
	/**
	 * Constructor
	 */
	public void init () {
		canJump = true;
		right = false;
		left = false;
		dimension.set(1, 1);
		regHead = Assets.instance.waterPlayer.waterPlayer;
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		// Bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		// Set physics values
		terminalVelocity.set(10.0f, 15.0f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -25.0f);
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		timeJumping = 0;
		// Power-ups
		hasLavaPowerup = false;
		timeLeftLavaPowerup = 0;
		hasIcePowerup = false;
		timeLeftIcePowerup = 0;
		// Particles
		dustParticles.load(Gdx.files.internal("../rebok-gdx-game-core/assets/particles/dust.pfx"),  
		Gdx.files.internal("../rebok-gdx-game-core/assets/particles"));
		
	}
	
	/**
	 * Control the current state of a character's jump
	 * @param jumpKeyPressed
	 */
	public void setJumping (boolean jumpKeyPressed) {
		//AudioManager.instance.play(Assets.instance.sounds.jump);
		if(jumpKeyPressed && canJump){
			AudioManager.instance.play(Assets.instance.sounds.jump);
			jump = true;
		}else{
			jump = false;
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
	private void resetSpeed(){ terminalVelocity.set(10.0f,15.0f); }
	
	/**
	 * Enhance the players speed from a lava block
	 */
	private void enhanceSpeed(){ terminalVelocity.set(15.0f,20.0f); }
	
	/**
	 * Enhance the players speed from an ice block
	 */
	private void denhanceSpeed(){ terminalVelocity.set(5.0f,10.0f); }
	
	/**
	 * For when a player picks up the feather powerup
	 * @param pickedUp
	 */
	public void setIcePowerup (boolean pickedUp) {
		hasIcePowerup = pickedUp;
		if (pickedUp) {
		    timeLeftIcePowerup = Constants.ITEM_FEATHER_POWERUP_DURATION;
		}
	}
	
	
	/**
	 * Update function, overridden to also change the view direction
	 * @author Dr.Girard, Justin
	 */
	@Override
	public void update (float deltaTime) {
		dustParticles.update(deltaTime);
		updateMotionX(deltaTime);
		updateMotionY(deltaTime);
		
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
		if (timeLeftIcePowerup > 0) {
			denhanceSpeed(); //double the players speed
			timeLeftIcePowerup -= deltaTime;
			if (timeLeftIcePowerup < 0) {
				// disable power-up
				timeLeftIcePowerup = 0;
				resetSpeed(); //take away the speed boost
				setIcePowerup(false);
			}
		}
	}
	
	/**
	 * Overridden for box2d
	 */
	@Override
	public void updateMotionX(float delta){
		if (body != null)
		{
			if(right){ //move right
				body.applyLinearImpulse(new Vector2(0.75f,0.0f), origin, true);
				right = false;
			}else if(left){ //move left
				body.applyLinearImpulse(new Vector2(-0.75f,0.0f), origin, true);
				left = false;
			}
			//Cap velocity if it is higher than the terminal velocity
			if(body.getLinearVelocity().x > terminalVelocity.x){//right
				body.setLinearVelocity(new Vector2(terminalVelocity.x,0.0f));
				body.setLinearVelocity(body.getLinearVelocity().sub(new Vector2(0.5f,2.5f))); //fall more
			}else if(body.getLinearVelocity().x < -terminalVelocity.x){ //left
				body.setLinearVelocity(new Vector2(-terminalVelocity.x,0.0f));
				body.setLinearVelocity(body.getLinearVelocity().sub(new Vector2(-0.5f,2.5f))); //fall more
			}
			if(body.getLinearVelocity().x > 0){ //slow down if no keys pressed
				body.setLinearVelocity(body.getLinearVelocity().sub(new Vector2(0.25f,0.0f)));
			}else if(body.getLinearVelocity().x < 0){
				body.setLinearVelocity(body.getLinearVelocity().sub(new Vector2(-0.25f,0.0f)));
			}
		position.set(body.getPosition());
		dustParticles.setPosition(position.x, position.y);
		}
	}
	
	/**
	 * Overridden to include jump state in our y axis calculations
	 */
	@Override
	protected void updateMotionY (float deltaTime) {
		if (body != null)
		{
			if(jump) //jump
				body.applyLinearImpulse(new Vector2(0.0f,3.0f), origin, true);
			
			if(body.getLinearVelocity().y > terminalVelocity.y){ //cap at terminal velocity
				body.setLinearVelocity(new Vector2(0.0f,terminalVelocity.y));
				canJump = false; //prevent player from jumping again
			}
			position.set(body.getPosition());
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
			batch.setColor(1.0f, 0.0f, 0.0f, 1.0f);
		}
		if (hasIcePowerup) {
			batch.setColor(0.0f, 0.0f, 1.0f, 1.0f);
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
