package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.desktop.utilities.StringUtilities;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
	// 获取当前系统的所有的PidName
	public static Set<String> getCurrOsAllPidNameSet() throws Exception {
		Set<String> pidNameSet = new HashSet<>();
		InputStream is = null;
		InputStreamReader ir = null;
		BufferedReader br = null;
		String line = null;
		String[] array = (String[]) null;
		try {
			Process p = Runtime.getRuntime().exec("TASKLIST /NH /FO CSV");
			is = p.getInputStream();
			ir = new InputStreamReader(is);
			br = new BufferedReader(ir);
			while ((line = br.readLine()) != null) {
				array = line.split(",");
				line = array[0].replaceAll("\"", "");
				line = line.replaceAll(".exe", "");
				line = line.replaceAll(".exe".toUpperCase(), "");
				if (!StringUtilities.isNullOrEmpty(line)) {
					pidNameSet.add(line);
				}
			}
		} catch (IOException localIOException) {
			throw new Exception("获取系统所有进程名出错！");
		} finally {
			if (br != null) {
				br.close();
			}
			if (ir != null) {
				ir.close();
			}
			if (is != null) {
				is.close();
			}
		}
		return pidNameSet;
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
			String jarPath = "";
			if (isWindows()) {
				jarPath = ".;" + projectPath + "\\bin\\com.supermap.data.jar;" + projectPath + "\\bin\\com.supermap.mapping.jar;" + projectPath + "\\bin\\com.supermap.tilestorage.jar;" + projectPath + "\\bin\\com.supermap.data.processing.jar;" + projectPath + "\\bundles\\idesktop_bundles\\MapView.jar";
			} else {
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
			manager.addProcess(thread,cacheType);
			thread.start();
		} catch (Exception e) {
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
