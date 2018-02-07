package com.tpprod.stupor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/*
 * The Mob Class is used to create both enemy and player mobs with 
 * 	variables that can be used to control movement as well as size 
 * 	and color.
 */

public class Mob {

	public ArrayList<Projectile> projectileList = new ArrayList<Projectile>();
	public BufferedImage image = null;
	public BufferedImage arm = null;
	public boolean wallSlide, FacingLeft = false;
	public int accelerationX, accelerationY, currentX, currentY, velocityX, velocityY, Health, Mana, height, width, EXP;
	public int MaxHealth = 30, MaxMana = 30, jump = 0, speed = 12, maxVelocity = 20, shootingVelocity = 60,
			projectileSize = 10, maxJump = 30, dampening = 1, ManaRefreshTimer = 20;
	public Inventory inventory = new Inventory();
	
	private final int spriteWidth = 10, spriteHeight = 10;
	private final int rows = 10, cols = 10;
	private BufferedImage[] sprites = new BufferedImage[rows * cols];
	private int currentFrame = 0;
	
	public Mob (int posX, int posY, int tempHeight, int tempWidth) {
		/*
		 * This is a constructor where the position, color, and size can be set.
		 */
		currentX = posX;
		currentY = posY;
		height = tempHeight;
		width = tempWidth;
		try {
			image = ImageIO.read(new File("./Content/Textures/PlayerSpriteSheet.png"));
			arm = ImageIO.read(new File("./Content/Textures/PlayerArm.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < cols; i++)
		{
		    for (int j = 0; j < rows; j++)
		    {
		        sprites[(i * rows) + j] = image.getSubimage(
		            i * spriteWidth,
		            j * spriteHeight,
		            spriteWidth,
		            spriteHeight
		        );
		    }
		}
		ResetHealth();
		ResetMana();
	}

	public void NextFrame() {
		if (currentFrame < sprites.length-1) {
			currentFrame++;
		} else {
			currentFrame = 0;
		}
		image = sprites[currentFrame];
		if (FacingLeft) {
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-image.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			image = op.filter(image, null);
			FacingLeft = false;
		} else {
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-image.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			image = op.filter(image, null);
			FacingLeft = true;
		}
	}
	
	public void HurtMob(int damage) {
		if (Health - damage <= 0) {
			Health = 0;
		} else {
			Health -= damage;
		}
	}
	
	public void ResetHealth() {
		Health = MaxHealth;
	}
	
	public void ResetMana() {
		Mana = MaxMana;
	}
	
	public void FaceLeft() {
		if (!FacingLeft) {
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-image.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			image = op.filter(image, null);
			FacingLeft = true;
		}
	}

	public void FaceRight() {
		if (FacingLeft) {
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-image.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			image = op.filter(image, null);
			FacingLeft = false;
		}
	}
	
	public void Attack () {
		if (Mana >= 5) {
			if (FacingLeft) {
				projectileList.add(new Projectile(currentX - width/2, currentY + height/2, width, 20));
			} else {
				projectileList.add(new Projectile(currentX + width/2, currentY + height/2, width, 20));
			} 
			Mana -= 5;
		}
	}
	
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
			Mana -= 10;
		}
	}

	public void Jump() {
		/*
		 * The Jump method sets the Mobs velocity to a negative maxJump (this is in the
		 * northern direction) and sets jump to 1 if jump is 0 initially.
		 */
		if (jump > 0) {
			velocityY = -maxJump;
			jump--;
		}
	}

	public void healthUp(int heal) {
		if (Health + heal >= MaxHealth) {
			Health = MaxHealth;
		} else {
			Health += heal;
		}
	}

	public void useItem(Item item) {
		String itemType = item.name;
		if (inventory.currentItems.size() != 0) {
			if (itemType == "health") {
				healthUp(1);
				inventory.removeInventoryItem(item);
			}
		}
	}

	public void addItem(Item item) {
		inventory.addInventoryItem(item);
	}

	public void removeItem(Item item) {
		try {
			inventory.removeInventoryItem(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Item> readItems() {
		return inventory.currentItems;
	}

}
