package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.utilities.SystemUIUtilities;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionApplicationExit extends CtrlAction {

	public CtrlActionApplicationExit(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		SystemUIUtilities.exit();
	}

	@Override
	public boolean enable() {
		return true;
	}

}
