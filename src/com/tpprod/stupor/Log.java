package com.tpprod.stupor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {
	/*
	 * Sets up some variables as well as the format for the time
	 */
	private static BufferedWriter bw;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
	private static String date = sdf.format(Calendar.getInstance().getTime());

	/*
	 * When starting we create a new file based on the current time that is the log
	 * for this game session
	 */
	public static void start() throws IOException {
		File file = new File("./Content/Logs/" + date);
		file.createNewFile();
	}

	/*
	 * Here we add lines to the log, wither it be an error message or an general
	 * update on the coarse of the game
	 */
	public static void add(String s) throws IOException {
		File file = new File("./Content/Logs/" + date);
		bw = new BufferedWriter(new FileWriter(file, true));
		bw.write(sdf.format(Calendar.getInstance().getTime()) + "\n " + s + "\n");
		bw.close();
	}

	/*
	 * Here we delete all old and useless log files
	 */
	public static void close() throws IOException {
		String[] logs = new File("./Content/Logs").list();
		while(logs.length > 5) {
			new File("./Content/Logs/" + logs[0]).delete();
			logs = new File("./Content/Logs").list();
		}
	}

}
