package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilities.FileLocker;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.PathUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.*;
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

	public static boolean selectedMapIsEnabled() {
		boolean enable = false;
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		if (workspaceTree.getSelectionCount() == 1) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) workspaceTree.getSelectionPath().getLastPathComponent();
			TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
			if (selectedNodeData != null && selectedNodeData.getType() == NodeDataType.MAP_NAME) {
				enable = true;
			}
		}
		return enable;
	}

	/**
	 * get selected workspcace map
	 *
	 * @return
	 */
	public static Map getWorkspaceSelectedMap() {
		Map result = null;
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) workspaceTree.getSelectionPath().getLastPathComponent();
		TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
		String mapName = (String) selectedNodeData.getData();
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		Map map = new Map(workspace);
		if (map.open(mapName)) {
			result = map;
		} else {
			Application.getActiveApplication().getOutput().output(MapViewProperties.getString("MapCache_OpenMapFailed"));
		}
		return result;
	}

//	// 获取当前系统的所有的PidName
//	public static Set<String> getCurrOsAllPidNameSet() throws Exception {
//		Set<String> pidNameSet = new HashSet<>();
//		InputStream is = null;
//		InputStreamReader ir = null;
//		BufferedReader br = null;
//		String line = null;
//		String[] array = (String[]) null;
//		try {
//			process p = Runtime.getRuntime().exec("TASKLIST /NH /FO CSV");
//			is = p.getInputStream();
//			ir = new InputStreamReader(is);
//			br = new BufferedReader(ir);
//			while ((line = br.readLine()) != null) {
//				array = line.split(",");
//				line = array[0].replaceAll("\"", "");
//				line = line.replaceAll(".exe", "");
//				line = line.replaceAll(".exe".toUpperCase(), "");
//				if (!StringUtilities.isNullOrEmpty(line)) {
//					pidNameSet.add(line);
//				}
//			}
//		} catch (IOException localIOException) {
//			throw new Exception("获取系统所有进程名出错！");
//		} finally {
//			if (br != null) {
//				br.close();
//			}
//			if (ir != null) {
//				ir.close();
//			}
//			if (is != null) {
//				is.close();
//			}
//		}
//		return pidNameSet;
//	}

	public static boolean voladateDatasource() {
		boolean result = true;
		if (null != Application.getActiveApplication().getWorkspace().getDatasources()) {
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			for (int i = 0; i < datasources.getCount(); i++) {
				if (datasources.get(i).getEngineType() == EngineType.UDB && !datasources.get(i).isReadOnly()) {
					SmOptionPane pane = new SmOptionPane();
					pane.showConfirmDialog(MapViewProperties.getString("String_DatasourceOpenedNotReadOnly"));
					result = false;
					break;
				}
			}
			Application.getActiveApplication().getWorkspace().save();
			ToolbarUIUtilities.updataToolbarsState();
		}
		return result;
	}


	public static boolean isEnabled() {
		boolean result = false;
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
		if (formMap != null) {
			ArrayList<Layer> arrayList = MapUtilities.getLayers(formMap.getMapControl().getMap(), true);
			if (arrayList.size() > 0) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * 获取Cross图标
	 *
	 * @return
	 */
	public static ArrayList getIconImages() {
		String path = PathUtilities.getRootPathName();
		String[] paths = new String[2];
		paths[0] = path;
		paths[1] = "../Resources/Frame";
		path = PathUtilities.combinePath(paths, true);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		ArrayList<Image> images = new ArrayList<>();
		images.add(toolkit.createImage(path + "iDesktop_Cross_16.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_24.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_32.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_64.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_128.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_256.png"));
		images.add(toolkit.createImage(path + "iDesktop Cross.ico"));
		return images;
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
			ArrayList<String> arguments = new ArrayList<>();
			String javaexeHome = System.getProperty("java.home");
			if (StringUtilities.isNullOrEmpty(javaexeHome)) {
				javaexeHome = "." + File.separator + "jre" + File.separator + "bin";
				javaexeHome = isWindows() ? javaexeHome + File.separator + "java.exe" : javaexeHome + File.separator + "java";
			} else if (!javaexeHome.endsWith("bin")) {
				if (isWindows()) {
					javaexeHome = CacheUtilities.replacePath(javaexeHome, "bin") + File.separator + "java.exe";
				} else {
					javaexeHome = CacheUtilities.replacePath(javaexeHome, "bin") + File.separator + "java";
				}
			}
			arguments.add(javaexeHome);
			arguments.add("-cp");
//			String projectPath = replacePath(System.getProperty("user.dir"));
			String jarPath = "";
			if (isWindows()) {
				jarPath = ".;" + ".\\bin\\com.supermap.data.jar;" + ".\\bin\\com.supermap.mapping.jar;" + ".\\bin\\com.supermap.tilestorage.jar;" + ".\\bin\\com.supermap.data.processing.jar;" + ".\\bundles\\require_bundles\\Core.jar;" + ".\\bundles\\require_bundles\\Controls.jar;" + ".\\bundles\\idesktop_bundles\\MapView.jar";
			} else {
				jarPath = "./bin/com.supermap.data.jar:" + "./bin/com.supermap.mapping.jar:" + "./bin/com.supermap.tilestorage.jar:" + "./bin/com.supermap.data.processing.jar:" + "./bundles/require_bundles/Core.jar:" + "./bundles/require_bundles/Controls.jar:" + "./bundles/idesktop_bundles/MapView.jar: ";
			}
//		String jarPath = ".;" + projectPath + "\\bundles\\require_bundles\\Core.jar;" + projectPath + "\\bundles\\idesktop_bundles\\MapView.jar";
			arguments.add(jarPath);
			arguments.add(className);
			for (int i = 0; i < params.length; i++) {
				String param = params[i].endsWith(File.separator) ? params[i].substring(0, params[i].length() - 1) : params[i];
				if (isWindows()) {
					arguments.add("\"" + param + "\"");
				} else {
					arguments.add(param);
				}
			}
			ProcessManager manager = ProcessManager.getInstance();

			SubprocessThread thread = new SubprocessThread(arguments, cacheType);
			manager.addProcess(thread);
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean dynamicEffectClosed(Map map) {
		boolean result = true;
		int count = 0;
		String dynamicEffectLayers = MapViewProperties.getString("String_DynamicEffectLayer") + "\n";
		for (int i = 0; i < map.getLayers().getCount(); i++) {
			if (null != map.getLayers().get(i).getTheme()) {
				Theme tempTheme = map.getLayers().get(i).getTheme();
				if (tempTheme instanceof ThemeLabel && ((ThemeLabel) tempTheme).isFlowEnabled()) {
					dynamicEffectLayers += map.getLayers().get(i).getCaption() + "\n";
					count++;
				}
				if (tempTheme instanceof ThemeGraph && ((ThemeGraph) tempTheme).isFlowEnabled()) {
					dynamicEffectLayers += map.getLayers().get(i).getCaption() + "\n";
					count++;
				}
			}
		}

		if (count > 0) {
			Application.getActiveApplication().getOutput().output(dynamicEffectLayers);
			if (new SmOptionPane().showConfirmDialog(MapViewProperties.getString("String_isDisableDynamicEffect")) == JOptionPane.OK_OPTION) {
				for (int i = 0; i < map.getLayers().getCount(); i++) {
					if (null != map.getLayers().get(i).getTheme()) {
						Theme tempTheme = map.getLayers().get(i).getTheme();
						if (tempTheme instanceof ThemeLabel) {
							((ThemeLabel) map.getLayers().get(i).getTheme()).setFlowEnabled(false);
						}
						if (tempTheme instanceof ThemeGraph) {
							((ThemeGraph) map.getLayers().get(i).getTheme()).setFlowEnabled(false);
						}
					}
				}
			} else {
				result = false;
			}
		}
		return result;
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

	public static String getCachePath(String path) {
		String result = null;
		//获取缓存任务根路径
		File propertyFile = new File(CacheUtilities.replacePath(path, "Cache.property"));
		try {
			if (propertyFile.exists()) {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(propertyFile), "UTF-8"));
				String line = null;
				String cacheName = "";
				while (null != (line = bufferedReader.readLine())) {
					cacheName += line.replace("CacheName=", "");
				}
				result = CacheUtilities.replacePath(path, cacheName);
				bufferedReader.close();
			}
		} catch (Exception e) {
			if (null != e.getMessage()) {
				new SmOptionPane().showConfirmDialog(e.getMessage());
			}
		}
		return result;
	}

	public static void renameDoingFile(String doingPath, String taskPath) {
		//实时检查doing目录下的文件是否加锁,如果文件已经加锁则表示有进程正在使用,否则表示为以前进程挂了没有处理的
		File doingDirectory = new File(doingPath);
		if (doingDirectory.exists() && hasSciFiles(doingDirectory)) {
			renameFiles(taskPath, doingDirectory);
		}
	}

	public static void renameFiles(String taskPath, File doingDirectory) {
		File[] doingFailedSci = doingDirectory.listFiles();
		for (File doingSci : doingFailedSci) {
			//文件加了锁说明文件正在被用于切图任务
			FileLocker locker = new FileLocker(doingSci);
			if (locker.tryLock()) {
				//文件未加锁则判断该文件为上一次任务执行失败时遗留的任务,则将该任务移到task目录下,重新切图
				locker.release();
				doingSci.renameTo(new File(new File(taskPath), doingSci.getName()));
			}
		}
	}

	public static boolean hasSciFiles(File sciDirectory) {
		int size = 0;
		if (null != sciDirectory.list(CacheUtilities.getFilter())) {
			size = sciDirectory.list(CacheUtilities.getFilter()).length;
		}
		return size > 0 ? true : false;
	}
}
