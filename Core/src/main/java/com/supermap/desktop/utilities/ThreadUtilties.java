package com.supermap.desktop.utilities;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池
 *
 * @author XiaJT
 */
public class ThreadUtilties {
	private static ExecutorService executorService;

	private ThreadUtilties() {

	}

	public static void execute(Runnable runnable) {
		getExecutorService().execute(runnable);
	}

	private static ExecutorService getExecutorService() {
		if (executorService == null) {
			executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 2);
		}
		return executorService;
	}


}
