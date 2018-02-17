package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

	private String currentWorldFilePath = Paths.get(".").toAbsolutePath().normalize().toString() + "/Content/CurrentWorldFile";
	private String defaultWorldFilePath = Paths.get(".").toAbsolutePath().normalize().toString() + "/Content/DefaultWorldFile";
	private BufferedWriter bW = null;
	private BufferedReader bR;
	private ArrayList<String> line;
	private String lineString;
	 

	public World() {
	}

	public void Initialize() throws IOException {
		/*
		 * The Initialize method reads a file and separates the numbers within the file
		 * into an ArrayList of NewRectangles each with a type of that number in the
		 * file.
		 */
		
		if (!ResourceManager.hasData("CurrentWorldFile")) {
			try {
				createCurrentWorldFile();
			} catch (Exception e) {
			}
		} else {
			bR = new BufferedReader(new FileReader(currentWorldFilePath));
		}
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
						inventory.addInventoryItem(new Item(accX * StateMachine.getTileSize() + StateMachine.getTileSize()/4, accY * StateMachine.getTileSize() + StateMachine.getTileSize()/4, Color.MAGENTA, StateMachine.getTileSize()/2, "healthRegen"));
					} else if (Integer.parseInt(item) == 5) {
						inventory.addInventoryItem(new Item(accX * StateMachine.getTileSize() + StateMachine.getTileSize()/4, accY * StateMachine.getTileSize() + StateMachine.getTileSize()/4, Color.ORANGE, StateMachine.getTileSize()/2, "health"));
					}
					worldGrid.get(accY)
							.add(new NewRectangle(Integer.parseInt(item), new Rectangle(accX * StateMachine.getTileSize(),
									accY * StateMachine.getTileSize(), StateMachine.getTileSize(), StateMachine.getTileSize())));
					accX++;
				}
				if (bW != null) {
					bW.write(lineString);
					bW.write("\n");
				}
				accY++;

			}
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {
				
			}

		}
		if (bW != null)
			bW.close();
		bR.close();
	}

	/*
	 * Getters and setters for world
	 */
	
	public Inventory getWorldInventory() {
		return inventory;
	}

	public void saveWorldData(SaveWorldData worldData){
		ArrayList<ArrayList<String>> tempWorldGrid = new ArrayList<ArrayList<String>>();
		worldData.setWorldSavedGrid(new ArrayList<ArrayList<String>>());
		try {
			bR = new BufferedReader(new FileReader(currentWorldFilePath));

			while ((lineString = bR.readLine()) != null) {
				line = new ArrayList<String>(Arrays.asList(lineString.split(" ")));
				tempWorldGrid.add(line);
			}
			bR.close();
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {

			}
		}
		for (ArrayList<String> finalLine : tempWorldGrid) {
			worldData.getWorldSavedGrid().add(finalLine);
		}




	}
	
	public void loadWorldData(SaveWorldData worldData) throws Exception {
		if (ResourceManager.hasData("CurrentWorldFile")) {
			bW = new BufferedWriter(new FileWriter(new File(currentWorldFilePath)));
			ArrayList<ArrayList<String>> savedWorldGrid = worldData.getWorldSavedGrid();
			for (ArrayList<String> row : savedWorldGrid) {
				for (String col : row) {
					bW.write(col);
				}
				bW.write("\n");
			}
		}
	}

	public void createCurrentWorldFile() throws Exception{
		bR = new BufferedReader(new FileReader(defaultWorldFilePath));
		bW = new BufferedWriter(new FileWriter(new File(currentWorldFilePath)));
		while((lineString = bR.readLine()) != null) {
			bW.write(lineString);
			bW.write("\n");
		}
		bW.close();
		bR.close();
	}

	public void setWorldData(int row, int col, int itemType) throws Exception {
		ArrayList<ArrayList<String>> tempWorldGrid = new ArrayList<ArrayList<String>>();
		if (ResourceManager.hasData("CurrentWorldFile")) {
			bR = new BufferedReader(new FileReader(currentWorldFilePath));
			int acc = 0;
			while ((lineString = bR.readLine()) != null) {
				line = new ArrayList<String>(Arrays.asList(lineString.split(" ")));
				for (String item : line)
					tempWorldGrid.get(acc).add(item);
				acc++;
			}
			tempWorldGrid.get(row).set(col, Integer.toString(itemType));
			bW = new BufferedWriter(new FileWriter(new File(currentWorldFilePath)));

			for (ArrayList<String> tempRow : tempWorldGrid) {
				for (String tempCol : tempRow)
					bW.write(tempCol);
				bW.write('\n');
			}
			//bW.write("0", col * 960 + row, col * 960 + row + 1);
			bW.close();
			bR.close();
		}
		
	}
	
	public ArrayList<ArrayList<NewRectangle>> getWorldGrid() {
		return worldGrid;
	}

	public void setWorldGrid(ArrayList<ArrayList<NewRectangle>> worldGrid) {
		this.worldGrid = worldGrid;
	}
}
