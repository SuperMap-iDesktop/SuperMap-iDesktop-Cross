package com.supermap.desktop.utilities;


import javax.swing.ImageIcon;

import com.supermap.Interface.ITask;
import com.supermap.Interface.ITaskFactory;
import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IFormLBSControl;
import com.supermap.desktop.http.FileManagerContainer;
import com.supermap.desktop.http.TaskManagerContainer;
import com.supermap.desktop.http.callable.DownloadProgressCallable;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.task.TaskFactory;

/**
 * LBSClient项目公用方法类
 * 
 * @author xie
 *
 */
public class CommonUtilities {
	private static final String FILE_MANAGER_CONTROL_CLASS = "com.supermap.desktop.http.FileManagerContainer";
	private static final String TASK_MANAGER_CONTROL_CLASS = "com.supermap.desktop.http.TaskManagerContainer";

	/**
	 * 获取显示进度条ManagerContainer,并激活显示dockbar
	 * 
	 * @return
	 */
	public static FileManagerContainer getFileManagerContainer() {

		FileManagerContainer fileManagerContainer = null;
		IDockbar dockbarPropertyContainer;
		dockbarPropertyContainer = getDockBar(FILE_MANAGER_CONTROL_CLASS);
		if (dockbarPropertyContainer != null) {
			fileManagerContainer = (FileManagerContainer) dockbarPropertyContainer.getComponent();
			dockbarPropertyContainer.setVisible(true);
			dockbarPropertyContainer.active();
		}
		return fileManagerContainer;
	}
	/**
	 * 获取任务管理界面，并激活dockbar
	 * @return
	 */
	public static TaskManagerContainer getTaskManagerContainer() {
		TaskManagerContainer taskManagerContainer = null;
		IDockbar dockbarPropertyContainer;
		dockbarPropertyContainer = getDockBar(TASK_MANAGER_CONTROL_CLASS);
		if (dockbarPropertyContainer != null) {
			taskManagerContainer = (TaskManagerContainer) dockbarPropertyContainer.getComponent();
			dockbarPropertyContainer.setVisible(true);
			dockbarPropertyContainer.active();
		}
		return taskManagerContainer;
	}

	/**
	 * 获取要显示的docbar
	 * 
	 * @return
	 */
	public static IDockbar getDockBar(String name) {
		IDockbar dockbarPropertyContainer = null;
		try {
			dockbarPropertyContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(name));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return dockbarPropertyContainer;
	}

	/**
	 * 删除任务项
	 * 
	 * @param downloadInfo
	 */
	public static void removeItem(ITask task) {
		IDockbar dockbarPropertyContainer = CommonUtilities.getDockBar(FILE_MANAGER_CONTROL_CLASS);
		if (null != dockbarPropertyContainer) {
			FileManagerContainer fileManagerContainer = (FileManagerContainer) dockbarPropertyContainer.getComponent();
			fileManagerContainer.removeItem(task);
		}
	}

	public static IFormLBSControl getActiveLBSControl() {
		IFormLBSControl result = null;
		if (null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormLBSControl) {
			result = (IFormLBSControl) Application.getActiveApplication().getActiveForm();
		}
		return result;
	}

	/**
	 * 获取图片资源
	 * 
	 * @param path
	 * @return
	 */
	public static ImageIcon getImageIcon(String path) {
		ImageIcon imageIcon = null;
		try {
			if (!StringUtilities.isNullOrEmptyString(path)) {
				imageIcon = new ImageIcon(CommonUtilities.class.getResource("/com/supermap/desktop/lbsresources/Toolbar/" + path));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return imageIcon;
	}

	/**
	 * 添加下载任务
	 * 
	 * @param downloadInfo
	 */
	public static void addDownLoadTask(FileInfo downloadInfo) {
		FileManagerContainer fileManagerContainer = CommonUtilities.getFileManagerContainer();

		if (fileManagerContainer != null) {
			ITaskFactory taskFactory = TaskFactory.getInstance();
			ITask task = taskFactory.getTask(TaskEnum.DOWNLOADTASK, downloadInfo);
			DownloadProgressCallable uploadProgressCallable = new DownloadProgressCallable(downloadInfo,true);
			task.doWork(uploadProgressCallable);
			fileManagerContainer.addItem(task);
		}
	}

}
