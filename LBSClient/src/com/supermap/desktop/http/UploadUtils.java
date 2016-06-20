package com.supermap.desktop.http;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.supermap.desktop.Application;
import com.supermap.desktop.event.NewWindowListener;
import com.supermap.desktop.ui.controls.progress.FormProgress;

 
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
	
	private static Map<DownloadInfo, BatchUploadFile> hashMap = new HashMap<DownloadInfo, BatchUploadFile>();
 
    public static DownloadInfo upload(String url) {
        DownloadInfo bean = new DownloadInfo(url);
        return upload(bean);
    }
    
    public static DownloadInfo upload(String url, int threadNum) {
        DownloadInfo bean = new DownloadInfo(url, threadNum);
        return upload(bean);
    }
    
    public static DownloadInfo upload(String webPath, String localFilePath, String fileName, long fileSize, Boolean isHDFSFile) {
    	DownloadInfo bean = new DownloadInfo(webPath);
    	bean.setFilePath(localFilePath);
    	bean.setFileName(fileName);
    	bean.setFileSize(fileSize);
    	bean.setHDFSFile(isHDFSFile);
        return upload(bean);
    }
    
    public static DownloadInfo upload(String url, String fileName, String filePath, long fileSize, int threadNum, Boolean isHDFSFile) {
        DownloadInfo bean = new DownloadInfo(url, fileName, filePath, fileSize, threadNum, isHDFSFile);
        return upload(bean);
    }
    
    public static DownloadInfo upload(DownloadInfo bean) {
        LogUtils.info(bean);
        BatchUploadFile down = new BatchUploadFile(bean);
        hashMap.put(bean, down);
        new Thread(down).start();
        
        return bean;
    }
    
    public static BatchUploadFile getBatchUploadFileWorker(DownloadInfo bean) {
    	BatchUploadFile batchUPloadFile = null;
    	if (hashMap.containsKey(bean)) {
    		batchUPloadFile = hashMap.get(bean);
    	}
    	return batchUPloadFile;
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
	
    public static void fireSteppedEvent(Object source, DownloadInfo uploadInfo, int progress, int remainTime) {
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