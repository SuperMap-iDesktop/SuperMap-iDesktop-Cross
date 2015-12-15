package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionGoToNext extends CtrlAction {

	public CtrlActionGoToNext(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		if (((IFormTabular) Application.getActiveApplication().getActiveForm()).getSelectedRow() < ((IFormTabular) Application.getActiveApplication()
				.getActiveForm()).getRowCount() - 1) {
			((IFormTabular) Application.getActiveApplication().getActiveForm()).goToRow(((IFormTabular) Application.getActiveApplication().getActiveForm())
					.getSelectedRow() + 1);
		}
	}

	@Override
	public boolean enable() {
		boolean flag = false;
		if (((IFormTabular) Application.getActiveApplication().getActiveForm()).getRowCount() > 1) {
			flag = true;
		}
		if (((IFormTabular) Application.getActiveApplication().getActiveForm()).getSelectedRow() == ((IFormTabular) Application.getActiveApplication()
				.getActiveForm()).getRowCount() - 1) {
			flag = false;
		}
		if (((IFormTabular) Application.getActiveApplication().getActiveForm()).getSelectColumnCount() == 0) {
			flag = false;
		}
		return flag;
	}
}
