package com.tpprod.stupor;

import java.awt.Color;

public class Mob {
	
	public Color playerColor = Color.RED;
	public int jump = 0;
	public int height = 90;
	public int width = height;
	public int currentX;
	public int currentY;
    public int velocityX, velocityY;
    public int accelerationX, accelerationY;
	public int speed = 8;
	public int timeJump = 0;
	public int maxVelocity = 15;
	public int maxJump = 25;
	public int dampening = 1;
	public boolean wallSlide;
	
	public Mob (int posX, int posY, Color tempCol, int tempSize) {
		currentX = posX;
		currentY = posY;
		playerColor = tempCol;
		height = tempSize;
		width = tempSize;
	}
	
	public Mob (int posX, int posY) {
		currentX = posX;
		currentY = posY;
	}
	
	public void Jump () {
		if (jump == 0) {
			velocityY = -maxJump;
			jump = 1;
		}
	}

}
