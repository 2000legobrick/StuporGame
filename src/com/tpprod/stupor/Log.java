package com.tpprod.stupor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log {
	private static BufferedWriter bw;
	public static void start() throws IOException{
		File file = new File("./Content/Logs/" +new java.util.Date());
		file.createNewFile();
		bw = new BufferedWriter(new FileWriter(file,true));
	}
	
	public static void add(String s) throws IOException {		
		bw.write(new java.util.Date() + "\n " + s + "\n");

	}
	public static void close() throws IOException {
		bw.close();
		String[] logs = new File("./Content/Logs").list();
		if(logs.length > 5) {
			new File("./Content/Logs/" + logs[0]).delete();
		}
	}

}

