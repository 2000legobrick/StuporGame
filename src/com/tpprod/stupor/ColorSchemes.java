package com.tpprod.stupor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.imageio.ImageIO;

/*
 * 
 */

public class ColorSchemes {

	private BufferedImage GroundTile = null;
	private BufferedImage Background = null;
	private BufferedImage SplashScreen1 = null;
	private BufferedImage SplashScreen2 = null;
	private BufferedImage ItemHealth = null;
	private BufferedImage ItemRegen = null;
	private BufferedImage UpgradeHealth = null;
	private BufferedImage UpgradeHealthNot = null;
	private BufferedImage UpgradeJump = null;
	private BufferedImage UpgradeJumpNot = null;
	private BufferedImage UpgradeMana = null;
	private BufferedImage UpgradeManaNot = null;

	/*
	 * The base of the ColorSchemes class, this method sets up the pictures for all
	 * tiles and the background for the initial world
	 */
	public ColorSchemes() {

		try {
			GroundTile = ImageIO.read(new File("./Content/Textures/Ground.jpg"));
			Background = ImageIO.read(new File("./Content/Textures/TreeBackground.png"));
			SplashScreen1 = ImageIO.read(new File("./Content/Textures/LogoCognitiveThought.png"));
			SplashScreen2 = ImageIO.read(new File("./Content/Textures/LogoThunderPunch.png"));
			ItemHealth = ImageIO.read(new File("./Content/Textures/HealthPotion.png"));
			ItemRegen = ImageIO.read(new File("./Content/Textures/HealthRegen.png"));
			UpgradeHealth =  ImageIO.read(new File("./Content/Textures/UpgradeHealth.png"));
			UpgradeHealthNot =  ImageIO.read(new File("./Content/Textures/UpgradeHealthNot.png"));
			UpgradeJump =  ImageIO.read(new File("./Content/Textures/UpgradeJump.png"));
			UpgradeJumpNot =  ImageIO.read(new File("./Content/Textures/UpgradeJumpNot.png"));
			UpgradeMana =  ImageIO.read(new File("./Content/Textures/UpgradeMana.png"));
			UpgradeManaNot =  ImageIO.read(new File("./Content/Textures/UpgradeManaNot.png"));
		} catch (IOException e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try {
				Log.add(error.toString());
			} catch (Exception e1) {

			}
		}
	}

	/*
	 * Changes the background to a different BufferedImage
	 */
	public void ChangeBackground() {
		try {
			Background = ImageIO.read(new File("./Content/Textures/brickFloor.jpg"));
		} catch (IOException e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try {
				Log.add(error.toString());
			} catch (Exception e1) {

			}
		}
	}

	/*
	 * Getters for objects in ColorSchemes that are needed elsewhere
	 */
	public BufferedImage getGroundTile() {
		return GroundTile;
	}

	public BufferedImage getBackground() {
		return Background;
	}

	public BufferedImage getSplashScreen1() {
		return SplashScreen1;
	}

	public BufferedImage getSplashScreen2 () {
		return SplashScreen2;
	}

	public BufferedImage getItemRegen () {
		return ItemRegen;
	}

	public BufferedImage getItemHealth() {
		return ItemHealth;
	}
	
	public BufferedImage getUpgradeHealth() {
		return UpgradeHealth;
	}
	
	public BufferedImage getUpgradeHealthNot() {
		return UpgradeHealthNot;
	}
	
	public BufferedImage getUpgradeJump() {
		return UpgradeJump;
	}
	
	public BufferedImage getUpgradeJumpNot() {
		return UpgradeJumpNot;
	}
	
	public BufferedImage getUpgradeMana() {
		return UpgradeMana;
	}
	
	public BufferedImage getUpgradeManaNot() {
		return UpgradeManaNot;
	}
}
