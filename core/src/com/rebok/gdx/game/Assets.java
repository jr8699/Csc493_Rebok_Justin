package com.rebok.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.rebok.gdx.game.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Assets implements Disposable, AssetErrorListener {
	public static final String TAG = Assets.class.getName(); //libGDX tag
    public static final Assets instance = new Assets(); //Assets instance (singleton)
    private AssetManager assetManager; //asset manager instance
    
    public AssetWaterPlayer waterPlayer; //water player asset
    public AssetRock rock; //Rock asset
    public AssetGoldCoin goldCoin; //Coin asset
    public AssetBlocks blocks; //Feather asset
    public AssetLevelDecoration levelDecoration; //Level decorations asset
    public AssetFonts fonts;
    
    public AssetSounds sounds;
    public AssetMusic music;
    
    
    // singleton: prevent instantiation from other classes
    private Assets () {}
    
    /**
     * Initialize the asset manager and assets
     * @param assetManager
     */
    public void init (AssetManager assetManager) {
    	this.assetManager = assetManager;
	    // set asset manager error handler
        assetManager.setErrorListener(this);
	    // load texture atlas
	    assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
	    
	    // load sounds
	    assetManager.load("../rebok-gdx-game-core/assets/sounds/jump.wav", Sound.class);
	    assetManager.load("../rebok-gdx-game-core/assets/sounds/jump_with_lava.wav", Sound.class);
	    assetManager.load("../rebok-gdx-game-core/assets/sounds/pickup_coin.wav", Sound.class);
	    assetManager.load("../rebok-gdx-game-core/assets/sounds/pickup_lava.wav", Sound.class);
	    assetManager.load("../rebok-gdx-game-core/assets/sounds/live_lost.wav", Sound.class);
	    
	    // load music
	    assetManager.load("../rebok-gdx-game-core/assets/music/keith303_-_brand_new_highscore.mp3", Music.class);
	    
	    // start loading assets and wait until finished
	    assetManager.finishLoading();
	    Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
	    for (String a : assetManager.getAssetNames())
	    	Gdx.app.debug(TAG, "asset: " + a);
	    
	    TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
	    
	    // enable texture filtering for pixel smoothing
	    for (Texture t : atlas.getTextures()) {
	    	t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	    }
	    
	    // create game resource objects
	    waterPlayer = new AssetWaterPlayer(atlas);
	    rock = new AssetRock(atlas);
	    goldCoin = new AssetGoldCoin(atlas);
	    blocks = new AssetBlocks(atlas);
	    fonts = new AssetFonts();
	    levelDecoration = new AssetLevelDecoration(atlas);
	    sounds = new AssetSounds(assetManager);
	    music = new AssetMusic(assetManager);
	}
    
    /**
     * For destroying the assetManager
     */
	@Override
	public void dispose () {
		assetManager.dispose();
	}
	
	/**
	 * For logging errors
	 * @param filename
	 * @param type
	 * @param throwable
	 */
	public void error (String filename, Class type, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception)throwable);
	}
	
	/**
	 * Also used for logging errors
	 * @param asset
	 * @param throwable
	 */
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
	}
	
	/**
	 * The bunny head class
	 * @author Justin
	 *
	 */
	public class AssetWaterPlayer {
	    public final AtlasRegion waterPlayer; //player
	    
	    /**
	     * Constructor for the water player
	     * @param atlas
	     */
	    public AssetWaterPlayer (TextureAtlas atlas) {
	    	waterPlayer = atlas.findRegion("player");
	    }
	}
	
	/**
	 * Rock assets
	 * @author Justin
	 *
	 */
	public class AssetRock {
	    public final AtlasRegion ground; //walkable rock
	    
	    /**
	     * Constructor for the rock
	     * @param atlas
	     */
	    public AssetRock (TextureAtlas atlas) {
	        ground = atlas.findRegion("ground");
	    }
	}
	
	/**
	 * Coin asset
	 * @author Justin
	 *
	 */
	public class AssetGoldCoin {
	    public final AtlasRegion goldCoin; //gold coin
	    
	    /**
	     * Constructor for the coin
	     * @param atlas
	     */
	    public AssetGoldCoin (TextureAtlas atlas) {
	        goldCoin = atlas.findRegion("goldCoin5");
	    }
	}
	
	/**
	 * Feather asset
	 * @author Justin
	 *
	 */
	public class AssetBlocks {
	    public final AtlasRegion ice; //ice
	    public final AtlasRegion lavaBlock; //lavablock
	    
	    /**
	     * Constructor for the feather asset
	     * @param atlas
	     */
	    public AssetBlocks (TextureAtlas atlas) {
	        ice = atlas.findRegion("ice_block");
	        lavaBlock = atlas.findRegion("lava_block");
	    }
	}
	
	/**
	 * Level decorations
	 * @author Justin
	 *
	 */
	public class AssetLevelDecoration {
	    public final AtlasRegion cloud01; //Cloud 1
	    public final AtlasRegion cloud02; //Cloud 2
	    public final AtlasRegion cloud03; //Cloud 3
	    public final AtlasRegion mountain; //mountain
	    public final AtlasRegion lavaOverlay; //water
	    
	    /**
	     * Constructor for the level decorations
	     * @param atlas
	     */
	    public AssetLevelDecoration (TextureAtlas atlas) {
	    	cloud01 = atlas.findRegion("cloud-1");
	    	cloud02 = atlas.findRegion("cloud-1");
	    	cloud03 = atlas.findRegion("cloud-1");
	        mountain = atlas.findRegion("background_game");
	        lavaOverlay = atlas.findRegion("lava_dangerous");
	    }
	}
	
	/**
	 * Our fonts for text in the game
	 * @author Justin
	 *
	 */
	public class AssetFonts {
		  public final BitmapFont defaultSmall;
		  public final BitmapFont defaultNormal;
		  public final BitmapFont defaultBig;
		  
		  public AssetFonts () {
			  // create three fonts using Libgdx's 15px bitmap font
		      defaultSmall = new BitmapFont(Gdx.files.internal("assets-raw/images/arial-15.fnt"), true);
		      defaultNormal = new BitmapFont(Gdx.files.internal("assets-raw/images/arial-15.fnt"), true);
		      defaultBig = new BitmapFont(Gdx.files.internal("assets-raw/images/arial-15.fnt"), true);
		      // set font sizes
		      defaultSmall.getData().setScale(.75f,.75f);
		      defaultNormal.getData().setScale(1.0f,1.0f);
		      defaultBig.getData().setScale(2.0f,2.0f);
		      // enable linear texture filtering for smooth fonts
		      defaultSmall.getRegion().getTexture().setFilter(
		          TextureFilter.Linear, TextureFilter.Linear);
		      defaultNormal.getRegion().getTexture().setFilter(
		          TextureFilter.Linear, TextureFilter.Linear);
		      defaultBig.getRegion().getTexture().setFilter(
		          TextureFilter.Linear, TextureFilter.Linear);
		    }
	}
	
	/**
	 * Asset controlling sounds for jumping, pikcing up coins, etc.
	 * @author Justin
	 *
	 */
	public class AssetSounds {
		public final Sound jump; //sounds of all the actions
		public final Sound jumpWithLava;
		public final Sound pickupCoin;
		public final Sound pickupLava;
		public final Sound liveLost;
		
		/**
		 * Load all the sounds
		 * @param am
		 */
		public AssetSounds (AssetManager am) {
			jump = am.get("../rebok-gdx-game-core/assets/sounds/jump.wav", Sound.class);
			jumpWithLava = am.get("../rebok-gdx-game-core/assets/sounds/jump_with_lava.wav", Sound.class);
			pickupCoin = am.get("../rebok-gdx-game-core/assets/sounds/pickup_coin.wav", Sound.class);
			pickupLava = am.get("../rebok-gdx-game-core/assets/sounds/pickup_lava.wav", Sound.class);
			liveLost = am.get("../rebok-gdx-game-core/assets/sounds/live_lost.wav", Sound.class);
		}
	
	}
	
	/**
	 * Asset controlling the music of the game
	 * @author Justin
	 *
	 */
	public class AssetMusic {
		public final Music song01; //1st song of our game
		
		/**
		 * Load our song
		 * @param am
		 */
		public AssetMusic (AssetManager am) {
			song01 = am.get("../rebok-gdx-game-core/assets/music/keith303_-_brand_new_highscore.mp3", Music.class);
		}
	}
}
