package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.JDialogTaskManager;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.CommonUtilities;

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
		if (null == JDialogTaskManager.getTaskManager()) {
			CommonUtilities.recoverTask();
		}
	}
	
	@Override
	public boolean enable() {
		if (CommonUtilities.isTaskManagerOpened()) {
			return false;
		}
		return true;
	}

}
