package com.tpprod.stupor;

import java.util.ArrayList;
import java.util.Arrays;

public class Inventory {
	private ArrayList<Item> currentItems = new ArrayList<Item>();
	private Item[] currentMobItems = new Item[0];

	/*
	 * Adds to a mobs inventory, mob inventories have a limited space so the player
	 * has to choose which items to keep
	 */
	public void addMobInventoryItem(Item item) {
		boolean nullItem = false;
		if (currentMobItems.length < 4) {
			for (Item i : currentMobItems) {
				if (i == null) {
					ArrayList<Item> list = new ArrayList<Item>(Arrays.asList(currentMobItems));
					int index = list.indexOf(i);
					list.set(index, item);
					currentMobItems = list.toArray(new Item[0]);
					nullItem = true;
					break;
				}
			}
			if (!nullItem) {
				currentMobItems = Arrays.copyOf(currentMobItems, currentMobItems.length + 1);
				currentMobItems[currentMobItems.length - 1] = item;
			}
		}
		if (currentMobItems.length >= 4) {
			for (Item i : currentMobItems) {
				if (i == null) {
					ArrayList<Item> list = new ArrayList<Item>(Arrays.asList(currentMobItems));
					int index = list.indexOf(i);
					list.set(index, item);
					currentMobItems = list.toArray(new Item[0]);
					break;
				}
			}
		}
	}

	/*
	 * Removes an item from a mobs inventory
	 */
	public void removeMobInventoryItem(Item item) {
		for (Item i : currentMobItems) {
			if (item == i) {
				ArrayList<Item> list = new ArrayList<Item>(Arrays.asList(currentMobItems));
				int index = list.indexOf(item);
				list.set(index, null);
				currentMobItems = list.toArray(new Item[0]);
			}
		}
	}

	/*
	 * Add and remove methods for an item to an inventory, mainly used in the world
	 * inventory which is theoretically infinite
	 */
	public void addInventoryItem(Item item) {
		currentItems.add(item);
	}

	public void removeInventoryItem(Item item) {
		currentItems.remove(item);
	}

	/*
	 * Getters for objects in Inventory that are needed elsewhere
	 */
	public ArrayList<Item> getCurrentItems() {
		return currentItems;
	}

	public Item[] getCurrentMobItems() {
		return currentMobItems;
	}
}
