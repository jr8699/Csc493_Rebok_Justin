package com.rebok.gdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.rebok.gdx.game.objects.Goal;
import com.rebok.gdx.game.objects.GoldCoin;
import com.rebok.gdx.game.objects.IceBlock;
import com.rebok.gdx.game.objects.LavaBlock;
import com.rebok.gdx.game.objects.Rock;
import com.rebok.gdx.game.objects.WaterPlayer;
import com.rebok.gdx.game.screens.GameScreen;
import com.rebok.gdx.game.screens.MenuScreen;
import com.rebok.gdx.game.util.AudioManager;
import com.rebok.gdx.game.util.CameraHelper;
import com.rebok.gdx.game.util.Constants;
import com.rebok.gdx.game.util.HighScoreHelper;
import com.rebok.gdx.game.util.Highscores;

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
	public int score; //Current score
	public float livesVisual; //visual lives
	public float scoreVisual; //visual score

	private float timeLeftGameOverDelay; //level time left

	public Array<Body> objectsToRemove; //for box2d physics
	public World myWorld; //box2d world

	private Game game; //the game

	//for the sensors
	private short coinMask = 0x001;
	private short playerMask = 0x002;

	private boolean enteredScore; //score from prompt

	private String currentLevel; //this is the beginning level
	
	public ParticleEffect lavaParticle; //take a particle so it does not get destroyed

	//Constructor
	public WorldController(Game game, String level) {
		this.game = game;
		init(level);
	}


	//Constructor
	private void initLevel(){
		score = Highscores.instance.currentScore;
		scoreVisual = score;
		level = new Level(currentLevel);
		cameraHelper.setTarget(level.waterPlayer);
		initPhysics();
	}

	/**
	 * Constructor code
	 */
	private void init(String level) {
		Highscores.instance.currentScore = 0;
		currentLevel = level;
		enteredScore = false;
		objectsToRemove = new Array<Body>();
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		livesVisual = lives;
		timeLeftGameOverDelay = 0;
		Gdx.input.setInputProcessor(this);
		initLevel();
	}

	/**
	 * Initialize the physics
	 * @author Dr.Girard, Justin
	 */
	private void initPhysics()
	{
		if (myWorld != null)
			myWorld.dispose();
		myWorld = new World(new Vector2(0, -9.81f), true);
		myWorld.setContactListener(new CollisionHandler(this));  // Not in the book
		Vector2 origin = new Vector2();
		for (Rock pieceOfLand : level.rocks)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(pieceOfLand.position);
			bodyDef.type = BodyType.KinematicBody;
			Body body = myWorld.createBody(bodyDef);
			//body.setType(BodyType.DynamicBody);
			body.setUserData(pieceOfLand);
			pieceOfLand.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = pieceOfLand.bounds.width / 2.0f;
			origin.y = pieceOfLand.bounds.height / 2.0f;
			polygonShape.setAsBox(pieceOfLand.bounds.width / 2.0f, (pieceOfLand.bounds.height-0.04f) / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}

		//our goal object
		Goal goal = level.goal;
		BodyDef bodyDefgoal = new BodyDef();
		bodyDefgoal.position.set(goal.position);
		bodyDefgoal.type = BodyType.StaticBody;
		Body bodygoal = myWorld.createBody(bodyDefgoal);
		bodygoal.setUserData(goal);
		goal.body = bodygoal;
		PolygonShape polygonShapegoal = new PolygonShape();
		origin.x = goal.bounds.width / 2.0f;
		origin.y = goal.bounds.height / 2.0f;
		polygonShapegoal.setAsBox(goal.bounds.width / 2.0f, (goal.bounds.height-0.04f) / 2.0f, origin, 0);
		FixtureDef fixtureDefgoal = new FixtureDef(); //make the coins a sensor
		fixtureDefgoal.isSensor = true;
		fixtureDefgoal.filter.categoryBits = coinMask; //use coin mask
		fixtureDefgoal.filter.maskBits = playerMask;
		fixtureDefgoal.shape = polygonShapegoal;
		bodygoal.createFixture(fixtureDefgoal);
		polygonShapegoal.dispose();

		for (GoldCoin coin : level.goldcoins)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(coin.position);
			bodyDef.type = BodyType.StaticBody;
			Body body = myWorld.createBody(bodyDef);
			body.setUserData(coin);
			coin.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = coin.bounds.width / 2.0f;
			origin.y = coin.bounds.height / 2.0f;
			polygonShape.setAsBox(coin.bounds.width / 2.0f, (coin.bounds.height-0.04f) / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef(); //make the coins a sensor
			fixtureDef.isSensor = true;
			fixtureDef.filter.categoryBits = coinMask;
			fixtureDef.filter.maskBits = playerMask;
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}

		for (LavaBlock lava : level.lavaBlocks)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(lava.position);
			bodyDef.type = BodyType.StaticBody;
			Body body = myWorld.createBody(bodyDef);
			body.setUserData(lava);
			lava.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = lava.bounds.width / 2.0f;
			origin.y = lava.bounds.height / 2.0f;
			polygonShape.setAsBox(lava.bounds.width / 2.0f, (lava.bounds.height-0.04f) / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef(); //make the coins a sensor
			fixtureDef.isSensor = true;
			fixtureDef.filter.categoryBits = coinMask;
			fixtureDef.filter.maskBits = playerMask;
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}

		for (IceBlock ice : level.iceBlocks)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(ice.position);
			bodyDef.type = BodyType.StaticBody;
			Body body = myWorld.createBody(bodyDef);
			body.setUserData(ice);
			ice.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = ice.bounds.width / 2.0f;
			origin.y = ice.bounds.height / 2.0f;
			polygonShape.setAsBox(ice.bounds.width / 2.0f, (ice.bounds.height-0.04f) / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef(); //make the coins a sensor
			fixtureDef.isSensor = true;
			fixtureDef.filter.categoryBits = coinMask;
			fixtureDef.filter.maskBits = playerMask;
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}

		// For PLayer
		WaterPlayer player = level.waterPlayer;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(player.position);
		bodyDef.fixedRotation = false;
		Body body = myWorld.createBody(bodyDef);
		body.setType(BodyType.DynamicBody);
		body.setGravityScale(5.00f);
		body.setUserData(player);
		player.body = body;

		PolygonShape polygonShape = new PolygonShape();
		origin.x = (player.bounds.width) / 2.0f;
		origin.y = (player.bounds.height) / 2.0f;
		polygonShape.setAsBox((player.bounds.width) / 2.0f, (player.bounds.height) / 2.0f, origin, 0);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.filter.categoryBits = playerMask;
		fixtureDef.filter.maskBits = coinMask;
		 fixtureDef.friction = 0.5f;
		body.createFixture(fixtureDef);
		polygonShape.dispose();
	}

	/**
	 * Flag an object for removal, box2d
	 * @param obj
	 */
	public void flagForRemoval(Body obj)
	{
		objectsToRemove.add(obj);
	}

	/**
	 * Get the game status depending on the player's lives
	 * @return
	 */
	public boolean isGameOver () {
		if(lives < 0) return true;
		return false;
	}

	/**
	 * Has the player collected the goal?
	 * @return
	 */
	public boolean isGoalCollected(){
		if(level.goal.collected) return true;
		else return false;
	}
	/**
	 * Find out if the player hit the water
	 * @return
	 */
	public boolean isPlayerInWater () {
		return level.waterPlayer.position.y < -5;
	}

	/**
	 * Add a high score to the highscores file.
	 * Also preserves the state of the score between live loss
	 */
	public void addHighScore(){
		if(lives >= 0){ //keep playing the game
			if(level.goal.collected && !enteredScore){ //no more levels
				HighScoreHelper listener = new HighScoreHelper();
				Gdx.input.getTextInput(listener, "High Scores", "Enter your name", "");
				enteredScore = true;
				//backToMenu();

			}
			if(Highscores.instance.currentScore < score){ //keep going
				Highscores.instance.currentScore = score;
			}
		}else{//bring up to prompt to enter a name
			if(!enteredScore){
				HighScoreHelper listener = new HighScoreHelper();
				Gdx.input.getTextInput(listener, "High Scores", "Enter your name", "");
				enteredScore = true;
			}
		}
	}


	/**
	 * Goes back to the menu screen
	 */
	private void backToMenu () {
		// switch to menu screen
		addHighScore();
		//myWorld.dispose();
		game.setScreen(new MenuScreen(game));
	}

	/**
	 * Used for debugging, also includes movement of sprites and the camera
	 * @param deltaTime
	 */
	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop) return;
		if (!cameraHelper.hasTarget(level.waterPlayer)) {
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
		  if (cameraHelper.hasTarget(level.waterPlayer)) {
		    // Player Movement
			  if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				  level.waterPlayer.left = true;
			  }else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				  level.waterPlayer.right = true;
			  }
			  // player Jump
			  if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) {
				  level.waterPlayer.setJumping(true);
			  } else {
				  level.waterPlayer.setJumping(false);
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
		if(lavaParticle != null) //grab particle from a lava object so it doesnt get destroyed
			lavaParticle.update(deltaTime);
		if(isGoalCollected()){ //switch levels
			if(currentLevel == Constants.LEVEL_01){
				if(Highscores.instance.currentScore < score){
					Highscores.instance.currentScore = score;
				}
				currentLevel = Constants.LEVEL_02;
				initLevel();
			}else{ //end of levels
				backToMenu();
			}
		}
		
		if (isGameOver()) { //player died
		timeLeftGameOverDelay -= deltaTime;
		if (timeLeftGameOverDelay< 0)
			backToMenu();
		} else {
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		myWorld.step(deltaTime, 8, 3);
		for(Body b : objectsToRemove){ //remove old gold coins
			if(b.getUserData() != null){
				if(b.getUserData() instanceof GoldCoin){
					if(((GoldCoin)b.getUserData()).toRemove == true){
						myWorld.destroyBody(b);
						b.setUserData(null);
						b = null;
					}
				}else if(b.getUserData() instanceof LavaBlock){
					if(((LavaBlock)b.getUserData()).toRemove == true){
						myWorld.destroyBody(b);
						b.setUserData(null);
						b = null;
					}
				}
			}
		}
		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInWater()) {
			lives--;
			if(Highscores.instance.currentScore < score){ //update the highscores score
				Highscores.instance.currentScore = score;
			}
			AudioManager.instance.play(Assets.instance.sounds.liveLost);
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
	 */
	  @Override
	public boolean keyUp (int keycode) {
	      // Reset game world
	    if (keycode == Keys.R) {
	    	init(currentLevel);
	    	Gdx.app.debug(TAG, "Game world resetted");
	    }
	    // Back to Menu
	    else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
	      backToMenu();
	    }
	    return false;
	}
}
