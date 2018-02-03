package com.tpprod.stupor;

import java.util.ArrayList;

public class Inventory {
	ArrayList<Item> currentItems = new ArrayList<Item>(10);

	public void addInventoryItem(Item item) {
		currentItems.add(item);
	}

	public void removeInventoryItem(Item item) {
		currentItems.remove(item);
	}
}
