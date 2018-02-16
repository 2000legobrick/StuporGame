package com.tpprod.stupor;

import java.awt.Color;

public class Item implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
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
	public int getItemX() {
		return itemX;
	}
	public void setItemX(int itemX) {
		this.itemX = itemX;
	}
	public int getItemY() {
		return itemY;
	}
	public void setItemY(int itemY) {
		this.itemY = itemY;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Color getItemColor() {
		return itemColor;
	}
	public void setItemColor(Color itemColor) {
		this.itemColor = itemColor;
	}
	public int getItemSize() {
		return itemSize;
	}
	public void setItemSize(int itemSize) {
		this.itemSize = itemSize;
	}
	
	
}
