package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.mapview.MapViewProperties;

import java.io.File;
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
			File doingDirectory = new File(doingPath);
			if (doingDirectory.exists()) {
				File[] doingScis = doingDirectory.listFiles();
				for (int i = 0; i < doingScis.length; i++) {
					doingScis[i].renameTo(new File(taskFiles, doingScis[i].getName()));
				}
			}
			BuildCache buildCache = new BuildCache();
			buildCache.startProcess(newProcessCount, params);
			SmOptionPane optionPane = new SmOptionPane();
			optionPane.showConfirmDialog(MapViewProperties.getString("String_ProcessStoped"));
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
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
