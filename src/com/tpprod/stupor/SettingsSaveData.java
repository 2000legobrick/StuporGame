package com.tpprod.stupor;

public class SettingsSaveData extends SaveData {
	/*
	 * Saves settings that are changed in options
	 */
	private int volumeSetting = 5;

	/*
	 * Getters and setters for SettingsSaveData
	 */
	public int getVolumeSetting() {
		return volumeSetting;
	}

	public void setVolumeSetting(int volumeSetting) {
		this.volumeSetting = volumeSetting;
	}
}
