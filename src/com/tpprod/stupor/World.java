package com.tpprod.stupor;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class World
{
	
    public ArrayList<ArrayList<NewRectangle>> worldGrid = new ArrayList<ArrayList<NewRectangle>>();
    
    public World() {
    }
    
    public void Initialize () throws FileNotFoundException {
        
    	BufferedReader bR = new BufferedReader(new FileReader(Paths.get(".").toAbsolutePath().normalize().toString() + "\\Content\\WorldFile"));
        String lineString;
        ArrayList<String> line;
        int accY = 0;
        try {
            while ((lineString = bR.readLine()) != null) {
                line =  new ArrayList<String>(Arrays.asList(lineString.split(" ")));

                worldGrid.add(new ArrayList<NewRectangle>());

                int accX = 0;
                for (String item : line) {
                    worldGrid.get(accY).add(new NewRectangle(Integer.parseInt(item),  new Rectangle(accX * StateMachine.tileSize, accY * StateMachine.tileSize, StateMachine.tileSize, StateMachine.tileSize)));
                    // add(new NewRectangle(Integer.parseInt(item),  new Rectangle(line.indexOf(item) * tileSize, (worldGrid.size - 1) * tileSize, tileSize, tileSize)))
                    accX++;
                }
                accY++;
            }
        } catch (Exception e) {
            System.err.println("Error thrown: " + e);
        }
    }
}
