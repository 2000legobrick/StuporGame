package com.tpprod.stupor;

import java.util.ArrayList;

public class SaveWorldData extends SaveData {
    private ArrayList<ArrayList<String>> worldSavedGrid = new ArrayList<>();

    public ArrayList<ArrayList<String>> getWorldSavedGrid() {
		return worldSavedGrid;
	}

	public void setWorldSavedGrid(ArrayList<ArrayList<String>> worldSavedGrid) {
        this.worldSavedGrid = worldSavedGrid;
    }
    
   

}
