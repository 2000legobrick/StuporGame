package com.tpprod.stupor;

public class SettingsSaveData extends SaveData {
<<<<<<< Updated upstream
	/*
	 * Saves settings that are changed in options
	 */
	private int volumeSetting = 5;
	private int renderVolume = 5;
	private float musicPlayerVolume = 5;
	/*
	 * Getters and setters for SettingsSaveData
	 */
	public int getVolumeSetting() {
		return volumeSetting;
	}
	
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
=======
	private int volumeSetting = 5;

	public int getVolumeSetting() {
		return volumeSetting;
	}

	public void setVolumeSetting(int volumeSetting) {
		this.volumeSetting = volumeSetting;
>>>>>>> Stashed changes
	}
}
