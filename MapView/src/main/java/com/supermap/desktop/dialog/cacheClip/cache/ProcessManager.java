package com.supermap.desktop.dialog.cacheClip.cache;

import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/5/17.
 * ProcessManager class build for store SubprocessThread(Thread for create a process)
 */
public class ProcessManager {
	private CopyOnWriteArrayList<SubprocessThread> threadList;
	private static volatile ProcessManager processManager;
//	private volatile ProtectThread protectThread;

	public static ProcessManager getInstance() {
		if (null == processManager) {
			synchronized (new Object()) {
				if (null == processManager) {
					processManager = new ProcessManager();
				}
			}
		}
		return processManager;
	}

	private ProcessManager() {
		if (null == this.threadList) {
			this.threadList = new CopyOnWriteArrayList<>();
		}
		// Create a protect thread while some process destroyed unexpected(Create one/more process)
//		protectThread = new ProtectThread();
//		protectThread.setExit(true);
//		protectThread.start();
	}

	public void addProcess(SubprocessThread thread, String cacheType) {
		synchronized (new Object()) {
			threadList.add(thread);
		}
	}

	public void removeAllProcess(String sciPath) {
		try {
			dispose();
			Thread.sleep(2000);
			LogWriter.removeAllLogs();
			String doingPath = null;
			File taskFiles = new File(sciPath);
			if (taskFiles.exists()) {
				doingPath = CacheUtilities.replacePath(taskFiles.getParentFile().getAbsolutePath(), "doing");
			}
			File doingDirectory = new File(doingPath);
			if (doingDirectory.exists()) {
				File[] doingScis = doingDirectory.listFiles();
				for (int i = 0; i < doingScis.length; i++) {
					doingScis[i].renameTo(new File(taskFiles, doingScis[i].getName()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param params
	 * @param newProcessCount
	 * @param sciPath
	 */
	public void removeProcess(String[] params, int newProcessCount, String sciPath) {
		try {
			for (int i = threadList.size() - 1; i >= 0; i--) {
				threadList.get(i).process.destroy();
			}
			Thread.sleep(2 * 1000);
			threadList.clear();
			File taskFiles = new File(sciPath);
			String doingPath = null;
			if (taskFiles.exists()) {
				doingPath = CacheUtilities.replacePath(taskFiles.getParentFile().getAbsolutePath(), "doing");
			}

			String logFolder = ".\\temp_log\\";
			if (CacheUtilities.isLinux()) {
				logFolder = "./temp_log/";
			}
			File logDirectory = new File(logFolder);
			CopyOnWriteArrayList<String> undoScis = new CopyOnWriteArrayList<>();
			if (logDirectory.exists() && logDirectory.isDirectory()) {
//				int mergeSciCount = Integer.valueOf(params[BuildCache.MERGESCICOUNT_INDEX]);
				undoScis = getUndoScis(logDirectory, 3);
			}
			LogWriter.removeAllLogs();
			File doingDirectory = new File(doingPath);
			if (doingDirectory.exists()) {
				File[] doingScis = doingDirectory.listFiles();
				for (int i = 0; i < doingScis.length; i++) {
					for (int j = 0; j < undoScis.size(); j++) {
						if (undoScis.get(j).contains(doingScis[i].getName()))
							doingScis[i].renameTo(new File(taskFiles, doingScis[i].getName()));
					}
				}
			}
			BuildCache buildCache = new BuildCache();
			buildCache.startProcess(newProcessCount, params);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	private CopyOnWriteArrayList<String> getUndoScis(File logDirectory, int mergeCount) throws IOException {
		CopyOnWriteArrayList<String> result = new CopyOnWriteArrayList<>();
		try {
			File[] logfiles = logDirectory.listFiles();
			for (int i = 0; i < logfiles.length; i++) {
				CopyOnWriteArrayList<String> doingSci = new CopyOnWriteArrayList<>();
				if (logfiles[i].getName().contains(LogWriter.BUILD_CACHE)) {
					InputStream stream = new FileInputStream(logfiles[i]);
					String osName = System.getProperty("os.name").toLowerCase();
					BufferedReader bufferedReader;
					if (osName.startsWith("linux")) {
						bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
					} else {
						bufferedReader = new BufferedReader(new InputStreamReader(stream, "GBK"));
					}
					String line = null;
					while ((line = bufferedReader.readLine()) != null) {
						if (line.contains("doing:")) {
							String sciName = line.substring(line.indexOf("doing:"), line.length());
							doingSci.add(sciName);
						}
					}
					stream.close();
					bufferedReader.close();
				}
				if (doingSci.size() > 0) {
					for (int j = doingSci.size() - 1; j > doingSci.size() - 1 - mergeCount; j--) {
						result.add(doingSci.get(j));
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	public CopyOnWriteArrayList<SubprocessThread> getThreadList() {
		return threadList;
	}

	public void dispose() {
		try {
			for (int i = threadList.size() - 1; i >= 0; i--) {
				threadList.get(i).process.destroy();
			}
			threadList.clear();
//			protectThread.exit = false;
			ProcessManager.this.finalize();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

//	class ProtectThread extends Thread {
//		public volatile boolean exit = true;
//
//		public boolean isExit() {
//			return exit;
//		}
//
//		public void setExit(boolean exit) {
//			this.exit = exit;
//		}
//
//		@Override
//		public void run() {
//			while (isExit()) {
//				if (null == threadList) {
//					break;
//				}
//				//If process count <threadList.size() add one/more process
//				for (int i = 0; i < threadList.size(); i++) {
//					try {
//						SubprocessThread tempThread = threadList.get(i);
//						ArrayList arguments = tempThread.getArguments();
//						String type = tempThread.getType();
//						if (1 == tempThread.process.exitValue()) {
//							SubprocessThread newProcess = new SubprocessThread(arguments, type);
//							newProcess.start();
//							threadList.remove(i);
//							threadList.add(i, newProcess);
//						}
//					} catch (IllegalThreadStateException e) {
////							e.printStackTrace();
////							exist++;
//					}
//				}
//				try {
//					TimeUnit.SECONDS.sleep(30);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}

}
