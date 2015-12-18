package com.supermap.desktop.CtrlAction.Layout;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionLayoutSave extends CtrlAction {

	public CtrlActionLayoutSave(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		IFormLayout formLayout = (IFormLayout)Application.getActiveApplication().getActiveForm();
		formLayout.save();
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		IFormLayout formLayout = (IFormLayout)Application.getActiveApplication().getActiveForm();
		if (formLayout != null && formLayout.getMapLayoutControl().getMapLayout().isModified()) {
			enable = true;
		}
		return enable;
	}

}
