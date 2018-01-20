package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Rectangle;

/*
 * The NewRectangle class stores data for the tiles that will be rendered.
 */

public class NewRectangle {
	public int type = 3;
	public Color color;
	public Rectangle rect;
	
	public NewRectangle(Color tempColor, Rectangle tempRectangle) {
		/*
		 * This is a constructor where the type is not important,
		 * 	but the color and the size needs to be set when the 
		 * 	object is created.
		 */
		
		rect = tempRectangle;
		color = tempColor;
	}
	
	public NewRectangle (int tempType, Rectangle tempRectangle) {
		/*
		 * This is another constructor where the the type and size are
		 * 	set when the object is created.
		 */
		
		type = tempType;
		rect = tempRectangle;
		
		if (type == 0) { 		// This is a CYAN empty tile
			color = null;
		} else if (type == 1) { // This is a wall tile
			color = new Color(51, 63, 72);
		} else if (type == 2) { // This is a WHITE empty tile
			color = new Color(255, 255, 255);
		} else if (type == 3) { // This is a RED empty tile
			color = Color.RED;
		}
	}
}
