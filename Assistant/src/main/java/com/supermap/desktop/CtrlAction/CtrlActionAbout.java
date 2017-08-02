package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.BrowseUtilities;

public class CtrlActionAbout extends CtrlAction {
	
	public CtrlActionAbout(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			BrowseUtilities.openUrl("http://support.supermap.com.cn/SuperMap-iDesktop-Cross/");
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
