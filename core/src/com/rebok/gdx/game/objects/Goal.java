package com.rebok.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.rebok.gdx.game.Assets;

/**
 * The goal object of the game. Game ends when the player interacts with this
 * @author jr8699
 *
 */
public class Goal extends AbstractGameObject{

	private TextureRegion regGoal; //for the texture
	public boolean collected; //whether the goal was collected
	public boolean toRemove;

	public Goal () {
		init();
	}

	/**
	 * Constructor
	 */
	private void init () {
	    dimension.set(0.5f, 0.5f);

	    stateTime = MathUtils.random(0.0f, 1.0f);
	    regGoal = Assets.instance.goal.goalAsset;
	    // Set bounding box for collision detection
	    bounds.set(0, 0, dimension.x, dimension.y);
	    collected = false;
	    toRemove = false;
	}

	/**
	 * Renders the gold coin
	 * @param batch
	 */
	public void render (SpriteBatch batch) {
		if (collected) return;
		TextureRegion reg = null;
		reg = regGoal;
		batch.draw(reg.getTexture(),
					position.x, position.y,
					origin.x, origin.y,
					dimension.x, dimension.y,
					scale.x, scale.y,
					rotation,
					reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(),
					false, false);
	}

}
