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
		currentItems.remove(item);
		ArrayList<ArrayList<NewRectangle>> tempWorld = StateMachine.getPhysics().getWorld().getWorldGrid();
		int row = 0,col = 0;
		for(ArrayList<NewRectangle> rList : tempWorld) {
			for (NewRectangle r : rList) {
				int x = (int) r.getRect().getX() + StateMachine.getTileSize()/4;
				int y = (int) r.getRect().getY() + StateMachine.getTileSize()/4;
				if (item.getItemX() == x && item.getItemY() == y) {
					col = (int) (r.getRect().getX() - StateMachine.getTileSize()/4) / StateMachine.getTileSize();
					row = (int) (r.getRect().getY() - StateMachine.getTileSize()/4) / StateMachine.getTileSize();
					r.setType(0);
					break;
				}
				
			}
		}
		StateMachine.getPhysics().getWorld().setWorldGrid(tempWorld);
		try {
			StateMachine.getPhysics().getWorld().setWorldData(row, col, 0);
		} catch (Exception e) {}
	}

	/*
	 * Getters for objects in Inventory that are needed elsewhere
	 */
	public ArrayList<Item> getCurrentItems() {
		return currentItems;
	}

	public void setCurrentItems(ArrayList<Item> currentItems) {
		this.currentItems = new ArrayList<Item>();
		this.currentItems = currentItems;}
	
	public Item[] getCurrentMobItems() {
		return currentMobItems;
	}

	public void setCurrentMobItems(Item[] currentMobItems) {
		this.currentMobItems = currentMobItems;
	}
}
