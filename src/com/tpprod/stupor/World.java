package com.tpprod.stupor;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

public class World
{
    public ArrayList<ArrayList<Integer>> worldGrid = new ArrayList<ArrayList<Integer>>();
    
    public void Initialize () throws FileNotFoundException {
        
    	BufferedReader bR = new BufferedReader(new FileReader(Paths.get(".").toAbsolutePath().normalize().toString() + "\\Content\\WorldFile"));
        String lineString;
        String[] line;
        int acc = 0;
        try {
            while ((lineString = bR.readLine()) != null) {
                line = lineString.split(" ");

                worldGrid.add(new ArrayList<Integer>());
                
                for (String item : line) {
                    worldGrid.get(acc).add(Integer.parseInt(item));
                }
                
                acc++;
            }
        } catch (Exception e) {
            System.err.println("Error thrown: " + e);
        }
    }
}
