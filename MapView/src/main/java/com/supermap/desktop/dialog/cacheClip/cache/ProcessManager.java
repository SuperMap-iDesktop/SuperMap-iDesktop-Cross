package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.mapview.MapViewProperties;

import java.io.*;
import java.text.MessageFormat;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/5/17.
 * ProcessManager class build for store SubprocessThread(Thread for create a process)
 */
public class ProcessManager {
	private CopyOnWriteArrayList<SubprocessThread> threadList;
	private static volatile ProcessManager processManager;
	private volatile ProtectThread protectThread;

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
		protectThread = new ProtectThread();
		protectThread.start();
	}

	public void addProcess(SubprocessThread thread, String cacheType) {
		synchronized (new Object()) {
			threadList.add(thread);
		}
	}

	/**
	 * @param params
	 * @param newProcessCount
	 * @param sciPath
	 */
	public void removeProcess(String[] params, int newProcessCount, String sciPath) {
		try {
			LogWriter.removeAllLogs();
			for (int i = threadList.size() - 1; i >= 0; i--) {
				threadList.get(i).process.destroy();
			}
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
				int mergeSciCount = Integer.valueOf(params[BuildCache.MERGESCICOUNT_INDEX]);
				undoScis = getUndoScis(logDirectory, mergeSciCount);
			}

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
			SmOptionPane optionPane = new SmOptionPane();
			optionPane.showConfirmDialog(MessageFormat.format(MapViewProperties.getString("String_ProcessStoped"), String.valueOf(newProcessCount)));
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
			protectThread.exit = false;
			ProcessManager.this.finalize();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	class ProtectThread extends Thread {
		public volatile boolean exit = true;

		@Override
		public void run() {
			try {
				while (exit) {
					if (null == threadList) {
						break;
					}

					//If process count <threadList.size() add one/more process

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
