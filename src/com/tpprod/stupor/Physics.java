package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Physics {
	
	public Mob player = null;
	public World world;
    public ArrayList<Mob> mobs = new ArrayList<Mob>(); 
    
    private int physicsFogOfWar = 3;
	private ArrayList<NewRectangle> DisplayedObjects = new ArrayList<NewRectangle>();
    
    private int GRAVITY = 2;
	
	public void Gravity() { 
		for (Mob entity: mobs) {
			if (entity.wallSlide) {
				entity.accelerationY = GRAVITY/2;
			} else {
				entity.accelerationY = GRAVITY;
			}
		}
	}
	
	public void WallSlide (Mob entity, boolean sliding) {
	}
	
	public void Movement() {
		// 1: North
		// 2: South
		// 3: West
		// 4: East
		
		for (Mob entity: mobs) {
			entity.velocityY += entity.accelerationY;
			entity.velocityX += entity.accelerationX;

			
			if (entity.velocityY < 0) {
				if (Intersection(entity, 1, Math.abs(entity.velocityY), world) == 1) {
					entity.currentY += entity.velocityY;
				} else {
					MoveToWall(entity, 1);
				}
			} else {
				if (entity.velocityY > entity.maxVelocity) {
					entity.velocityY = entity.maxVelocity;
				}
				
				if (Intersection(entity, 2, Math.abs(entity.velocityY), world) == 2) {
					entity.currentY += entity.velocityY;
				} else {
					MoveToWall(entity, 2);
				}
				entity.velocityY -= entity.dampening;
				if (entity.velocityY <= 0) {
					entity.velocityY = 0;
				}
			}

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
			
			if (entity.velocityX < 0) {
				if (Intersection(entity, 3, Math.abs(entity.velocityX), world) == 3) {
					entity.currentX += entity.velocityX;
				} else {
					MoveToWall(entity, 3);
				}
			} else {
				if (Intersection(entity, 4, Math.abs(entity.velocityX), world) == 4) {
					entity.currentX += entity.velocityX;
				} else {
					MoveToWall(entity, 4);
				}
			}
		}
	}
	
	public void mobMove(Mob entity,int direction, int magnitude) {
		
		
		if (direction == 1) {
			entity.velocityY = - magnitude;
		} else if (direction == 2) {
			entity.velocityY = magnitude;
		} else if (direction == 3) {
			entity.velocityX = - magnitude;
		} else if (direction == 4) {
			entity.velocityX = magnitude;
		}
		
	}
	
	public void MoveToWall(Mob entity, int direction) {
		if (direction == 1) {
			while (Intersection(entity, direction, 1, world) == 1) {
				entity.currentY -= 1;
			}
		} else if (direction == 2) {
			while (Intersection(entity, direction, 1, world) == 2) {
				entity.currentY += 1;
			}
		} else if (direction == 3) {
			while (Intersection(entity, direction, 1, world) == 3) {
				entity.currentX -= 1;
			}
		} else if (direction == 4) {
			while (Intersection(entity, direction, 1, world) == 4) {
				entity.currentX += 1;
			}
		}
	}
	
	public void Dampening(Mob entity) {
		if (entity.velocityX < 0) {
			entity.velocityX += entity.dampening;
			if (entity.velocityX > 0) {
				entity.velocityX = 0;
			}
		} else if (entity.velocityX > 0) {
			entity.velocityX -= entity.dampening;
			if (entity.velocityX < 0) {
				entity.velocityX = 0;
			}
		}
	}

    public int Intersection(Mob entity, int direction, int magnitude, World world) {
		// 1: North
		// 2: South
		// 3: West
		// 4: East
    	
		DisplayedObjects = new ArrayList<NewRectangle>();
		for (int y = (int)(entity.currentY / StateMachine.tileSize)-physicsFogOfWar; y <= (int)(entity.currentY / StateMachine.tileSize)+physicsFogOfWar; y++) {
			for (int x = (int)(entity.currentX / StateMachine.tileSize)-physicsFogOfWar; x <= (int)(entity.currentX / StateMachine.tileSize)+physicsFogOfWar; x++) {
				try {
					if (world.worldGrid.get(y).get(x).type == 1) {
						DisplayedObjects.add(world.worldGrid.get(y).get(x));
					}
				} catch (Exception e) {}
			}
		}
		
		for (NewRectangle rect: DisplayedObjects) {
			if (direction == 1) {
				Rectangle tempRect = new Rectangle(entity.currentX, entity.currentY - magnitude, entity.width, entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationY = 0;
					entity.velocityY = 0;
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
					return 0;
				} 
			} else if (direction == 4) {
				Rectangle tempRect = new Rectangle(entity.currentX + magnitude, entity.currentY, entity.width, entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationX = 0;
					entity.velocityX = 0;
					return 0;
				} 
			}
		}
		return direction;
	}

	public Physics () {
		mobs.add(new Mob(115, 125, Color.BLACK, 25));
		mobs.add(new Mob(150, 100));
		mobs.add(new Mob(500, 100));
		player = mobs.get(0);
	}
}
