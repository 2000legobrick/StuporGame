package com.tpprod.stupor;

public class SettingsSaveData extends SaveData {
	/*
	 * Saves settings that are changed in options
	 */
	 private int renderVolume = 5;
	 private float musicPlayerVolume = -19;
	 

	/*
	 * Getters and setters for SettingsSaveData
	 */
	public int getRenderVolume() {
		return renderVolume;
	}

	public void setRenderVolume(int renderVolume) {
		this.renderVolume = renderVolume;
	}

	public float getMusicPlayerVolume() {
		return musicPlayerVolume;
	}

	public void setMusicPlayerVolume(float musicPlayerVolume) {
		this.musicPlayerVolume = musicPlayerVolume;
	}
}
