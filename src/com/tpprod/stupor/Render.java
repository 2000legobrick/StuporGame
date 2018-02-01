package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/*
 * The Render Class draws everything to the canvas that is stored in 
 * 	the class StateMachine. 
 */

public class Render implements Runnable {
	
	public World world = new World();
	
	public static int fogOfWar = 12;
	
	public int currentWorld;
	public int currentMenuPos = 0;
	public int currentMouseX, currentMouseY;
	public BufferedImage img = null;
	public BufferedImage person = null;
	public BufferedImage arm = null;

	private ArrayList<NewRectangle> DisplayedObjects = new ArrayList<NewRectangle>();
	private ArrayList<NewRectangle> DisplayedMobs = new ArrayList<NewRectangle>();
	private ArrayList<NewRectangle> DisplayedItems = new ArrayList<NewRectangle>();
	
	
	//private Mob player;
	
	
	public Render() {
		/*
		 * This is the constructor for the Render Class, intentionally left blank
		 */
		try {
			img = ImageIO.read(new File("./Content/Textures/brickFloor.jpg"));
			person = ImageIO.read(new File("./Content/Textures/Player.png"));
			arm = ImageIO.read(new File("./Content/Textures/PlayerArm.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void InitializeWorld() throws IOException {
		/*
		 * The InitializeWorld method gets the most current version of the world to reference.
		 * 
		 */
		world.Initialize();
	}
	
	
	public void RenderState(Graphics g, int width, int height, int state, Mob player, World world){
		
		// renders a state based on what state is passed through the constructor
		
		switch(state) {
			case StateMachine.GameState:
				RenderBackground(g, width, height);
				RenderForeground(g, width, height, StateMachine.tileSize, Physics.mobs, player, world);
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
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, width, height);
	} 
	
	public void RenderMenu(Graphics g, int width, int height) {
		/*
		 * The method RenderMenu renders out the menu for the game.
		 */
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.RED);
		g.drawString("testing awesome", 500, 500);
		
		if (currentMouseX > 90 && currentMouseX < 210) {
			if (currentMouseY > 90 && currentMouseY < 160) {
				currentMenuPos = 0;
			} else if (currentMouseY > 190 && currentMouseY < 260) {
				currentMenuPos = 1;
			} else if (currentMouseY > 290 && currentMouseY < 360) {
				currentMenuPos = 2;
			}
		}
			
		if (currentMenuPos == 0) {
			g.setColor(Color.RED);
			g.drawString("Game", 100, 100);
		} else {
			g.setColor(Color.GREEN);
			g.drawString("Game", 100, 100);
		}
		
		if (currentMenuPos == 1) {
			g.setColor(Color.RED);
			g.drawString("Settings", 100, 200);
		} else {
			g.setColor(Color.GREEN);
			g.drawString("Settings", 100, 200);
		}
		
		if (currentMenuPos == 2) {
			g.setColor(Color.RED);
			g.drawString("Exit", 100, 300);
		} else {
			g.setColor(Color.GREEN);
			g.drawString("Exit", 100, 300);
		}
	} 
	
	public void RenderForeground(Graphics g, int width, int height, int tileSize, ArrayList<Mob> entities, Mob player, World world) {
		/*
		 * The RenderForeground method takes the blocks on screen and actually prints them to the canvas,
		 *  allowing the player to see the world.
		 */
		
		DisplayedObjects = new ArrayList<NewRectangle>();
		DisplayedMobs = new ArrayList<NewRectangle>();
		DisplayedItems = new ArrayList<NewRectangle>();
		
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
					DisplayedMobs.add(new NewRectangle(entity.image, new Rectangle(entity.currentX, entity.currentY, entity.width, entity.height)));
				}
			}
			if (entity.projectileList[0] != null) {
				if (entity.projectileList[0].shown) {
					DisplayedMobs.add(new NewRectangle(Color.BLACK, new Rectangle(entity.projectileList[0].currentX, entity.projectileList[0].currentY, entity.projectileList[0].size, entity.projectileList[0].size)));
				}
			}
		}
		for (Item item: world.inventory.currentItems) { 
			if (item.itemX + tileSize > player.currentX - tileSize*fogOfWar && item.itemX < player.currentX + tileSize*fogOfWar) {
				if (item.itemY + tileSize > player.currentY - tileSize*fogOfWar && item.itemY < player.currentY + tileSize*fogOfWar) {
					DisplayedItems.add(new NewRectangle(item.itemColor, new Rectangle(item.itemX, item.itemY, item.itemSize, item.itemSize)));
				}
			}
		}
		
		// Iterates through all objects that are on screen and displays them based off the players current position
		//  which is located at the center of the screen
		for (NewRectangle rect: DisplayedObjects) {
			try {
				if (rect.type == 1) {
					g.drawImage(img, rect.rect.x - player.currentX - player.width/2 + width / 2, rect.rect.y - player.currentY - player.height/2 + height/2, rect.rect.width, rect.rect.height, null);
					/*
					g.setColor(GetColorArray(currentWorld)[rect.type]);
					g.fillRect(rect.rect.x - player.currentX - player.width/2 + width / 2, rect.rect.y - player.currentY - player.height/2 + height/2, rect.rect.width, rect.rect.height);
					*/
				} else if (rect.type != 0) {
					g.setColor(GetColorArray(currentWorld)[rect.type]);
					g.fillRect(rect.rect.x - player.currentX - player.width/2 + width / 2, rect.rect.y - player.currentY - player.height/2 + height/2, rect.rect.width, rect.rect.height);
				}
			} catch (Exception e) {}
		}
		
		for (NewRectangle rect: DisplayedItems) {
			try {
				g.setColor(rect.color);
				g.fillRect(rect.rect.x - player.currentX - player.width/2 + width / 2,  rect.rect.y - player.currentY - player.height/2 + height/2, rect.rect.width, rect.rect.height);
			}catch(Exception e) {}
		}
		
		for (NewRectangle rect: DisplayedMobs) {
			//g.setColor(rect.color);
			//g.fillRect(rect.rect.x - player.currentX - player.width/2 + width / 2,  rect.rect.y - player.currentY - player.height/2 + height/2, rect.rect.width, rect.rect.height);
			if (rect.image != null) {
				AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-person.getWidth(null), 0);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				person = op.filter(person, null);
		
				g.drawImage(rect.image, rect.rect.x - player.currentX - player.width/2 + width / 2, rect.rect.y - player.currentY - player.height/2 + height/2, rect.rect.width, rect.rect.height, null);
			} else {
				g.setColor(rect.color);
				g.fillRect(rect.rect.x - player.currentX - player.width/2 + width / 2,  rect.rect.y - player.currentY - player.height/2 + height/2, rect.rect.width, rect.rect.height);
			}
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


	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
}
