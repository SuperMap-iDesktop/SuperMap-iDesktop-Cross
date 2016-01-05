package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Dataset.Pyramid.JDialogPyramidManager;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

/**
 * 影像金字塔管理窗口
 * Created by XiaJt on 2016/1/1.
 */
public class CtrlActionPyramid extends CtrlAction {


	public CtrlActionPyramid(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JDialogPyramidManager jDialogPyramidManager = new JDialogPyramidManager();
		jDialogPyramidManager.showDialog();
	}

	@Override
	public boolean enable() {
		boolean result = false;
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		if (workspace != null && workspace.getDatasources() != null && workspace.getDatasources().getCount() > 0) {
			result = true;
		}
		return result;
	}
}
