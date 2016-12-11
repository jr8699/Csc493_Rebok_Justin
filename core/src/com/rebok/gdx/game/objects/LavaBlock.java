package com.rebok.gdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.rebok.gdx.game.Assets;

/**
 * A Lava Block that the player can collect, provides score and speed boost
 * @author Justin
 *
 */
public class LavaBlock extends AbstractGameObject {
	private TextureRegion regLavaBlock; //for the texture
	public boolean collected; //whether the block has been collected or not
	public boolean toRemove;
	public ParticleEffect fireParticles = new ParticleEffect();
	  
	public LavaBlock() {
	    init();
	}
	
	/**
	 * Constructor
	 */
	private void init () {
	    dimension.set(0.5f, 0.5f);
	    regLavaBlock = Assets.instance.lava.lavaBlock;
	    
	    setAnimation(Assets.instance.lava.animLavaBlock);
	    stateTime = MathUtils.random(0.0f, 1.0f);
	    // Set bounding box for collision detection
	    bounds.set(0, 0, dimension.x, dimension.y);
	    collected = false;
	    toRemove = false;
	    
	    // Particles
	 	fireParticles.load(Gdx.files.internal("../rebok-gdx-game-core/assets/particles/fireParticle.pfx"),  
	 	Gdx.files.internal("../rebok-gdx-game-core/assets/particles"));
	 	fireParticles.start();
	}
	
	/**
	 * Renders the lava block
	 */
	public void render (SpriteBatch batch) {
	    if (collected) return;
	    TextureRegion reg = null;
	    fireParticles.draw(batch);
		reg = animation.getKeyFrame(stateTime, true);
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
	
	/**
	 * Controls the amount of points that the player will receive when collected
	 * @return
	 */
	public int getScore() {
	    return 250;
	}
	
	/**
	 * Override to add a particle effect
	 */
	@Override
	public void update(float deltaTime){
		//if(fireParticles.isComplete())
		fireParticles.update(deltaTime);
		//else
		//	fireParticles.allowCompletion();
		super.update(deltaTime);
	}
}
