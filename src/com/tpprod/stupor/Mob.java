package com.tpprod.stupor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/*
 * The Mob Class is used to create both enemy and player mobs with 
 * 	variables that can be used to control movement as well as size 
 * 	and color.
 */

public class Mob {

	private ArrayList<Projectile> projectileList = new ArrayList<Projectile>();
	private BufferedImage runningSpriteSheet = null;
	private BufferedImage walkingSpriteSheet = null;
	private BufferedImage idleSpriteSheet = null;
	private BufferedImage image = null;
	private BufferedImage arm = null;
	private boolean wallSlide, FacingLeft = false;
	private boolean L1, L2, L3, R1, R2, R3;
	private int accelerationX, accelerationY, currentX, currentY, velocityX, velocityY, Health, Mana, height, width,
			EXP;
	private int MaxHealth = 30, MaxMana = 30, jump = 0, speed = 20, maxVelocity = 20, shootingVelocity = 60,
			projectileSize = 10, maxJump = 36, jumpAmount = 2, dampening = 1, ManaRefreshTimer = 20, wantsToShoot = 0;
	private Inventory inventory = new Inventory();
	private HealthRegen healthRegen = new HealthRegen();

	private final int spriteWidth = 12, spriteHeight = 32;
	private BufferedImage[] runningSprites = new BufferedImage[5];
	private BufferedImage[] walkingSprites = new BufferedImage[10];
	private BufferedImage[] idleSprites = new BufferedImage[6];
	private int currentFrame = 0;

	/*
	 * General Constructor for a mob, sets up all of the variables needed to be able
	 * to place the mob in the world
	 */
	public Mob(int posX, int posY, int tempHeight, int tempWidth) {
		/*
		 * This is a constructor where the position, color, and size can be set.
		 */
		currentX = posX;
		currentY = posY;
		height = tempHeight;
		width = tempWidth;
		try {
			runningSpriteSheet = ImageIO.read(new File("./Content/Textures/PlayerRunningSpriteSheet.png"));
			walkingSpriteSheet = ImageIO.read(new File("./Content/Textures/PlayerWalkingSpriteSheet.png"));
			idleSpriteSheet = ImageIO.read(new File("./Content/Textures/PlayerIdleSpriteSheet.png"));
			arm = ImageIO.read(new File("./Content/Textures/Sword.png"));
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 1; j++) {
					runningSprites[(i) + j] = runningSpriteSheet.getSubimage(i * spriteWidth, j * spriteHeight,
							spriteWidth, spriteHeight);
				}
			}
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 1; j++) {
					walkingSprites[(i) + j] = walkingSpriteSheet.getSubimage(i * spriteWidth, j * spriteHeight,
							spriteWidth, spriteHeight);
				}
			}
			int accIdle = 0;
			for (int i = 0; i < 2; i++) {
				for (int x = 0; x < 3; x++) {
					idleSprites[accIdle] = idleSpriteSheet.getSubimage(i * spriteWidth, 0, spriteWidth, spriteHeight);
					accIdle++;
				}
			}
			image = idleSprites[0];
		} catch (IOException e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try {
				Log.add(error.toString());
			} catch (Exception e1) {

			}
		}
		ResetHealth();
		ResetMana();
	}

	/*
	 * Gets the next frame of the mob animation from the sprite sheet
	 */
	 public void NextFrame(int list) {
	    if (list == 1) {
	      if (currentFrame < runningSprites.length - 1) {
	        currentFrame++;
	      } else {
	        currentFrame = 0;
	      }
	      image = runningSprites[currentFrame];
	      if (FacingLeft) {
	        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
	        tx.translate(-image.getWidth(null), 0);
	        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	        image = op.filter(image, null);
	      }
	    } else if (list == 2) {
	      if (currentFrame < walkingSprites.length-1) {
	        currentFrame++;
	      } else {
	        currentFrame = 0;
	      }
	      image = walkingSprites[currentFrame];
	      if (FacingLeft) {
		        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		        tx.translate(-image.getWidth(null), 0);
		        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		        image = op.filter(image, null);
	      }
	    } else if (list == 3) {
	      if (currentFrame < idleSprites.length-1) {
	        currentFrame++;
	      } else {
	        currentFrame = 0;
	      }
	      image = idleSprites[currentFrame];
	      if (FacingLeft) {
		        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		        tx.translate(-image.getWidth(null), 0);
		        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		        image = op.filter(image, null);
	      }
	   }
	}

	/*
	 * Increments damage to the mob
	 */
	public void HurtMob(int damage) {
		if (Health - damage <= 0) {
			Health = 0;
		} else {
			Health -= damage;
		}
	}

	/*
	 * Resets the mobs health, mainly used on the player for development purposes
	 */
	public void ResetHealth() {
		Health = MaxHealth;
	}

	/*
	 * Resets the mobs health, mainly used on the player for development purposes
	 */
	public void ResetMana() {
		Mana = MaxMana;
	}

	/*
	 * Changes which way the mob is facing
	 */
	public void FaceLeft() {
		if (!FacingLeft) {
			FacingLeft = true;
			for (Projectile proj: projectileList) {
				if (proj.getType() == Projectile.getArm()) {
					AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			        tx.translate(-arm.getWidth(null), 0);
			        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			        BufferedImage tempArm = op.filter(arm, null);
					proj.setImage(tempArm);
				}
			}
		}
	}

	/*
	 * Changes which way the mob is facing
	 */
	public void FaceRight() {
		if (FacingLeft) {
			FacingLeft = false;
			for (Projectile proj: projectileList) {
				if (proj.getType() == Projectile.getArm()) {
					proj.setImage(arm);
				}
			}
		}
	}

	/*
	 * Causes the mob to enter into a physical attack, think sword or fist
	 */
	public void Attack() {
		if (Mana >= 5) {
			if (FacingLeft) {
				AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		        tx.translate(-arm.getWidth(null), 0);
		        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		        BufferedImage tempArm = op.filter(arm, null);
				projectileList.add(new Projectile(tempArm, currentX - width / 2, currentY + height / 2, width, 20));
			} else {
				projectileList.add(new Projectile(arm, currentX + width / 2, currentY + height / 2, width, 20));
			}
			Mana -= 5;
		}
	}

	/*
	 * Causes the mob to shoot a projective with given velocities
	 */
	public void Shoot(double velX, double velY) {
		if (Mana >= 10) {
			projectileList.add(new Projectile((int) (currentX) + width / 2, (int) (currentY) + height / 2, (int) velY,
					(int) velX, projectileSize));
			new AudioFile("Content/Audio/SoundEffects/Shoot.wav").play();
			Mana -= 10;
		}
	}

	/*
	 * Causes the mob to shoot a projective in the direction of the cursor.
	 */
	public void Shoot(Point mousePoint, Point middleScreen) {
		if (Mana >= 10) {
			double rY = mousePoint.getY() - middleScreen.getY();
			double rX = mousePoint.getX() - middleScreen.getX();
			double theta = Math.atan(rY / rX);
			double magnitude = Math.sqrt(rX * rX + rY * rY) / 360;
			if (magnitude > 1.35) {
				magnitude = 1.35;
			} else if (magnitude < .3) {
				magnitude = .3;
			}
			magnitude = (int) (shootingVelocity * magnitude);
			if (rX > 0) {
				projectileList.add(new Projectile((int) (currentX) + width / 2, (int) (currentY) + height / 2,
						(int) (-magnitude * Math.sin(theta)), (int) (magnitude * Math.cos(theta)), projectileSize));
			} else if (rX < 0) {
				projectileList.add(new Projectile((int) (currentX) + width / 2, (int) (currentY) + height / 2,
						(int) (magnitude * Math.sin(theta)), (int) (-magnitude * Math.cos(theta)), projectileSize));
			}
			new AudioFile("Content/Audio/SoundEffects/Shoot.wav").play();
			Mana -= 10;
		}
	}

	/*
	 * The Jump method sets the Mobs velocity to a negative maxJump (this is in the
	 * northern direction) and sets jump to 1 if jump is 0 initially.
	 */
	public void Jump() {
		if (jump > 0 && velocityY >= 0) {
			velocityY = -maxJump;
			jump--;
		}
	}

	/*
	 * Causes the Mob to heal by a certain amount
	 */
	public void healthUp(int heal) {
		if (Health + heal >= MaxHealth) {
			Health = MaxHealth;
		} else {
			Health += heal;
		}
	}

	public void addItem(Item item) {
		inventory.addMobInventoryItem(item);
	}

	public void removeItem(Item item) {
		try {
			inventory.removeMobInventoryItem(item);
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try {
				Log.add(error.toString());
			} catch (Exception e1) {
			}
		}
	}

	public void resetInventory() {
		for (Item i : inventory.getCurrentMobItems()) {
			inventory.removeMobInventoryItem(i);
		}
		inventory.setCurrentMobItems(new Item[0]);
	}
	
	public ArrayList<Item> readItems() {
		return inventory.getCurrentItems();
	}

	public Item[] readMobItems() {
		return inventory.getCurrentMobItems();
	}

	public boolean getWallSlide() {
		return wallSlide;
	}

	public int getAccelerationX() {
		return accelerationX;
	}

	public void setAccelerationX(int accelerationX) {
		this.accelerationX = accelerationX;
	}

	public int getAccelerationY() {
		return accelerationY;
	}

	public void setAccelerationY(int accelerationY) {
		this.accelerationY = accelerationY;
	}

	public int getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(int velocityX) {
		this.velocityX = velocityX;
	}

	public int getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(int velocityY) {
		this.velocityY = velocityY;
	}

	public int getSpeed() {
		return speed;
	}

	public int getMaxVelocity() {
		return maxVelocity;
	}

	public int getJumpAmount() {
		return jumpAmount;
	}

	public void setJumpAmount(int jumpAmount) {
		this.jumpAmount = jumpAmount;
	}

	public int getDampening() {
		return dampening;
	}

	public int getManaRefreshTimer() {
		return ManaRefreshTimer;
	}

	public void setManaRefreshTimer(int manaRefreshTimer) {
		ManaRefreshTimer = manaRefreshTimer;
	}

	public ArrayList<Projectile> getProjectileList() {
		return projectileList;
	}

	public int getHealth() {
		return Health;
	}

	public void setHealth(int health) {
		Health = health;
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

	public boolean isFacingLeft() {
		return FacingLeft;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getEXP() {
		return EXP;
	}

	public void setEXP(int eXP) {
		EXP = eXP;
	}

	public int getMana() {
		return Mana;
	}

	public boolean isL1() {
		return L1;
	}

	public void setL1(boolean l1) {
		L1 = l1;
	}

	public boolean isL2() {
		return L2;
	}

	public void setL2(boolean l2) {
		L2 = l2;
	}

	public boolean isL3() {
		return L3;
	}

	public void setL3(boolean l3) {
		L3 = l3;
	}

	public boolean isR1() {
		return R1;
	}

	public void setR1(boolean r1) {
		R1 = r1;
	}

	public boolean isR2() {
		return R2;
	}

	public void setR2(boolean r2) {
		R2 = r2;
	}

	public boolean isR3() {
		return R3;
	}

	public void setR3(boolean r3) {
		R3 = r3;
	}

	public void setMana(int mana) {
		Mana = mana;
	}

	public int getJump() {
		return jump;
	}

	public void setJump(int jump) {
		this.jump = jump;
	}

	public int getMaxHealth() {
		return MaxHealth;
	}

	public void setMaxHealth(int MaxHealth) {
		this.MaxHealth = MaxHealth;
	}

	public int getMaxMana() {
		return MaxMana;
	}

	public BufferedImage getImage() {
		return image;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public int getWantsToShoot() {
		return wantsToShoot;
	}

	public void setWantsToShoot(int i) {
		wantsToShoot = i;
	}

	public void subtractFromWantsToShoot() {
		if (wantsToShoot > 0)
			wantsToShoot--;
	}

	public HealthRegen getHealthRegen() {
		return healthRegen;
	}

	public void setHealthRegen(HealthRegen healthRegen) {
		this.healthRegen = healthRegen;
	}

}
