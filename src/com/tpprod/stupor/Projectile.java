package com.tpprod.stupor;

public class Projectile {
	
	private static final int BULLET = 0, ARM = 1;
	private int currentX, currentY, previousX, previousY, timer, type,
				velocityY, velocityX, accelerationY, bulletSize, width, height;
	private int damage = 10;
	private boolean shown;
	
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

	public int getCurrentX() {
		return currentX;
	}

	public void setCurrentX(int currentX) {
		this.currentX = currentX;
	}

	public int getCurrentY() {
		return currentY;
	}

	public void setCurrentY(int currentY) {
		this.currentY = currentY;
	}

	public int getPreviousX() {
		return previousX;
	}

	public void setPreviousX(int previousX) {
		this.previousX = previousX;
	}

	public int getPreviousY() {
		return previousY;
	}

	public void setPreviousY(int previousY) {
		this.previousY = previousY;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public int getType() {
		return type;
	}

	public int getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(int velocityY) {
		this.velocityY = velocityY;
	}

	public int getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(int velocityX) {
		this.velocityX = velocityX;
	}

	public int getAccelerationY() {
		return accelerationY;
	}

	public void setAccelerationY(int accelerationY) {
		this.accelerationY = accelerationY;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getDamage() {
		return damage;
	}

	public boolean isShown() {
		return shown;
	}

	public static int getBullet() {
		return BULLET;
	}

	public static int getArm() {
		return ARM;
	}
}
