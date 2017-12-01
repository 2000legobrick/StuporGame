package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;

/*
 * The Render Class draws everything to the canvas that is stored in 
 * 	the class StateMachine. 
 */

public class Render {
	
	public World world = new World();
	
	public static int fogOfWar = 12;
	
	public int currentWorld;

	private ArrayList<NewRectangle> DisplayedObjects = new ArrayList<NewRectangle>();
	private ArrayList<NewRectangle> DisplayedMobs = new ArrayList<NewRectangle>();
	
	
	//private Mob player;
	
	
	public Render() {
		/*
		 * This is the constructor for the Render Class, intentionally left blank
		 */
	}
	
	
	public void InitializeWorld() throws IOException {
		/*
		 * The InitializeWorld method gets the most current version of the world to reference.
		 * 
		 */
		world.Initialize();
	}
	
	
	public void RenderState(Graphics g, int width, int height, int state){
		
		// renders a state based on what state is passed through the constructor
		
		switch(state) {
			case StateMachine.GameState:
				RenderBackground(g, width, height);
				RenderForeground(g, width, height, StateMachine.tileSize, Physics.mobs, Physics.player);
				break;
			case StateMachine.MenuState:
				RenderMenu(g, width,height);
				break;
			case StateMachine.PauseState:
				
				break;
			case StateMachine.InventoryState:
				
				break;
			case StateMachine.DeadState:
				
				break;
		}
		
	}
	
	public void RenderBackground(Graphics g, int width, int height) {
		/*
		 * The method RenderBackground renders out the backdrop of the game.
		 */
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, width, height);
	} 
	
	public void RenderMenu(Graphics g, int width, int height) {
		/*
		 * The method RenderMenud renders out the menu for the game.
		 */
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.RED);
		g.drawString("testing awesome", 500, 500);
	} 
	
	public void RenderForeground(Graphics g, int width, int height, int tileSize, ArrayList<Mob> entities, Mob player) {
		/*
		 * The RenderForeground method takes the blocks on screen and actually prints them to the canvas,
		 *  allowing the player to see the world.
		 */
		
		DisplayedObjects = new ArrayList<NewRectangle>();
		DisplayedMobs = new ArrayList<NewRectangle>();
		
		// Iterates through the world blocks and mobs adding the objects and mobs that will be on screen
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
		
		// Iterates through all objects that are on screen and displays them based off the players current position
		//  which is located at the center of the screen
		for (NewRectangle rect: DisplayedObjects) {
			try {
				g.setColor(GetColorArray(currentWorld)[rect.type]);
				g.fillRect(rect.rect.x - player.currentX - player.width/2 + width / 2, rect.rect.y - player.currentY - player.height/2 + height/2, rect.rect.width, rect.rect.height);
			} catch (Exception e) {}
		}
		
		for (NewRectangle rect: DisplayedMobs) {
			g.setColor(rect.color);
			g.fillRect(rect.rect.x - player.currentX - player.width/2 + width / 2,  rect.rect.y - player.currentY - player.height/2 + height/2, rect.rect.width, rect.rect.height);
		}
	}
	
	public Color[] GetColorArray(int selection) {
		if (selection == 1) {
			return ColorSchemes.World1;
		} else {
			return ColorSchemes.World2;
		}
	}
	
	public void ChangeWorld(int newWorld) {
		/*
		 * This is used to change the current world that
		 * 	is being rendered.
		 */
		
		currentWorld = newWorld;
	}
}
