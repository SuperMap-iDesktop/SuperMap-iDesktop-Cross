package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.mapping.Map;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/5/17.
 */
public class BuildCache {
	public static final int SCIPATH_INDEX = 0;
	public static final int WORKSPACEPATH_INDEX = 1;
	public static final int MAPNAME_INDEX = 2;
	public static final int CACHEPATH_INDEX = 3;
	public static final int PROCESSCOUNT_INDEX = 4;
	public static final int MERGESCICOUNT_INDEX = 5;

	//Add new process
	public void addProcess(String[] params) {
		CacheUtilities.startProcess(params, getClass().getName(),LogWriter.BUILD_CACHE);
	}

	//Start process
	public void startProcess(int processCount, String[] params) {
		try {
			if (0 == processCount) {
				main(params);
			} else {
				//Write executing info to log
				for (int i = 0; i < processCount; i++) {
					CacheUtilities.startProcess(params, getClass().getName(),LogWriter.BUILD_CACHE);
					Thread.sleep(2000);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("need params");
		} else {
			BuildCache buildCache = new BuildCache();
			buildCache.buildCache(args);
		}
	}

	//Core executor for build cache
	public void buildCache(String[] params) {
		try {
			String taskPath = params[SCIPATH_INDEX];
			String workspacePath = params[WORKSPACEPATH_INDEX];
			String mapName = params[MAPNAME_INDEX];
			String cachePath = params[CACHEPATH_INDEX];
			String mergeCount = "1";
			if (params.length > MERGESCICOUNT_INDEX && !params[MERGESCICOUNT_INDEX].equals("0"))
				mergeCount = params[MERGESCICOUNT_INDEX];
			//Instance LogWriter
			LogWriter log = LogWriter.getInstance(LogWriter.BUILD_CACHE);
			int sciLength;
			WorkspaceConnectionInfo connectionInfo = new WorkspaceConnectionInfo(workspacePath);
			Workspace workspace = new Workspace();
			workspace.open(connectionInfo);
			Map map = new Map(workspace);
			map.open(mapName);
			File sciPath = new File(taskPath);
			if (sciPath.exists()) {
				do {
					long start = System.currentTimeMillis();
					//Recalculate sci file length
					String[] sciFileNames = sciPath.list(CacheUtilities.getFilter());
					sciLength = sciFileNames.length;

					File doingDir = null;
					if (sciLength > 0) {
						File sci = new File(taskPath + "\\" + sciFileNames[0]);
						doingDir = new File(sci.getParentFile().getParent() + "\\doing");
						if (!doingDir.exists()) {
							doingDir.mkdir();
						}
					}
					CopyOnWriteArrayList<String> doingSciNames = new CopyOnWriteArrayList<>();
					//Now give mergeSciCount sci files to every process if sciLength>mergeSciCount
					int mergeSciCount = Integer.valueOf(mergeCount);
					if (sciLength > mergeSciCount) {
						//First step:Move mergeSciCount sci to doing directory
						int success = 0;
						for (int i = 0; success < mergeSciCount; i++) {
							if (doingSci(taskPath + "\\" + sciFileNames[sciLength - 1 - i], doingDir, doingSciNames)) {
								success++;
							}
						}

					} else {
						//First step:Move last sci file to doing directory
						for (int i = sciLength - 1; i >= 0; i--) {
							doingSci(taskPath + "\\" + sciFileNames[i], doingDir, doingSciNames);
						}
					}
					//Second step:get sci file from doing dir and build cache
					for (int i = 0; i < doingSciNames.size(); i++) {
						String sciName = doingSciNames.get(i);
						build(cachePath, log, start, map, sciName);
					}

				} while (sciLength != 0);
				map.close();
				map.dispose();
				workspace.close();
				workspace.dispose();
			} else {
				log.writelog("Task files does not exist");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private boolean doingSci(String sciFileName, File doingDir, CopyOnWriteArrayList<String> doingSciNames) {
		String sciName = sciFileName;
		File sci = new File(sciName);
		boolean renameSuccess = sci.renameTo(new File(doingDir, sci.getName()));
		if (renameSuccess) {
			doingSciNames.add(doingDir.getAbsolutePath() + "\\" + sci.getName());
		}
		return renameSuccess;
	}

	private void build(String cachePath, LogWriter log, long start, Map map, String sciName) {
		log.writelog(String.format("start sciName:%s , PID:%s", sciName, LogWriter.getPID()));
		log.writelog(String.format("init PID:%s, cost(ms):%d", LogWriter.getPID(), System.currentTimeMillis() - start));
		File sci = new File(sciName);
		if (!sci.exists()) {
			log.writelog(String.format("sciFile: %s does not exist. Maybe has done at before running. ", sciName));
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
			File doneDir = new File(sci.getParentFile().getParent() + "\\build");
			if (!doneDir.exists()) {
				doneDir.mkdir();
			}
			sci.renameTo(new File(doneDir, sci.getName()));
		}

		long end = System.currentTimeMillis();
		log.writelog(String.format("%s %s done,PID:%s, cost(ms):%d, done", sciName, String.valueOf(result), LogWriter.getPID(), end - oneStart));
	}

}
