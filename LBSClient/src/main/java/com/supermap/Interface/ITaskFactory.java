package com.supermap.Interface;

import com.supermap.desktop.lbs.FileInfo;

public interface ITaskFactory {
	ILBSTask getTask(TaskEnum taskEnum, FileInfo fileInfo);
}
