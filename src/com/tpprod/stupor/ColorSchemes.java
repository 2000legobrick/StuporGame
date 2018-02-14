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
	
	private BufferedImage GroundTile  = null;
	private BufferedImage Background = null;
	
	public ColorSchemes () {

		try {
			GroundTile = ImageIO.read(new File("./Content/Textures/brickFloor.jpg"));
			Background = ImageIO.read(new File("./Content/Textures/Plains.png"));
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

	public BufferedImage getGroundTile() {
		return GroundTile;
	}

	public BufferedImage getBackground() {
		return Background;
	}
}
