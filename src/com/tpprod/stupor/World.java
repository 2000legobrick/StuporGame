package com.tpprod.stupor;

import java.awt.Rectangle;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * The World Class is used to read the World files and store the current worldGrid
 * 	to be referenced in other portions of the game.
 */

public class World
{
	
    public ArrayList<ArrayList<NewRectangle>> worldGrid = new ArrayList<ArrayList<NewRectangle>>();
    
    public World() {
    }
    
    public void Initialize () throws FileNotFoundException, IOException {
        /*
         * The Initialize method reads a file and separates the numbers within the file into an
         * 	ArrayList of NewRectangles each with a type of that number in the file.
         */
    	
    	BufferedReader bR = new BufferedReader(new FileReader(Paths.get(".").toAbsolutePath().normalize().toString() + "\\Content\\WorldFile"));
        String lineString;
        ArrayList<String> line;
        int accY = 0;
        try {
        	// While the file being read still has new lines to read iterate through the
        	//  split lines and add a NewRectangle to the 2d ArrayList worldGrid 
            while ((lineString = bR.readLine()) != null) {
                line =  new ArrayList<String>(Arrays.asList(lineString.split(" ")));

                worldGrid.add(new ArrayList<NewRectangle>());

                int accX = 0;
                for (String item : line) {
                    worldGrid.get(accY).add(new NewRectangle(Integer.parseInt(item),  new Rectangle(accX * StateMachine.tileSize, accY * StateMachine.tileSize, StateMachine.tileSize, StateMachine.tileSize)));
                    accX++;
                }
                accY++;
            }
        } catch (Exception e) {
            System.err.println("Error thrown: " + e);
        }
        bR.close();
    }
}
