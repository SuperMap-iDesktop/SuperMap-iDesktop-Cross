package com.supermap.desktop.WorkflowView.demo;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.WorkflowView.demo.Demo;
import com.supermap.desktop.ui.FormBaseChild;

/**
 * Created by yuanR on 2017/7/19  .
 * 用户大会展示功能入口
 */
public class CtrlActionDEMO extends CtrlAction {

	public CtrlActionDEMO(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	FormBaseChild workspaceManagerWindow;
	IFormManager formManager;

	public void run() {
		try {
			formManager = Application.getActiveApplication().getMainFrame().getFormManager();
			if (workspaceManagerWindow == null) {
				workspaceManagerWindow = new Demo();
				formManager.showChildForm(workspaceManagerWindow);
			} else {
				if (!workspaceManagerWindow.isShowing()) {
					formManager.showChildForm(workspaceManagerWindow);
				} else {
					formManager.close(workspaceManagerWindow);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}
