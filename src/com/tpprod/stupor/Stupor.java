package com.tpprod.stupor;

import java.io.IOException;

public class Stupor {
	public static void main(String[] args) throws IOException {
		/*
		 * The main method is used to create the frames and canvases that are displayed
		 * and printed to respectively.
		 */

		/*
		 * Starts a new State Machine to start the game
		 */
		StateMachine game = new StateMachine();
		game.start();
		
		/*
		 * Starts the log
		 */
		Log.start();
		Log.add("Game Start");
		
		/*
		 * Creates a shutDownHook for error reporting
		 */
		Runtime.getRuntime().addShutdownHook(new Crash());
	}
}
