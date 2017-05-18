package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.mapping.Map;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/5/17.
 */
public class BuildCache {

	private void startProcess(String[] params) {
		ArrayList<String> arguments = new ArrayList<String>();
		arguments.add("java");
		//arguments.addAll(jvmArgs);
		arguments.add("-cp");
		String projectPath = System.getProperty("user.dir");
		projectPath = projectPath.replace("/", "\\");
		String jarPath = ".;" + projectPath + "\\bin\\com.supermap.data.jar;" + projectPath + "\\bin\\com.supermap.mapping.jar;" + projectPath + "\\bin\\com.supermap.tilestorage.jar;" + projectPath + "\\bin\\com.supermap.data.processing.jar;" + projectPath + "\\bundles\\idesktop_bundles\\MapView.jar";
		arguments.add(jarPath);
		arguments.add(getClass().getName());
		for (int i = 0; i < params.length; i++) {
			arguments.add(params[i]);
		}
		ProcessManager manager = ProcessManager.getInstance();
		SubprocessThread thread = new SubprocessThread(arguments);
		manager.addProcess(thread);
		thread.start();
	}

	public void startProcess(int processCount, String[] params) {
		if (0 == processCount) {
			main(params);
		} else {
			for (int i = 0; i < processCount; i++) {
				startProcess(params);
			}
		}
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			LogWriter.getInstance().writelog("need params");
		} else {
			String taskPath = args[0];
			String workspacePath = args[1];
			String mapName = args[2];
			String cachePath = args[3];
			BuildCache buildCache = new BuildCache();
			buildCache.buildCache(taskPath, workspacePath, mapName, cachePath);
		}
	}

	public void buildCache(String taskPath, String workspacePath, String mapName, String cachePath) {
		try {
			LogWriter log = LogWriter.getInstance();
			int sciLength;
			do {
				File sciPath = new File(taskPath);

				if (sciPath.exists()) {
					long start = System.currentTimeMillis();
					String[] sciFileNames = sciPath.list(getFilter());
					sciLength = sciFileNames.length;
					WorkspaceConnectionInfo connectionInfo = new WorkspaceConnectionInfo(workspacePath);
					Workspace workspace = new Workspace();
					workspace.open(connectionInfo);
					Map map = new Map(workspace);
					map.open(mapName);
					File doingDir = null;
					if (sciLength > 0) {
						File sci = new File(taskPath + "\\" + sciFileNames[0]);
						doingDir = new File(sci.getParentFile().getParent() + "/doing");
						if (!doingDir.exists()) {
							doingDir.mkdir();
						}
					}
					CopyOnWriteArrayList<String> doingSciNames = new CopyOnWriteArrayList<>();
					//Now give 5 sci files to every process if sciLength>5
					int mergeSciCount = 1;
					if (sciLength > mergeSciCount) {
						//First step:Move 5 sci to doing directory
						for (int i = 0; i < mergeSciCount; i++) {
							doingSci(taskPath + "\\" + sciFileNames[i], doingDir, doingSciNames);
						}
						//Second step:get sci file from doing dir and build cache
						for (int i = 0; i < doingSciNames.size(); i++) {
							String sciName = doingSciNames.get(i);
							build(cachePath, log, start, map, sciName);
						}
					} else {
						//First step:Move last sci file to doing directory
						for (int i = 0; i < sciLength; i++) {
							doingSci(taskPath + "\\" + sciFileNames[i], doingDir, doingSciNames);
						}
						//Second step:get sci file from doing dir and build cache
						for (int i = 0; i < sciLength; i++) {
							String sciName = doingSciNames.get(i);
							build(cachePath, log, start, map, sciName);
						}
					}
					map.close();
					map.dispose();
					workspace.close();
					workspace.dispose();
				} else {
					log.writelog("Task files does not exist");
					break;
				}
			} while (sciLength != 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doingSci(String sciFileName, File doingDir, CopyOnWriteArrayList<String> doingSciNames) {
		String sciName = sciFileName;
		File sci = new File(sciName);
		if (sci.exists() && null != doingDir) {
			sci.renameTo(new File(doingDir, sci.getName()));

			doingSciNames.add(doingDir.getAbsolutePath() + "\\" + sci.getName());
		}
	}

	private void build(String cachePath, LogWriter log, long start, Map map, String sciName) {
		log.writelog(String.format("start sciName:%s , PID:%s", sciName, LogWriter.getPID()));
		log.writelog(String.format("init PID:%s, cost(ms):%d", LogWriter.getPID(), System.currentTimeMillis() - start));
		File sci = new File(sciName);
		if (!sci.exists()) {
			LogWriter.getInstance().writelog(String.format("sciFile: %s does not exist. Maybe has done at before running. ", sciName));
		}
		long oneStart = System.currentTimeMillis();
		MapCacheBuilder builder = new MapCacheBuilder();
		builder.setMap(map);
		builder.fromConfigFile(sciName);
		builder.setOutputFolder(cachePath);
		builder.resumable(false);

		boolean result = builder.buildWithoutConfigFile();
		builder.dispose();

		if (result) {
			File doneDir = new File(sci.getParentFile().getParent() + "/build");
			if (!doneDir.exists()) {
				doneDir.mkdir();
			}
			sci.renameTo(new File(doneDir, sci.getName()));
		}

		long end = System.currentTimeMillis();
		log.writelog(String.format("%s %s done,PID:%s, cost(ms):%d, done", sciName, String.valueOf(result), LogWriter.getPID(), end - oneStart));
		log.flush();
	}

	//List all sci file's name
	private FilenameFilter getFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".sci");
			}
		};
	}

}
