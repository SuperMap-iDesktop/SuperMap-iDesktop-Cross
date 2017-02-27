package com.supermap.desktop.http.download;

import com.supermap.desktop.http.LogUtils;
import com.supermap.desktop.http.callable.FileEvent;
import com.supermap.desktop.http.callable.FileSteppedListener;
import com.supermap.desktop.ui.lbs.impl.FileInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <b>function:</b> 分块多线程下载工具类
 * 
 * @author xie
 */
public abstract class DownloadUtils {

	private static Map<FileInfo, BatchDownloadFile> hashMap = Collections.synchronizedMap(new HashMap());

	public static FileInfo download(String url) {
		FileInfo bean = new FileInfo(url);
		return download(bean);
	}

	public static FileInfo download(String url, int threadNum) {
		FileInfo bean = new FileInfo(url, threadNum);
		return download(bean);
	}

	public static FileInfo download(String url, String fileName, String filePath, long fileSize, int threadNum, Boolean isHDFSFile) {
		FileInfo bean = new FileInfo(url, fileName, "", filePath, fileSize, threadNum, isHDFSFile);
		return download(bean);
	}

	public static FileInfo download(FileInfo bean) {
		LogUtils.info(bean);
		BatchDownloadFile down = new BatchDownloadFile(bean);
		hashMap.put(bean, down);
		down.start();

		return bean;
	}

	public static BatchDownloadFile getBatchDownloadFileWorker(FileInfo bean) {
		BatchDownloadFile batchDownloadFile = null;
		if (hashMap.containsKey(bean)) {
			batchDownloadFile = hashMap.get(bean);
		}
		return batchDownloadFile;
	}

	private static transient CopyOnWriteArrayList<FileSteppedListener> stepListeners = new CopyOnWriteArrayList<FileSteppedListener>();

	public static synchronized void addNewWindowListener(FileSteppedListener listener) {
		if (stepListeners == null) {
			stepListeners = new CopyOnWriteArrayList<FileSteppedListener>();
		}

		if (!stepListeners.contains(listener)) {
			stepListeners.add(listener);
		}
	}

	public static void removeNewWindowListener(FileSteppedListener listener) {
		if (stepListeners != null && stepListeners.contains(listener)) {
			stepListeners.remove(listener);
		}
	}

	public static void fireSteppedEvent(Object source, FileInfo downloadInfo, int progress, int remainTime) {
		if (stepListeners != null) {
			CopyOnWriteArrayList<FileSteppedListener> listeners = stepListeners;
			Iterator<FileSteppedListener> iter = listeners.iterator();
			while (iter.hasNext()) {
				FileSteppedListener listener = iter.next();
				FileEvent event = new FileEvent(source, downloadInfo, progress, remainTime);
				listener.stepped(event);
			}
		}
	}

	public static Map<FileInfo, BatchDownloadFile> getHashMap() {
		return hashMap;
	}

}