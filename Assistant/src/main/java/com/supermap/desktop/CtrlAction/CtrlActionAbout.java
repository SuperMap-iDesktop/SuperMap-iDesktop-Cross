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
import com.supermap.desktop.utilities.BrowseUtilities;

public class CtrlActionAbout extends CtrlAction {
	
	public CtrlActionAbout(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			BrowseUtilities.openUrl("http://www.supermap.com/xhtml/SuperMap-iDesktop-Cross-8C.html");
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
