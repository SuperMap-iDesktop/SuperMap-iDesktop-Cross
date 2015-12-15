package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilties.BrowseUtilties;

/**
 * Created by Administrator on 2015/11/19.
 */
public class CtrlActionHelp extends CtrlAction{
	public CtrlActionHelp(IBaseItem caller, IForm formClass) {
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
