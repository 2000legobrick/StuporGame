package com.tpprod.stupor;

import java.awt.Color;

public class Item {
	/*
	 * Some basic variables that all items need in the game
	 */
	int itemX = 0;
	int itemY = 0;
	String name = "";
	Color itemColor = Color.GREEN;
	int itemSize = 25;

	/*
	 * Default constructor
	 */
	public Item() {

	}

	/*
	 * Overrides the default for special items
	 */
	public Item(int x, int y, Color color, int size, String n) {
		itemX = x;
		itemY = y;
		itemColor = color;
		itemSize = size;
		name = n;
	}
}
