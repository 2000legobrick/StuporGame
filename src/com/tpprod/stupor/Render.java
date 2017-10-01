package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Graphics;

public class Render {

	private int tempPos = 200;
	private boolean flip = false;
	
	public void RenderBackground(Graphics g, int width, int height) {
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, width, height / 2);
	}
	
	public void RenderForeground(Graphics g, int width, int height) {
		g.setColor(Color.GREEN);
		g.fillRect(0, height / 2, width, height / 2);
	}
	
	public void Movement(Graphics g) {
		g.setColor(Color.BLACK);
		if (tempPos > 500) {
			flip = true;
		} else if (tempPos < 100) {
			flip = false;
		}
		if (!flip)
			tempPos++;
		else
			tempPos--;
		g.fillRect(tempPos, 200, 10, 10);
	}
}
