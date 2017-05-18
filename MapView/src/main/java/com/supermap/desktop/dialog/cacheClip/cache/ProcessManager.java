package com.supermap.desktop.dialog.cacheClip.cache;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/5/17.
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
	}

	public void addProcess(SubprocessThread thread) {
		synchronized (new Object()) {
			threadList.add(thread);
		}
	}
}
