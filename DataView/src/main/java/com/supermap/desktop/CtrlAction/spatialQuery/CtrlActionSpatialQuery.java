package com.supermap.desktop.CtrlAction.spatialQuery;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author XiaJT
 */
public class CtrlActionSpatialQuery extends CtrlAction {
	public CtrlActionSpatialQuery(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JDialogSpatialQuery jDialogSpatialQuery = new JDialogSpatialQuery();
		jDialogSpatialQuery.showDialog();
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm == null || (!(activeForm instanceof IFormMap) && !(activeForm instanceof IFormScene))) {
			return false;
		}
		if (Application.getActiveApplication().getWorkspace().getDatasources().getCount() <= 0) {
			return false;
		}
		return true;
	}
}
