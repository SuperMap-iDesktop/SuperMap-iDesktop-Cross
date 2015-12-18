package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionSortOrderDescending extends CtrlAction {

	public CtrlActionSortOrderDescending(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		((IFormTabular) Application.getActiveApplication().getActiveForm()).sortRecordset("desc", ((IFormTabular) Application.getActiveApplication()
				.getActiveForm()).getSelectedColumns());

	}

	@Override
	public boolean enable() {
		int tabularSelectNumberCount = ((IFormTabular) Application.getActiveApplication().getActiveForm()).getSelectColumnCount();
		return tabularSelectNumberCount > 0 ? true : false;
	}
}
