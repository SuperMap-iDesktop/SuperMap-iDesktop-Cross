package com.supermap.desktop.CtrlAction;

import java.util.List;

import com.supermap.Interface.ITask;
import com.supermap.Interface.ITaskFactory;
import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.JDialogTaskManager;
import com.supermap.desktop.http.FileManagerContainer;
import com.supermap.desktop.http.callable.DownloadProgressCallable;
import com.supermap.desktop.http.callable.UploadPropressCallable;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.task.TaskFactory;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.desktop.utilities.ManagerXMLParser;

public class CtrlActionTaskManager extends CtrlAction {

	public CtrlActionTaskManager(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.supermap.desktop.implement.CtrlAction#run()
	 */
	@Override
	public void run() {
		FileManagerContainer fileManagerContainer = CommonUtilities.getFileManagerContainer();
		if (null != fileManagerContainer) {
			ITaskFactory taskFactory = TaskFactory.getInstance();
			List<String> downloadTaskPropertyLists = ManagerXMLParser.getTaskPropertyList(TaskEnum.DOWNLOADTASK);
			List<String> uploadTaskPropertyLists = ManagerXMLParser.getTaskPropertyList(TaskEnum.UPLOADTASK);
			JDialogTaskManager taskManager = new JDialogTaskManager(null, true);
			taskManager.setDownloadTaskCount(downloadTaskPropertyLists.size());
			taskManager.setUploadTaskCount(uploadTaskPropertyLists.size());
			if (taskManager.showDialog().equals(DialogResult.OK) && taskManager.isRecoverTask()) {
				for (String downloadAttris : downloadTaskPropertyLists) {
					String[] attriArrayForDownload = downloadAttris.split(",");
					FileInfo downloadInfo = new FileInfo(attriArrayForDownload[0], attriArrayForDownload[1], attriArrayForDownload[2], Long.parseLong(attriArrayForDownload[3]), 1, true);
					ITask downloadTask = taskFactory.getTask(TaskEnum.DOWNLOADTASK, downloadInfo);
					DownloadProgressCallable downloadProgressCallable = new DownloadProgressCallable(downloadInfo, false);
					downloadTask.doWork(downloadProgressCallable);
					fileManagerContainer.addItem(downloadTask);
				}
				for (String uploadAttris : uploadTaskPropertyLists) {
					String[] attriArrayForUpload = uploadAttris.split(",");
					FileInfo uploadInfo = new FileInfo(attriArrayForUpload[0], attriArrayForUpload[1], attriArrayForUpload[2], Long.parseLong(attriArrayForUpload[3]), 1, true);
					ITask uploadTask = taskFactory.getTask(TaskEnum.UPLOADTASK, uploadInfo);
					UploadPropressCallable downloadProgressCallable = new UploadPropressCallable(uploadInfo, false);
					uploadTask.doWork(downloadProgressCallable);
					fileManagerContainer.addItem(uploadTask);
				}
			}
		}
	}

}
