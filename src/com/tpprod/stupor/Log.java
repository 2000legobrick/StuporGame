package com.tpprod.stupor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {
	private static BufferedWriter bw;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	private static String date = sdf.format(Calendar.getInstance().getTime());
	public static void start() throws IOException{
		File file = new File("./Content/Logs/" + date);
		file.createNewFile();
	}
	
	public static void add(String s) throws IOException {	
		File file = new File("./Content/Logs/" + date);
		bw = new BufferedWriter(new FileWriter(file,true));
		bw.write(sdf.format(Calendar.getInstance().getTime()) + "\n " + s + "\n");
		bw.close();
	}
	public static void close() throws IOException {
		String[] logs = new File("./Content/Logs").list();
		while(logs.length > 5) {
			new File("./Content/Logs/" + logs[0]).delete();
			logs = new File("./Content/Logs").list();
		}
	}

}

