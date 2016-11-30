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
	
	public boolean jump;
	
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
		if(jumpKeyPressed){
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
		super.updateMotionX(deltaTime);
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
}
	
	/**
	 * Overridden to include jump state in our y axis calculations
	 */
	@Override
	protected void updateMotionY (float deltaTime) {
		if (body != null)
		{
			if(jump){
				Vector2 forceUp = new Vector2();
				Vector2 point = origin;
				forceUp.y = 2.0f;
				body.applyLinearImpulse(forceUp, point, true);
			}
			body.applyForceToCenter(velocity, true);
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
