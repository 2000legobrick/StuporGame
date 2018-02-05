package com.tpprod.stupor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * 
 */

public class ColorSchemes {
	
	public BufferedImage GroundTile  = null;
	public BufferedImage Background = null;
	public BufferedImage Player     = null;
	public BufferedImage PlayerArm  = null;
	
	public ColorSchemes () {

		try {
			GroundTile = ImageIO.read(new File("./Content/Textures/brickFloor.jpg"));
			Background = ImageIO.read(new File("./Content/Textures/cave.png"));
			Player = ImageIO.read(new File("./Content/Textures/Player.png"));
			PlayerArm = ImageIO.read(new File("./Content/Textures/PlayerArm.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ChangeBackground() {
		try {
			Background = ImageIO.read(new File("./Content/Textures/brickFloor.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
