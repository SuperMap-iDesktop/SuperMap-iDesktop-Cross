package com.supermap.desktop.task;

import com.supermap.Interface.ITask;
import com.supermap.Interface.ITaskFactory;
import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.http.download.FileInfo;

public class TaskFactory implements ITaskFactory {
	private static TaskFactory taskFactory;

	public static TaskFactory getInstance() {
		if (null == taskFactory) {
			taskFactory = new TaskFactory();
		}
		return taskFactory;
	}

	@Override
	public ITask getTask(TaskEnum taskEnum, FileInfo fileInfo) {
		ITask task = null;
		switch (taskEnum) {
		case DOWNLOADTASK:
			// 下载任务
			if (null != fileInfo) {
				task = new DownLoadTask(fileInfo);
			} else {
				task = new DownLoadTask();
			}
			break;
		case UPLOADTASK:
			// 上传任务
			if (null != fileInfo) {
				task = new UploadTask(fileInfo);
			}
			break;
		case KERNELDENSITYTASK:
			// 计算热度图
			if (null != fileInfo) {
				task = new KernelDensityTask();
			}
			break;
		case KERNELDENSITYREALTIMETASK:
			// 实时热度图
			if (null != fileInfo) {
				task = new KernelDensityRealtimeTask();
			}
			break;
		case CREATESPATIALINDEXTASK:
			// 创建空间索引
			if (null != fileInfo) {
				task = new CreateSpatialIndexTask();
			}
			break;
		case SPATIALQUERY:
			// 空间查询
			if (null != fileInfo) {
				task = new SpatialQueryTask();
			}
			break;
		case ATTRIBUTEQUERY:
			// 属性查询
			if (null != fileInfo) {
				task = new AttributeQueryTask();
			}
			break;

		default:
			break;
		}
		return task;
	}

}
