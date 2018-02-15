package com.tpprod.stupor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceManager {

	private static boolean hasData;

	public static void Save(Serializable data, String fileName) throws Exception {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				Files.newOutputStream(Paths.get("./Content/" + fileName)))) {
			oos.writeObject(data);
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {}
		}
	}

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
		if (file.exists())
			hasData = true;
		else
			hasData = false;

		return hasData;
	}
}