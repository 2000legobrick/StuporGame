package com.tpprod.stupor;

import java.awt.*;

public class Item {
	int itemX = 0;
	int itemY = 0;
	String name = "";
	Color itemColor = Color.GREEN;
	int itemSize = 25;

	public Item() {

	}
	public Item(int x, int y, Color color, int size, String n) {
		itemX = x;
		itemY = y;
		itemColor = color;
		itemSize = size;
		name = n;
	}



}
