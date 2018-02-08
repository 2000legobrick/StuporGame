package com.tpprod.stupor;


import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class MusicPlayer implements Runnable {

    private ArrayList<AudioFile> musicFiles;
    private int currentSongIndex = 0;
    private String bgPath = "Content/Audio/BackgroundMusic/";
    private String sePath = "Content/Audio/Sound/";
    private boolean running;
    private ArrayList<AudioFile> bgMusic = new ArrayList<AudioFile>();;
    private ArrayList<AudioFile> soundEffects;
    
    //Creates a playlist of all the songs in the audio folder
    public MusicPlayer() {
        File[] bgFiles = new File(bgPath).listFiles();
        //File[] seFiles = new File(sePath).listFiles();
    	setPlaylist(bgMusic,bgPath, bgFiles);
    }
    
    public void setPlaylist(ArrayList<AudioFile> playlist, String pathName, File[] files) {
    	try {
	    	for (File file : files) {
	    	    playlist.add(new AudioFile("./" + pathName + file.getName()));
	    	}
    	} catch(Exception e) {
    		StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {
				
			}
    	}
    	
    }

    public void playBackgroundMusic() {
        running = true;
        AudioFile song = bgMusic.get(currentSongIndex);
        song.play();
        while(running) {
            if(!song.isPlaying()) {
                currentSongIndex++;
                if(currentSongIndex >= bgMusic.size())
                    currentSongIndex = 0;
                song = bgMusic.get(currentSongIndex);
                song.play();
            }
            try {
                Thread.sleep(1);
            } catch (Exception e) {
	            	StringWriter error = new StringWriter();
	    			e.printStackTrace(new PrintWriter(error));
	    			try{
	    				Log.add(error.toString());
	    			}catch (Exception e1) {
	    				
	    			}
            }
        }
        song.stop();
    }
    public void run() {
        running = true;
        playBackgroundMusic();
    }
    public void start() {
        if (!running) {
            running = true;
            new Thread(this).start();
        }
    }

    public void stop() {
        running = false;
    }
}
