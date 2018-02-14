package com.tpprod.stupor;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;


/*
 * The StateMachine Class does all the heavy lifting, controlling all the logic for each state of the game.
 * 
 * 	Possible States:
 * 		GameState      - Actual game is ran in this state
 * 		MenuState      - The main menu is displayed (typically at start of game)
 * 		PauseState     - The game does not update but instead stops updating until state is changed
 * 		InventoryState - Displays the current inventory, game functions are suspended here like PuaseState 
 * 		DeadState      - Informs the player on their death and resets state to the MenuState 
 */

public class StateMachine extends Canvas implements Runnable, KeyListener, MouseInputListener {

	// Static variables

	private static final int GameState    = 0;
	private static final int MenuState    = 1;
	private static final int PauseState   = 2;
	private static final int UpgradeState = 3;
	private static final int DeadState    = 4;
	private static final int LoadState    = 4;

	private static int CurrentState = MenuState;

	private static ArrayList<Integer> currentKeys = new ArrayList<Integer>();
	private JFrame frame = new JFrame();
	private boolean closeGame = false;
	private Physics physics = new Physics();
	private static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width,
			HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	private static int tileSize = 150;
	private static final int tickPerSec = 60; // Limits the amount of ticks per second, serves to limit the all powerful ticks
	private static final String NAME = "Stupor";

	private static final long serialVersionUID = 1L;
	private static boolean running = false;
	private Render render = new Render();
	private static int NextState = MenuState;
	
	static GraphicsDevice device = GraphicsEnvironment
	        .getLocalGraphicsEnvironment().getScreenDevices()[0];
	
	public StateMachine() {
		/*
		 * This is the StateMachine constructor, intentionally left empty.
		 */
	}
	
	@Override
	public void run() {
		/*
		 * The run method is called at the launch of the program, allowing us to
		 * effectively package our game into a .jar file type.
		 * 
		 * We use this method to show and run the current state that the game is in.
		 * 
		 * Possible States: GameState - Actual game is ran in this state MenuState - The
		 * main menu is displayed (typically at start of game) PauseState - The game
		 * does not update but instead stops updating until state is changed
		 * InventoryState - Displays the current inventory, game functions are suspended
		 * here like PuaseState DeadState - Informs the player on their death and resets
		 * state to the MenuState
		 */

		// Since the World file might be lost we surrounded it with a try-catch
		try {
			// tries to initialize the world in render, and updates physics with the same
			// world
			render.InitializeWorld();

			physics.setWorld(render.getWorld());
		} catch (Exception e) {
			// If the file isn't found an error is printed and the program stops
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {
				
			}
		}

		// This resets the current world to the first world
		//render.ChangeWorld(1);

		// Required so that the programs keeps tracks KeyEvents
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		// These variables are specific only to the run method, and keep track of
		// the FramesPerSecond and each tick as well as if the program is allowed
		// to render objects
		int tick = 0;
		double timer = System.currentTimeMillis();

		double nsPerTick = 1000000000.0d / tickPerSec;
		double previous = System.nanoTime();
		double unprocessed = 0;
		int fps = 0;

		// If the program is running, we run this chunk of code
		while (running) {
			double current = System.nanoTime();
			unprocessed += (current - previous) / nsPerTick;
			previous = current;
			while (unprocessed >= 1) {
				/*
				 * Switches actual game state based on current state
				 */
				//System.out.println(currentKeys);
				switch (CurrentState) {
					case GameState:
		
						// These are the actions that each key is tied to,
						// the key that is referenced is found in-line with
						// the if statement.
	
						if (currentKeys.indexOf(17) != -1) { // Ctrl key
							physics.getPlayer().ResetMana();
							physics.getPlayer().ResetHealth();
						}
						if (currentKeys.indexOf(27) != -1) { // Escape Key
							physics.stop();
							NextState = MenuState;
						}
						if (currentKeys.indexOf(87) != -1) { // W Key or Space Bar
							physics.getPlayer().Jump();
						}
						if (currentKeys.indexOf(16) != -1) {
							if (currentKeys.indexOf(65) != -1) { // A Key
								physics.mobMove(physics.getPlayer(), 3, physics.getPlayer().getSpeed());
							}
							if (currentKeys.indexOf(68) != -1) { // D Key
								physics.mobMove(physics.getPlayer(), 4, physics.getPlayer().getSpeed());
							}
						} else {
							if (currentKeys.indexOf(65) != -1) { // A Key
								physics.mobMove(physics.getPlayer(), 3, physics.getPlayer().getSpeed()*2/3);
							}
							if (currentKeys.indexOf(68) != -1) { // D Key
								physics.mobMove(physics.getPlayer(), 4, physics.getPlayer().getSpeed()*2/3);
							}
						}
						if (currentKeys.indexOf(83) != -1) { // S Key
						}
						if (currentKeys.indexOf(90) != -1) { // Z Key
                            physics.pickUpItem(physics.getPlayer());
						}
						if (currentKeys.indexOf(88) != -1) { // X key
							physics.getPlayer().HurtMob(1);
						}
						if (currentKeys.indexOf(192) != -1) { // Tilde Key
							physics.stop();
							NextState = UpgradeState;
							CurrentState = UpgradeState;
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {}
						}
                        if (currentKeys.indexOf(72) != -1) { // H key
                            if(physics.getPlayer().getInventory().getCurrentItems().size() != 0)
                                physics.getPlayer().useItem(physics.getPlayer().getInventory().getCurrentItems().get(0));
						}
						if (currentKeys.indexOf(87) == -1 && currentKeys.indexOf(87) == -1) { // A Key AND D key
							for (Mob entity : physics.getMobs()) {
								physics.Dampening(entity);
							}
						}
						if (tick % physics.getPlayer().getManaRefreshTimer() == 0) {
							if (physics.getPlayer().getMana() < physics.getPlayer().getMaxMana())
								physics.getPlayer().setMana(physics.getPlayer().getMana()+1);
						}
					case MenuState:
						if (currentKeys.indexOf(87) != -1) { // W Key
							render.setCurrentMenuPos(render.getCurrentMenuPos()-1);
							currentKeys.remove(currentKeys.indexOf(87));
						}
						if (currentKeys.indexOf(83) != -1) { // S Key
							render.setCurrentMenuPos(render.getCurrentMenuPos()+1);
							currentKeys.remove(currentKeys.indexOf(83));
						}
						if (currentKeys.indexOf(38) != -1) { // Up Arrow
							render.setCurrentMenuPos(render.getCurrentMenuPos()-1);
							currentKeys.remove(currentKeys.indexOf(38));
						}
						if (currentKeys.indexOf(40) != -1) { // Down Arrow
							render.setCurrentMenuPos(render.getCurrentMenuPos()+1);
							currentKeys.remove(currentKeys.indexOf(40));
						}
						if (render.getCurrentMenuPos() > 2) {
							render.setCurrentMenuPos(2);
						} else if (render.getCurrentMenuPos() < 0) {
							render.setCurrentMenuPos(0);
						}
						if (currentKeys.indexOf(10) != -1) { // Enter Key
							if (render.getCurrentMenuPos() == 0) {
								NextState = GameState;
								physics.start();
							} else if (render.getCurrentMenuPos() == 2) {
								this.stop();
							}
						}
					case UpgradeState:
						if (currentKeys.indexOf(10) != -1) { // EnterKey
							if (render.getCurrentMenuPos() == 1) {
								if (physics.getPlayer().getEXP() >= 5) {
									physics.getPlayer().setEXP(physics.getPlayer().getEXP() - 5);
									physics.getPlayer().setJumpAmount(physics.getPlayer().getJumpAmount() + 1);
									currentKeys.remove(currentKeys.indexOf(10));
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {}
								}
							}
							if (render.getCurrentMenuPos() == 2) {
								if (physics.getPlayer().getEXP() >= 5 && physics.getPlayer().getManaRefreshTimer() > 5) {
									physics.getPlayer().setEXP(physics.getPlayer().getEXP() - 5);
									physics.getPlayer().setManaRefreshTimer(physics.getPlayer().getManaRefreshTimer() - 5);
									currentKeys.remove(currentKeys.indexOf(10));
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {}
								}
							}
							if (render.getCurrentMenuPos() == 3) {
								if (physics.getPlayer().getEXP() >= 5) {
									physics.getPlayer().setEXP(physics.getPlayer().getEXP()- 5);
									physics.getPlayer().setManaRefreshTimer(physics.getPlayer().getManaRefreshTimer() + 1);
									currentKeys.remove(currentKeys.indexOf(10));
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {}
								}
								if (render.getCurrentMenuPos() == 1) {
									if (physics.getPlayer().getEXP() >= 5 && physics.getPlayer().getManaRefreshTimer() > 5) {
										physics.getPlayer().setEXP(physics.getPlayer().getEXP() - 5);
										physics.getPlayer().setManaRefreshTimer(physics.getPlayer().getManaRefreshTimer()-5);
										currentKeys.remove(currentKeys.indexOf(10));
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {}
									}
								}
								if (render.getCurrentMenuPos() == 2) {
									if (physics.getPlayer().getEXP() >= 5) {
										physics.getPlayer().setEXP(physics.getPlayer().getEXP() - 5);
										physics.getPlayer().setMaxHealth(physics.getPlayer().getMaxHealth() + 1);
										currentKeys.remove(currentKeys.indexOf(10));
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {}
									}
								}
							}
							break;
					}
				}

				// Beautiful ticks are ticking!!!
				tick++;
				tick();
				--unprocessed;
				//draws current frame
				fps++;
				render();
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				StringWriter error = new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				try{
					Log.add(error.toString());
				}catch (Exception e1) {
					
				}
			}
			// Print current fps and ticks
			if (System.currentTimeMillis() - timer > 1000) {
				//System.out.printf("%d fps, %d tick%n", fps,  tick);
				tick = 0;
				fps = 0;
				timer += 1000;
				requestFocusInWindow();
				//System.out.println(currentKeys);
			}
		}
	}

	private void render() {
		/*
		 * The render method tells the render class to draw to the canvas.
		 * 
		 * We use this class to create a buffer strategy and call the render class'
		 * 	methods that draw the background and foreground to the canvas and then 
		 * 	update the canvas that is shown.
		 */

		// BufferStategy is a way of rendering a certain amount of frames ahead
		// 	of the current frame to help with shuttering issues
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			// If no BufferStrategy is found, create another one that buffers 2 frames ahead
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		
		try {
			Graphics g = bs.getDrawGraphics();
			// Calling the RenderState here to render based off of the current state wanting
			// to be displayed
			render.RenderState(g, getWidth(), getHeight(), CurrentState, physics.getPlayer(), NextState);
			if (!render.isLoading()) {
				CurrentState = NextState;
			}
			// Done with rendering, Moving to situating the canvas and displaying it
			g.dispose();
			bs.show();
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {
				
			}
		}
	}

	private void tick() {
		/*
		 * This method Ticks, entirely crucial to the flow of this game.
		 *   Updates the canvas by calling the render method
		 */
		
		// Draws the current frame
	}

	public void start() {
		/*
		 * The start method is called upon launching the app and starts the thread that
		 * 	the game runs on.
		 */
		if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			Dimension dimension = new Dimension(WIDTH, HEIGHT);
			this.setMaximumSize(dimension);
			this.setMinimumSize(dimension);
			this.setPreferredSize(dimension);
			this.setSize(dimension);
		}
		
		// The second chunk adds the canvas to a JFrame in order to display everything onto 
		// 	a frame that is more versatile than a canvas in terms of dimensioning and 
		//  positioning. 
		frame.setFocusable(true);
		frame.setUndecorated(true);	
		frame.setTitle(NAME);
		frame.add(this);
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		if(System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
			device.setFullScreenWindow(frame);
		}
		if (!running) {
			running = true;
			new Thread(this).start();
		} 
	}

	public void stop() {
		/*
		 * The stop method ends the program by causing the run method's while loop
		 *  to finish and finally reach the end of the run method
		 */
		SaveData data = new SaveData();
		data.setPlayerCurrentX(physics.getPlayer().getCurrentX());
		data.setPlayerCurrentY(physics.getPlayer().getCurrentY());
		try {
			ResourceManager.Save(data, "SaveData");
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {
				
			}
		}
		try {
			Log.add("Game End");
			Log.close();
		} catch (Exception e){
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {
				
			}
		}
		
//		new Crash().run();
		frame.setVisible(false);
		frame.dispose();
		physics.stop();
		
		running = false;
	}

	public void keyTyped(KeyEvent e) {
		/*
		 * The method keyTyped is required by the KeyListener class and is an event that
		 * is called every time a Char is pressed.
		 * 
		 * Example of Keys that this event registers: A, S, D, F, etc.
		 * 
		 * Keys NOT registered: F5, F11, Enter, Ctrl
		 */
		
	}
	
	public void keyReleased(KeyEvent e) {
		/*
		 * keyReleased is called when ANY key is released from a "Pressed" state.
		 * 
		 * We use it to remove a key from an ArrayList of all Keys currently "Pressed"
		 */
		
		if(currentKeys.indexOf(e.getKeyCode()) != -1) {
			currentKeys.remove(currentKeys.indexOf(e.getKeyCode()));
		} 
	}
	
	public void keyPressed(KeyEvent e) {
		/*
		 * keyPressed is called every time a Key registers as "Pressed"
		 * 
		 * This means that Every time a key is held down, this event is continuously
		 * called. To avoid the issue of repeat KeyCodes being added to "currentKeys" (a
		 * list of every Key that is currently "Pressed"), we are iterating through
		 * "currentKeys" and making sure that the Key that is being checked is not in
		 * the list.
		 */

		
		if(currentKeys.indexOf(e.getKeyCode()) == -1) {
			currentKeys.add(e.getKeyCode());
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		render.setCurrentMouseX(arg0.getX());
		render.setCurrentMouseY(arg0.getY());
		if (CurrentState == GameState && arg0.getX() > getWidth() / 2) {
			physics.getPlayer().FaceRight();
		} else if (CurrentState == GameState && arg0.getX() <= getWidth() / 2) {
			physics.getPlayer().FaceLeft();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (render.getCurrentMenuPos() == 0 && CurrentState == MenuState  && arg0.getButton() == MouseEvent.BUTTON1) {
			NextState = GameState;
			physics.start();
		} else if (render.getCurrentMenuPos() == 1 && CurrentState == MenuState  && arg0.getButton() == MouseEvent.BUTTON1) {
			NextState = GameState;
			physics.start();
		} else if(render.getCurrentMenuPos() == 3 && CurrentState == MenuState  && arg0.getButton() == MouseEvent.BUTTON1) {
			stop();
		}
		if (CurrentState == GameState && arg0.getButton() == MouseEvent.BUTTON1) {
			physics.getPlayer().Shoot(arg0.getPoint(), new Point(getWidth() / 2, getHeight() / 2));
		} else if (CurrentState == GameState && arg0.getButton() == MouseEvent.BUTTON3) {
			physics.getPlayer().Attack();
		}
		if (CurrentState == UpgradeState && arg0.getButton() == MouseEvent.BUTTON1) {
			if (render.getCurrentMenuPos() == 0) {
				if (physics.getPlayer().getEXP() >= 5) {
					physics.getPlayer().setEXP(physics.getPlayer().getEXP() - 5);
					physics.getPlayer().setJumpAmount(physics.getPlayer().getJumpAmount() + 1);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
				}
			}
			if (render.getCurrentMenuPos() == 1) {
				if (physics.getPlayer().getEXP() >= 5 && physics.getPlayer().getManaRefreshTimer() > 5) {
					physics.getPlayer().setEXP(physics.getPlayer().getEXP() - 5);
					physics.getPlayer().setManaRefreshTimer(physics.getPlayer().getManaRefreshTimer()-5);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
				}
			}
			if (render.getCurrentMenuPos() == 2) {
				if (physics.getPlayer().getEXP() >= 5) {
					physics.getPlayer().setEXP(physics.getPlayer().getEXP() - 5);
					physics.getPlayer().setMaxHealth(physics.getPlayer().getMaxHealth() + 1);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	public static boolean isRunning() {
		return running;
	}
	
	public static int getTileSize() {
		return tileSize;
	}

	public static int getTickpersec() {
		return tickPerSec;
	}

	public static final int getGamestate() {
		return GameState;
	}

	public static final int getMenustate() {
		return MenuState;
	}

	public static final int getPausestate() {
		return PauseState;
	}

	public static final int getUpgradestate() {
		return UpgradeState;
	}

	public final static int getDeadstate() {
		return DeadState;
	}

	public static int getCurrentState() {
		return CurrentState;
	}

	public static ArrayList<Integer> getCurrentKeys() {
		return currentKeys;
	}

}
