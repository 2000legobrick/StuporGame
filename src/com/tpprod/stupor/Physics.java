package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Physics {
	
	public World world;
    public ArrayList<Mob> mobs = new ArrayList<Mob>(); 
    
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
    	
    	int tileSize = StateMachine.tileSize;
    	int fogOfWar = Render.fogOfWar;
    	Mob player = mobs.get(0);
    	
		DisplayedObjects = new ArrayList<NewRectangle>();
		for (int y = (int)(player.currentY / tileSize)-fogOfWar; y <= (int)(player.currentY / tileSize)+fogOfWar; y++) {
			for (int x = (int)(player.currentX / tileSize)-fogOfWar; x <= (int)(player.currentX / tileSize)+fogOfWar; x++) {
				try {
					DisplayedObjects.add(world.worldGrid.get(y).get(x));
				} catch (Exception e) {}
			}
		}
		
		for (Mob thing: mobs) {
			DisplayedObjects.add(new NewRectangle(3, new Rectangle(thing.currentX, thing.currentY, thing.width, thing.height)));
		}
		
		for (NewRectangle rect: DisplayedObjects) {
			if (direction == 1) {
				Rectangle tempRect = new Rectangle(entity.currentX, entity.currentY - magnitude, entity.width, entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationY = 0;
					entity.velocityY = 0;
					/*
					if (entity.velocityX < 0) {
						entity.velocityX += entity.dampening;
						if (entity.velocityX >= 0) {
							entity.velocityX = 0;
						}
					} else {
						entity.velocityX -= entity.dampening;
						if (entity.velocityX <= 0) {
							entity.velocityX = 0;
						}
					}*/
					return 0;
				}
			} else if (direction == 2) {
				Rectangle tempRect = new Rectangle(entity.currentX, entity.currentY + magnitude, entity.width, entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationY = 0;
					entity.velocityY = 0;
					entity.jump = 0;
					/*
					if (entity.velocityX < 0) {
						entity.velocityX += entity.dampening;
						if (entity.velocityX >= 0) {
							entity.velocityX = 0;
						}
					} else {
						entity.velocityX -= entity.dampening;
						if (entity.velocityX <= 0) {
							entity.velocityX = 0;
						}
					}*/
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
		mobs.add(new Mob(100, 100, Color.BLACK, 25));
		mobs.add(new Mob(150, 100));
	}
}
