package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Physics {
	
	public World world;
    public ArrayList<Mob> mobs = new ArrayList<Mob>(); 
    
	private ArrayList<NewRectangle> DisplayedObjects = new ArrayList<NewRectangle>();
    
    private int GRAVITY = 1;
    private int tileSize = 70;
	
	public void Gravity() { 
		
		for (Mob entity: mobs) {
			if (entity.jump == 0) {
				if (Intersection(entity, 2, GRAVITY, world, tileSize) == 2) {
					entity.accelerationY = GRAVITY;
				}
			}
			if (entity.jump == 2 && entity.timeJump <= 0) {
				entity.timeJump = 30;
				entity.jump = 2;
			} else if (entity.timeJump >= 0) {
				if (Intersection(entity, 1, entity.timeJump / entity.maxJump, world, tileSize) == 1) {
					entity.accelerationY = - entity.timeJump / entity.maxJump;
				}
				entity.timeJump--;
			}
		}
	}
	
	public void Movement() {
		for (Mob entity: mobs) {
			
			entity.velocityX = entity.accelerationX;
			entity.velocityY = entity.accelerationY;
			
			if (Math.abs(entity.velocityX) >= entity.maxVelocity) {
				entity.velocityX = entity.maxVelocity;
			}
			
			if (Math.abs(entity.velocityY) >= entity.maxVelocity) {
				entity.velocityY = entity.maxVelocity;
			}
			
			if (entity.velocityX > 0) 
				if (Intersection(entity, 1, entity.velocityX, world, tileSize) == 1) 
					entity.currentX += entity.velocityX;
			else 
				if (Intersection(entity, 2, entity.velocityX, world, tileSize) == 2) 
					entity.currentX += entity.velocityX;
			
			if (entity.velocityY > 0) 
				if (Intersection(entity, 3, entity.velocityY, world, tileSize) == 3) 
					entity.currentY += entity.velocityY;
			else 
				if (Intersection(entity, 4, entity.velocityY, world, tileSize) == 4) 
					entity.currentY += entity.velocityY;
		}
	}
	
	public void dedicatedMobMove(Mob entity,int direction, int magnitude) {
		// 1: North
		// 2: South
		// 3: West
		// 4: East
		
		/*
		// Archaic Version
		
		if (Intersection(entity, direction, magnitude, world, tileSize) == 1) {
			entity.accelerationY = - magnitude;
		} else if (Intersection(entity, direction, magnitude, world, tileSize) == 2) {
			entity.accelerationY = magnitude;
		} else if (Intersection(entity, direction, magnitude, world, tileSize) == 3) {
			entity.accelerationX = - magnitude;
		} else if (Intersection(entity, direction, magnitude, world, tileSize) == 4) {
			entity.accelerationX = magnitude;
		}
		
		entity.velocityX = entity.accelerationX;
		entity.velocityY = entity.accelerationY;
		
		entity.currentX += entity.velocityX;
		entity.currentY += entity.velocityY;

		entity.accelerationX = 0;
		entity.accelerationY = 0;
		*/
	}
	
	public void mobMove(Mob entity,int direction, int magnitude) {
		// 1: North
		// 2: South
		// 3: West
		// 4: East
		
		if (Intersection(entity, direction, magnitude, world, tileSize) == 1) {
			entity.accelerationY = - magnitude;
		} else if (Intersection(entity, direction, magnitude, world, tileSize) == 2) {
			entity.accelerationY = magnitude;
		} else if (Intersection(entity, direction, magnitude, world, tileSize) == 3) {
			entity.accelerationX = - magnitude;
		} else if (Intersection(entity, direction, magnitude, world, tileSize) == 4) {
			entity.accelerationX = magnitude;
		}
	}
	
    public int Intersection(Mob entity, int direction, int magnitude, World world, int tileSize) {
		// 1: North
		// 2: South
		// 3: West
		// 4: East
    	
		DisplayedObjects = new ArrayList<NewRectangle>();
		for (int row = 0; row < world.worldGrid.size(); row++) {
			for (int col = 0; col < world.worldGrid.get(0).size(); col++) {
				if (world.worldGrid.get(row).get(col) == 1) {
					DisplayedObjects.add(new NewRectangle(1, Color.GREEN, new Rectangle(col*tileSize, row*tileSize, tileSize, tileSize)));
				}
			}
		}
		
		for (NewRectangle rect: DisplayedObjects) {
			if (direction == 1) {
				Rectangle tempRect = new Rectangle(entity.currentX, entity.currentY - magnitude, entity.width, entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationY = 0;
					entity.velocityY = 0;
					entity.timeJump = 0;
					entity.jump = 1;
					return 0;
				}
			} else if (direction == 2) {
				Rectangle tempRect = new Rectangle(entity.currentX, entity.currentY + magnitude, entity.width, entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationY = 0;
					entity.velocityY = 0;
					entity.timeJump = 0;
					entity.jump = 1;
					return 0;
				} 
			} else if (direction == 3) {
				Rectangle tempRect = new Rectangle(entity.currentX - magnitude, entity.currentY, entity.width, entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationX = 0;
					entity.velocityX = 0;
					entity.jump = 1;
					return 0;
				} 
			} else if (direction == 4) {
				Rectangle tempRect = new Rectangle(entity.currentX + magnitude, entity.currentY, entity.width, entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationX = 0;
					entity.velocityX = 0;
					entity.jump = 1;
					return 0;
				} 
			}
		}
		return direction;
	}
	
	public Physics (int tempTileSize) {
		tileSize = tempTileSize;
		mobs.add(new Mob(300,200));
	}
}
