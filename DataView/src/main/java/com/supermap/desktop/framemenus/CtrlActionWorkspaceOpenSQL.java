package com.supermap.desktop.framemenus;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.JDialogWorkspaceOpenSQL;
import com.supermap.desktop.utilities.SystemPropertyUtilities;

import javax.swing.*;

public class CtrlActionWorkspaceOpenSQL extends CtrlAction {

	public CtrlActionWorkspaceOpenSQL(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			JFrame parent = (JFrame)Application.getActiveApplication().getMainFrame();
			JDialogWorkspaceOpenSQL dialog = new JDialogWorkspaceOpenSQL(parent, false, "SQL");
			dialog.setVisible(true);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		// TODO Auto-generated method stub
		return SystemPropertyUtilities.isWindows();
	}

}