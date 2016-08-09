package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionGoToForward extends CtrlAction {

	public CtrlActionGoToForward(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IFormTabular formTabular = (IFormTabular) Application.getActiveApplication().getActiveForm();
		if (formTabular.getSelectedRow() > 0) {
			formTabular.goToRow(formTabular.getSelectedRow() - 1);
		}
	}

	@Override
	public boolean enable() {
		boolean flag = false;
		if (Application.getActiveApplication().getActiveForm() instanceof IFormTabular) {
			if (((IFormTabular) Application.getActiveApplication().getActiveForm()).getRowCount() > 1) {
				flag = true;
			}
			if (((IFormTabular) Application.getActiveApplication().getActiveForm()).getSelectedRow() == 0) {
				flag = false;
			}
			if (((IFormTabular) Application.getActiveApplication().getActiveForm()).getSelectColumnCount() == 0) {
				flag = false;
			}
		}
		return flag;
	}
}
