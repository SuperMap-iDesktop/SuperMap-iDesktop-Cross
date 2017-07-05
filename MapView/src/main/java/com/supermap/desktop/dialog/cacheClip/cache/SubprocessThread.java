package com.supermap.desktop.dialog.cacheClip.cache;

import java.io.InputStream;
import java.util.ArrayList;


class SubprocessThread extends Thread {
	private ArrayList<String> arguments;
	//	private long start;
	public Process process;
	private InputStream stream;
	private String type = null;

	public SubprocessThread(ArrayList<String> arguments, String type) {
		this.arguments = arguments;
		this.type = type;
//		start = 0;
	}

	public ArrayList<String> getArguments() {
		return arguments;
	}

	public String getType() {
		return type;
	}

	public void setArguments(ArrayList<String> arguments) {
		this.arguments = arguments;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * If thread is died, destroy this process
	 */
	public void timeout() {
		synchronized (this) {
			if (!SubprocessThread.this.isAlive() && null != this.process) {
				int psHash = process.hashCode();
				LogWriter.getInstance(this.type).writelog("time out and kill it, PIDHASH:" + psHash);
			}
		}
	}


	@Override
	public void run() {
		try {
			ProcessBuilder builder = new ProcessBuilder(arguments);
			builder.redirectErrorStream(true);
			process = builder.start();
//			int psHash = process.hashCode();
//			stream = process.getInputStream();
//			String osName = System.getProperty("os.name").toLowerCase();
//			BufferedReader br1;
//			if (osName.startsWith("linux")) {
//				br1 = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
//			} else {
//				br1 = new BufferedReader(new InputStreamReader(stream, "GBK"));
//			}
//			String line1 = null;
//			while ((line1 = br1.readLine()) != null) {
////				start = System.currentTimeMillis();
//				//log.writelog("PIDHASH:"+psHash +"," + line1);
//				LogWriter.getInstance(this.type).writelog(line1);
//			}
//			stream.close();
//			isExit = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
