package com.rebok.gdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class GamePreferences {
	  public static final String TAG = GamePreferences.class.getName();
	  public static final GamePreferences instance = new GamePreferences();
	  //All the various options
	  public boolean sound;
	  public boolean music;
	  public float volSound;
	  public float volMusic;
	  public int charSkin;
	  public boolean showFpsCounter;
	  private Preferences prefs;
	  public boolean useMonochromeShader; //for shading
	  
	  // singleton: prevent instantiation from other classes
	  private GamePreferences () {
		  prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
	  }
	  
	  /**
	   * Load our preferences
	   */
	  public void load () {
		  sound = prefs.getBoolean("sound", true);
		  music = prefs.getBoolean("music", true);
		  volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0.0f, 1.0f);
		  volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0.0f, 1.0f);
		  charSkin = MathUtils.clamp(prefs.getInteger("charSkin", 0), 0, 2);
		  showFpsCounter = prefs.getBoolean("showFpsCounter", false);
		  useMonochromeShader = prefs.getBoolean("useMonochromeShader", false);
	  }
	  
	  /**
	   * Save our preferences
	   */
	  public void save () {
		  prefs.putBoolean("sound", sound);
		  prefs.putBoolean("music", music);
		  prefs.putFloat("volSound", volSound);
		  prefs.putFloat("volMusic", volMusic);
		  prefs.putInteger("charSkin", charSkin);
		  prefs.putBoolean("showFpsCounter", showFpsCounter);
		  prefs.putBoolean("useMonochromeShader", useMonochromeShader);
		  prefs.flush();
	  }
}
