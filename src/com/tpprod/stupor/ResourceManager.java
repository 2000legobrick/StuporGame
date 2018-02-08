package com.tpprod.stupor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceManager {

	public static void Save(Serializable data, String fileName) throws Exception {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				Files.newOutputStream(Paths.get("./Content/" + fileName)))) {
			oos.writeObject(data);
		} catch (Exception e) {
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			try{
				Log.add(error.toString());
			}catch (Exception e1) {
				
			}
		}
		//System.out.println(Paths.get(fileName));
	}

	public static Object Load(String fileName) throws Exception {
		try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("./Content/" + fileName)))) {
			return ois.readObject();
		}
	}

}