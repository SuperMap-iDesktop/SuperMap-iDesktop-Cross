package com.supermap.desktop.task;

import com.supermap.Interface.ITask;
import com.supermap.Interface.ITaskFactory;
import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.http.download.FileInfo;

public class TaskFactory implements ITaskFactory {
	private static TaskFactory taskFactory;
	public static TaskFactory getInstance(){
		if (null==taskFactory) {
			taskFactory = new TaskFactory();
		}
		return taskFactory;
	}
	@Override
	public  ITask getTask(TaskEnum taskEnum,FileInfo fileInfo) {
		ITask task = null;
		if (taskEnum.equals(TaskEnum.DOWNLOADTASK)) {
			// 下载任务
			task = new DownLoadTask(fileInfo);
		}
		if (taskEnum.equals(TaskEnum.UPLOADTASK)) {
			// 上传任务
			task = new UploadTask(fileInfo);
		}
		if (taskEnum.equals(TaskEnum.KERNELDENSITYTASK)) {
			// 计算热度图任务
			task = new KernelDensityTask();
		}
		if (taskEnum.equals(TaskEnum.KERNELDENSITYREALTIMETASK)) {
			// 实时热度图任务
			task = new KernelDensityRealtimeTask();
		}
		if (taskEnum.equals(TaskEnum.CREATESPATIALINDEXTASK)) {
			// 创建空间索引
			task = new CreateSpatialIndexTask();
		}
		if (taskEnum.equals(TaskEnum.SPATIALQUERY)) {
			// 创建空间索引
			task = new SpatialQueryTask();
		}
		if (taskEnum.equals(TaskEnum.ATTRIBUTEQUERY)) {
			// 创建空间索引
			task = new AttributeQueryTask();
		}
		return task;
	}

}
