package com.supermap.desktop.CtrlAction;

import java.io.IOException;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.utilties.BrowseUtilties;

public class CtrlActionTechnicalSupport extends CtrlAction {
	
	public CtrlActionTechnicalSupport(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			BrowseUtilties.openUrl("http://www.supermap.com/cn/");
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
