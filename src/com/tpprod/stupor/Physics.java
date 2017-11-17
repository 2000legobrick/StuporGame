package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Physics {
	
	public World world;
    public ArrayList<Mob> mobs = new ArrayList<Mob>(); 
    
	private ArrayList<NewRectangle> DisplayedObjects = new ArrayList<NewRectangle>();
    
    private int GRAVITY = -1;
    private int tileSize = 70;
	
	public void Gravity() { 
		for (Mob entity: mobs) {
			if (entity.jump != 1) {
				if (Intersection(entity, 2, GRAVITY, world, tileSize) == 2) {
					entity.accelerationY = - GRAVITY;
				}
			} else {
				entity.accelerationY = GRAVITY;
			}
		}
	}
	
	public void Movement() {
		for (Mob entity: mobs) {
			
			if (entity.velocityX > 0) {
				entity.velocityX -= entity.dampening;
				if (entity.velocityX < 0) {
					entity.velocityX = 0;
				}
			} else if (entity.velocityX < 0) {
				entity.velocityX += entity.dampening;
				if (entity.velocityX > 0) {
					entity.velocityX = 0;
				}
			}
			
			if (entity.velocityY > 0) {
				entity.velocityY -= entity.dampening;
				if (entity.velocityY < 0) {
					entity.velocityY = 0;
				}
			} else if (entity.velocityY < 0) {
				entity.velocityY += entity.dampening;
				if (entity.velocityY > 0) {
					entity.velocityY = 0;
				}
			}

			entity.velocityX += entity.accelerationX;
			entity.velocityY += entity.accelerationY;

			entity.currentX += entity.velocityX;
			entity.currentY += entity.velocityY;

			if (entity.accelerationX > 0) {
				entity.accelerationX -= entity.dampening;
				if (entity.accelerationX < 0) {
					entity.accelerationX = 0;
				}
			} else if (entity.accelerationX < 0) {
				entity.accelerationX += entity.dampening;
				if (entity.accelerationX > 0) {
					entity.accelerationX = 0;
				}
			}
			
			if (entity.accelerationY > 0) {
				entity.accelerationY -= entity.dampening;
				if (entity.accelerationY < 0) {
					entity.accelerationY = 0;
				}
			} else if (entity.accelerationY < 0) {
				entity.accelerationY += entity.dampening;
				if (entity.accelerationY > 0) {
					entity.accelerationY = 0;
				}
			}
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
			entity.Jump();
		}
		
		if (Intersection(entity, direction, magnitude, world, tileSize) == 3) {
			entity.velocityX = - magnitude;
		} else if (Intersection(entity, direction, magnitude, world, tileSize) == 4) {
			entity.velocityX = magnitude;
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
					entity.jump = 0;
					return 0;
				}
			} else if (direction == 2) {
				Rectangle tempRect = new Rectangle(entity.currentX, entity.currentY + magnitude, entity.width, entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationY = 0;
					entity.velocityY = 0;
					entity.jump = 0;
					return 0;
				} 
			} else if (direction == 3) {
				Rectangle tempRect = new Rectangle(entity.currentX - magnitude, entity.currentY, entity.width, entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationX = 0;
					entity.velocityX = 0;
					entity.jump = 0;
					return 0;
				} 
			} else if (direction == 4) {
				Rectangle tempRect = new Rectangle(entity.currentX + magnitude, entity.currentY, entity.width, entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationX = 0;
					entity.velocityX = 0;
					entity.jump = 0;
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
