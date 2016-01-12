package com.supermap.desktop.netservices.iserver.action;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.netservices.iserver.JDialogServerRelease;
import com.supermap.desktop.netservices.iserver.WorkspaceInfo;

public class CtrlActionServerRelease extends CtrlAction {

	public CtrlActionServerRelease(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			JDialogServerRelease dialog = new JDialogServerRelease(WorkspaceInfo.getCurrentWorkspaceInfo());
			dialog.setVisible(true);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
}
