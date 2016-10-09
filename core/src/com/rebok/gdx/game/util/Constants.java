package com.rebok.gdx.game.util;

/**
 * Class that holds all of our constants
 * @author Justin
 *
 */
public class Constants {
	
	// Visible game world is 5 meters wide
	public static final float VIEWPORT_WIDTH = 5.0f;
	
	// Visible game world is 5 meters tall
	public static final float VIEWPORT_HEIGHT = 5.0f;
	
	// GUI Width
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	
	// GUI Height
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
	
	//Assets atlas
    public static final String TEXTURE_ATLAS_OBJECTS = "../rebok-gdx-game-core/assets/rebok-mygame.atlas";
    
    // Location of image file for level 01
    public static final String LEVEL_01 = "assets-raw/levels/level-01.png";
    
    // Amount of extra lives at level start
    public static final int LIVES_START = 3;
    
    // Duration of feather power-up in seconds
    public static final float ITEM_FEATHER_POWERUP_DURATION = 9;
    
    // Delay after game over
    public static final float TIME_DELAY_GAME_OVER = 3;
    
    //desktop ui stuff
    public static final String TEXTURE_ATLAS_UI = "../rebok-gdx-game-core/assets/game-ui.atlas";
    
    public static final String TEXTURE_ATLAS_LIBGDX_UI = "../rebok-gdx-game-core/assets/uiskin.atlas";

    public static final String SKIN_LIBGDX_UI = "assets/uiskin.json";
    
    public static final String SKIN_CANYONBUNNY_UI = "assets/game-ui.json";

    public static final String PREFERENCES = null;
}
