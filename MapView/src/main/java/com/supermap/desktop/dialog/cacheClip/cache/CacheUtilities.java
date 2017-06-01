package com.supermap.desktop.dialog.cacheClip.cache;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * Created by xie on 2017/5/31.
 */
public class CacheUtilities {
	public static void startProcess(String[] params, String className) {
		ArrayList<String> arguments = new ArrayList<String>();
		arguments.add("java");
		//arguments.addAll(jvmArgs);
		arguments.add("-cp");
		String projectPath = System.getProperty("user.dir");
		projectPath = projectPath.replace("/", "\\");
		String jarPath = ".;" + projectPath + "\\bin\\com.supermap.data.jar;" + projectPath + "\\bin\\com.supermap.mapping.jar;" + projectPath + "\\bin\\com.supermap.tilestorage.jar;" + projectPath + "\\bin\\com.supermap.data.processing.jar;" + projectPath + "\\bundles\\idesktop_bundles\\MapView.jar";
//		String jarPath = ".;" + projectPath + "\\bundles\\require_bundles\\Core.jar;" + projectPath + "\\bundles\\idesktop_bundles\\MapView.jar";
		arguments.add(jarPath);
		arguments.add(className);
		for (int i = 0; i < params.length; i++) {
			arguments.add(params[i]);
		}
		ProcessManager manager = ProcessManager.getInstance();
		SubprocessThread thread = new SubprocessThread(arguments);
		manager.addProcess(thread);
		thread.start();
	}


	//List all sci file's name
	public static FilenameFilter getFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".sci");
			}
		};
	}

}
