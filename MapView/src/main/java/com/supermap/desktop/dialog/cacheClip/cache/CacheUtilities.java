package com.supermap.desktop.dialog.cacheClip.cache;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by xie on 2017/5/31.
 */
public class CacheUtilities {

	/**
	 * Replace path
	 *
	 * @param sourceStr
	 * @return
	 */
	public static String replacePath(String sourceStr) {
		if (isWindows()) {
			sourceStr = sourceStr.replaceAll("/", "\\\\");
		} else if (isLinux()) {
			sourceStr = sourceStr.replaceAll("\\\\", "/");
		}
		return sourceStr;
	}

	public static String replacePath(String sourceStr, String name) {
		sourceStr = replacePath(sourceStr);
		if (isWindows() && !sourceStr.endsWith("\\")) {
			sourceStr += "\\";
		} else if (isLinux() && !sourceStr.endsWith("/")) {
			sourceStr += "/";
		}
		return sourceStr + name;
	}

	/**
	 * Start a new process
	 *
	 * @param params
	 * @param className
	 * @param cacheType
	 */
	public static void startProcess(String[] params, String className, String cacheType) {
		try {
			ArrayList<String> arguments = new ArrayList<String>();
			arguments.add("java");
			//arguments.addAll(jvmArgs);
			arguments.add("-cp");
			String projectPath = replacePath(System.getProperty("user.dir"));

//		String jarPath = ".;" + projectPath + "\\bin\\com.supermap.data.jar;" + projectPath + "\\bin\\com.supermap.mapping.jar;" + projectPath + "\\bin\\com.supermap.tilestorage.jar;" + projectPath + "\\bin\\com.supermap.data.processing.jar;" + projectPath + "\\bundles\\idesktop_bundles\\MapView.jar";
			String jarPath = "";
			if (isWindows()) {
				jarPath = ".;" + projectPath + "\\bin\\com.supermap.data.jar;" + projectPath + "\\bin\\com.supermap.mapping.jar;" + projectPath + "\\bin\\com.supermap.tilestorage.jar;" + projectPath + "\\bin\\com.supermap.data.processing.jar;" + projectPath + "\\bundles\\idesktop_bundles\\MapView.jar";
			} else {
				//直接运行时必须依赖主键，动态添加LD_LIBRARY_PATH
				String[] commonds = {"sh", "-c", "export LD_LIBRARY_PATH=" + projectPath + "/bin" + ";export PATH=$LD_LIBRARY_PATH:$PATH"};
				Runtime.getRuntime().exec(commonds);
				jarPath = projectPath + "/bin/com.supermap.data.jar:" + projectPath + "/bin/com.supermap.mapping.jar:" + projectPath + "/bin/com.supermap.tilestorage.jar:" + projectPath + "/bin/com.supermap.data.processing.jar:" + projectPath + "/bundles/idesktop_bundles/MapView.jar: ";
			}
//		String jarPath = ".;" + projectPath + "\\bundles\\require_bundles\\Core.jar;" + projectPath + "\\bundles\\idesktop_bundles\\MapView.jar";
			arguments.add(jarPath);
			arguments.add(className);
			for (int i = 0; i < params.length; i++) {
				arguments.add(params[i]);
			}
			ProcessManager manager = ProcessManager.getInstance();
			SubprocessThread thread = new SubprocessThread(arguments, cacheType);
			manager.addProcess(thread);
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isWindows() {
		boolean isWindows = false;
		String system = System.getProperties().getProperty("os.name");
		if (system.startsWith("Windows")) {
			isWindows = true;
		}
		return isWindows;
	}

	/**
	 * 判断是否是linux系统，暂时认为不是windows就是linux
	 * 避免增加平台时修改“!SystemPropertyUtilities.isWindows()”这样的调用
	 *
	 * @return
	 */
	public static boolean isLinux() {
		return !isWindows();
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
