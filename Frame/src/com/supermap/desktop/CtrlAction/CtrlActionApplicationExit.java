package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.WorkspaceUtilities;

import javax.swing.*;

public class CtrlActionApplicationExit extends CtrlAction {

	public CtrlActionApplicationExit(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JFrame mainFrame = (JFrame)Application.getActiveApplication().getMainFrame();
		if(WorkspaceUtilities.closeWorkspace()){
			mainFrame.dispose();
			System.exit(0);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}

}
