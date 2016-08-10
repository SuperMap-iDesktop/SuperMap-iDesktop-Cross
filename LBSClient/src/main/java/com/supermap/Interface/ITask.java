package com.supermap.Interface;

import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.progress.Interface.IUpdateProgress;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

public interface ITask extends IUpdateProgress{
	
	
	/**
	 * 获取任务类型
	 * @return
	 */
	TaskEnum getTaskType();
	/**
	 * 移除事件
	 */
	void removeEvents();
	/**
	 * 获取文件信息
	 * @return
	 */
	FileInfo getFileInfo();
	/**
	 * 设置文件信息
	 * @param downloadInfo
	 */
	void setFileInfo(FileInfo downloadInfo);
	/**
	 * 任务操作
	 * @param doWork
	 */
	void doWork(UpdateProgressCallable doWork);
}
