package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionGoToLast extends CtrlAction {
	public CtrlActionGoToLast(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		((IFormTabular) Application.getActiveApplication().getActiveForm()).goToRow(((IFormTabular) Application.getActiveApplication().getActiveForm())
				.getRowCount() - 1);
	}

	public boolean enable() {
		boolean flag = false;
		if (Application.getActiveApplication().getActiveForm() instanceof IFormTabular
				&& ((IFormTabular) Application.getActiveApplication().getActiveForm()).getRowCount() > 0) {
			flag = true;
		}
		return flag;
	}
}
