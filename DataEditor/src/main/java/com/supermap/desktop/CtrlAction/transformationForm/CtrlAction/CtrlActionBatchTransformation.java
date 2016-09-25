package com.supermap.desktop.CtrlAction.transformationForm.CtrlAction;

import com.supermap.data.Datasources;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.transformationForm.Dialogs.JDialogTransformation;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author XiaJT
 */
public class CtrlActionBatchTransformation extends CtrlAction {
	public CtrlActionBatchTransformation(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		new JDialogTransformation().showDialog();
	}

	@Override
	public boolean enable() {
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		Datasources datasources = workspace.getDatasources();
		for (int count = datasources.getCount() - 1; count >= 0; count--) {
			if (datasources.get(count).isOpened() && !datasources.get(count).isReadOnly()) {
				return true;
			}
		}
		return false;
	}
}
