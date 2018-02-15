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

	public static int GRAVITY = 3;

	private Mob player = null;
	private static ArrayList<Mob> mobs = new ArrayList<Mob>();
	private AI ai = new AI();
	private World world;

	private int physicsFogOfWar = 2;
	private ArrayList<NewRectangle> wallObjects = new ArrayList<NewRectangle>();
	private boolean running = false;

	/*
	 * Adds gravity to all mobs and projectile
	 */
	public void Gravity() {
		/*
		 * The Gravity method applies a vertical acceleration to the south that depends
		 * on is the mobs is wall sliding.
		 */
		for (Mob entity : mobs) {
			if (entity.getWallSlide()) {
				entity.setAccelerationY(GRAVITY / 2);
			} else {
				entity.setAccelerationY(GRAVITY);
			}
			try {
				for (Projectile proj : entity.getProjectileList())
					proj.setAccelerationY(GRAVITY);
			} catch (Exception e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try {
					Log.add(error.toString());
				} catch (Exception e1) {

				}
			}
		}
	}

	/*
	 * Checks if a mob is dead, if so it removes it
	 */
	public void CheckForDead() {
		ArrayList<Integer> removeList = new ArrayList<Integer>();
		for (Mob entity : mobs) {
			if (entity.getHealth() <= 0) {
				removeList.add(mobs.indexOf(entity));
			}
		}
		for (Integer i : removeList) {
			try {
				mobs.remove((int) i);
			} catch (Exception e) {
			}
		}
	}

	/*
	 * The Movement method iterates through each of the mobs within the ArrayList
	 * mobs and changes their velocity based off their acceleration and their
	 * position based off their velocities.
	 */
	public void Movement() {

		CheckForDead();

		for (Mob entity : mobs) {
			// Changing velocity by acceleration
			entity.setVelocityY(entity.getVelocityY() + entity.getAccelerationY());
			entity.setVelocityX(entity.getVelocityX() + entity.getAccelerationX());

			if (entity.getVelocityY() < 0) {
				// If the vertical velocity is less than zero (moving north) and the object
				// won't
				// intersect with another object, move it by the amount of velocityY
				// else call move to wall
				if (Intersection(entity, 1, Math.abs(entity.getVelocityY()), world) == 1) {
					entity.setCurrentY(entity.getCurrentY() + entity.getVelocityY());
				} else {
					MoveToWall(entity, 1);
				}
			} else {
				// If the velocity is greater than max velocity then cap it off at maxVelocity
				if (entity.getVelocityY() > entity.getMaxVelocity()) {
					entity.setVelocityY(entity.getMaxVelocity());
				}

				// If the object won't intersect with another object, move it by the amount of
				// velocityY
				// else call move to wall
				if (Intersection(entity, 2, Math.abs(entity.getVelocityY()), world) == 2) {
					entity.setCurrentY(entity.getCurrentY() + entity.getVelocityY());
				} else {
					MoveToWall(entity, 2);
				}
				entity.setVelocityY(entity.getVelocityY() - entity.getDampening());
				if (entity.getVelocityY() <= 0) {
					entity.setVelocityY(0);
				}
			}

			if (entity.getVelocityX() < 0) {
				// If horizontal velocity is less than 0 (moving west) check if there is an
				// intersection
				// with its next move and if not then move the object
				// else call move to wall to the west
				if (Intersection(entity, 3, Math.abs(entity.getVelocityX()), world) == 3) {
					entity.setCurrentX(entity.getCurrentX() + entity.getVelocityX());
				} else {
					MoveToWall(entity, 3);
				}
			} else {
				// If there is not an intersection with its next move then move the object
				// else call move to wall to the east
				if (Intersection(entity, 4, Math.abs(entity.getVelocityX()), world) == 4) {
					entity.setCurrentX(entity.getCurrentX() + entity.getVelocityX());
				} else {
					MoveToWall(entity, 4);
				}
			}

			// Applies dampening to to horizontal acceleration
			if (entity.getAccelerationX() > 0) {
				entity.setAccelerationX(entity.getAccelerationX() - entity.getDampening());
				if (entity.getAccelerationX() < 0) {
					entity.setAccelerationX(0);
				}
			} else if (entity.getAccelerationX() < 0) {
				entity.setAccelerationX(entity.getAccelerationX() - entity.getDampening());
				if (entity.getAccelerationX() > 0) {
					entity.setAccelerationX(0);
				}
			}

			ArrayList<Integer> removeIndex = new ArrayList<Integer>();
			/*
			 * Iterates through all of the projectiles and runs their needed physics
			 * calculations and decides if the projectile needs to be removed
			 */
			try {
				if (!entity.getProjectileList().isEmpty()) {
					for (Projectile proj : entity.getProjectileList()) {
						if (proj.getTimer() == 0) {
							proj.setPreviousX(proj.getCurrentX());
							proj.setPreviousY(proj.getCurrentY());
							proj.setVelocityY(proj.getVelocityY() - proj.getAccelerationY());
							if (MobIntersection(proj, proj.getType())) {
								removeIndex.add(entity.getProjectileList().indexOf(proj));
							} else if (!ProjectileIntersection(proj)) {
								proj.setCurrentX(proj.getCurrentX() + proj.getVelocityX());
								proj.setCurrentY(proj.getCurrentY() - proj.getVelocityY());
							} else {
								removeIndex.add(entity.getProjectileList().indexOf(proj));
							}
							if (getDistance(new Point(proj.getCurrentX(), proj.getCurrentY()),
									new Point(entity.getCurrentX(), entity.getCurrentY())) > 5000) {
								removeIndex.add(entity.getProjectileList().indexOf(proj));
							}
						} else {
							if (MobIntersection(proj, proj.getType())) {
								removeIndex.add(entity.getProjectileList().indexOf(proj));
							}
							if (entity.isFacingLeft()) {
								proj.setCurrentX(entity.getCurrentX() - entity.getWidth() / 2);
							} else {
								proj.setCurrentX(entity.getCurrentX() + entity.getWidth() / 2);
							}
							proj.setCurrentY(entity.getCurrentY() + entity.getHeight() / 2);
							proj.setTimer(proj.getTimer() - 1);
							if (proj.getTimer() == 1) {
								removeIndex.add(entity.getProjectileList().indexOf(proj));
							}
						}
					}
					/*
					 * Removes all projectiles that have intersected something or existed too too
					 * long.
					 */
					for (int i : removeIndex) {
						entity.getProjectileList().remove(i);
					}
				}
			} catch (Exception e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try {
					Log.add(error.toString());
				} catch (Exception e1) {

				}
			}
		}
	}

	/*
	 * Using the pythagorean theorem to determine the distance between two points
	 */
	public double getDistanceTo(Point point1, Point point2) {
		double distance = Math
				.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
		return distance;
	}

	/*
	 * Removes an item from the world and gives it to a mob
	 */
	public void pickUpItem(Mob entity) {
		Item closestItem = null;
		double closestDistance = 100;
		double tempDistance;
		for (Item i : world.getWorldInventory().getCurrentItems()) {
			tempDistance = getDistanceTo(new Point(entity.getCurrentX(), entity.getCurrentY()),
					new Point(i.itemX, i.itemY));
			if (tempDistance < closestDistance) {
				closestDistance = tempDistance;
				closestItem = i;
			}
		}
		if (closestItem != null) {
			try {
				if (entity.getInventory().getCurrentMobItems().length < 5) {
					entity.addItem(closestItem);
					world.getWorldInventory().removeInventoryItem(closestItem);
				}
			} catch (Exception e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try {
					Log.add(error.toString());
				} catch (Exception e1) {
				}
			}
		}
	}

	/*
	 * Using the pythagorean theorem to determine the distance between two points,
	 * and returns it as an int
	 */
	public int getDistance(Point p1, Point p2) {
		return (int) Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
	}

	/*
	 * The mobMove method sets the velocity of a certain mob to a specified
	 * magnitude in the direction specified.
	 */
	public void mobMove(Mob entity, int direction, int magnitude) {

		// 1: North
		// 2: South
		// 3: West
		// 4: East

		if (direction == 1) {
			entity.setVelocityY(-magnitude);
		} else if (direction == 2) {
			entity.setVelocityY(magnitude);
		} else if (direction == 3) {
			entity.setVelocityX(-magnitude);
		} else if (direction == 4) {
			entity.setVelocityX(magnitude);
		}

	}

	/*
	 * The MoveToWall method is used to take a specific mob and move it in the
	 * specified direction until it hits a wall.
	 */
	public void MoveToWall(Mob entity, int direction) {

		// 1: North
		// 2: South
		// 3: West
		// 4: East

		if (direction == 1) {
			while (Intersection(entity, direction, 1, world) == 1) {
				entity.setCurrentY(entity.getCurrentY() - 1);
			}
		} else if (direction == 2) {
			while (Intersection(entity, direction, 1, world) == 2) {
				entity.setCurrentY(entity.getCurrentY() + 1);
			}
		} else if (direction == 3) {
			while (Intersection(entity, direction, 1, world) == 3) {
				entity.setCurrentX(entity.getCurrentX() - 1);
			}
		} else if (direction == 4) {
			while (Intersection(entity, direction, 1, world) == 4) {
				entity.setCurrentX(entity.getCurrentX() + 1);
			}
		}
	}

	/*
	 * The Dampening method is used to bring the specified mob's horizontal velocity
	 * back to zero.
	 */
	public void Dampening(Mob entity) {

		if (entity.getVelocityX() < 0) {
			entity.setVelocityX(entity.getVelocityX() + entity.getDampening());
			if (entity.getVelocityX() > 0) {
				entity.setVelocityX(0);
			}
		} else if (entity.getVelocityX() > 0) {
			entity.setVelocityX(entity.getVelocityX() - entity.getDampening());
			if (entity.getVelocityX() < 0) {
				entity.setVelocityX(0);
			}
		}
	}

	/*
	 * The Intersection method checks if in the next movement of the entity will be
	 * intersecting with another block. If it does intersect the speed and
	 * acceleration are set to 0.
	 */
	public int Intersection(Mob entity, int direction, int magnitude, World world) {

		// 1: North
		// 2: South
		// 3: West
		// 4: East

		// Iterates through the world surrounding the block (checks a 2 block square
		// radius around the entity)
		// and adds the blocks that impede movement
		wallObjects = new ArrayList<NewRectangle>();
		for (int y = (int) (entity.getCurrentY() / StateMachine.getTileSize())
				- physicsFogOfWar; y <= (int) (entity.getCurrentY() / StateMachine.getTileSize())
						+ physicsFogOfWar; y++) {
			for (int x = (int) (entity.getCurrentX() / StateMachine.getTileSize())
					- physicsFogOfWar; x <= (int) (entity.getCurrentX() / StateMachine.getTileSize())
							+ physicsFogOfWar; x++) {
				try {
					if (x >= 0 && y >= 0) {
						if (world.getWorldGrid().get(y).get(x).getType() == 1) {
							wallObjects.add(world.getWorldGrid().get(y).get(x));
						}
					}
				} catch (Exception e) {
					// NON FATAL ERROR, do not report
				}
			}
		}

		// Iterates through the ArrayList wallObjects and checks if the next players
		// movement will intersect with
		// any of the NewRectangles
		for (NewRectangle rect : wallObjects) {
			if (direction == 1) {
				Rectangle tempRect = new Rectangle(entity.getCurrentX(), entity.getCurrentY() - magnitude,
						entity.getWidth(), entity.getHeight());
				if (tempRect.intersects(rect.getRect())) {
					entity.setAccelerationY(0);
					entity.setVelocityY(0);
					return 0;
				}
			} else if (direction == 2) {
				Rectangle tempRect = new Rectangle(entity.getCurrentX(), entity.getCurrentY() + magnitude,
						entity.getWidth(), entity.getHeight());
				if (tempRect.intersects(rect.getRect())) {
					entity.setAccelerationY(0);
					entity.setVelocityY(0);
					entity.setJump(entity.getJumpAmount());
					return 0;
				}
			} else if (direction == 3) {
				Rectangle tempRect = new Rectangle(entity.getCurrentX() - magnitude, entity.getCurrentY(),
						entity.getWidth(), entity.getHeight());
				if (tempRect.intersects(rect.getRect())) {
					entity.setAccelerationX(0);
					entity.setVelocityX(0);
					return 0;
				}
			} else if (direction == 4) {
				Rectangle tempRect = new Rectangle(entity.getCurrentX() + magnitude, entity.getCurrentY(),
						entity.getWidth(), entity.getHeight());
				if (tempRect.intersects(rect.getRect())) {
					entity.setAccelerationX(0);
					entity.setVelocityX(0);
					return 0;
				}
			}
		}
		return direction;
	}

	/*
	 * The Intersection method checks if in the next movement of the proj will be
	 * intersecting with another block. If it does intersect the speed and
	 * acceleration are set to 0.
	 */
	public boolean ProjectileIntersection(Projectile proj) {

		// 1: North
		// 2: South
		// 3: West
		// 4: East

		// Iterates through the world surrounding the block (checks a 2 block square
		// radius around the proj)
		// and adds the blocks that impede movement
		wallObjects = new ArrayList<NewRectangle>();
		for (int y = (int) (proj.getCurrentY() / StateMachine.getTileSize())
				- physicsFogOfWar; y <= (int) (proj.getCurrentY() / StateMachine.getTileSize())
						+ physicsFogOfWar; y++) {
			for (int x = (int) (proj.getCurrentX() / StateMachine.getTileSize())
					- physicsFogOfWar; x <= (int) (proj.getCurrentX() / StateMachine.getTileSize())
							+ physicsFogOfWar; x++) {
				try {
					if (x >= 0 && y >= 0) {
						if (world.getWorldGrid().get(y).get(x).getType() == 1) {
							wallObjects.add(world.getWorldGrid().get(y).get(x));
						}
					}
				} catch (Exception e) {
					StringWriter error = new StringWriter();
					e.printStackTrace(new PrintWriter(error));
					try {
						Log.add(error.toString());
					} catch (Exception e1) {

					}
				}
			}
		}

		Line2D projectedLine = new Line2D.Float(proj.getPreviousX(), proj.getPreviousY(), proj.getCurrentX(),
				proj.getCurrentY());

		// Iterates through the ArrayList wallObjects and checks if the next players
		// movement will intersect with
		// any of the NewRectangles
		for (NewRectangle rect : wallObjects) {
			if (projectedLine.intersects(rect.getRect())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Checks if a projectile intersects a mob
	 */
	public boolean MobIntersection(Projectile proj, int type) {
		for (Mob entity : mobs) {
			if (!entity.getProjectileList().contains(proj)) {
				if (proj.getType() == Projectile.getArm()) {
					if (new Rectangle(entity.getCurrentX(), entity.getCurrentY(), entity.getWidth(), entity.getHeight())
							.intersects(new Rectangle(proj.getCurrentX(), proj.getCurrentY(), proj.getWidth(),
									proj.getHeight()))) {
						entity.setHealth(entity.getHealth() - proj.getDamage());
						if (player.getProjectileList().contains(proj)) {
							player.setEXP(player.getEXP() + 1);
						}
						return true;
					}
				} else if (proj.getType() == Projectile.getBullet()) {
					Line2D projectedLine = new Line2D.Float(proj.getPreviousX(), proj.getPreviousY(),
							proj.getCurrentX(), proj.getCurrentY());
					if (projectedLine.intersects(new Rectangle(entity.getCurrentX(), entity.getCurrentY(),
							entity.getWidth(), entity.getHeight()))) {
						entity.setHealth(entity.getHealth() - proj.getDamage());
						if (player.getProjectileList().contains(proj)) {
							player.setEXP(player.getEXP() + 1);
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * Saves all relevant data to physics
	 */
	public void Save() {
		SaveData data = new SaveData();
		data.setPlayerCurrentX(player.getCurrentX());
		data.setPlayerCurrentY(player.getCurrentY());
		data.setPlayerHealth(player.getHealth());
		data.setPlayerMana(player.getMana());
		data.setPlayerEXP(player.getEXP());
		for (int i = 0; i < player.getInventory().getCurrentMobItems().length; i++) {
			data.getPlayerInventory()[i] = player.getInventory().getCurrentMobItems()[i];
		}
		try {
			ResourceManager.Save(data, "SaveData");
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try {
				Log.add(error.toString());
			} catch (Exception e1) {

			}
		}
	}

	/*
	 * Loads all relevant data to physics
	 */
	public void Load() {
		try {
			SaveData data = (SaveData) ResourceManager.Load("SaveData");
			player.setCurrentX(data.getPlayerCurrentX());
			player.setCurrentY(data.getPlayerCurrentY());
			player.setHealth(data.getPlayerHealth());
			player.setMana(data.getPlayerMana());
			player.setEXP(data.getPlayerEXP());
			for (int i = 0; i < data.getPlayerInventory().length; i++) {
				player.getInventory().addMobInventoryItem(data.getPlayerInventory()[i]);
			}
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try {
				Log.add(error.toString());
			} catch (Exception e1) {

			}
		}
	}

	/*
	 * Sets up physics by creating mobs and loading data
	 */
	public Physics() {
		/*
		 * This is the constructor for the Physics class.
		 * 
		 * Currently we are using this to create enemy mobs, and the player entity. We
		 * also set the player entity to the first index of the mobs ArrayList
		 */

		mobs.add(new Mob(0, 0, 100, 50));
		player = mobs.get(0);

		try {
			Load();
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try {
				Log.add(error.toString());
			} catch (Exception e1) {

			}
		}

		for (int x = 1; x < 5; x++) {
			mobs.add(new Mob(100 * x, 0, 50, 50));
		}

		ai.setMobAIList(mobs);

	}

	/*
	 * makes physics multithreaded
	 */
	@Override
	public void run() {

		// These variables are specific only to the run method
		double nsPerTick = 1000000000.0d / StateMachine.getTickpersec();
		double previous = System.nanoTime();
		double unprocessed = 0;

		Load();

		while (running) {
			double current = System.nanoTime();
			unprocessed += (current - previous) / nsPerTick;
			previous = current;
			while (unprocessed >= 1) {
				// Updates game objects
				ai.Move(world, player);
				Gravity();
				Movement();
				--unprocessed;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try {
					Log.add(error.toString());
				} catch (Exception e1) {

				}
			}
		}
	}

	/*
	 * Starts physics
	 */
	public void start() {
		if (!running) {
			running = true;
			new Thread(this).start();
		}
	}

	/*
	 * Stops physics
	 */
	public void stop() {
		Save();
		running = false;
	}

	/*
	 * Getters and setters for objects in physics
	 */
	public Mob getPlayer() {
		return player;
	}

	public static ArrayList<Mob> getMobs() {
		return mobs;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}

