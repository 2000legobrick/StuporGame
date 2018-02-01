package com.tpprod.stupor;

import java.util.ArrayList;

public class Inventory {
	ArrayList<Item> currentItems = new ArrayList<Item>();
	public Inventory () {
		
	}
	
	public void addItem(Item item){
		currentItems.add(item);
	}
	public void removeItem(Item item){
		currentItems.remove(item);
	}
}
