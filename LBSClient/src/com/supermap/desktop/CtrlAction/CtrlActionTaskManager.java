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
		CommonUtilities.recoverTask();
	}

}
