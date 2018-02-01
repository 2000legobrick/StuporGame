package com.tpprod.stupor;


import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;

public class MusicPlayer implements Runnable {

    private ArrayList<AudioFile> musicFiles;
    private int currentSongIndex;
    private boolean running;

    //Creates a playlist of all the songs in the audio folder
    public MusicPlayer(String... files) {
        musicFiles = new ArrayList<AudioFile>();
        for(String file : files)
            musicFiles.add(new AudioFile("./resources/audio/" + file + ".wav"));
    }

    public void run() {
        running = true;
        AudioFile song = musicFiles.get(currentSongIndex);
        song.play();
        while(running) {
                if(!song.isPlaying()) {
                    currentSongIndex++;
                    if(currentSongIndex >= musicFiles.size())
                        currentSongIndex = 0;
                    song = musicFiles.get(currentSongIndex);
                    song.play();
                }
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
