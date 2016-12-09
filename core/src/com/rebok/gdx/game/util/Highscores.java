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
	public int currentScore; //for when the player is still alive, prevent reset after each life
	private Preferences prefs;

	// singleton: prevent instantiation from other classes
	private Highscores () {
		prefs = Gdx.app.getPreferences(Constants.HIGHSCORES);
		currentScore = 0;
	}
	/**
	 * Get all the high scores
	 * @return
	 */
	public Array<String> getScores(){
		if(highscores != null)
			return highscores;
		else return null;
	}

	/**
	 * Get the number of highscores
	 * @return
	 */
	public int getNumber(){
		return numHighscores;
	}

	/**
	 * Add a highscore to the list
	 * @param s
	 */
	public void addElement(String s){
		highscores.add(s);
	}

	/**
	 * Set new number of highscores
	 * @param num
	 */
	public void setNumber(int num){
		numHighscores = num;
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
		//load(); //load to pick the highest 3
		prefs.putInteger("number", numHighscores);
		for(int i=0;i<numHighscores;i++){
			prefs.putString("score" + i, highscores.get(i));
		}
		prefs.flush();
	}
}
