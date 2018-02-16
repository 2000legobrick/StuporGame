package com.tpprod.stupor;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceManager {

	/*
	 * Saves a serialized file to a given location
	 */
	public static void Save(Serializable data, String fileName) throws Exception {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				Files.newOutputStream(Paths.get("./Content/" + fileName)))) {
			oos.writeObject(data);
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try {
				Log.add(error.toString());
			} catch (Exception e1) {

			}
		}
	}

	/*
	 * Opens a serialized file
	 */
	public static Object Load(String fileName) throws Exception {
		try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("./Content/" + fileName)))) {
			return ois.readObject();
		}
	}

	public static void deleteSave(String fileName) throws Exception {
		try {
			File file = new File("./Content/" + fileName);
			file.delete();
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {}
	}
	}

	public static boolean hasData(String fileName) {
		File file = new File("./Content/" + fileName);
		boolean hasData;
		if (file.exists())
			hasData = true;
		else
			hasData = false;

		return hasData;
	}
}