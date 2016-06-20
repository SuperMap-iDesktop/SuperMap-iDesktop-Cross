package com.supermap.desktop.CtrlAction;


import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.BrowseUtilities;

public class CtrlActionSparkOverview extends CtrlAction {

	public CtrlActionSparkOverview(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			BrowseUtilities.openUrl("http://192.168.14.1:8080/");
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}