package com.supermap.Interface;

import com.supermap.desktop.ui.lbs.FileInfo;

public interface ITaskFactory {
	ILBSTask getTask(TaskEnum taskEnum, FileInfo fileInfo);
}
