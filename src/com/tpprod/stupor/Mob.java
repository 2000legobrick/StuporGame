package com.tpprod.stupor;

import java.awt.Color;

/*
 * The Mob Class is used to create both enemy and player mobs with 
 * 	variables that can be used to control movement as well as size 
 * 	and color.
 */

public class Mob {
	
	public Color playerColor = Color.RED;
    public int accelerationX, accelerationY;
	public int currentX, currentY;
	public int jump = 0;
	public int height = 90;
	public int width = height;
	public int speed = 8;
    public int velocityX, velocityY;
	public int maxVelocity = 15;
	public int maxJump = 25;
	public int dampening = 1;
	public boolean wallSlide;
	
	private Inventory inventory = new Inventory();
	
	public Mob (int posX, int posY, Color tempCol, int tempSize) {
		/*
		 * This is a constructor where the position, color, and size can be set.
		 */
		currentX = posX;
		currentY = posY;
		playerColor = tempCol;
		height = tempSize;
		width = tempSize;
		
	}
	
	public Mob (int posX, int posY) {
		/*
		 * This is a constructor where the position can be set.
		 */
		currentX = posX;
		currentY = posY;
	}
	
	public void Jump () {
		/*
		 * The Jump method sets the Mobs velocity to a negative maxJump (this is in the
		 *  northern direction) and sets jump to 1 if jump is 0 initially.
		 */
		if (jump == 0) {
			velocityY = -maxJump;
			jump = 1;
		}
	}
	
	public void addItem(Item item) {
		inventory.addItem(item);
	}
	public void removeItem(Item item) {
		inventory.removeItem(item);
	}

}
