package com.supermap.desktop.dialog.cacheClip.cache;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/5/17.
 * ProcessManager class build for store SubprocessThread(Thread for create a process)
 */
public class ProcessManager {
	private CopyOnWriteArrayList<SubprocessThread> threadList;
	private static volatile ProcessManager processManager;

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
		new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						if (null == threadList) {
							break;
						}
						for (SubprocessThread thread : threadList) {
							thread.timeout();
						}
						for (SubprocessThread thread : threadList) {
							if (!thread.isAlive() && null != thread.process) {
								SubprocessThread newThread = thread.clone();
								thread.process.destroy();
								threadList.remove(thread);
								threadList.add(newThread);
								newThread.start();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void addProcess(SubprocessThread thread) {
		synchronized (new Object()) {
			threadList.add(thread);
		}
	}

	public void dispose() {
		try {
			for (int i = threadList.size() - 1; i >= 0; i--) {
				threadList.get(i).process.destroy();
				threadList.get(i).process = null;
			}
			threadList = null;
			ProcessManager.this.finalize();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}
}
