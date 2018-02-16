package com.tpprod.stupor;

import java.awt.Color;

public class SaveData implements java.io.Serializable {

	/*
	 * Basic data that needs to be saved
	 */
	private static final long serialVersionUID = 1L;
	private int playerCurrentX = 0;
	private int playerCurrentY = 0;
	private int playerEXP = 0;
	private int playerHealth = 0;
	private int playerMana = 0;
	private Item[] playerInventory = new Item[4];
 

	/*
	 * Getters and setters for SaveData
	 */
	public int getPlayerCurrentX() {
		return playerCurrentX;
	}

	public void setPlayerCurrentX(int playerCurrentX) {
		this.playerCurrentX = playerCurrentX;
	}

	public int getPlayerCurrentY() {
		return playerCurrentY;
	}

	public void setPlayerCurrentY(int playerCurrentY) {
		this.playerCurrentY = playerCurrentY;
	}

	public int getPlayerEXP() {
		return playerEXP;
	}

	public void setPlayerEXP(int playerEXP) {
		this.playerEXP = playerEXP;
	}

	public int getPlayerHealth() {
		return playerHealth;
	}

	public void setPlayerHealth(int playerHealth) {
		this.playerHealth = playerHealth;
	}

	public int getPlayerMana() {
		return playerMana;
	}

	public void setPlayerMana(int playerMana) {
		this.playerMana = playerMana;
	}

	public Item[] getPlayerInventory() {
		return playerInventory;
	}
	
	private Item item1 = null, item2=null, item3=null, item4=null;
	public int getPlayerCurrentX() {
		return playerCurrentX;
	
	}
	public void setPlayerCurrentX(int playerCurrentX) {
		this.playerCurrentX = playerCurrentX;
	}
	public int getPlayerCurrentY() {
		return playerCurrentY;
	}
	public void setPlayerCurrentY(int playerCurrentY) {
		this.playerCurrentY = playerCurrentY;
	}
	public int getPlayerEXP() { return playerEXP; }
	public void setPlayerEXP(int playerEXP) {
		this.playerEXP = playerEXP;
	}
	public int getPlayerHealth() {
		return playerHealth;
	}
	public void setPlayerHealth(int playerHealth) {
		this.playerHealth = playerHealth;
	}
	public int getPlayerMana() {
		return playerMana;
	}
	public void setPlayerMana(int playerMana) {
		this.playerMana = playerMana;
	}
	public Item getItem1() {
		return item1;
	}
	public void setItem1(int x, int y, Color color, int size, String n) {
		this.item1 = new Item(x,y,color,size,n);
	}
	public void setItem1(Item i) {
		this.item1 = i;
	}
	public Item getItem2() {
		return item2;
	}
	public void setItem2(int x, int y, Color color, int size, String n) {
		this.item2 = new Item(x,y,color,size,n);
	}
	public void setItem2(Item i) {
		this.item2 = i;
	}
	public Item getItem3() {
		return item3;
	}
	public void setItem3(int x, int y, Color color, int size, String n) {
		this.item3 = new Item(x,y,color,size,n);
	}
	public void setItem3(Item i) {
		this.item3 = i;
	}
	public Item getItem4() {
		return item4;
	}
	public void setItem4(int x, int y, Color color, int size, String n) {
		this.item4 = new Item(x,y,color,size,n);
	}
	public void setItem4(Item i) {
		this.item4 = i;
	}
	public String getSavedItemType(Item item) {
		String itemType = item.getName();
		return itemType;
	}
}
	

