package com.tpprod.stupor;

public class Projectile {
	
	public int currentX, currentY, velocityY, velocityX, accelerationY, size;
	public boolean shown;
	
	public Projectile () {
		shown = false;
	}
	
	public Projectile (int x, int y, int vX, int vY, int s) {
		currentX  = x;
		currentY  = y;
		velocityY = vX;
		velocityX = vY;
		size      = s;
		shown = true;
	}
}
