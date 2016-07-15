package com.supermap.Interface;

import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.progress.Interface.IUpdateProgress;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

public interface ITask extends IUpdateProgress{
	void removeEvents();
	FileInfo getDownloadInfo();
	void setDownloadInfo(FileInfo downloadInfo);
	void doWork(UpdateProgressCallable doWork);
}
