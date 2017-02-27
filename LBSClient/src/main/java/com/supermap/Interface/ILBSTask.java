package com.supermap.Interface;

import com.supermap.desktop.ui.lbs.impl.FileInfo;
import com.supermap.desktop.progress.Interface.IUpdateProgress;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

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
