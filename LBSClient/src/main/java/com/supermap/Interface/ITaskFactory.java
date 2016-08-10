package com.supermap.Interface;

import com.supermap.desktop.http.download.FileInfo;

public interface ITaskFactory {
	ITask getTask(TaskEnum taskEnum,FileInfo fileInfo);
}
