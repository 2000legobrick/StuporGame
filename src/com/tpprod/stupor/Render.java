package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Render {
	
	public World world = new World();
	
	public static int fogOfWar = 12;

	private ArrayList<NewRectangle> DisplayedObjects = new ArrayList<NewRectangle>();
	private ArrayList<NewRectangle> DisplayedMobs = new ArrayList<NewRectangle>();
	private Mob player;
	
	
	public Render() {
	}
	
	
	public void InitializeWorld() throws FileNotFoundException {
		world.Initialize();
	}
	
	public void RenderBackground(Graphics g, int width, int height) {
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, width, height);
	} 
	
	public void RenderForeground(Graphics g, int width, int height, int tileSize, ArrayList<Mob> entities) {
		/*
		for (int row = 0; row < world.worldGrid.size(); row++) {
			for (int col = 0; col < world.worldGrid.get(0).size(); col++) {
				if (world.worldGrid.get(row).get(col) == 1) {
					DisplayedObjects.add(new NewRectangle(1, Color.GREEN, new Rectangle(col*tileSize, row*tileSize, tileSize, tileSize)));
				} else if (world.worldGrid.get(row).get(col) == 0) {
					DisplayedObjects.add(new NewRectangle(0, Color.CYAN, new Rectangle(col*tileSize, row*tileSize, tileSize, tileSize)));
				}
			}
		}
		for (Mob entity: entities) {
			DisplayedMobs.add(new NewRectangle(10, entity.playerColor, new Rectangle(entity.currentX, entity.currentY, entity.width, entity.height)));
		}*/
		
		//BROKEN UP TOP

		player = entities.get(0);
		
		DisplayedObjects = new ArrayList<NewRectangle>();
		DisplayedMobs = new ArrayList<NewRectangle>();
		
		for (int y = (int)(player.currentY / tileSize)-fogOfWar; y <= (int)(player.currentY / tileSize)+fogOfWar; y++) {
			for (int x = (int)(player.currentX / tileSize)-fogOfWar; x <= (int)(player.currentX / tileSize)+fogOfWar; x++) {
				try {
					DisplayedObjects.add(world.worldGrid.get(y).get(x));
				} catch (Exception e) {}
			}
		}
		
		for (Mob entity: entities) { 
			if (entity.currentX + tileSize > player.currentX - tileSize*fogOfWar && entity.currentX < player.currentX + tileSize*fogOfWar) {
				if (entity.currentY + tileSize > player.currentY - tileSize*fogOfWar && entity.currentY < player.currentY + tileSize*fogOfWar) {
					DisplayedMobs.add(new NewRectangle(entity.playerColor, new Rectangle(entity.currentX, entity.currentY, entity.width, entity.height)));
				}
			}
		}
		
		
		for (NewRectangle rect: DisplayedObjects) {
			try {
				g.setColor(rect.color);
				g.fillRect(rect.rect.x - DisplayedMobs.get(0).rect.x + width / 2, rect.rect.y - DisplayedMobs.get(0).rect.y + height/2, rect.rect.width, rect.rect.height);
			} catch (Exception e) {}
		}
		
		for (NewRectangle rect: DisplayedMobs) {
			g.setColor(rect.color);
			g.fillRect(rect.rect.x - DisplayedMobs.get(0).rect.x + width / 2,  rect.rect.y - DisplayedMobs.get(0).rect.y + height/2, rect.rect.width, rect.rect.height);
		}
		
		/*
		for (NewRectangle rect: DisplayedObjects) {
			g.setColor(rect.color);
			g.fillRect(rect.rect.x - DisplayedMobs.get(0).rect.x + width / 2, rect.rect.y - DisplayedMobs.get(0).rect.y + height/2, rect.rect.width, rect.rect.height);
		}
		for (NewRectangle rect: DisplayedMobs) {
			g.setColor(rect.color);
			g.fillRect(rect.rect.x - DisplayedMobs.get(0).rect.x + width / 2,  rect.rect.y - DisplayedMobs.get(0).rect.y + height/2, rect.rect.width, rect.rect.height);
		}*/
	}
}
