package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionNewLayout extends CtrlAction {

	public CtrlActionNewLayout(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.LAYOUT);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}
