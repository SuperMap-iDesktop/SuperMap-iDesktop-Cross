package com.supermap.desktop.process.core;

import com.sun.org.apache.bcel.internal.generic.LADD;
import com.supermap.desktop.process.loader.DefaultProcessLoader;
import com.supermap.desktop.process.loader.IProcessLoader;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by highsad on 2017/1/5.
 */
public class ProcessManager {

	private static final Vector<IProcessLoader> PROCESS_LOADERS = new Vector<>();
	private Vector<IProcess> processes;
	public static final ProcessManager SINGLETON = new ProcessManager();

	private ProcessManager() {
		this.processes = new Vector<>();
		registerProcessLoader(DefaultProcessLoader.SINGLETON);
	}

	public static void registerProcessLoader(IProcessLoader loader) {
		PROCESS_LOADERS.add(loader);
	}

	public void addProcesses(IProcess[] processes) {
	}
}
