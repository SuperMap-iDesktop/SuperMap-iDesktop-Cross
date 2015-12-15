package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DockbarManager;

public class CtrlActionLayersManager extends CtrlAction {
	
	public CtrlActionLayersManager(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}
	
	@Override
	public void run() {
		IFormMain formMain = Application.getActiveApplication().getMainFrame();
		IDockbar layersTreeDockBar = ((DockbarManager) formMain
				.getDockbarManager()).getLayersComponentManager();
		if (layersTreeDockBar != null) {
			layersTreeDockBar.setVisible(!layersTreeDockBar.isVisible());
		}
	}

	@Override
	public boolean enable() {
		// TODO Auto-generated method stub
		return true;
	}
}
