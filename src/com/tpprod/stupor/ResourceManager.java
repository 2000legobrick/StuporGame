package com.tpprod.stupor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceManager {
	
	public static void Save(Serializable data, String fileName) throws Exception {
		try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("./Content/" +fileName)))) {
			oos.writeObject(data);
		} catch (Exception e) {
		}
		System.out.println(Paths.get(fileName));
	}
	
	public static Object Load(String fileName) throws Exception {
		try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("./Content/" +fileName)))) {
			return ois.readObject();
		}
	}
	


}




//Main Class Commands

/*

Button btnSave = new Button("SAVE");
btnSave.setOnAction(event -> {
	SaveData data = new SaveData();
	data.playerCurrentX = currentX;
	data.playerCurrentY = currentY;
	try {
		ResourceManager.save(data, "1.save");
	}
	catch (Exception e) {
		System.out.println("Couldn't save: " + e.getMessage());
	}
});

Button btnLoad = new Button("LOAD");
btnLoad.setOnAction(event -> {
	try {
		SaveData data = (SaveData) ResourceManager.load("1.save");
		currentX = data.playerCurrentX;
		currentY = data.playerCurrentY;
	}
	catch (Exception e) {
		System.out.println("Couldn't load save data: " + e.getMessage());
	}

*/