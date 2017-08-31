package com.supermap.desktop.process;

import com.supermap.desktop.process.loader.DefaultProcessGroup;

/**
 * Created by highsad on 2017/8/4.
 */
public class ProcessManager extends DefaultProcessGroup {

	public final static ProcessManager INSTANCE = new ProcessManager("Manager", "ProcessManager");

	public ProcessManager(String id, String title) {
		super(id, title, "0", null);
	}

//	public IProcess createProcess(IProcessDescriptor processDescriptor) {
//		if (processDescriptor == null) {
//			return null;
//		}
//
//		String className = processDescriptor.getClassName();
//		if (StringUtilities.isNullOrEmpty(className) || !this.loaderMap.containsKey(className)) {
//			return null;
//		}
//
//		return this.loaderMap.get(className).loadProcess();
//	}
//
//	public void registerProcessLoader(String className, IProcessLoader loader) {
//		if (this.loaderMap.containsKey(className)) {
//			return;
//		}
//
//		this.loaderMap.put(className, loader);
//	}
}
