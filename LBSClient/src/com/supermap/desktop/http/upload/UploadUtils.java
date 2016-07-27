package com.supermap.desktop.http.upload;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.supermap.desktop.http.LogUtils;
import com.supermap.desktop.http.callable.FileEvent;
import com.supermap.desktop.http.callable.FileSteppedListener;
import com.supermap.desktop.http.download.FileInfo;
 
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
public abstract class UploadUtils {
	
	public static Map<FileInfo, BatchUploadFile> hashMap = Collections.synchronizedMap(new HashMap());
 
    public static FileInfo upload(String url) {
        FileInfo bean = new FileInfo(url);
        return upload(bean);
    }
    
    public static FileInfo upload(String url, int threadNum) {
        FileInfo bean = new FileInfo(url, threadNum);
        return upload(bean);
    }
    
    public static FileInfo upload(FileInfo bean, String localFilePath, String fileName, long fileSize, Boolean isHDFSFile) {
    	bean.setFilePath(localFilePath);
    	bean.setFileName(fileName);
    	bean.setFileSize(fileSize);
    	bean.setHDFSFile(isHDFSFile);
        return upload(bean);
    }
    
    public static FileInfo upload(String url, String fileName, String filePath, long fileSize, int threadNum, Boolean isHDFSFile) {
        FileInfo bean = new FileInfo(url, fileName, filePath, fileSize, threadNum, isHDFSFile);
        return upload(bean);
    }
    
    public static FileInfo upload(FileInfo bean) {
        LogUtils.info(bean);
        BatchUploadFile down = new BatchUploadFile(bean);
        hashMap.put(bean, down);
        down.start();
        return bean;
    }
    
    public static BatchUploadFile getBatchUploadFileWorker(FileInfo bean) {
    	BatchUploadFile batchUPloadFile = null;
    	if (hashMap.containsKey(bean)) {
    		batchUPloadFile = hashMap.get(bean);
    	}
    	return batchUPloadFile;
    }
	
    public static Map<FileInfo, BatchUploadFile> getHashMap() {
		return hashMap;
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
	
    public static void fireSteppedEvent(Object source, FileInfo uploadInfo, int progress, int remainTime) {
    	if (stepListeners != null) {
			CopyOnWriteArrayList<FileSteppedListener> listeners = stepListeners;
			Iterator<FileSteppedListener> iter = listeners.iterator();
			while (iter.hasNext()) {
				FileSteppedListener listener = iter.next();
				FileEvent event = new FileEvent(source, uploadInfo, progress, remainTime);
				listener.stepped(event);
			}
		}
	}
}