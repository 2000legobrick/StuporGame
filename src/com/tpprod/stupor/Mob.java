package com.tpprod.stupor;

import java.awt.Color;

public class Mob {
	
	public Color playerColor = Color.RED;
	public int jump = 0;
	public int height = 25;
	public int width = height;
	public int currentX;
	public int currentY;
    public int velocityX, velocityY;
    public int accelerationX, accelerationY;
	public int speed = 5;
	public int timeJump = 0;
	public int maxVelocity = 5;
	public int maxJump = 2;
	
	public Mob (int posX, int posY) {
		currentX = posX;
		currentY = posY;
	}

}
