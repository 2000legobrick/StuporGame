package com.tpprod.stupor;

import java.awt.Color;

public class Player extends Mob {
	public Color playerColor = Color.MAGENTA;
	public int currentX = 300;
	public int currentY = 200;
	public int currentGridX = 0;
	public int currentGridY = 0;
	
	public void playerMove(int direction, int magnitude, World world, int tileSize) {
		// 1: North
		// 2: South
		// 3: West
		// 4: East

		currentGridX =(int) (currentX / tileSize);
		currentGridY =(int) (currentY / tileSize);
		
		System.out.println((currentY - magnitude) % tileSize);
		
		if (direction == 1 && ((currentY - magnitude) % tileSize < tileSize && world.worldGrid.get(currentGridY).get(currentGridX) == 0)) {
			currentY -= magnitude;
		} else if (direction == 2 ) {
			currentY += magnitude;
		} else if (direction == 3) {
			currentX -= magnitude;
		} else if (direction == 4) {
			currentX += magnitude;
		}
	}
}
