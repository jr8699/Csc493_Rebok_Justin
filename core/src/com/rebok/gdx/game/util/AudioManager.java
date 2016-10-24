package com.rebok.gdx.game.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Manager our audio assets
 * @author Justin
 *
 */
public class AudioManager {
	public static final AudioManager instance = new AudioManager(); //our instance
	private Music playingMusic; //current music that is playing
	
	// singleton: prevent instantiation from other classes
	private AudioManager () { }
	
	/**
	 * Play a sound
	 * @param sound
	 */
	public void play (Sound sound) {
	  play(sound, 1);
	}
	
	/**
	 * Play a sound w/ volume
	 * @param sound
	 * @param volume
	 */
	public void play (Sound sound, float volume) {
	  play(sound, volume, 1);
	}
	
	/**
	 * Play a sound w/ volume and pitch
	 * @param sound
	 * @param volume
	 * @param pitch
	 */
	public void play (Sound sound, float volume, float pitch) {
	  play(sound, volume, pitch, 0);
	}
	
	/**
	 * Play a sound w/ volume, pitch, and pan
	 * @param sound
	 * @param volume
	 * @param pitch
	 * @param pan
	 */
	public void play (Sound sound, float volume, float pitch, float pan) {
	    if (!GamePreferences.instance.sound) return;
	    sound.play(GamePreferences.instance.volSound * volume, pitch, pan);
	}
	
	/**
	 * Play our music
	 * @param music
	 */
	public void play (Music music) {
		stopMusic();
		playingMusic = music;
		if (GamePreferences.instance.music) {
		    music.setLooping(true);
		    music.setVolume(GamePreferences.instance.volMusic);
		    music.play();
		}
	}
	
	/**
	 * Stop our music
	 */
	public void stopMusic () {
		if (playingMusic != null) playingMusic.stop();
	}
	
	/**
	 * Whenever settings for the music are changed
	 */
	public void onSettingsUpdated () {
		if (playingMusic == null) return;
		playingMusic.setVolume(GamePreferences.instance.volMusic);
		if (GamePreferences.instance.music) {
		    if (!playingMusic.isPlaying()) playingMusic.play();
		} else {
		    playingMusic.pause();
		}
	}
}