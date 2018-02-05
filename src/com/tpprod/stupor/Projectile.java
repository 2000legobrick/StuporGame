package com.tpprod.stupor;

public class Projectile {
	
	public static final int BULLET = 0, ARM = 1;
	public int currentX, currentY, previousX, previousY, timer, type,
				velocityY, velocityX, accelerationY, bulletSize, width, height;
	public int damage = 10;
	public boolean shown;
	
	public Projectile () {
		shown = false;
	}
	
	public Projectile (int x, int y, int vX, int vY, int s) {
		currentX   = x;
		currentY   = y;
		previousX  = x;
		previousY  = y;
		velocityY  = vX;
		velocityX  = vY;
		width      = s;
		height     = s;
		shown      = true;
		type       = BULLET;
	}
	
	public Projectile(int x, int y, int tempWidth, int tempHeight) {
		currentX = x;
		currentY = y;
		width    = tempWidth;
		height   = tempHeight;
		timer    = 60; // Approximately a second
		type     = ARM;
	}
}
