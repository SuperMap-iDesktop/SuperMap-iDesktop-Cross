package com.supermap.desktop.CtrlAction;

import javax.swing.JFrame;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.MainFrame;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionApplicationExit extends CtrlAction {

	public CtrlActionApplicationExit(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JFrame mainFrame = (JFrame)Application.getActiveApplication().getMainFrame();
		if(CommonToolkit.WorkspaceWrap.closeWorkspace()){
			mainFrame.dispose();
			System.exit(0);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}

}
