package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/*
 * The NewRectangle class stores data for the tiles that will be rendered.
 */

public class NewRectangle {

	private int type = 3;
	private Color color;
	private Rectangle rect;
	private BufferedImage image;

	/*
	 * makes a new rectangle based on arguments
	 */
	public NewRectangle(BufferedImage tempImage, Rectangle tempRectangle) {
		/*
		 * This is a constructor where the type is not important, but the color and the
		 * size needs to be set when the object is created.
		 */

		rect = tempRectangle;
		image = tempImage;
	}

	/*
	 * makes a new rectangle based on arguments
	 */
	public NewRectangle(Color tempColor, Rectangle tempRectangle) {
		/*
		 * This is a constructor where the type is not important, but the color and the
		 * size needs to be set when the object is created.
		 */

		rect = tempRectangle;
		color = tempColor;
	}

	/*
	 * makes a new rectangle based on arguments
	 */
	public NewRectangle(int tempType, Rectangle tempRectangle) {
		/*
		 * This is another constructor where the the type and size are set when the
		 * object is created.
		 */

		type = tempType;
		rect = tempRectangle;

		if (type == 0) { // This is a CYAN empty tile
			color = null;
		} else if (type == 1) { // This is a wall tile
			color = new Color(51, 63, 72);
		} else if (type == 2) { // This is a WHITE empty tile
			color = new Color(255, 255, 255);
		} else if (type == 3) { // This is a RED empty tile
			color = Color.RED;
		}
	}

	/*
	 * Getters for variables needed outside of newRectangle
	 */
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public Color getColor() {
		return color;
	}

	public Rectangle getRect() {
		return rect;
	}

	public BufferedImage getImage() {
		return image;
	}
}
