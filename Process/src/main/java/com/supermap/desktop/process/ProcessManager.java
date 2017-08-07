package com.supermap.desktop.process;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.loader.DefaultProcessDescriptor;
import com.supermap.desktop.process.loader.IProcessGroup;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/8/4.
 */
public class ProcessManager {
	private ArrayList<IProcess> processes = new ArrayList<>();
	private ArrayList<IProcessGroup> groups = new ArrayList<>();
	private Map<String, IProcessLoader> loaderMap = new ConcurrentHashMap<>();

	public IProcess createProcess(DefaultProcessDescriptor processDescriptor) {
		if (processDescriptor == null) {
			return null;
		}

		String className = processDescriptor.getClassName();
		if (StringUtilities.isNullOrEmpty(className) || !this.loaderMap.containsKey(className)) {
			return null;
		}

		return this.loaderMap.get(className).loadProcess(processDescriptor);
	}

	public void registerProcessLoader(String className, IProcessLoader loader) {
		if (this.loaderMap.containsKey(className)) {
			return;
		}

		this.loaderMap.put(className, loader);
	}
}
