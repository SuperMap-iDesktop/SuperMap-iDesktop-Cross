package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionSortOrderAscending extends CtrlAction {

	public CtrlActionSortOrderAscending(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		((IFormTabular) Application.getActiveApplication().getActiveForm()).sortRecordset("asc", ((IFormTabular) Application.getActiveApplication()
				.getActiveForm()).getSelectedColumns());
	}

	@Override
	public boolean enable() {
		IFormTabular activeForm = (IFormTabular) Application.getActiveApplication().getActiveForm();
		int tabularSelectNumberCount = activeForm.getSelectColumnCount();
		return tabularSelectNumberCount > 0 && activeForm.getRecordset().getRecordCount() > 0;
	}
}
