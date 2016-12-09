package com.rebok.gdx.game.util;

import com.badlogic.gdx.Input.TextInputListener;

/**
 * Class that gets input for the highscores table
 * @author jr8699
 *
 */
public class HighScoreHelper implements TextInputListener{

	@Override
	public void input(String text) {
		Highscores scores = Highscores.instance;
		int num = scores.getNumber();
		 //add new info
		int score = scores.currentScore;
		scores.addElement(text + " - " + score);
		scores.save(); //save it
	}

	@Override
	public void canceled() {
		//World Controller handles this
	}

}
