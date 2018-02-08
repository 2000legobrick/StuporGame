package com.tpprod.stupor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.io.PrintWriter;
import java.io.StringWriter;
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
	private boolean running = false;

	private int GRAVITY = 2;

	public void Gravity() {
		/*
		 * The Gravity method applies a vertical acceleration to the south that depends
		 * on is the mobs is wall sliding.
		 */
		for (Mob entity : mobs) {
			if (entity.wallSlide) {
				entity.accelerationY = GRAVITY / 2;
			} else {
				entity.accelerationY = GRAVITY;
			}
			try {
				for (Projectile proj: entity.projectileList)
					proj.accelerationY = GRAVITY;
			} catch (Exception e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try{
					Log.add(error.toString());
				}catch (Exception e1) {
					
				}
			}
		}
	}
	
	public void CheckForDead() {
		ArrayList<Integer> removeList = new ArrayList<Integer>();
		for (Mob entity:mobs) {
			if (entity.Health <= 0)  {
				removeList.add(mobs.indexOf(entity));
			}
		}
		for (Integer i:removeList) {
			mobs.remove((int) i);
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
		
		CheckForDead();
		
		for (Mob entity : mobs) {
			// Changing velocity by acceleration
			entity.velocityY += entity.accelerationY;
			entity.velocityX += entity.accelerationX;

			if (entity.velocityY < 0) {
				// If the vertical velocity is less than zero (moving north) and the object won't
				// intersect with another object, move it by the amount of velocityY
				// else call move to wall
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
				// else call move to wall
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
				// with its next move and if not then move the object
				// else call move to wall to the west
				if (Intersection(entity, 3, Math.abs(entity.velocityX), world) == 3) {
					entity.currentX += entity.velocityX;
				} else {
					MoveToWall(entity, 3);
				}
			} else {
				// If there is not an intersection with its next move then move the object
				// else call move to wall to the east
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
			
			ArrayList<Integer> removeIndex = new ArrayList<Integer>();
			try {
				for (Projectile proj: entity.projectileList) {
					if (proj.timer == 0) {
						proj.previousX = proj.currentX;
						proj.previousY = proj.currentY;
						proj.velocityY -= proj.accelerationY;
						if (MobIntersection(proj, proj.type)) {
							removeIndex.add(entity.projectileList.indexOf(proj));
						} else if (!ProjectileIntersection(proj)) {
							proj.currentX += proj.velocityX;
							proj.currentY -= proj.velocityY;
						} else {
							removeIndex.add(entity.projectileList.indexOf(proj));
						}
						if (getDistance(new Point(proj.currentX, proj.currentY), new Point(entity.currentX, entity.currentY)) > 5000) {
							removeIndex.add(entity.projectileList.indexOf(proj));
						}
					} else {
						if (MobIntersection(proj, proj.type)) {
							removeIndex.add(entity.projectileList.indexOf(proj));
						}
						if (entity.FacingLeft) {
							proj.currentX = entity.currentX - entity.width/2;
						} else {
							proj.currentX = entity.currentX + entity.width/2;
						}
						proj.currentY = entity.currentY + entity.height/2;
						proj.timer--;
						if (proj.timer == 1) {
							removeIndex.add(entity.projectileList.indexOf(proj));
						}
					}
				}
				for (int i: removeIndex) {
					entity.projectileList.remove(i);
				}
			} catch (Exception e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try{
					Log.add(error.toString());
				}catch (Exception e1) {
					
				}
			}
		}
	}

	public double getDistanceTo(Point point1, Point point2) {
		double distance = Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
		return distance;
	}

	public void pickUpItem(Mob entity) {
		Item closestItem = null;
		double closestDistance = 100;
		double tempDistance;
		for (Item i:world.inventory.currentItems) {
			tempDistance = getDistanceTo(new Point (entity.currentX, entity.currentY), new Point(i.itemX, i.itemY));
			if (tempDistance < closestDistance) {
				closestDistance = tempDistance;
				closestItem = i;
			}
		}
		if (closestItem != null) {
			entity.addItem(closestItem);
			world.inventory.removeInventoryItem(closestItem);
		}
	}
	
	public int getDistance(Point p1, Point p2) {
		return  (int) Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2)+ Math.pow(p2.getY() - p1.getY(), 2));
	}
	
	public void mobMove(Mob entity, int direction, int magnitude) {
		/*
		 * The mobMove method sets the velocity of a certain mob to a specified magnitude
		 * 	in the direction specified.
		 */

		// 1: North
		// 2: South
		// 3: West
		// 4: East

		if (direction == 1) {
			entity.velocityY = -magnitude;
		} else if (direction == 2) {
			entity.velocityY = magnitude;
		} else if (direction == 3) {
			entity.velocityX = -magnitude;
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

		// Iterates through the world surrounding the block (checks a 2 block square
		// radius around the entity)
		// and adds the blocks that impede movement
		wallObjects = new ArrayList<NewRectangle>();
		for (int y = (int) (entity.currentY / StateMachine.tileSize)
				- physicsFogOfWar; y <= (int) (entity.currentY / StateMachine.tileSize) + physicsFogOfWar; y++) {
			for (int x = (int) (entity.currentX / StateMachine.tileSize)
					- physicsFogOfWar; x <= (int) (entity.currentX / StateMachine.tileSize) + physicsFogOfWar; x++) {
				try {
					if (world.worldGrid.get(y).get(x).type == 1) {
						wallObjects.add(world.worldGrid.get(y).get(x));
					}
				} catch (Exception e) {
					StringWriter error = new StringWriter();
					e.printStackTrace(new PrintWriter(error));
					try{
						Log.add(error.toString());
					}catch (Exception e1) {
						
					}
				}
			}
		}

		// Iterates through the ArrayList wallObjects and checks if the next players
		// movement will intersect with
		// any of the NewRectangles
		for (NewRectangle rect : wallObjects) {
			if (direction == 1) {
				Rectangle tempRect = new Rectangle(entity.currentX, entity.currentY - magnitude, entity.width,
						entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationY = 0;
					entity.velocityY = 0;
					return 0;
				}
			} else if (direction == 2) {
				Rectangle tempRect = new Rectangle(entity.currentX, entity.currentY + magnitude, entity.width,
						entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationY = 0;
					entity.velocityY = 0;
					entity.jump = 2;
					return 0;
				}
			} else if (direction == 3) {
				Rectangle tempRect = new Rectangle(entity.currentX - magnitude, entity.currentY, entity.width,
						entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationX = 0;
					entity.velocityX = 0;
					return 0;
				}
			} else if (direction == 4) {
				Rectangle tempRect = new Rectangle(entity.currentX + magnitude, entity.currentY, entity.width,
						entity.height);
				if (tempRect.intersects(rect.rect)) {
					entity.accelerationX = 0;
					entity.velocityX = 0;
					return 0;
				}
			}
		}
		return direction;
	}

	public boolean ProjectileIntersection(Projectile proj) {
		/*
    	 * The Intersection method checks if in the next movement of the proj will be intersecting with
    	 * 	another block. If it does intersect the speed and acceleration are set to 0.
		 */

		// 1: North
		// 2: South
		// 3: West
		// 4: East

		// Iterates through the world surrounding the block (checks a 2 block square
		// radius around the proj)
		// and adds the blocks that impede movement
		wallObjects = new ArrayList<NewRectangle>();
		for (int y = (int) (proj.currentY / StateMachine.tileSize)
				- physicsFogOfWar; y <= (int) (proj.currentY / StateMachine.tileSize) + physicsFogOfWar; y++) {
			for (int x = (int) (proj.currentX / StateMachine.tileSize)
					- physicsFogOfWar; x <= (int) (proj.currentX / StateMachine.tileSize) + physicsFogOfWar; x++) {
				try {
					if (world.worldGrid.get(y).get(x).type == 1) {
						wallObjects.add(world.worldGrid.get(y).get(x));
					}
				} catch (Exception e) {
					StringWriter error = new StringWriter();
					e.printStackTrace(new PrintWriter(error));
					try{
						Log.add(error.toString());
					}catch (Exception e1) {
						
					}
				}
			}
		}

		Line2D projectedLine = new Line2D.Float(proj.previousX, proj.previousY,
				proj.currentX, proj.currentY);

		// Iterates through the ArrayList wallObjects and checks if the next players
		// movement will intersect with
		// any of the NewRectangles
		for (NewRectangle rect : wallObjects) {
			if (projectedLine.intersects(rect.rect)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean MobIntersection (Projectile proj, int type) {
		for (int x = 1; x < mobs.size(); x++) {
			Mob entity = mobs.get(x);
			if (proj.type == Projectile.ARM) {
				if (new Rectangle(entity.currentX, entity.currentY, entity.width, entity.height).intersects(
						new Rectangle(proj.currentX, proj.currentY, proj.width, proj.height))) {
					entity.Health -= proj.damage;
					if (player.projectileList.contains(proj)) {
						player.EXP++;
					}
					return true;
				}
			} else if (proj.type == Projectile.BULLET) {
				Line2D projectedLine = new Line2D.Float(proj.previousX, proj.previousY, proj.currentX, proj.currentY);
				if (projectedLine.intersects(new Rectangle(entity.currentX, entity.currentY, entity.width, entity.height))) {
					entity.Health -= proj.damage;
					if (player.projectileList.contains(proj)) {
						player.EXP++;
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public void Save() {
		SaveData data = new SaveData();
		data.playerCurrentX = player.currentX;
		data.playerCurrentY = player.currentY;
		data.playerHealth = player.Health;
		data.playerMana = player.Mana;
		try {
			ResourceManager.Save(data, "SaveData");
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {
				
			}
		}
	}
	
	public void Load() {
		try {
			SaveData data = (SaveData) ResourceManager.Load("SaveData");
			player.currentX = data.playerCurrentX;
			player.currentY = data.playerCurrentY;
			player.Health = data.playerHealth;
			player.Mana = data.playerMana;
			player.EXP = data.playerEXP;
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {
				
			}
		}
	}

	public Physics() {
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
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {
				
			}
		}
		
		mobs.add(new Mob(playerStartingX, playerStartingY, 125, 50));
		player = mobs.get(0);

		for (int x = 1; x < 5; x++) {
			mobs.add(new Mob( 100 * x, 0, 50,50));
		}
		player = mobs.get(0);
		
		StateMachine.ai.AIs(mobs,player);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		// These variables are specific only to the run method
		
		double nsPerTick = 1000000000.0d / StateMachine.tickPerSec;
		double previous = System.nanoTime();
		double unprocessed = 0;
		
		while (running) {
			double current = System.nanoTime();
			unprocessed += (current - previous) / nsPerTick;
			previous = current;
			while (unprocessed >= 1) {
				// Updates game objects
				Gravity();
				Movement();
				--unprocessed;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try{
					Log.add(error.toString());
				}catch (Exception e1) {
					
				}
			}
		}
	}
	
	public void start() {
		if (!running) {
			running = true;
			new Thread(this).start();
		}
	}
	
	public void stop() {
		Save();
		running = false;
	}
}
