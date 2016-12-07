package com.rebok.gdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * A class to handle saving and loading of highscores
 * @author Justin
 *
 */
public class Highscores {
	public static final String TAG = Highscores.class.getName();
	public static final Highscores instance = new Highscores();
	//All the various options
	public Array<String> highscores; //the highscores
	public int numHighscores; //the number of high scores stored
	private Preferences prefs;
	
	// singleton: prevent instantiation from other classes
	private Highscores () {
		prefs = Gdx.app.getPreferences(Constants.HIGHSCORES);
	}
	
	/**
	 * Load our highscores
	 */
	public void load () {
		numHighscores = prefs.getInteger("number");
		highscores = new Array<String>();
		for(int i=0;i<numHighscores;i++){
			highscores.add(prefs.getString("score" + i));
		}
	}
	
	/**
	 * Save our highscores
	 */
	public void save () {
		prefs.putInteger("number", numHighscores);
		for(int i=0;i<numHighscores;i++){
			prefs.putString("score" + i, highscores.get(i));
		}
		prefs.flush();
	}
}
