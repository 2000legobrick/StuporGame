package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/*
 * The Mob Class is used to create both enemy and player mobs with 
 * 	variables that can be used to control movement as well as size 
 * 	and color.
 */

public class Mob {
	
	public Color playerColor = Color.RED;
	public BufferedImage image = null;
	public BufferedImage arm = null;
    public int accelerationX, accelerationY;
	public int currentX, currentY;
	public int jump = 0;
	public int height = 75;
	public int width = height;
	public int speed = 12;
    public int velocityX, velocityY;
	public int maxVelocity = 20;
	public int shootingVelocity = 60;
	public int projectileSize = 10;
	public int maxJump = 30;
	public int dampening = 1;
	public boolean wallSlide;
	public Projectile[] projectileList = new Projectile[2];
	
	private boolean FacingLeft = false;
	
	public Mob (int posX, int posY, Color tempCol, int tempHeight, int tempWidth) {
		/*
		 * This is a constructor where the position, color, and size can be set.
		 */
		currentX = posX;
		currentY = posY;
		playerColor = tempCol;
		height = tempHeight;
		width = tempWidth;
		try {
			image = ImageIO.read(new File("./Content/Textures/Player.png"));
			arm = ImageIO.read(new File("./Content/Textures/PlayerArm.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		projectileList[0] = new Projectile();
	}
	
	public Mob (int posX, int posY) {
		/*
		 * This is a constructor where the position can be set.
		 */
		currentX = posX;
		currentY = posY;
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
	
	public void Shoot(Point mousePoint, Point middleScreen) {
		if (!projectileList[0].shown) {
			double rY = mousePoint.getY() - middleScreen.getY();
			double rX = mousePoint.getX() - middleScreen.getX();
			
			double theta = Math.atan(rY/rX);
	
			double magnitude = Math.sqrt(rX*rX + rY*rY) / 360;
			magnitude = (int) (shootingVelocity * magnitude);
			
			if (rX > 0) {
				projectileList[0] = new Projectile((int)(currentX)+width/2, (int)(currentY)+height/2,
						(int)(-magnitude*Math.sin(theta)), (int)(magnitude*Math.cos(theta)), projectileSize);
			} else if (rX < 0) {
				projectileList[0] = new Projectile((int)(currentX)+width/2, (int)(currentY)+height/2,
						(int)(magnitude*Math.sin(theta)), (int)(-magnitude*Math.cos(theta)), projectileSize);
			}
		}
	}
	
	public void Jump () {
		/*
		 * The Jump method sets the Mobs velocity to a negative maxJump (this is in the
		 *  northern direction) and sets jump to 1 if jump is 0 initially.
		 */
		if (jump > 0) {
			velocityY = -maxJump;
			jump--;
		}
	}
	
	public void addItem(Item item) {
		inventory.addItem(item);
	}
	public void removeItem(Item item) {
		inventory.removeItem(item);
	}

}
