package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DockbarManager;

public class CtrlActionWorkspaceManager extends CtrlAction {
	
	public CtrlActionWorkspaceManager(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IFormMain formMain = Application.getActiveApplication().getMainFrame();
		IDockbar workspaceTreeDockbar = ((DockbarManager) formMain
				.getDockbarManager()).getWorkspaceComponentManager();
		if (workspaceTreeDockbar != null) {
			workspaceTreeDockbar.setVisible(!workspaceTreeDockbar.isVisible());
		}
	}

	@Override
	public boolean enable() {
		// TODO Auto-generated method stub
		return true;
	}
}
