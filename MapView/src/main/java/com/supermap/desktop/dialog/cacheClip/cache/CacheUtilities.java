package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilities.FileLocker;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.WorkspaceUtilities;
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
	private static boolean isOutSide;

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

	public static boolean voladateDatasource(boolean modify) {
		boolean result = true;
		SmOptionPane optionPane = new SmOptionPane();
		if (null != Application.getActiveApplication().getWorkspace().getDatasources()) {
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			for (int i = 0; i < datasources.getCount(); i++) {
				if (datasources.get(i).getEngineType() == EngineType.UDB && !datasources.get(i).isReadOnly()) {
					optionPane.showErrorDialog(MapViewProperties.getString("String_DatasourceOpenedNotReadOnly"));
					result = false;
					break;
				}
			}
		}
		if (WorkspaceUtilities.isWorkspaceModified() && modify) {
			optionPane.showErrorDialog(MapViewProperties.getString("String_WorkSpaceNotSave"));
			return false;
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
		String path = "./MapView/src/main/resources/mapviewresources/logo/";

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		ArrayList<Image> images = new ArrayList<>();
		images.add((toolkit.createImage(path + "iDesktop_Cross_16.png")));
		images.add(toolkit.createImage(path + "iDesktop_Cross_24.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_32.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_64.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_128.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_256.png"));
		images.add(toolkit.createImage(path + "iDesktop Cross.ico"));
		return images;
	}

	private static Image getIconImage() {
		String path = "./MapView/src/main/resources/mapviewresources/logo/";
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		return toolkit.createImage(path + "iDesktop_Cross_24.png");
	}

	/**
	 * 有图标的信息提示框
	 *
	 * @param parent
	 * @param message
	 * @return
	 */
	public static int showMessageDialog(Component parent, String message) {
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
		JDialog dialog = optionPane.createDialog(parent, GlobalParameters.getDesktopTitle());
		dialog.setIconImage(getIconImage());
		dialog.setVisible(true);
		return (int) optionPane.getValue();
	}

	/**
	 * 有图标的确认提示框
	 *
	 * @param parent
	 * @param message
	 * @return
	 */
	public static int showConfirmDialog(Component parent, String message) {
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION);
		JDialog dialog = optionPane.createDialog(parent, GlobalParameters.getDesktopTitle());
		dialog.setIconImage(getIconImage());
		dialog.setVisible(true);
		return (int) optionPane.getValue();
	}

	/**
	 * 获取目录集合下的所有sci个数
	 *
	 * @param taskPaths
	 * @return
	 */
	public static int getTaskSci(ArrayList<String> taskPaths) {
		int taskSize = 0;
		for (String taskPath : taskPaths) {
			File taskFile = new File(taskPath);
			if (taskFile.exists()) {
				taskSize += taskFile.list().length;
			}
		}
		return taskSize;
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
			String jarPath = System.getProperties().getProperty("java.class.path");
			if (!jarPath.contains("MapView.jar")) {
				isOutSide = false;
				if (isWindows()) {
					jarPath = ".;" + ".\\bin\\com.supermap.data.jar;" + ".\\bin\\com.supermap.mapping.jar;" + ".\\bin\\com.supermap.tilestorage.jar;" + ".\\bin\\com.supermap.data.processing.jar;" + ".\\bundles\\require_bundles\\Core.jar;" + ".\\bundles\\require_bundles\\Controls.jar;" + ".\\bundles\\idesktop_bundles\\MapView.jar";
				} else {
					jarPath = "./bin/com.supermap.data.jar:" + "./bin/com.supermap.mapping.jar:" + "./bin/com.supermap.tilestorage.jar:" + "./bin/com.supermap.data.processing.jar:" + "./bundles/require_bundles/Core.jar:" + "./bundles/require_bundles/Controls.jar:" + "./bundles/idesktop_bundles/MapView.jar: ";
				}
			} else {
				isOutSide = true;
			}
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

	/**
	 * 获取缓存目录下所有包含childPath的路径集合
	 *
	 * @param childPath
	 * @param cachePath
	 * @return
	 */
	public static ArrayList<String> getTaskPath(String childPath, String cachePath) {
		ArrayList<String> result = new ArrayList<>();
		if (null != cachePath) {
			String[] updateFiles = new File(cachePath).list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.contains("Update");
				}
			});

			String cacheTask = CacheUtilities.replacePath(cachePath, "CacheTask");
			result.add(CacheUtilities.replacePath(cacheTask, childPath));
			if (null != updateFiles) {
				for (int i = 0, length = updateFiles.length; i < length; i++) {
					String updateTask = CacheUtilities.replacePath(cachePath, updateFiles[i]);
					result.add(CacheUtilities.replacePath(updateTask, childPath));
				}
			}
		}
		return result;
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

	/**
	 * 将sci的名字添加到集合中
	 *
	 * @param names
	 * @param sciPath
	 */
	public static void addSciToArray(ArrayList<String> names, File sciPath) {
		if (sciPath.exists()) {
			String[] scis = sciPath.list(getFilter());
			for (String sci : scis) {
				names.add(sci);
			}
		}
	}

	/**
	 * 获取缓存名称
	 *
	 * @param path
	 * @return
	 */
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

	/**
	 * 判断任务是否完成
	 *
	 * @param taskPaths
	 * @return
	 */
	public static boolean taskFinished(ArrayList<String> taskPaths) {
		int finised = 0;
		for (String taskPath : taskPaths) {
			File taskFile = new File(taskPath);

			if (taskFile.exists() && null != taskFile.list(getFilter())) {
				finised = taskFile.list(getFilter()).length;
			}
			String doingPath = CacheUtilities.replacePath(taskFile.getParent(), "doing");
			if (taskPath.contains("error")) {
				doingPath = CacheUtilities.replacePath(taskFile.getParent(), "checking");
			}
			File doingFile = new File(doingPath);
			if (doingFile.exists() && null != doingFile.list())
				finised += doingFile.list().length;
		}
		return 0 == finised;
	}


	/**
	 * 获取总的sci集合
	 *
	 * @param cachePath
	 * @return
	 */
	public static ArrayList<String> getTotalCacheSci(String cachePath) {
		ArrayList<String> result = new ArrayList<>();
		if (null != CacheUtilities.getCachePath(cachePath)) {
			File cacheFlie = new File(CacheUtilities.getCachePath(cachePath));
			File[] scis = cacheFlie.listFiles();
			if (null != scis) {
				for (int i = 0; i < scis.length; i++) {
					if (scis[i].getName().endsWith(".sci")) {
						result.add(scis[i].getAbsolutePath());
					}
				}
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

	public static void renameFailedFiles(ArrayList<String> taskPaths, String failedPath) {
		for (String taskDirectory : taskPaths) {
			String parentStr = new File(taskDirectory).getParent();
			File failedDirectory = new File(CacheUtilities.replacePath(parentStr, failedPath));
			File[] failedSci = failedDirectory.listFiles();
			for (int i = 0; i < failedSci.length; i++) {
				failedSci[i].renameTo(new File(taskDirectory, failedSci[i].getName()));
			}
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
