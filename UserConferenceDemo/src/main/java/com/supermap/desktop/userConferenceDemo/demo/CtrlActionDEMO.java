package com.supermap.desktop.userConferenceDemo.demo;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.controls.DockbarManager;

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
			IDockbar workspaceTreeDockbar = ((DockbarManager) Application.getActiveApplication().getMainFrame().getDockbarManager()).getWorkspaceComponentManager();
			IDockbar layersComponentManager = ((DockbarManager) Application.getActiveApplication().getMainFrame().getDockbarManager()).getLayersComponentManager();
			if (workspaceTreeDockbar != null) {
				workspaceTreeDockbar.setVisible(false);
			}
			if (layersComponentManager != null) {
				layersComponentManager.setVisible(false);
			}

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
