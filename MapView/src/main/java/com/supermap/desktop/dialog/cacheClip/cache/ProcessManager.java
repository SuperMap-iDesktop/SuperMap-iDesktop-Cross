package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.desktop.utilities.FileLocker;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by xie on 2017/5/17.
 * ProcessManager class build for store SubprocessThread(Thread for create a process)
 */
public class ProcessManager {
	private static CopyOnWriteArrayList<SubprocessThread> threadList;
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

	public void addProcess(SubprocessThread thread) {
		synchronized (new Object()) {
			threadList.add(thread);
		}
	}

	public void removeAllProcess(String taskPath, String path) {
		try {
			removeProcesses();
			String doingPath = null;
			File taskFiles = new File(taskPath);
			if (taskFiles.exists()) {
				doingPath = CacheUtilities.replacePath(taskFiles.getParent(), path);
			}
			File doingDirectory = new File(doingPath);
			if (doingDirectory.exists()) {
				CacheUtilities.renameFiles(taskPath,doingDirectory);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param params
	 * @param newProcessCount
	 * @param taskPath
	 */
	public void removeProcess(String[] params, int newProcessCount, String taskPath) {
		try {
			removeProcesses();
			File taskFiles = new File(taskPath);
			String doingPath = null;
			if (taskFiles.exists()) {
				doingPath = CacheUtilities.replacePath(taskFiles.getParent(), "doing");
			} else {
				return;
			}

			LogWriter.removeAllLogs();
			File doingDirectory = new File(doingPath);
			CacheUtilities.renameFiles(taskPath, doingDirectory);
			BuildCache buildCache = new BuildCache();
			buildCache.startProcess(newProcessCount, params);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}


	public void dispose() {
		try {
			removeProcesses();
			processManager.finalize();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	private void removeProcesses() throws InterruptedException {
		if (threadList.size() == 1) {
			threadList.get(0).process.destroy();
		} else {
			for (int i = threadList.size() - 1; i >= 0; i--) {
				threadList.get(i).process.destroy();
			}
		}
		TimeUnit.SECONDS.sleep(2);
		threadList.clear();
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
