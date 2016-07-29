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
			List<String> taskPropertyLists = ManagerXMLParser.getTaskPropertyList();
			JDialogTaskManager taskManager = new JDialogTaskManager(null, true);
			taskManager.setDownloadTaskNumber(taskPropertyLists.size());
			if (taskManager.showDialog().equals(DialogResult.OK) && taskManager.isRecoverDownloadTask()) {
				for (String attris : taskPropertyLists) {
					String[] attriArray = attris.split(",");
					FileInfo downloadInfo = new FileInfo(attriArray[0], attriArray[1], attriArray[2], Long.parseLong(attriArray[3]), 1, true);
					ITask task = taskFactory.getTask(TaskEnum.DOWNLOADTASK, downloadInfo);
					DownloadProgressCallable downloadProgressCallable = new DownloadProgressCallable(downloadInfo, false);
					task.doWork(downloadProgressCallable);
					fileManagerContainer.addItem(task);
				}
			}
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (null != ManagerXMLParser.getTaskPropertyList() && ManagerXMLParser.getTaskPropertyList().size() > 0) {
			enable = true;
		}
		return enable;
	}
}
