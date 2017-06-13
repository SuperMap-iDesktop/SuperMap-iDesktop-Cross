package com.supermap.desktop.dialog.cacheClip.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

class LogWriter {

	private static File logDirectory;
	private File logFile;
	private OutputStreamWriter writer;
	public static SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm");
	private static LogWriter gInstance;
	public static String BUILD_CACHE = "BuildCache";
	public static String CHECK_CACEH = "CheckCache";

	public static String getPID() {
		return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
	}

	public static LogWriter getInstance(String type) {
		if (gInstance == null) {
			gInstance = new LogWriter(type);
		}
		return gInstance;
	}

	private LogWriter(String type) {
		if (logFile == null) {
			String logFolder = ".\\temp_log\\";
			if (CacheUtilities.isLinux()) {
				logFolder = "./temp_log/";
			}
			logDirectory = new File(logFolder);
			if (!logDirectory.exists()) {
				logDirectory.mkdir();
			}
			String logName = dFormat.format(new Date()) + "_" + type + "_" + getPID() + ".log";
			logFile = new File(logFolder + logName);
		}
		try {
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			writer = new OutputStreamWriter(new FileOutputStream(logFile), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void removeAllLogs() {
		if (null != logDirectory && logDirectory.exists() && logDirectory.isDirectory()) {
			File[] logFiles = logDirectory.listFiles();
			for (int i = 0; i < logFiles.length; i++) {
				logFiles[i].delete();
			}
		}
	}

	public void writelog(String line) {
		synchronized (this) {
			line = dFormat.format(new Date()) + "," + line + "\n";
			//line = line + "\n";	
			try {
				if (writer != null) {
					writer.write(line);
					writer.flush();
				} else
					System.out.print(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void flush() {
		synchronized (this) {
			try {
				if (writer != null)
					writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closs() {
		try {
			if (writer != null)
				writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
