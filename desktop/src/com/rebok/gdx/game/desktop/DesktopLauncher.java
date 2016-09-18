package com.rebok.gdx.game.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rebok.gdx.game.RebokGdxGame;

/**
 * Launcher for the desktop
 * @author Justin
 *
 */
public class DesktopLauncher {
	
    private static boolean rebuildAtlas = true; //Controls if we rebuild the atlas every time on startup
    private static boolean drawDebugOutline = true; //Debugging for the atlas
    
    /**
     * Main method for using the desktop version of the game.
     * Also creates an atlas
     * @param arg
     */
	public static void main (String[] arg) {
		if (rebuildAtlas) {
	          Settings settings = new Settings();
	          settings.maxWidth = 1024;
	          settings.maxHeight = 1024;
	          settings.duplicatePadding = false;
	          settings.debug = drawDebugOutline;
	          TexturePacker.process(settings, "assets-raw/images", "../rebok-gdx-game-core/assets", "rebok-mygame.atlas");
	     }
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	    config.title = "CanyonBunny";
	    config.width = 800;
	    config.height = 480;
		new LwjglApplication(new RebokGdxGame(), config);
	}
}
