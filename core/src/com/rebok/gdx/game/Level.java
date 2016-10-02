package com.rebok.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.rebok.gdx.game.objects.AbstractGameObject;
import com.rebok.gdx.game.objects.Clouds;
import com.rebok.gdx.game.objects.GoldCoin;
import com.rebok.gdx.game.objects.LavaBlock;
import com.rebok.gdx.game.objects.LavaOverlay;
import com.rebok.gdx.game.objects.Mountains;
import com.rebok.gdx.game.objects.Rock;
import com.rebok.gdx.game.objects.WaterPlayer;

/**
 * Our level loader
 * @author Justin
 *
 */
public class Level {
	public static final String TAG = Level.class.getName(); //libGDX tag
	
	public WaterPlayer waterPlayer; //the player
	public Array<GoldCoin> goldcoins; //all the coins on the level
	public Array<LavaBlock> lavaBlocks; //all the lava blocks on the level
	
	public enum BLOCK_TYPE { //Block type enumerable, All data for the level
	    EMPTY(0, 0, 0), // black
	    ROCK(88, 255, 42), // green
	    PLAYER_SPAWNPOINT(255, 255, 255), // white
	    ITEM_LAVA_BLOCK(255, 17, 17), // RED
	    ITEM_ICE_BLOCK(0, 255, 240), // BLUE/cyan
	    ITEM_GOLD_COIN(246, 255, 0); // yellow
	private int color;
	
	private BLOCK_TYPE (int r, int g, int b) {
		color = r << 24 | g << 16 | b << 8 | 0xff;
	}
	
	public boolean sameColor (int color) {
		return this.color == color;
	}
	
	public int getColor () {
	    return color;
	}
	
	}
	
	// objects
	public Array<Rock> rocks;
	
	// decoration
	public Clouds clouds;
	public Mountains mountains;
	public LavaOverlay lavaOverlay;
	public Level (String filename) {
		init(filename);
	}
	
	/**
	 * Reads our level file
	 * @param filename
	 */
	private void init (String filename) {
		// player character
		waterPlayer = null;
		// objects
	    rocks = new Array<Rock>();
	    goldcoins = new Array<GoldCoin>();
	    lavaBlocks = new Array<LavaBlock>();
	    // load image file that represents the level data
	    Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
	    // scan pixels from top-left to bottom-right
	    int lastPixel = -1;
	    for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
	    	for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
	    			AbstractGameObject obj = null;
	    			float offsetHeight = 0;
	    			// height grows from bottom to top
	    			float baseHeight = pixmap.getHeight() - pixelY;
	    			// get color of current pixel as 32-bit RGBA value
	    			int currentPixel = pixmap.getPixel(pixelX, pixelY);
	    			// find matching color value to identify block type at (x,y)
	    			// point and create the corresponding game object if there is
	    			// a match
	    			// empty space
	    			if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
	    				// do nothing
	    			}
	    			// rock
	    			else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
	    				if (lastPixel != currentPixel) {
	    					obj = new Rock();
	    					float heightIncreaseFactor = 0.25f;
	    					offsetHeight = -2.5f;
	    					obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
	    					rocks.add((Rock)obj);
	    				} else {
	    					rocks.get(rocks.size - 1).increaseLength(1);
	    				}
	    			}
	    			// player spawn point
	    			else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
	    		        obj = new WaterPlayer();
	    		        offsetHeight = -3.0f;
	    		        obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
	    		        waterPlayer = (WaterPlayer)obj;
	    			}
	    			//ice block
	    			else if (BLOCK_TYPE.ITEM_ICE_BLOCK.sameColor(currentPixel)) {
	    			}
	    			//lava block
	    			else if (BLOCK_TYPE.ITEM_LAVA_BLOCK.sameColor(currentPixel)){
	    				obj = new LavaBlock();
	    		        offsetHeight = -1.5f;
	    		        obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
	    		        lavaBlocks.add((LavaBlock)obj);
	    			}
	    			// gold coin
	    			else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
	    		        obj = new GoldCoin();
	    		        offsetHeight = -1.5f;
	    		        obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
	    		        goldcoins.add((GoldCoin)obj);
	    			}
	    			// unknown object/pixel color
	    			else {
	    				int r = 0xff & (currentPixel >>> 24); //red color channel
	    				int g = 0xff & (currentPixel >>> 16); //green color channel
	    				int b = 0xff & (currentPixel >>> 8);  //blue color channel
	    				int a = 0xff & currentPixel;  //alpha channel
	    				Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r+ "> g<" + g + "> b<" + b + "> a<" + a + ">");
	    			}
	        lastPixel = currentPixel;
	    	}
	    }
	    
	    // decoration
	    clouds = new Clouds(pixmap.getWidth());
	    clouds.position.set(0, 2);
	    mountains = new Mountains(pixmap.getWidth());
	    mountains.position.set(-1, -1);
	    lavaOverlay = new LavaOverlay(pixmap.getWidth());
	    lavaOverlay.position.set(0, -3.75f);
	    
	    // free memory
	    pixmap.dispose();
	    Gdx.app.debug(TAG, "level '" + filename + "' loaded");
		
	}
	
	/**
	 * Render game objects like mountains, rocks, etc.
	 * @param batch
	 */
	public void render (SpriteBatch batch) {
	    // Draw Mountains
	    mountains.render(batch);
	    // Draw Rocks
	    for (Rock rock : rocks)
	      rock.render(batch);
	    // Draw Gold Coins
	    for (GoldCoin goldCoin : goldcoins)
	        goldCoin.render(batch);
	    // Draw lavaBlocks
	    for (LavaBlock lava : lavaBlocks)
	    	lava.render(batch);
	    // Draw Player Character
	    waterPlayer.render(batch);
	    // Draw Water Overlay
	    lavaOverlay.render(batch);
	    // Draw Clouds
	    clouds.render(batch);
	}
	
	/**
	 * Update all the objects on the level
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		waterPlayer.update(deltaTime);
		for(Rock rock : rocks)
		    rock.update(deltaTime);
		for(GoldCoin goldCoin : goldcoins)
		    goldCoin.update(deltaTime);
		for(LavaBlock lava : lavaBlocks)
			lava.update(deltaTime);
		clouds.update(deltaTime);
}
}