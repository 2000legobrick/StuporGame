package com.tpprod.stupor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/*
 * The Render Class draws everything to the canvas that is stored in 
 * 	the class StateMachine. 
 */

public class Render {
	
	private static final int GameState    =  StateMachine.getGamestate();
	private static final int MenuState    =  StateMachine.getMenustate();
	private static final int PauseState   =  StateMachine.getPausestate();
	private static final int OptionState  =  StateMachine.getOptionstate();
	private static final int UpgradeState =  StateMachine.getUpgradestate();
	private static final int DeadState    =  StateMachine.getDeadstate();
	
	private static volatile int CurrentState       =  StateMachine.getCurrentState();

	private World world = new World();
	private static MusicPlayer bgMusic = new MusicPlayer(true);
	private ColorSchemes palette = new ColorSchemes();
	private static int volume = 5;

	private static int fogOfWar = 7;

	private boolean loading = false;
	private int currentMenuPos = 0;
	private int currentMouseX, currentMouseY;

	private ArrayList<NewRectangle> DisplayedObjects = new ArrayList<NewRectangle>();
	private ArrayList<NewRectangle> DisplayedMobs = new ArrayList<NewRectangle>();
	private ArrayList<NewRectangle> DisplayedItems = new ArrayList<NewRectangle>();
	private int percentLoad = 0;

	public Render() {
		/*
		 * This is the constructor for the Render Class, intentionally left blank
		 */
	}

	public void stopBackgroundMusic() {
		bgMusic.stop();
	}

	public void startBackgroundMusic() {
		bgMusic.start();
	}

	public void InitializeWorld() throws IOException {
		/*
		 * The InitializeWorld method gets the most current version of the world to
		 * reference.
		 */
		world.Initialize();
	}

	/*
	 * Renders the different states of the game
	 */
	public void RenderState(Graphics g, int width, int height, int state, Mob player, int NextState) {
		CurrentState = state;
		// renders a state based on what state is passed through the constructor
		if (NextState == state) {
			if (CurrentState == GameState) {
				RenderBackground(g, width, height, player);
				RenderForeground(g, width, height, StateMachine.getTileSize(), Physics.getMobs(), player, world);
				RenderHUD(g, player, width, height);
				bgMusic.start();
			} else if (CurrentState == MenuState) {
				RenderMenu(g, width, height);
				bgMusic.stop();
			} else if (CurrentState == OptionState) {
				RenderOption(g, width, height);
				bgMusic.stop();
			} else if (CurrentState == PauseState) {
				RenderPause(g, width, height);
			} else if (CurrentState == UpgradeState) {
				RenderUpgrade(g, width, height);
			} else if (CurrentState == DeadState) {

			}
		} else {
			loading = true;
			RenderLoad(g, width, height);
		}
	}

	/*
	 * Renders the loading screen
	 */
	public void RenderLoad(Graphics g, int width, int height) {

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.WHITE);
		g.fillRect(width - 150, height - 150, 110, 40);
		g.setColor(Color.BLACK);
		g.fillRect(width - 150, height - 150, 100, 35);
		g.setColor(Color.WHITE);
		g.fillRect(width - 150, height - 150, percentLoad, 30);
		g.setFont(new Font("Impact", Font.PLAIN, 500));
		g.setColor(new Color(150, 150, 150));
		g.drawString("LOADING", 125, 475);
		g.setColor(new Color(200, 200, 200));
		g.drawString("LOADING", 160, 525);
		g.setColor(Color.WHITE);
		g.drawString("LOADING", 150, 500);
		if (percentLoad >= 100) {
			loading = false;
			percentLoad = 0;
		} else {
			percentLoad++;
		}
	}

	/*
	 * Renders the pause screen
	 */
	private void RenderPause(Graphics g, int width, int height) {
		/*
		 * The method RenderPause renders out the pause menu for the game.
		 */

		g.setFont(new Font("Impact", Font.PLAIN, 40));
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, width, height);
		g.setColor(Color.RED);

		ArrayList<Point> menuPoints = new ArrayList<Point>();

		for (int x = 0; x < 4; x++)
			menuPoints.add(new Point(150, 90 + (100 * x)));

		currentMenuPos = getClosestIndex(menuPoints, new Point(currentMouseX, currentMouseY));

		if (currentMenuPos == 0) {
			g.setColor(Color.RED);
			g.drawString("Resume", 100, 100);
		} else {
			g.setColor(Color.WHITE);
			g.drawString("Resume", 100, 100);
		}

		if (currentMenuPos == 1) {
			g.setColor(Color.RED);
			g.drawString("Save", 100, 200);
		} else {
			g.setColor(Color.WHITE);
			g.drawString("Save", 100, 200);
		}

		if (currentMenuPos == 2) {
			g.setColor(Color.RED);
			g.drawString("Options", 100, 300);
		} else {
			g.setColor(Color.WHITE);
			g.drawString("Options", 100, 300);
		}

		if (currentMenuPos == 3) {
			g.setColor(Color.RED);
			g.drawString("Main Menu", 100, 400);
		} else {
			g.setColor(Color.WHITE);
			g.drawString("Main Menu", 100, 400);
		}
	}

	/*
	 * Renders the background for the game
	 */
	public void RenderBackground(Graphics g, int width, int height, Mob player) {
		/*
		 * The method RenderBackground renders out the backdrop of the game.
		 */
		boolean flipBool = false;
		for (int x = (width - player.getCurrentX() / 5) - width; x < width; x += width) {
			if (flipBool) {
				g.drawImage(palette.getBackground(), x, 0, width, height, null);
				flipBool = !flipBool;
			} else {
				g.drawImage(palette.getBackground(), x + width, 0, -width, height, null);
				flipBool = !flipBool;
			}
		}
	}

	/*
	 * Returns the closes index of a point to given point
	 */
	public int getClosestIndex(ArrayList<Point> pointList, Point p2) {
		int closestIndex = 0, closestDistance = -1, currentDistance;
		for (Point p1: pointList) {
			currentDistance =  (int) Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2)+ Math.pow(p2.getY() - p1.getY(), 2));
			if (closestDistance == -1 || closestDistance > currentDistance) {
				closestDistance = currentDistance;
				closestIndex = pointList.indexOf(p1);
			}
		}
		if (closestDistance < 100 ) { 
			if (currentMenuPos != closestIndex)
				new AudioFile("Content/Audio/SoundEffects/Menu.wav").play(bgMusic.getAudioVolume()-2);
			return closestIndex;
		}
		return -1;
	}
	

	/*
	 * Renders the menu screen
	 */
	public void RenderMenu(Graphics g, int width, int height) {
		/*
		 * The method RenderMenu renders out the menu for the game.
		 */

		g.setFont(new Font("Impact", Font.PLAIN, 40));
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.RED);

		ArrayList<Point> menuPoints = new ArrayList<Point>();

		for (int x = 0; x < 4; x++)
			menuPoints.add(new Point(150, 90 + (100 * x)));

		currentMenuPos = getClosestIndex(menuPoints, new Point(currentMouseX, currentMouseY));

		if (currentMenuPos == 0) {
			g.setColor(Color.RED);
			g.drawString("New Game", 100, 100);
		} else {
			g.setColor(Color.WHITE);
			g.drawString("New Game", 100, 100);
		}

		if (ResourceManager.hasData("SaveData")) {
			if (currentMenuPos == 1) {
				g.setColor(Color.RED);
				g.drawString("Load Game", 100, 200);
			} else {
				g.setColor(Color.WHITE);
				g.drawString("Load Game", 100, 200);
			}
		} else {
			g.setColor(Color.GRAY);
			g.drawString("Load Game", 100, 200);
		}

		if (currentMenuPos == 2) {
			g.setColor(Color.RED);
			g.drawString("Settings", 100, 300);
		} else {
			g.setColor(Color.WHITE);
			g.drawString("Settings", 100, 300);
		}

		if (currentMenuPos == 3) {
			g.setColor(Color.RED);
			g.drawString("Exit", 100, 400);
		} else {
			g.setColor(Color.WHITE);
			g.drawString("Exit", 100, 400);
		}
	}

	/*
	 * Renders the options screen
	 */
	public void RenderOption(Graphics g, int width, int height) {
		/*
		 * The method RenderOption renders out the option menu for the game.
		 */
		bgMusic.stop();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

        Dimension box = new Dimension(50,50);
        g.setColor(new Color(20,75,40)); // A Greenish Color

        for (int x = 1; x <= 10; x++) {
            g.fillRect(1000 - (box.width + 10) * x, 100 - (box.height/2), box.width, box.height);
        }
        Dimension box1 = new Dimension(40,40);
        g.setColor(new Color(255,255,255)); // Pure White
        for (int x = 1; x <= volume; x++) {
            g.fillRect(345 + (box.width + 10) * x, 100 - (box1.height/2), box1.width, box1.height);
        }

		g.setFont(new Font("Impact", Font.PLAIN, 40));
		g.setColor(Color.RED);

		ArrayList<Point> optionPoints = new ArrayList<Point>();

		optionPoints.add(new Point(350, 115));
		optionPoints.add(new Point(1150, 115));
		optionPoints.add(new Point(150, 215));
		
		currentMenuPos = getClosestIndex(optionPoints, new Point(currentMouseX, currentMouseY));

		g.setColor(Color.WHITE);
		g.drawString("Volume", 100, 125);
		
		g.setFont(new Font("Impact", Font.PLAIN, 80));

		if (currentMenuPos == 0) {
			g.setColor(Color.RED);
			g.drawString("-", 300, 125);
		} else {
			g.setColor(Color.WHITE);
			g.drawString("-", 300, 125);
		}

		if (currentMenuPos == 1) {
			g.setColor(Color.RED);
			g.drawString("+", 1050, 135);
		} else {
			g.setColor(Color.WHITE);
			g.drawString("+", 1050, 135);
		}
		
		g.setFont(new Font("Impact", Font.PLAIN, 40));
		
		if (currentMenuPos == 2) {
			g.setColor(Color.RED);
			g.drawString("Main Menu", 100, 225);
		} else {
			g.setColor(Color.WHITE);
			g.drawString("Main Menu", 100, 225);
		}
	}

	/*
	 * Renders the foreground of the game
	 */
	public void RenderForeground(Graphics g, int width, int height, int tileSize, ArrayList<Mob> entities, Mob player,
			World world) {
		/*
		 * The RenderForeground method takes the blocks on screen and actually prints
		 * them to the canvas, allowing the player to see the world.
		 */

		DisplayedObjects = new ArrayList<NewRectangle>();
		DisplayedMobs = new ArrayList<NewRectangle>();
		DisplayedItems = new ArrayList<NewRectangle>();

		// Iterates through the world blocks and mobs adding the objects and mobs that
		// will be on screen
		for (int y = (int) (player.getCurrentY() / tileSize) - fogOfWar; y <= (int) (player.getCurrentY() / tileSize)
				+ fogOfWar; y++) {
			for (int x = (int) (player.getCurrentX() / tileSize)
					- fogOfWar; x <= (int) (player.getCurrentX() / tileSize) + fogOfWar; x++) {
				try {
					DisplayedObjects.add(world.getWorldGrid().get(y).get(x));
				} catch (Exception e) {
					// NON FATAL ERROR
				}
			}
		}

		for (Mob entity : entities) {
			if (entity.getHealth() > 0) {
				if (entity.getCurrentX() + tileSize > player.getCurrentX() - tileSize * fogOfWar
						&& entity.getCurrentX() < player.getCurrentX() + tileSize * fogOfWar) {
					if (entity.getCurrentY() + tileSize > player.getCurrentY() - tileSize * fogOfWar
							&& entity.getCurrentY() < player.getCurrentY() + tileSize * fogOfWar) {
						DisplayedMobs.add(new NewRectangle(entity.getImage(), new Rectangle(entity.getCurrentX(),
								entity.getCurrentY(), entity.getWidth(), entity.getHeight())));
					}
				}
			}
			for (Projectile proj : entity.getProjectileList()) {
				DisplayedMobs.add(new NewRectangle(Color.WHITE,
						new Rectangle(proj.getCurrentX(), proj.getCurrentY(), proj.getWidth(), proj.getHeight())));
			}
		}

		for (Item item : world.getWorldInventory().getCurrentItems()) {
			if (item.itemX + tileSize > player.getCurrentX() - tileSize * fogOfWar
					&& item.itemX < player.getCurrentX() + tileSize * fogOfWar) {
				if (item.itemY + tileSize > player.getCurrentY() - tileSize * fogOfWar
						&& item.itemY < player.getCurrentY() + tileSize * fogOfWar) {
					DisplayedItems.add(new NewRectangle(item.itemColor,
							new Rectangle(item.itemX, item.itemY, item.itemSize, item.itemSize)));
				}
			}
		}

		// Iterates through all objects that are on screen and displays them based off
		// the players current position
		// which is located at the center of the screen
		for (NewRectangle rect : DisplayedMobs) {
			if (rect.getImage() != null) {
				g.drawImage(rect.getImage(),
						rect.getRect().x - player.getCurrentX() - player.getWidth() / 2 + width / 2,
						rect.getRect().y - player.getCurrentY() - player.getHeight() / 2 + height / 2,
						rect.getRect().width, rect.getRect().height, null);
			} else {
				g.setColor(rect.getColor());
				g.fillRect(rect.getRect().x - player.getCurrentX() - player.getWidth() / 2 + width / 2,
						rect.getRect().y - player.getCurrentY() - player.getHeight() / 2 + height / 2,
						rect.getRect().width, rect.getRect().height);
			}
		}

		for (NewRectangle rect : DisplayedItems) {
			try {
				g.setColor(rect.getColor());
				g.fillRect(rect.getRect().x - player.getCurrentX() - player.getWidth() / 2 + width / 2,
						rect.getRect().y - player.getCurrentY() - player.getHeight() / 2 + height / 2,
						rect.getRect().width, rect.getRect().height);
			} catch (Exception e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try {
					Log.add(error.toString());
				} catch (Exception e1) {

				}
			}
		}

		for (NewRectangle rect : DisplayedObjects) {
			try {
				if (rect.getType() == 1) {
					g.drawImage(palette.getGroundTile(),
							rect.getRect().x - player.getCurrentX() - player.getWidth() / 2 + width / 2,
							rect.getRect().y - player.getCurrentY() - player.getHeight() / 2 + height / 2,
							rect.getRect().width, rect.getRect().height, null);
				} else if (rect.getType() != 0) {

				}
			} catch (Exception e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try {
					Log.add(error.toString());
				} catch (Exception e1) {

				}
			}
		}

		for (NewRectangle rect : DisplayedObjects) {
			try {
				if (rect.getType() == 1) {
					g.drawImage(palette.getGroundTile(),
							rect.getRect().x - player.getCurrentX() - player.getWidth() / 2 + width / 2,
							rect.getRect().y - player.getCurrentY() - player.getHeight() / 2 + height / 2,
							rect.getRect().width, rect.getRect().height, null);
				} else if (rect.getType() != 0) {

				}
			} catch (Exception e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try {
					Log.add(error.toString());
				} catch (Exception e1) {

				}
			}
		}
	}

	/*
	 * Renders the upgrade state of the game
	 */
	public void RenderUpgrade(Graphics g, int width, int height) {
		int middleWidth = width / 2;
		int middleHeight = height / 2;
		int distanceFromCenter = 300;

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		ArrayList<Point> upgradePoints = new ArrayList<Point> ();
		
		Dimension box = new Dimension(125,125);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(4));
		g.setColor(Color.WHITE);
		for (int x = -1; x < 2; x++) {
			g.drawRect(middleWidth - box.width/2 - (300 * x), middleHeight - box.height/2, box.width, box.height);
			upgradePoints.add(new Point(middleWidth - (300 * x), middleHeight));
		}
		
		currentMenuPos = getClosestIndex(upgradePoints, new Point(currentMouseX, currentMouseY));
		
		g.setColor(Color.RED);
		if (currentMenuPos != -1)
			g.drawRect(middleWidth - box.width/2 - (300 * (currentMenuPos - 1)), middleHeight - box.height/2, box.width, box.height);
		
	}

	/*
	 * renders the HUD of the player, items, health, mana, etc
	 */
	public void RenderHUD(Graphics g, Mob player, int width, int height) {
		int middleWidth = width / 2;
		int middleHeight = height / 2;
		Dimension box = new Dimension(100, 100);
		// Render Background for Items
		g.setColor(new Color(40, 0, 50, 200));
		g.fillArc(middleWidth - box.width * 2 - (box.width + 10) - 20, height - (box.height + 20) - 10, box.width + 20,
				box.height + 20, 90, 180);
		g.fillArc(middleWidth + box.width + (box.width + 10), height - (box.height + 20) - 10, box.width + 20,
				box.height + 20, 270, 180);
		g.fillRect(middleWidth - box.width * 3 / 2 - (box.width + 10) - 10, height - (box.height + 20) - 10,
				box.width * 5 + 40, box.height + 20);
		// Render Item Boxes
		g.setColor(new Color(255, 255, 255, 200));
		for (int x = 2; x >= -1; x--) {
			g.fillRect(middleWidth + 5 - (box.width + 10) * x, height - (box.height + 20), box.width, box.height);
		}
		// Render Health
		double average = (double) player.getHealth() / player.getMaxHealth();
		;
		g.setColor(new Color(255, 0, 0, 200));
		g.fillArc(middleWidth - box.width - (box.width + 10) * 2, height - (box.height + 20), box.width, box.height, 90,
				(int) (360 * average));
		// Render Mana
		average = (double) player.getMana() / player.getMaxMana();
		g.setColor(new Color(50, 0, 255, 200));
		g.fillArc(middleWidth + (box.width + 10) * 2, height - (box.height + 20), box.width, box.height, 90,
				(int) (360 * average));
		// Render Item
		for (int i = 0; i < 4; i++) {
			try {
				g.setColor(player.getInventory().getCurrentMobItems()[i].itemColor);
				g.fillRect(middleWidth + 5 + (box.width + 10) * (i - 2) + box.width / 4,
						height - (box.height + 20) + box.height / 4, box.width / 2, box.height / 2);
			} catch (Exception e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try {
					Log.add(error.toString());
				} catch (Exception e1) {

				}
			}
		}
		// Render EXP
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 40));
		g.drawString("EXP: " + Integer.toString(player.getEXP()), 10, height - 10);
		g.drawString("JUMP: " + Integer.toString(player.getJumpAmount()), 10, height - 50);
		g.drawString("MANA REGEN: " + Integer.toString(player.getManaRefreshTimer()), 10, height - 90);
	}

	/*
	 * Getters and setters for Render
	 */
	public boolean isLoading() {
		return loading;
	}

	public int getCurrentMenuPos() {
		return currentMenuPos;
	}

	public void setCurrentMenuPos(int currentMenuPos) {
		this.currentMenuPos = currentMenuPos;
	}

	public void setCurrentMouseX(int currentMouseX) {
		this.currentMouseX = currentMouseX;
	}

	public void setCurrentMouseY(int currentMouseY) {
		this.currentMouseY = currentMouseY;
	}

	public World getWorld() {
		return world;
	}

	public static MusicPlayer getBackgroundMusic() {
		return bgMusic;
	}

	public static int getVolume() {
		return volume;
	}

	public void setVolume(int v) {
		volume = v;
	}

}
