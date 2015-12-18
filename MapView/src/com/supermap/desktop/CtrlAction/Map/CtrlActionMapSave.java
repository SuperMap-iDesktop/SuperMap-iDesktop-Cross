package com.supermap.desktop.CtrlAction.Map;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionMapSave extends CtrlAction {

	public CtrlActionMapSave(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		IFormMap formMap = (IFormMap)Application.getActiveApplication().getActiveForm();
        formMap.save();
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		IFormMap formMap = (IFormMap)Application.getActiveApplication().getActiveForm();
		if (formMap != null && formMap.getMapControl().getMap().isModified()) {
			enable = true;
		}
		return enable;
	}

}
