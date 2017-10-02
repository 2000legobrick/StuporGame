package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Render {

	public World world = new World();
	
	private int tempPos = 200;
	private boolean flip = false;
	
	public Render() {
	}
	
	public void InitializeWorld() throws FileNotFoundException {
		world.Initialize();
	}
	
	public void RenderBackground(Graphics g, int width, int height) {
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, width, (height / 3) * 2);
	} 
	
	public void RenderForeground(Graphics g, int width, int height) {
		g.setColor(Color.GREEN);
		for (int row = 0; row < world.worldGrid.size(); row++) {
			for (int col = 0; col < world.worldGrid.get(0).size(); col++) {
				if (world.worldGrid.get(row).get(col) == 1) {
					g.setColor(Color.GREEN);
				} else if (world.worldGrid.get(row).get(col) == 0) {
					g.setColor(Color.CYAN);
				}
				g.fillRect(col * 10, row * 10, 10, 10);
			}
		}
	}
	
	public void Movement(Graphics g) {
		g.setColor(Color.RED);
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
