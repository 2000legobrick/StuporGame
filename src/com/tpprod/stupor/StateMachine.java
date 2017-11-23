package com.tpprod.stupor;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.swing.JFrame;

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

public class StateMachine extends Canvas implements Runnable, KeyListener{

    
	// Static variables
	public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width, HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	public static final String NAME = "Stupor";
	private static final long serialVersionUID = 1L;

    public static int tileSize = 100;
    
    public ArrayList<Integer> currentKeys = new ArrayList<Integer>(); 
    
	private boolean running = false;
    private boolean keyPressed;
    private int  tickPerSec = 60; // Limits the amount of ticks per second, serves to limit the all powerful ticks
	private Render render = new Render();
	private Physics physics = new Physics();

	
	public StateMachine () {
		/*
		 * This is the StateMachine constructor, intentionally left empty.
		 */
	}
	
	public void start() {
		/*
		 * The start method is called upon launching the app and starts the thread that
		 * 	the game runs on.
		 */
		if (!running) {
			running = true;
			new Thread(this).start();
		} 
	}
	
	public void stop () {
		/*
		 * The stop method ends the program by causing the run method's while loop
		 *  to finish and finally reach the end of the run method
		 */
		running = false;
	}
	
	@Override
	public void run() {
		/*
		 * The run method is called at the launch of the program, allowing us to effectively
		 * 	package our game into a .jar file type.
		 * 
		 * We use this method to show and run the current state that the game is in.
		 * 
		 * 	Possible States:
		 * 		GameState      - Actual game is ran in this state
		 * 		MenuState      - The main menu is displayed (typically at start of game)
		 * 		PauseState     - The game does not update but instead stops updating until state is changed
		 * 		InventoryState - Displays the current inventory, game functions are suspended here like PuaseState 
		 * 		DeadState      - Informs the player on their death and resets state to the MenuState 
		 */
		
		// Since the World file might be lost we surrounded it with a try-catch
		try {
			//tries to initialize the world in render, and updates physics with the same world
			render.InitializeWorld();
			physics.world = render.world;
		} catch (FileNotFoundException e1) {
			// If the file isn't found an error is printed and the program stops
			e1.printStackTrace();
			stop();
		}
		
		// Required so that the programs keeps tracks KeyEvents
		addKeyListener(this);
		
		// These variables are specific only to the run method, and keep track of
		//  the FramesPerSecond and each tick as well as if the program is allowed 
		//  to render objects
		int fps = 0, tick = 0;
		double timer = System.currentTimeMillis();
		
		double nsPerTick = 1000000000.0d / tickPerSec;
		double previous = System.nanoTime();
		double unprocessed = 0;
		
		boolean canRender = false;
		
		// If the program is running, we run this chunk of code
		while(running) {
			double current = System.nanoTime();
			unprocessed += (current - previous) / nsPerTick;
			previous = current;
			while (unprocessed >= 1) {
				//Updates game objects
				physics.Gravity();
				
				// These are the actions that each key is tied to, 
				//  the key that is referenced is found in-line with 
				//  the if statement.
				
				
				if (currentKeys.indexOf(17) != -1) { // Ctrl key
					// player does wallSlide
				} else {
					// player stops wallSlide
				} 
				
				if (currentKeys.indexOf(27) != -1) { // Escape Key
					stop();
				}
				
				if (currentKeys.indexOf(87) != -1) { // W Key
					physics.player.Jump();
		        } 
				if (currentKeys.indexOf(65) != -1) { // A Key
		        	physics.mobMove(physics.player, 3, physics.player.speed);
		        } 
				if (currentKeys.indexOf(83) != -1) { // S Key
					// Add ground pound function
		        } 
				if (currentKeys.indexOf(68) != -1) { // D Key
		        	physics.mobMove(physics.player, 4, physics.player.speed);
		        }

				if (currentKeys.indexOf(87) == -1 && currentKeys.indexOf(87) == -1) {
					for (Mob entity : physics.mobs) {
						physics.Dampening(entity);
					}
				}
				physics.Movement();
				
				// Beautiful ticks are ticking!!!
				tick ++;
				tick();
				canRender = true;
				--unprocessed;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//Draws the current frame
			if (canRender) {
				fps++;
				render();
			}
			//Print current fps and ticks
			if (System.currentTimeMillis() - timer > 1000) {
				System.out.printf("%d fps, %d tick%n", fps, tick);
				fps = 0;
				tick = 0;
				timer += 1000;
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
		// 	of the current frame to help with stuttering issues
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			// If no BufferStrategy is found, create another one that buffers 3 frames ahead
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		Graphics g = bs.getDrawGraphics();
		//Calling the Rendeer class here
		render.RenderBackground(g, getWidth(), getHeight());
		render.RenderForeground(g, getWidth(), getHeight(), tileSize, physics.mobs, physics.player);
		//Done with rendering, Moving to situating the canvas and displaying it
		g.dispose();
		bs.show();
	}
	
	private void tick() {
		/*
		 * This method Ticks, entirely crucial to the flow of this game.
		 */
	}
	
	public static void main(String[] args) {
		/*
		 * The main method is used to create the frames and canvases that are displayed
		 * 		and printed to respectively.
		 */
		
		// This first chunk of code creates the canvas that the object "render" will print to later.
		
		StateMachine game = new StateMachine();
		Dimension dimension = new Dimension(WIDTH, HEIGHT);
		game.setMaximumSize(dimension);
		game.setMinimumSize(dimension);
		game.setPreferredSize(dimension);
		game.setSize(dimension);
		
		// The second chunk adds the canvas to a JFrame in order to display everything onto 
		// 	a frame that is more versatile than a canvas in terms of dimensioning and 
		//  positioning. 
		
		JFrame frame = new JFrame();
		frame.setUndecorated(true);
		frame.setTitle(NAME);
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	}
	
	public void keyTyped(KeyEvent e) {
		/*
		 * The method keyTyped is required by the KeyListener class and is
		 * 	an event that is called every time a Char is pressed.
		 * 	
		 * 	Example of Keys that this event registers:
		 * 		A, S, D, F, etc.
		 * 
		 * 	Keys NOT registered: 
		 * 		F5, F11, Enter, Ctrl
		 */
    }
    
    public void keyReleased(KeyEvent e) {
    	/*
    	 * keyReleased is called when ANY key is released from a "Pressed" state.
    	 * 
    	 * We use it to remove a key from an ArrayList of all Keys currently "Pressed"
    	 */
    	
        currentKeys.remove(currentKeys.indexOf(e.getKeyCode()));
    }
    
    public void keyPressed(KeyEvent e) {
    	/*
    	 * keyPressed is called every time a Key registers as "Pressed"
    	 * 
    	 * This means that Every time a key is held down, this event is continuously called.
    	 * 	To avoid the issue of repeat KeyCodes being added to "currentKeys" (a list of every 
    	 * 	Key that is currently "Pressed"), we are iterating through "currentKeys" and making
    	 * 	sure that the Key that is being checked is not in the list.
    	 */

    	keyPressed = false;
        for (int item : currentKeys) {
            if (e.getKeyCode() == item) {
                keyPressed = true;
            }
        }
        if (!keyPressed) {
            currentKeys.add(e.getKeyCode());
        }
    }
}
