package com.tpprod.stupor;

<<<<<<< Updated upstream
=======

import javax.swing.plaf.nimbus.State;
>>>>>>> Stashed changes
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class MusicPlayer implements Runnable {

<<<<<<< Updated upstream
	private ArrayList<AudioFile> musicFiles;
	private int currentSongIndex = 0;
	private String bgPath = "Content/Audio/BackgroundMusic/";
	private String sePath = "Content/Audio/Sound/";
	private boolean running;
	private ArrayList<AudioFile> bgMusic = new ArrayList<AudioFile>();;
	private ArrayList<AudioFile> soundEffects;
	public float audioVolume = -19, MaxVolume = 6, MinVolume = -80;

	/*
	 * Creates a playlist of all the songs in the audio folder
	 */
	public MusicPlayer() {
		File[] bgFiles = new File(bgPath).listFiles();
		// File[] seFiles = new File(sePath).listFiles();
		setPlaylist(bgMusic, bgPath, bgFiles);
	}

	/*
	 * Sets the playlist to a given playlist
	 */
	public void setPlaylist(ArrayList<AudioFile> playlist, String pathName, File[] files) {
		try {
			for (File file : files) {
				playlist.add(new AudioFile("./" + pathName + file.getName()));
			}
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try {
				Log.add(error.toString());
			} catch (Exception e1) {

			}
		}

	}
=======
	public static final int HealthRegen = 0;
	public static final int Menu        = 1;
	public static final int PickUp      = 2;
	public static final int Shoot       = 3;
	public static final int UseItem     = 4;
	
    private ArrayList<AudioFile> musicFiles;
    private int currentSongIndex = 0;
    private String bgPath = "Content/Audio/BackgroundMusic/";
    private String sePath = "Content/Audio/SoundEffects/";
    private boolean running;
    private ArrayList<AudioFile> bgMusic = new ArrayList<AudioFile>();
    private ArrayList<AudioFile> soundEffects = new ArrayList<AudioFile>();
    public float audioVolume = -19, MaxVolume = 6, MinVolume = -80;
    
    //Creates a playlist of all the songs in the audio folder
    public MusicPlayer(boolean isForBackgroundMusic) {
    	if (isForBackgroundMusic) {
	        File[] bgFiles = new File(bgPath).listFiles();
	    	setPlaylist(bgMusic,bgPath,bgFiles);
    	} else {
    		File[] seFiles = new File(sePath).listFiles();
	    	setPlaylist(soundEffects,sePath,seFiles);
    	}
    }
    
    public void setPlaylist(ArrayList<AudioFile> playlist, String pathName, File[] files) {
        try {
            for (File file : files) {
                playlist.add(new AudioFile("./" + pathName + file.getName()));
            }
        } catch (Exception e) {
            StringWriter error = new StringWriter();
            e.printStackTrace(new PrintWriter(error));
            try {
                Log.add(error.toString());
            } catch (Exception e1) {

            }
        }
    }

    public void changeVolume(float i) {
        int volume = StateMachine.getRender().getVolume();
        if (volume + i <= 0 || audioVolume + i*5 <= MinVolume) {
            //volume = 0;
            audioVolume = MinVolume;
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                StringWriter error = new StringWriter();
                e.printStackTrace(new PrintWriter(error));
                try{
                    Log.add(error.toString());
                } catch (Exception e1) {}
            }
        } else if (volume + i >= 10 || audioVolume + i*5 >= MaxVolume) {
            volume = 10;
            audioVolume = MaxVolume;
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                StringWriter error = new StringWriter();
                e.printStackTrace(new PrintWriter(error));
                try{
                    Log.add(error.toString());
                } catch (Exception e1) {}
            }
        } else if (volume + i > 0 && volume == 0 && audioVolume == MinVolume) {
            volume += i;
            audioVolume = -44;
            audioVolume += i * 5;
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                StringWriter error = new StringWriter();
                e.printStackTrace(new PrintWriter(error));
                try{
                    Log.add(error.toString());
                } catch (Exception e1) {}
            }
        } else {
            volume += i;
            audioVolume += i*5;
        }
        StateMachine.getRender().setVolume(volume);
    }

    public AudioFile getCurrentBackgroundSong() {
        AudioFile song = bgMusic.get(currentSongIndex);
        return song;
    }

    public void playBackgroundMusic() {
        running = true;
        AudioFile song = getCurrentBackgroundSong();
        song.play(audioVolume);
        while(running) {
            if(!song.isPlaying()) {
                currentSongIndex++;
                if(currentSongIndex >= bgMusic.size())
                    currentSongIndex = 0;
                song = bgMusic.get(currentSongIndex);
                song.play(audioVolume);
            }
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                StringWriter error = new StringWriter();
                e.printStackTrace(new PrintWriter(error));
                try{
                    Log.add(error.toString());
                } catch (Exception e1) {}
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
>>>>>>> Stashed changes

	/*
	 * Changes the volume of the music
	 */
	public void changeVolume(float i) {
		int volume = StateMachine.getRender().getVolume();
		if (volume + i <= 0 || audioVolume + i * 5 <= MinVolume) {
			// volume = 0;
			audioVolume = MinVolume;
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try {
					Log.add(error.toString());
				} catch (Exception e1) {
				}
			}
		} else if (volume + i >= 10 || audioVolume + i * 5 >= MaxVolume) {
			volume = 10;
			audioVolume = MaxVolume;
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try {
					Log.add(error.toString());
				} catch (Exception e1) {
				}
			}
		} else if (volume + i > 0 && volume == 0 && audioVolume == MinVolume) {
			volume += i;
			audioVolume = -44;
			audioVolume += i * 5;
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try {
					Log.add(error.toString());
				} catch (Exception e1) {
				}
			}
		} else {
			volume += i;
			audioVolume += i * 5;
		}
		StateMachine.getRender().setVolume(volume);
	}

	/*
	 * Returns the Current background song
	 */
	public AudioFile getCurrentBackgroundSong() {
		AudioFile song = bgMusic.get(currentSongIndex);
		return song;
	}

	/*
	 * Starts playing background music
	 */
	public void playBackgroundMusic() {
		running = true;
		AudioFile song = getCurrentBackgroundSong();
		song.play(audioVolume);
		while (running) {
			if (!song.isPlaying()) {
				currentSongIndex++;
				if (currentSongIndex >= bgMusic.size())
					currentSongIndex = 0;
				song = bgMusic.get(currentSongIndex);
				song.play(audioVolume);
			}
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try {
					Log.add(error.toString());
				} catch (Exception e1) {
				}
			}
		}
		song.stop();
	}

	/*
	 * Makes this class multithreaded so that we can start and stop music while
	 * other things are happening
	 */
	public void run() {
		running = true;
		playBackgroundMusic();
	}

	/*
	 * Starts the thread
	 */
	public void start() {
		if (!running) {
			running = true;
			new Thread(this).start();
		}
	}

	/*
	 * Stops the thread
	 */
	public void stop() {
		running = false;
	}
	/*
	 * Getters and Setters for audio volume
	 */
    public float getAudioVolume() {
        return audioVolume;
    }
 
    public void setAudioVolume(float audioVolume) {
        this.audioVolume = audioVolume;
    }
<<<<<<< Updated upstream
 
=======
    
    public ArrayList<AudioFile> getSoundEffects () {
    	return soundEffects;
    }

	public float getAudioVolume() {
		return audioVolume;
	}
>>>>>>> Stashed changes
}
