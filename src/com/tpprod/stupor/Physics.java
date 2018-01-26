package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/*
 * The Physics Class accounts for the movement and storage of all mobs that are currently .
 */

public class Physics implements Runnable {
	
	public Mob player = null;
	public int playerStartingX = 1200;
	public int playerStartingY = 1200;
	public static ArrayList<Mob> mobs = new ArrayList<Mob>();
	public World world;
     
    
    private int physicsFogOfWar = 2;
	private ArrayList<NewRectangle> wallObjects = new ArrayList<NewRectangle>();
    
    private int GRAVITY = 2;
	

	public void Gravity() { 
		/*
		 * The Gravity method applies a vertical acceleration to the south that depends on is the mobs is wall sliding.
		 */
		for (Mob entity: mobs) {
			if (entity.wallSlide) {
				entity.accelerationY = GRAVITY/2;
			} else {
				entity.accelerationY = GRAVITY;
			}
			try {
				entity.projectileList[0].accelerationY = GRAVITY;
			} catch (Exception e) {}
		}
	}
	
	public void Movement() {
		/*
		 * The Movement method iterates through each of the mobs within the 
		 * 	ArrayList mobs and changes their velocity based off their acceleration
		 * 	and their position based off their velocities.
		 */
		
		// 1: North
		// 2: South
		// 3: West
		// 4: East
		
		for (Mob entity: mobs) {
			// Changing velocity by acceleration
			entity.velocityY += entity.accelerationY;
			entity.velocityX += entity.accelerationX;

			
			if (entity.velocityY < 0) {
				// If the vertical velocity is less than zero (moving north) and the object won't
				//  intersect with another object, move it by the amount of velocityY
				//  else call move to wall 
				if (Intersection(entity, 1, Math.abs(entity.velocityY), world) == 1) {
					entity.currentY += entity.velocityY;
				} else {
					MoveToWall(entity, 1);
				}
			} else {
				// If the velocity is greater than max velocity then cap it off at maxvelocity
				if (entity.velocityY > entity.maxVelocity) {
					entity.velocityY = entity.maxVelocity;
				}

				// If the object won't intersect with another object, move it by the amount of velocityY
				//  else call move to wall 
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
			
			if (entity.velocityX < 0) {
				// If horizontal velocity is less than 0 (moving west) check if there is an intersection
				//  with its next move and if not then move the object
				//  else call move to wall to the west
				if (Intersection(entity, 3, Math.abs(entity.velocityX), world) == 3) {
					entity.currentX += entity.velocityX;
				} else {
					MoveToWall(entity, 3);
				}
			} else {
				// If there is not an intersection with its next move then move the object
				//  else call move to wall to the east
				if (Intersection(entity, 4, Math.abs(entity.velocityX), world) == 4) {
					entity.currentX += entity.velocityX;
				} else {
					MoveToWall(entity, 4);
				}
			}

			// Applies dampening to to horizontal acceleration
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
			
			entity.projectileList[0].velocityY -= entity.projectileList[0].accelerationY;
			if (ProjectileIntersection(entity.projectileList[0], entity.projectileList[0].velocityX, entity.projectileList[0].velocityY)) {
				entity.projectileList[0].currentX += entity.projectileList[0].velocityX;
				entity.projectileList[0].currentY -= entity.projectileList[0].velocityY;
			} else { 
				entity.projectileList[0].shown = false;
			}
		}
	}
	
	public void mobMove(Mob entity,int direction, int magnitude) {
		/*
		 * The mobMove method sets the velocity of a certain mob to a specified magnitude
		 * 	in the direction specified.
		 */

		// 1: North
		// 2: South
		// 3: West
		// 4: East
		
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
		/*
		 * The MoveToWall method is used to take a specific mob and move it in the specified direction
		 * 	until it hits a wall.
		 */

		// 1: North
		// 2: South
		// 3: West
		// 4: East
		
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
		/*
		 * The Dampening method is used to bring the specified mob's horizontal
		 * 	velocity back to zero.
		 */
		
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
    	/*
    	 * The Intersection method checks if in the next movement of the entity will be intersecting with
    	 * 	another block. If it does intersect the speed and acceleration are set to 0.
    	 */
    	
		// 1: North
		// 2: South
		// 3: West
		// 4: East
    	
    	// Iterates through the world surrounding the block (checks a 2 block square radius around the entity)
    	//	and adds the blocks that impede movement
		wallObjects = new ArrayList<NewRectangle>();
		for (int y = (int)(entity.currentY / StateMachine.tileSize)-physicsFogOfWar; y <= (int)(entity.currentY / StateMachine.tileSize)+physicsFogOfWar; y++) {
			for (int x = (int)(entity.currentX / StateMachine.tileSize)-physicsFogOfWar; x <= (int)(entity.currentX / StateMachine.tileSize)+physicsFogOfWar; x++) {
				try {
					if (world.worldGrid.get(y).get(x).type == 1) {
						wallObjects.add(world.worldGrid.get(y).get(x));
					}
				} catch (Exception e) {}
			}
		}
		
		// Iterates through the ArrayList wallObjects and checks if the next players movement will intersect with
		//	any of the NewRectangles
		for (NewRectangle rect: wallObjects) {
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
					entity.jump = 2;
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

    public boolean ProjectileIntersection(Projectile entity, int magnitudeX, int magnitudeY) {
    	/*
    	 * The Intersection method checks if in the next movement of the entity will be intersecting with
    	 * 	another block. If it does intersect the speed and acceleration are set to 0.
    	 */
    	
		// 1: North
		// 2: South
		// 3: West
		// 4: East
    	
    	// Iterates through the world surrounding the block (checks a 2 block square radius around the entity)
    	//	and adds the blocks that impede movement
		wallObjects = new ArrayList<NewRectangle>();
		for (int y = (int)(entity.currentY / StateMachine.tileSize)-physicsFogOfWar; y <= (int)(entity.currentY / StateMachine.tileSize)+physicsFogOfWar; y++) {
			for (int x = (int)(entity.currentX / StateMachine.tileSize)-physicsFogOfWar; x <= (int)(entity.currentX / StateMachine.tileSize)+physicsFogOfWar; x++) {
				try {
					if (world.worldGrid.get(y).get(x).type == 1) {
						wallObjects.add(world.worldGrid.get(y).get(x));
					}
				} catch (Exception e) {}
			}
		}
		
		Line2D projectedLine = new Line2D.Float(entity.currentX + magnitudeX, entity.currentY + magnitudeY, entity.currentX + magnitudeX, entity.currentY + magnitudeY);
		
		// Iterates through the ArrayList wallObjects and checks if the next players movement will intersect with
		//	any of the NewRectangles
		for (NewRectangle rect: wallObjects) {
			if (projectedLine.intersects(rect.rect)) {
				return false;
			}
		}
		return true;
	}

	public Physics () {
		/*
		 * This is the constructor for the Physics class.
		 * 
		 * Currently we are using this to create enemy mobs, and the player entity.
		 * 	We also set the player entity to the first index of the mobs ArrayList
		 */
		
		try {	
			SaveData data = (SaveData) ResourceManager.Load("SaveData");
			playerStartingX = data.playerCurrentX;
			playerStartingY = data.playerCurrentY;
		} catch (Exception e) {
			System.out.println("Couldn't load save data: " + e.getMessage());
		}
		
		
		mobs.add(new Mob(playerStartingX, playerStartingY, new Color(191, 87, 0), 75, 33));
		player = mobs.get(0);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
}
