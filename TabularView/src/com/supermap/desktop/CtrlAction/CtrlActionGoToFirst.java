package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionGoToFirst extends CtrlAction {

	public CtrlActionGoToFirst(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		((IFormTabular) Application.getActiveApplication().getActiveForm()).goToRow(0);
	}

	@Override
	public boolean enable() {
		boolean flag = false;
		if (Application.getActiveApplication().getActiveForm() instanceof IFormTabular
				&& ((IFormTabular) Application.getActiveApplication().getActiveForm()).getRowCount() > 0) {
			flag = true;
		}
		return flag;
	}
}
