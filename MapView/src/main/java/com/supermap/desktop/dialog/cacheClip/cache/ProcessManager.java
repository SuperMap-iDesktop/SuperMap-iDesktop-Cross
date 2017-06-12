package com.supermap.desktop.dialog.cacheClip.cache;

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

	public void addProcess(SubprocessThread thread) {
		synchronized (new Object()) {
			threadList.add(thread);
		}
	}

	public void removeProcess(int newProcessCount){
		try {
			for (int i = threadList.size() - 1; i >= newProcessCount; i--) {
				threadList.get(i).process.destroy();
				threadList.remove(threadList.get(i));
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
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
//					String[] pids = ManagementFactory.getRuntimeMXBean().getName().split("@");

//					if (pids.length + 1 < threadList.size()) {
//						int newProcessLength = threadList.size() - pids.length;
//						for (int i = 0; i < newProcessLength; i++) {
//							if (null != threadList.get(0)) {
//								SubprocessThread thread = threadList.get(0).clone();
//								thread.start();
//								threadList.add(thread);
//							}
//						}
//					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
