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
	public void addMobInventoryItem(Item[] items) {
		boolean nullItem = false;
		for (Item item : items) {
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
	}
	
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

	public void removeWorldInventoryItem(Item item) {
		ArrayList<ArrayList<NewRectangle>> tempWorld = StateMachine.getPhysics().getWorld().getWorldGrid();
		boolean foundItem = false;
		int index1=0,index2=0;

		for(ArrayList<NewRectangle> rList : tempWorld) {
			for (NewRectangle r : rList) {
				int x = (int) r.getRect().getX();
				int y = (int) r.getRect().getY();
				if (item.getItemX() == x && item.getItemY() == y) {
					foundItem = true;
					index2 = rList.indexOf(r);
					r.setType(0);
				}
			}
			if (foundItem)
				index1 = tempWorld.indexOf(rList);
		}

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

	public void setCurrentMobItems(Item[] currentMobItems) {
		this.currentMobItems = currentMobItems;
	}
}
