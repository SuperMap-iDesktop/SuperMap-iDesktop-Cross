package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.desktop.utilities.FileLocker;
import com.supermap.mapping.Map;

import java.io.File;
import java.util.HashMap;
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
	//	public static final int MERGESCICOUNT_INDEX = 5;
	public static final int ISAPPENDING_INDEX = 5;

	//Add new process
	public void addProcess(String[] params) {
		CacheUtilities.startProcess(params, getClass().getName(), LogWriter.BUILD_CACHE);
	}

	//Start process
	public void startProcess(int processCount, String[] params) {
		try {
			LogWriter.removeAllLogs();
			for (int i = 0; i < processCount; i++) {
				CacheUtilities.startProcess(params, getClass().getName(), LogWriter.BUILD_CACHE);
				Thread.sleep(2000);
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
			String isAppendingStr = params[ISAPPENDING_INDEX];
			boolean isAppending = Boolean.valueOf(isAppendingStr);
			//Instance LogWriter
			LogWriter log = LogWriter.getInstance(LogWriter.BUILD_CACHE);
			int sciLength;
			WorkspaceConnectionInfo connectionInfo = new WorkspaceConnectionInfo(workspacePath);
			Workspace workspace = new Workspace();
			workspace.open(connectionInfo);
			Map map = new Map(workspace);
			map.open(mapName);
			File taskFiles = new File(taskPath);
			if (taskFiles.exists()) {
				File doingDir = new File(CacheUtilities.replacePath(taskFiles.getParent(), "doing"));
				if (!doingDir.exists()) {
					doingDir.mkdir();
				}
				do {
					long start = System.currentTimeMillis();
					//Recalculate sci file length
					String[] sciFileNames = taskFiles.list(CacheUtilities.getFilter());
					sciLength = sciFileNames.length;

					CopyOnWriteArrayList<String> doingSciNames = new CopyOnWriteArrayList<>();
					//Now give mergeSciCount sci files to every process if sciLength>mergeSciCount
					int mergeSciCount = 3;

					if (sciLength > mergeSciCount) {
						//First step:Move mergeSciCount sci to doing directory
						int success = 0;
						for (int i = 0; success < mergeSciCount; i++) {
							if (doingSci(log, CacheUtilities.replacePath(taskPath, sciFileNames[sciLength - 1 - i]), doingDir, doingSciNames)) {
								success++;
							}
						}

					} else {
						//First step:Move last sci file to doing directory
						for (int i = sciLength - 1; i >= 0; i--) {
							doingSci(log, CacheUtilities.replacePath(taskPath, sciFileNames[i]), doingDir, doingSciNames);
						}
					}
					log.writelog(String.format("get doing sci, cost(ms):%d", System.currentTimeMillis() - start));
					//Second step:get sci file from doing dir and build cache
					HashMap<String, FileLocker> lockerHashMap  = new HashMap<>();
					HashMap<String, MapCacheBuilder> cacheBuilderHashMap = new HashMap<>();
					for (int i = 0; i < doingSciNames.size(); i++) {
						//将数组中的sci文件全部加锁，执行完一个再释放锁
						String sciName = doingSciNames.get(i);
						File sci = new File(sciName);
						if (!sci.exists()) {
							return;
						}
						//加锁前先创建MapCacheBuilder(builder.fromConfigFile()需要用到sci文件)
						MapCacheBuilder builder = new MapCacheBuilder();
						builder.setMap(map);
						builder.fromConfigFile(sciName);
						builder.setOutputFolder(cachePath);
						builder.setCacheName(builder.getCacheName());
						builder.resumable(false);
						builder.setIsAppending(isAppending);
						cacheBuilderHashMap.put(sciName, builder);
						FileLocker locker = new FileLocker(sci);
						if (locker.tryLock()) {
							//加锁成功将文件添加到hashmap中
							lockerHashMap.put(sciName, locker);
						}
					}
					for (int i = 0; i < doingSciNames.size(); i++) {
						String sciName = doingSciNames.get(i);
						build(cacheBuilderHashMap.get(sciName), lockerHashMap.get(sciName), new File(sciName), log, isAppending);
					}

				} while (sciLength != 0);
				log.close();
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


	private boolean doingSci(LogWriter log, String sciFileName, File doingDir, CopyOnWriteArrayList<String> doingSciNames) {
		String sciName = sciFileName;
		File sci = new File(sciName);
		boolean renameSuccess = sci.renameTo(new File(doingDir, sci.getName()));
		if (renameSuccess) {
			log.writelog("doing:" + sci.getName());
			doingSciNames.add(CacheUtilities.replacePath(doingDir.getAbsolutePath(), sci.getName()));
		}
		return renameSuccess;
	}

	private void build(MapCacheBuilder builder, FileLocker locker, File sci, LogWriter log, boolean isAppending) {
		log.writelog(String.format("start sciName:%s , PID:%s", sci.getName(), LogWriter.getPID()));
		long oneStart = System.currentTimeMillis();

		boolean result;
		if (isAppending) {
			result = builder.build();
		} else {
			result = builder.buildWithoutConfigFile();
		}
		//释放锁
		locker.release();
		builder.dispose();
		if (result) {
			File doneDir = new File(CacheUtilities.replacePath(sci.getParentFile().getParent(), "build"));
			if (!doneDir.exists()) {
				doneDir.mkdir();
			}
			sci.renameTo(new File(doneDir, sci.getName()));
		} else {
			File failDir = new File(CacheUtilities.replacePath(sci.getParentFile().getParent(), "failed"));
			if (!failDir.exists()) {
				failDir.mkdir();
			}
			sci.renameTo(new File(failDir, sci.getName()));
		}

		long end = System.currentTimeMillis();
		log.writelog(String.format("%s %s done,PID:%s, cost(ms):%d, done", sci.getName(), String.valueOf(result), LogWriter.getPID(), end - oneStart));
	}
}
