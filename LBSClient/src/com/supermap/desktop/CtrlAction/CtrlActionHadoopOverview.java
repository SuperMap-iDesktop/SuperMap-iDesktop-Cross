package com.supermap.desktop.CtrlAction;


import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilties.BrowseUtilties;

public class CtrlActionHadoopOverview extends CtrlAction {

	public CtrlActionHadoopOverview(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			BrowseUtilties.openUrl("http://192.168.12.103:50070/dfshealth.html#tab-overview");
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}
