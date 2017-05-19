package com.supermap.desktop.dialog.cacheClip.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

class LogWriter {

	private File logFile;
	private OutputStreamWriter writer;
	public static SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	private static LogWriter gInstance;
	private static boolean writeToFile = false;

	public static String getPID() {
		return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
	}

	public static LogWriter getInstance() {
		if (gInstance == null) {
			gInstance = new LogWriter();
		}
		return gInstance;
	}

	private LogWriter() {
		if (isWriteToFile()) {
			if (logFile == null) {

				String logFolder = ".\\temp_log\\";

				File file = new File(logFolder);
				if (!file.exists()) {
					file.mkdir();
				}

				String logName = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date()) + "_" + getPID() + ".log";
				logFile = new File(logFolder + logName);
			}

			try {
				if (!logFile.exists()) {
					logFile.createNewFile();
				}
				writer = new OutputStreamWriter(new FileOutputStream(logFile), "UTF-8");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				} else
					System.out.print(line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void closs() {
		try {
			if (writer != null)
				writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean isWriteToFile() {
		return writeToFile;
	}

	public static void setWriteToFile(boolean writeToFile) {
		LogWriter.writeToFile = writeToFile;
	}
}
