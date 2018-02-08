package com.tpprod.stupor;

import java.io.IOException;

public class Stupor {
	public static void main(String[] args) throws IOException {
		/*
		 * The main method is used to create the frames and canvases that are displayed
		 * and printed to respectively.
		 */

		// This first chunk of code creates the canvas that the object "render" will
		// print to later.

		StateMachine game = new StateMachine();
		game.start();
		Log.start();
		Log.add("Game Start");
	}
}
