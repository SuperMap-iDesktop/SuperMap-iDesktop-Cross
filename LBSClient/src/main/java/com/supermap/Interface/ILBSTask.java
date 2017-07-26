package com.supermap.Interface;

import com.supermap.desktop.progress.Interface.IUpdateProgress;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.ui.lbs.FileInfo;

public interface ILBSTask extends IUpdateProgress{
	
	
	/**
	 * 获取文件信息
	 * @return
	 */
	FileInfo getFileInfo();
	/**
	 * 任务操作
	 * @param doWork
	 */
	void doWork(UpdateProgressCallable doWork);
}
