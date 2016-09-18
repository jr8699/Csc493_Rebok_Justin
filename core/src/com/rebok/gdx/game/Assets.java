package com.rebok.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
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
    
    public AssetBunny bunny; //Bunny asset
    public AssetRock rock; //Rock asset
    public AssetGoldCoin goldCoin; //Coin asset
    public AssetFeather feather; //Feather asset
    public AssetLevelDecoration levelDecoration; //Level decorations asset
    
    
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
	    bunny = new AssetBunny(atlas);
	    rock = new AssetRock(atlas);
	    goldCoin = new AssetGoldCoin(atlas);
	    feather = new AssetFeather(atlas);
	    levelDecoration = new AssetLevelDecoration(atlas);
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
	public class AssetBunny {
	    public final AtlasRegion head; //head asset
	    
	    /**
	     * Constructor for the bunny head
	     * @param atlas
	     */
	    public AssetBunny (TextureAtlas atlas) {
	        head = atlas.findRegion("bunny_head");
	    }
	}
	
	/**
	 * Rock assets
	 * @author Justin
	 *
	 */
	public class AssetRock {
	    public final AtlasRegion edge; //rock edge
	    public final AtlasRegion middle; //middle of rock
	    
	    /**
	     * Constructor for the rocks
	     * @param atlas
	     */
	    public AssetRock (TextureAtlas atlas) {
	        edge = atlas.findRegion("rock_edge");
	        middle = atlas.findRegion("rock_middle");
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
	        goldCoin = atlas.findRegion("item_gold_coin");
	    }
	}
	
	/**
	 * Feather asset
	 * @author Justin
	 *
	 */
	public class AssetFeather {
	    public final AtlasRegion feather; //feather
	    
	    /**
	     * Constructor for the feather asset
	     * @param atlas
	     */
	    public AssetFeather (TextureAtlas atlas) {
	        feather = atlas.findRegion("item_feather");
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
	    public final AtlasRegion mountainLeft; //Left mountain
	    public final AtlasRegion mountainRight; //Right mountain
	    public final AtlasRegion waterOverlay; //water
	    
	    /**
	     * Constructor for the level decorations
	     * @param atlas
	     */
	    public AssetLevelDecoration (TextureAtlas atlas) {
	    	cloud01 = atlas.findRegion("cloud01");
	        cloud02 = atlas.findRegion("cloud02");
	        cloud03 = atlas.findRegion("cloud03");
	        mountainLeft = atlas.findRegion("mountain_left");
	        mountainRight = atlas.findRegion("mountain_right");
	        waterOverlay = atlas.findRegion("water_overlay");
	    }
	}
	
	
}
