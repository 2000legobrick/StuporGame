package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Rectangle;

public class NewRectangle {
	public int type;
	public Color color;
	public Rectangle rect;
	
	public NewRectangle(Color tempColor, Rectangle tempRectangle) {
		type = 3;
		rect = tempRectangle;
		color = tempColor;
	}
	
	public NewRectangle (int tempType, Rectangle tempRectangle) {
		type = tempType;
		rect = tempRectangle;
		
		if (type == 0) {
			color = Color.CYAN;
		}else if (type == 1) {
			color = new Color(51, 63, 72);
		} else if (type == 2) {
			color = new Color(255, 255, 255);
		} else if (type == 3) {
			color = Color.RED;
		}
	}
}
