package com.supermap.Interface;

import com.supermap.desktop.ui.lbs.impl.FileInfo;

public interface ITaskFactory {
	ILBSTask getTask(TaskEnum taskEnum, FileInfo fileInfo);
}
