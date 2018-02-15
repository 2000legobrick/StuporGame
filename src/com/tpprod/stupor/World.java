package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * The World Class is used to read the World files and store the current worldGrid
 * 	to be referenced in other portions of the game.
 */

public class World {

	private ArrayList<ArrayList<NewRectangle>> worldGrid = new ArrayList<ArrayList<NewRectangle>>();

	private Inventory inventory = new Inventory();

	public World() {

	}

	public void Initialize() throws FileNotFoundException, IOException {
		/*
		 * The Initialize method reads a file and separates the numbers within the file
		 * into an ArrayList of NewRectangles each with a type of that number in the
		 * file.
		 */

		BufferedReader bR = new BufferedReader(new FileReader(worldFilePath));
		String lineString;
		ArrayList<String> line;
		int accY = 0;
		try {
			// While the file being read still has new lines to read iterate through the
			// split lines and add a NewRectangle to the 2d ArrayList worldGrid
			while ((lineString = bR.readLine()) != null) {
				line = new ArrayList<String>(Arrays.asList(lineString.split(" ")));

				worldGrid.add(new ArrayList<NewRectangle>());

				int accX = 0;
				for (String item : line) {
					if (Integer.parseInt(item) == 4) {
						inventory.addInventoryItem(
								new Item(accX * StateMachine.getTileSize() + StateMachine.getTileSize() / 4,
										accY * StateMachine.getTileSize() + StateMachine.getTileSize() / 4,
										Color.MAGENTA, StateMachine.getTileSize() / 2, "healthRegen"));
					} else if (Integer.parseInt(item) == 5) {
						inventory.addInventoryItem(
								new Item(accX * StateMachine.getTileSize() + StateMachine.getTileSize() / 4,
										accY * StateMachine.getTileSize() + StateMachine.getTileSize() / 4,
										Color.ORANGE, StateMachine.getTileSize() / 2, "health"));
					}
					worldGrid.get(accY)
							.add(new NewRectangle(Integer.parseInt(item),
									new Rectangle(accX * StateMachine.getTileSize(), accY * StateMachine.getTileSize(),
											StateMachine.getTileSize(), StateMachine.getTileSize())));
					accX++;
				}
				accY++;
			}
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try {
				Log.add(error.toString());
			} catch (Exception e1) {

			}
		}
		bR.close();
	}

	/*
	 * Getters and setters for world
	 */
	public Inventory getWorldInventory() {
		return inventory;
	}

	public ArrayList<ArrayList<NewRectangle>> getWorldGrid() {
		return worldGrid;
	}
}
