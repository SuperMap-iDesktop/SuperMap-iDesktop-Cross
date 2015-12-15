package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionOutputClear extends CtrlAction {

	public CtrlActionOutputClear(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		Application.getActiveApplication().getOutput().clear();
	}

	@Override
	public boolean enable() {
		return Application.getActiveApplication().getOutput().canClear();
	}

}
