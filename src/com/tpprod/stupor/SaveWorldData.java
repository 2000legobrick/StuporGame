package com.tpprod.stupor;

import java.util.ArrayList;

public class SaveWorldData extends SaveData {
    private ArrayList<ArrayList<String>> worldSavedGrid = new ArrayList<>();

    private ArrayList<Item> worldInv = new ArrayList<>();

    public ArrayList<ArrayList<String>> getWorldSavedGrid() {
		return worldSavedGrid;
	}

	public void setWorldSavedGrid(ArrayList<ArrayList<String>> worldSavedGrid) {
        this.worldSavedGrid = new ArrayList<ArrayList<String>>();
        this.worldSavedGrid = worldSavedGrid;
    }

    public ArrayList<Item> getWorldInv() {
        return worldInv;
    }

    public void setWorldInv(ArrayList<Item> worldInv) {
        this.worldInv = worldInv;
    }

}
