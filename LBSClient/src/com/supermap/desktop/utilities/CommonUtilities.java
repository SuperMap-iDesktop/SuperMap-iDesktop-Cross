package com.supermap.desktop.utilities;

import java.io.File;

import javax.swing.ImageIcon;

import com.supermap.Interface.ITask;
import com.supermap.Interface.ITaskFactory;
import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.http.FileManagerContainer;
import com.supermap.desktop.http.callable.DownloadProgressCallable;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.http.upload.UploadUtils;
import com.supermap.desktop.task.TaskFactory;

/**
 * LBSClient项目公用方法类
 * 
 * @author xie
 *
 */
public class CommonUtilities {
	private static final String FILE_MANAGER_CONTROL_CLASS = "com.supermap.desktop.http.FileManagerContainer";

	/**
	 * 获取显示进度条ManagerContainer,并激活显示docbar
	 * 
	 * @return
	 */
	public static FileManagerContainer getFileManagerContainer() {

		FileManagerContainer fileManagerContainer = null;
		IDockbar dockbarPropertyContainer = getDockBar();

		if (dockbarPropertyContainer != null) {
			fileManagerContainer = (FileManagerContainer) dockbarPropertyContainer.getComponent();
			dockbarPropertyContainer.setVisible(true);
			dockbarPropertyContainer.active();
		}
		return fileManagerContainer;
	}

	/**
	 * 获取显示进度条docbar
	 * 
	 * @return
	 */
	public static IDockbar getDockBar() {
		IDockbar dockbarPropertyContainer = null;
		try {
			dockbarPropertyContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(FILE_MANAGER_CONTROL_CLASS));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dockbarPropertyContainer;
	}
	
	/**
	 * 删除任务项
	 * @param downloadInfo
	 */
	public static void removeItem(ITask task) {
		IDockbar dockbarPropertyContainer = CommonUtilities.getDockBar();
		if (null != dockbarPropertyContainer) {
			FileManagerContainer fileManagerContainer = (FileManagerContainer) dockbarPropertyContainer.getComponent();
			UploadUtils.hashMap.clear();
			fileManagerContainer.removeItem(task);
		}
	}
//	/**
//	 * 获取资源图片
//	 * @param imagePath
//	 * @return
//	 */
//	public static ImageIcon getImageIcon(String imagePath) {
//		ImageIcon imageIcon = null;
//		try {
//			String[] pathPrams = new String[] { PathUtilities.getRootPathName(), "../LBSClient/reosurces/", imagePath };
//			imagePath = PathUtilities.combinePath(pathPrams, false);
//			File imageFile = new File(imagePath);
//			if (imageFile.exists()) {
//				imageIcon = new ImageIcon(imagePath);
//			}
//		} catch (Exception exception) {
//			Application.getActiveApplication().getOutput().output(exception);
//		}
//
//		return imageIcon;
//	}
	/**
	 * 获取图片资源
	 * @param path
	 * @return
	 */
	public static ImageIcon getImageIcon(String path){
		ImageIcon imageIcon = null;
		try{
			if (!StringUtilities.isNullOrEmptyString(path)) {
				imageIcon = new ImageIcon(CommonUtilities.class.getResource("/com/supermap/desktop/lbsresources/ToolBar/"+path));
			}
		} catch(Exception e){
			Application.getActiveApplication().getOutput().output(e);
		}
		return imageIcon;
	}
	/**
	 * 添加下载任务
	 * @param downloadInfo
	 */
	public static void addDownLoadTask(FileInfo downloadInfo){
		FileManagerContainer fileManagerContainer = CommonUtilities.getFileManagerContainer();

		if (fileManagerContainer != null) {
			ITaskFactory taskFactory = TaskFactory.getInstance();
			ITask task = taskFactory.getTask(TaskEnum.DOWNLOADTASK, downloadInfo);
			DownloadProgressCallable uploadProgressCallable = new DownloadProgressCallable(downloadInfo);
			task.doWork(uploadProgressCallable);
			fileManagerContainer.addItem(task);
		}
	}
	
}
