package com.supermap.desktop.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

 
/**
 * <b>function:</b> 分块多线程下载工具类
 * @author hoojo
 * @createDate 2011-9-28 下午05:22:18
 * @file DownloadUtils.java
 * @package com.hoo.util
 * @project MultiThreadDownLoad
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class DownloadUtils {
	
	private static Map<DownloadInfo, BatchDownloadFile> hashMap = Collections.synchronizedMap(new HashMap());
 
    public static DownloadInfo download(String url) {
        DownloadInfo bean = new DownloadInfo(url);
        return download(bean);
    }
    
    public static DownloadInfo download(String url, int threadNum) {
        DownloadInfo bean = new DownloadInfo(url, threadNum);
        return download(bean);
    }
    
    public static DownloadInfo download(String url, String fileName, String filePath, long fileSize, int threadNum, Boolean isHDFSFile) {
        DownloadInfo bean = new DownloadInfo(url, fileName, filePath, fileSize, threadNum, isHDFSFile);
        return download(bean);
    }
    
    public static DownloadInfo download(DownloadInfo bean) {
        LogUtils.info(bean);
        BatchDownloadFile down = new BatchDownloadFile(bean);
        hashMap.put(bean, down);
        down.start();
        
        return bean;
    }
    
    public static BatchDownloadFile getBatchDownloadFileWorker(DownloadInfo bean) {
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
	
    public static void fireSteppedEvent(Object source, DownloadInfo downloadInfo, int progress, int remainTime) {
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

	public static Map<DownloadInfo, BatchDownloadFile> getHashMap() {
		return hashMap;
	}
    
}