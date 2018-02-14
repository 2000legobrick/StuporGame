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
	
	public BufferedImage GroundTile  = null;
	public BufferedImage Background = null;
	public BufferedImage Player     = null;
	public BufferedImage PlayerArm  = null;
	
	public ColorSchemes () {

		try {
			GroundTile = ImageIO.read(new File("./Content/Textures/brickFloor.jpg"));
			Background = ImageIO.read(new File("./Content/Textures/Plains.png"));
			Player = ImageIO.read(new File("./Content/Textures/Player.png"));
			PlayerArm = ImageIO.read(new File("./Content/Textures/PlayerArm.png"));
		} catch (IOException e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {
				
			}
		}
	}
	
	public void ChangeBackground() {
		try {
			Background = ImageIO.read(new File("./Content/Textures/brickFloor.jpg"));
		} catch (IOException e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {
				
			}
		}
	}
}
