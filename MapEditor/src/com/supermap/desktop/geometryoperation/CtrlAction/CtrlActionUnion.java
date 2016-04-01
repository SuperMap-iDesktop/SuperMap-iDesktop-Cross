package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionUnion extends CtrlAction {

	public CtrlActionUnion(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (Application.getActiveApplication().getActiveForm() instanceof FormMap) {
			enable = ((FormMap) Application.getActiveApplication().getActiveForm()).getEditState().isUnionEnable();
		}
		return enable;
	}
}
