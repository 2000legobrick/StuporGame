package com.tpprod.stupor;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class AudioFile implements LineListener {

	private File soundFile;
	private AudioInputStream ais;
	private AudioFormat format;
	private DataLine.Info info;
	private Clip clip;
	private FloatControl gainControl;
	private volatile boolean playing;
	private float audioVolume = -5;

	/*
	 * This is the method that actually does the opening of the file based on a
	 * given fileName and and sets up the audio to be played
	 */

	public AudioFile(String fileName) {
		soundFile = new File(fileName);
		try {
			ais = AudioSystem.getAudioInputStream(soundFile);
			format = ais.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.addLineListener(this);
			clip.open(ais);
			gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try {
				Log.add(error.toString());
			} catch (Exception e1) {

			}
			System.exit(1);
		}
	}
	
	/*
	 * Plays a song at a static volume
	 */
	public void play () {
		play(audioVolume);
	}
	
	/*
	 * Plays a song based on a given volume
	 */
	
	public void play(float volume) {
		gainControl.setValue(volume);
		clip.start();
		playing = true;
	}

	/*
	 * Stops the current song and updates playing to indicate that
	 */
			
	public void stop() {
		clip.stop();
		playing = false;
	}

	/*
	 * Returns the boolean value of playing, which shows if a song is currently playing
	 */
	
	public boolean isPlaying() {
		return playing;
	}
	
	/*
	 * Automatically updates playing if a song starts and resets the clip if a song stops 
	 */
	@Override
	public void update(LineEvent event) {
		if (event.getType() == LineEvent.Type.START)
			playing = true;
		else if (event.getType() == LineEvent.Type.STOP) {
			clip.stop();
			clip.flush();
			clip.setFramePosition(0);
			playing = false;
		}
	}
}
