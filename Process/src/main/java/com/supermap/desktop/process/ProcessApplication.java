package com.supermap.desktop.process;

/**
 * Created by highsad on 2017/7/19.
 */
public class ProcessApplication {
	public static ProcessApplication INSTANCE;

	public static ProcessApplication getProcessApplication() {
		return INSTANCE;
	}

	public static void init() {
		INSTANCE = new ProcessApplication();
	}
}
