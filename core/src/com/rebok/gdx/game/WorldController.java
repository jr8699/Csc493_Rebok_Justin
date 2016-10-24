package com.rebok.gdx.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.rebok.gdx.game.objects.BunnyHead;
import com.rebok.gdx.game.objects.BunnyHead.JUMP_STATE;
import com.rebok.gdx.game.objects.Feather;
import com.rebok.gdx.game.objects.GoldCoin;
import com.rebok.gdx.game.objects.Rock;
import com.rebok.gdx.game.screens.MenuScreen;
import com.rebok.gdx.game.util.AudioManager;
import com.rebok.gdx.game.util.CameraHelper;
import com.rebok.gdx.game.util.Constants;

/**
 * Controls the world
 * @author Justin
 *
 */
public class WorldController extends InputAdapter{
	private static final String TAG = WorldController.class.getName(); //ligGDX tag
	public CameraHelper cameraHelper; //CameraHelper instance
	public Level level; //Our current level
	public int lives; //Current lives left
	public float livesVisual; //lives visualized
	public int score; //Current score
	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	private float timeLeftGameOverDelay; //level time left
	private Game game;
	public float scoreVisual; //score visual
	
	//Constructor
	public WorldController (Game game) {
		this.game = game;
		init();
	}
	
	
	//Constructor
	private void initLevel () {
		score = 0;
		scoreVisual = score;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.bunnyHead);
	}  
	
	/**
	 * Constructor code
	 */
	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
	    lives = Constants.LIVES_START;
	    timeLeftGameOverDelay = 0;
	    initLevel();
	}
	
	private void backToMenu () {
		// switch to menu screen
		game.setScreen(new MenuScreen(game));
	}
	
	/**
	 * Get the game status depending on the player's lives
	 * @return
	 */
	public boolean isGameOver () {
		return lives < 0;
	}
	
	/**
	 * Find out if the player hit the water
	 * @return
	 */
	public boolean isPlayerInWater () {
		return level.bunnyHead.position.y < -5;
	}
	
	/**
	 * Used for debugging, also includes movement of sprites and the camera
	 * @param deltaTime
	 */
	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop) return;

		if (!cameraHelper.hasTarget(level.bunnyHead)) {
			// Camera Controls (move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) 
				camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT)) 
				moveCamera(-camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) 
				moveCamera(camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.UP)) 
				moveCamera(0, camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN)) 
				moveCamera(0, -camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE))  
				cameraHelper.setPosition(0, 0);
		}
	    // Camera Controls (zoom)
	    float camZoomSpeed = 1 * deltaTime;
	    float camZoomSpeedAccelerationFactor = 5;
	    if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) 
	    	camZoomSpeed *= camZoomSpeedAccelerationFactor;
	    if (Gdx.input.isKeyPressed(Keys.COMMA)) 
	    	cameraHelper.addZoom(camZoomSpeed);
	    if (Gdx.input.isKeyPressed(Keys.PERIOD))
	    	cameraHelper.addZoom(-camZoomSpeed);
	    if (Gdx.input.isKeyPressed(Keys.SLASH)) 
	    	cameraHelper.setZoom(1);
	}
	
	/**
	 * Handle input. Movement, etc.
	 * @param deltaTime
	 */
	private void handleInputGame (float deltaTime) {
		  if (cameraHelper.hasTarget(level.bunnyHead)) {
		    // Player Movement
			  if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				  level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
			  }else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				  level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
			  } else {
				  // Execute auto-forward movement on non-desktop platform
				  if (Gdx.app.getType() != ApplicationType.Desktop) {
					  level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
				  }
			  }
			  // Bunny Jump
			  if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) {
				  level.bunnyHead.setJumping(true);
			  } else {
				  level.bunnyHead.setJumping(false);
			  }
		  }
	}
	
	/**
	 * Move the Camera
	 * @param x
	 * @param y
	 */
	private void moveCamera (float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}
	
	/**
	 * Frame update, update our test sprites and the camera
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		if (isGameOver()) {
			timeLeftGameOverDelay -= deltaTime;
		if (timeLeftGameOverDelay< 0)
			backToMenu();
		} else {
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);
		if (!isGameOver() &&isPlayerInWater()) {
			AudioManager.instance.play(Assets.instance.sounds.liveLost);
			lives--;
		if (isGameOver())
			timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
		else
			initLevel();
		}
		level.mountains.updateScrollPosition(cameraHelper.getPosition());
		if (livesVisual> lives)
			livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
		if (scoreVisual< score)
			scoreVisual = Math.min(score, scoreVisual + 250 * deltaTime);
	}
	
	/**
	 * Keyboard input
	 * Space - next sprite
	 * Enter - target follow
	 * ESC - menu
	 */
	@Override
	public boolean keyUp (int keycode) {
	    // Reset game world
	    if (keycode == Keys.R) {
	    	init();
	        Gdx.app.debug(TAG, "Game world resetted");
	    }
	    // Toggle camera follow
	    else if (keycode == Keys.ENTER) {
	    	cameraHelper.setTarget(cameraHelper.hasTarget() ? null: level.bunnyHead);
	    	Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
	    }
	    // Back to Menu
	    else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
	    	backToMenu();
	    }
	    return false;
	}
	
	/**
	 * Testing a bunny against a rock
	 * @param rock
	 */
	private void onCollisionBunnyHeadWithRock(Rock rock) {
		BunnyHead bunnyHead = level.bunnyHead;
		float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));
		if (heightDifference > 0.25f) {
		    boolean hitRightEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
		    if (hitRightEdge) {
		    	bunnyHead.position.x = rock.position.x + rock.bounds.width;
		    } else {
		      bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
		    }
		    return;
		}
		switch (bunnyHead.jumpState) {
		    case GROUNDED:
		    	break;
		    case FALLING:
		    case JUMP_FALLING:
		    	bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height  + bunnyHead.origin.y;
		    	bunnyHead.jumpState = JUMP_STATE.GROUNDED;
		    	break;
		    case JUMP_RISING:
		    	bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
		    break;
		}
	}
	private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin) {
		goldcoin.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
		score += goldcoin.getScore();
		Gdx.app.log(TAG, "Gold coin collected");
	}
	private void onCollisionBunnyWithFeather(Feather feather) {
		feather.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
		score += feather.getScore();
		level.bunnyHead.setFeatherPowerup(true);
		Gdx.app.log(TAG, "Feather collected");
	}
	
	/**
	 * For testing collision between objects
	 */
	private void testCollisions () {
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);
		// Test collision: Bunny Head <-> Rocks
		for (Rock rock : level.rocks) {
			r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyHeadWithRock(rock);
			// IMPORTANT: must do all collisions for valid
			// edge testing on rocks.
		}
		// Test collision: Bunny Head <-> Gold Coins
		for (GoldCoin goldcoin : level.goldcoins) {
			if (goldcoin.collected) continue;
			r2.set(goldcoin.position.x, goldcoin.position.y, goldcoin.bounds.width, goldcoin.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyWithGoldCoin(goldcoin);
			break;
		}
		// Test collision: Bunny Head <-> Feathers
		for (Feather feather : level.feathers) {
			if (feather.collected) continue;
			r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyWithFeather(feather);
			break;
	    }
	}
}
